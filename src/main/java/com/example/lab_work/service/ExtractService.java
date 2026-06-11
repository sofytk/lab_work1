package com.example.lab_work.service;

import com.example.lab_work.model.projects_db.Project;
import com.example.lab_work.model.projects_db.ProjectDbPerson;
import com.example.lab_work.model.resume_db.Resume;
import com.example.lab_work.model.resume_db.ResumeDbPerson;
import com.example.lab_work.model.resume_db.ResumeSkill;
import com.example.lab_work.model.resume_db.Skill;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Slf4j
@Service
public class ExtractService {

    @PersistenceContext(unitName = "projectsDb")
    @Qualifier("projectsDbEntityManagerFactory")
    private EntityManager projectEm;

    @PersistenceContext(unitName = "resumeDb")
    @Qualifier("resumeDbEntityManagerFactory")
    private EntityManager resumeEm;

    @Transactional(readOnly = true)
    public List<ProjectDbPerson> extractAllPersonsFromProjectDb() {
        return projectEm.createQuery(
            "SELECT p FROM ProjectDbPerson p",
            ProjectDbPerson.class
        ).getResultList();
    }

    @Transactional(readOnly = true)
    public List<Project> extractAllProjects() {
        return projectEm.createQuery(
            "SELECT p FROM ProjectDbProject p",
            Project.class
        ).getResultList();
    }

    @Transactional(readOnly = true)
    public List<ResumeDbPerson> extractAllPersonsFromResumeDb() {
        List<ResumeDbPerson> persons = resumeEm.createQuery(
                "SELECT p FROM ResumeDbPerson p",
                ResumeDbPerson.class)
            .getResultList();
        for (ResumeDbPerson person : persons) {
            Resume resume = null;
            try {
                resume = (Resume) resumeEm.createQuery(
                        "SELECT r FROM Resume r WHERE r.person.id = :personId")
                    .setParameter("personId", person.getId())
                    .getSingleResult();
            } catch (Exception e) {
                log.warn("No resume for person: {}", person.getFullName());
            }

            if (resume != null) {
                person.setResume(resume);
                resume.setPerson(person);

                List<Object[]> skillResults = resumeEm.createNativeQuery(
                        "SELECT s.name, rs.level " +
                        "FROM resume_skill rs " +
                        "JOIN skill s ON rs.skill_id = s.id " +
                        "WHERE rs.resume_id = :resumeId")
                    .setParameter("resumeId", resume.getId())
                    .getResultList();

                Set<ResumeSkill> skills = new HashSet<>();
                for (Object[] row : skillResults) {
                    String skillName = (String) row[0];
                    String level = (String) row[1];

                    Skill skill = new Skill();
                    skill.setName(skillName);

                    ResumeSkill rs = new ResumeSkill();
                    rs.setResume(resume);
                    rs.setSkill(skill);
                    rs.setLevel(level);

                    skills.add(rs);
                }

                resume.setSkills(skills);

            }
        }

        return persons;
    }
}