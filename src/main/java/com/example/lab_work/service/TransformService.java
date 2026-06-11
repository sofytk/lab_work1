package com.example.lab_work.service;

import com.example.lab_work.dto.MatchedPersonData;
import com.example.lab_work.dto.NormalizedName;
import com.example.lab_work.dto.PersonMatchCandidate;
import com.example.lab_work.model.projects_db.Project;
import com.example.lab_work.model.projects_db.ProjectDbPerson;
import com.example.lab_work.model.projects_db.ProjectMember;
import com.example.lab_work.model.resume_db.ResumeDbPerson;
import com.example.lab_work.model.resume_db.ResumeSkill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransformService {

    private final NameNormalizationService nameNormalizationService;

    /**
     * Матчинг людей из двух баз данных
     */
    public Map<ProjectDbPerson, ResumeDbPerson> matchPersons(
            List<ProjectDbPerson> projectPersons,
            List<ResumeDbPerson> resumePersons) {

        Map<ProjectDbPerson, ResumeDbPerson> matches = new LinkedHashMap<>();

        Map<ProjectDbPerson, NormalizedName> projectNorm = new HashMap<>();
        Map<ResumeDbPerson, NormalizedName> resumeNorm = new HashMap<>();

        for (var p : projectPersons) {
            projectNorm.put(p, nameNormalizationService.normalize(p.getFullName()));
        }

        for (var r : resumePersons) {
            resumeNorm.put(r, nameNormalizationService.normalize(r.getFullName()));
        }

        for (var projectPerson : projectPersons) {
            NormalizedName projName = projectNorm.get(projectPerson);

            log.info("========== Processing: {} ==========", projectPerson.getFullName());
            log.info("Normalized: surname='{}', name='{}', patronymic='{}'",
                    projName.surname(), projName.name(), projName.patronymic());

            List<PersonMatchCandidate> candidates = new ArrayList<>();

            for (var resumePerson : resumePersons) {
                NormalizedName resumeName = resumeNorm.get(resumePerson);

                double nameSimilarity =
                        nameNormalizationService.calculateFullSimilarity(projName, resumeName);

                boolean ageMatch =
                        projectPerson.getAge() != null &&
                                resumePerson.getAge() != null &&
                                Math.abs(projectPerson.getAge() - resumePerson.getAge()) <= 3;


                candidates.add(
                        new PersonMatchCandidate(
                                resumePerson,
                                nameSimilarity,
                                ageMatch
                        )
                );
            }

            log.info("Total candidates created: {}", candidates.size());

            if (candidates.isEmpty()) {
                log.warn("No candidates for: '{}'", projectPerson.getFullName());
                continue;
            }

            log.info("--- All candidates for {} ---", projectPerson.getFullName());
            for (int i = 0; i < candidates.size(); i++) {
                PersonMatchCandidate c = candidates.get(i);
                log.info("  [{}] {} -> nameScore={}, ageMatch={}, skillsScore={}, totalScore={}",
                        i,
                        c.person().getFullName(),
                        c.nameSimilarity(),
                        c.ageMatch(),
                        c.getTotalScore());
            }

            PersonMatchCandidate best = candidates.stream()
                    .max(Comparator.comparingDouble(PersonMatchCandidate::getTotalScore))
                    .orElse(null);

            if (best == null) {
                log.error("Best candidate is NULL for: {}", projectPerson.getFullName());
                continue;
            }

            double finalScore = best.getTotalScore();

            log.info("--- BEST CANDIDATE ---");
            log.info("  Name: {}", best.person().getFullName());
            log.info("  Name similarity: {}", best.nameSimilarity());
            log.info("  Age match: {}", best.ageMatch());
            log.info("  Total score: {}", finalScore);

            double threshold = 0.5;
            log.info("  Threshold: {}", threshold);

            if (finalScore >= threshold) {
                matches.put(projectPerson, best.person());
                log.info("✓✓✓ MATCHED: '{}' -> '{}' (score: {}) ✓✓✓",
                        projectPerson.getFullName(),
                        best.person().getFullName(),
                        finalScore);
            } else {
                log.warn("✗✗✗ REJECTED: '{}' -> '{}' (score: {} < {}) ✗✗✗",
                        projectPerson.getFullName(),
                        best.person().getFullName(),
                        finalScore,
                        threshold);
            }

            log.info("========================================\n");
        }

        log.info("=== FINAL RESULT: Matched {} out of {} persons ===",
                matches.size(),
                projectPersons.size());

        return matches;
    }


    /**
     * Сборка данных для хранилища
     */
    public List<MatchedPersonData> buildWarehouseData(
            Map<ProjectDbPerson, ResumeDbPerson> matches,
            List<Project> projects
    ) {

        List<MatchedPersonData> result = new ArrayList<>();

        log.info("Building warehouse data - matches: {}, projects: {}",
                matches.size(), projects.size());

        for (var entry : matches.entrySet()) {
            var projectPerson = entry.getKey();
            var resumePerson = entry.getValue();

            for (ProjectMember member : projectPerson.getProjectMembers()) {
                Project project = member.getProject();
                if (project == null) continue;

                result.add(
                        new MatchedPersonData(
                                projectPerson.getFullName(),
                                projectPerson.getAge(),
                                project.getId(),
                                project.getProjectName(),
                                member.getRoleName(),
                                resumePerson.getSalary() != null
                                        ? resumePerson.getSalary().getSalary()
                                        : null,
                                resumePerson.getSalary() != null
                                        ? resumePerson.getSalary().getBonus()
                                        : null,
                                resumePerson.getResume() != null
                                        ? resumePerson.getResume().getPerson().getSkillsArray()
                                        : new String[0],
                                project.getTechStack() != null
                                        ? String.join(", ", project.getTechStack())
                                        : ""
                        )
                );
            }
        }

        log.info("Built {} warehouse records", result.size());
        return result;
    }
}