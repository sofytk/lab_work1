package com.example.lab_work.service;

import com.example.lab_work.dto.MatchedPersonData;
import com.example.lab_work.model.data_warehouse.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;

import java.sql.Timestamp;
import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class LoadService {

    @PersistenceContext(unitName = "dataWarehouse")
    @Qualifier("dataWarehouseEntityManagerFactory")
    private EntityManager warehouseEm;

    @Transactional(transactionManager = "dataWarehouseTransactionManager")
    public void loadToWarehouse(List<MatchedPersonData> dataList) {
        Map<String, WarehousePerson> personCache = new HashMap<>();
        Map<String, Project> projectCache = new HashMap<>();

        int loadedCount = 0;

        for (MatchedPersonData data : dataList) {
            try {
                String personKey = data.normalizedName();
                WarehousePerson person = personCache.get(personKey);
                if (person == null) {
                    person = findOrCreatePerson(data);
                    personCache.put(personKey, person);
                }


                String projectKey = data.projectName();
                Project project = projectCache.get(projectKey);
                if (project == null) {
                    project = findOrCreateProject(data);
                    projectCache.put(projectKey, project);
                }

                if (!existsMemberInformation(person, project)) {
                    ProjectMemberInformation memberInfo = new ProjectMemberInformation();
                    memberInfo.setPerson(person);
                    memberInfo.setProjectId(project.getProjectId());
                    memberInfo.setRole(data.role());

                    warehouseEm.persist(memberInfo);
                    loadedCount++;
                }

                if (loadedCount % 20 == 0) {
                    warehouseEm.flush();
                }

            } catch (Exception e) {
                log.error("Error loading data for person: {}", data.normalizedName(), e);
            }
        }

        warehouseEm.flush();
        log.info("Loaded {} new records to warehouse (total unique: {})", loadedCount, dataList.size());
    }

    private WarehousePerson findOrCreatePerson(MatchedPersonData data) {
        try {
            return warehouseEm.createQuery(
                    "SELECT p FROM WarehousePerson p WHERE p.fullName = :fullName",
                    WarehousePerson.class)
                .setParameter("fullName", data.normalizedName())
                .getSingleResult();
        } catch (Exception e) {
            WarehousePerson person = new WarehousePerson();
            person.setFullName(data.normalizedName());
            person.setAge(data.age());

            if (data.skills() != null && data.skills().length > 0) {
                person.setSkills(data.skills());
            }
            if (data.salary() != null) {
                person.setSalary(data.salary());
            }
            if (data.bonus() != null) {
                person.setBonus(data.bonus());
            }

            warehouseEm.persist(person);
            warehouseEm.flush();
            return person;
        }
    }

    private Project findOrCreateProject(MatchedPersonData data) {
        try {
            return warehouseEm.createQuery(
                    "SELECT p FROM Project p WHERE p.projectName = :projectName",
                    Project.class)
                .setParameter("projectName", data.projectName())
                .getSingleResult();
        } catch (Exception e) {
            Project project = new Project();
            project.setProjectName(data.projectName());
            project.setDescription(data.projectName());

            if (data.techStack() != null && !data.techStack().isEmpty()) {
                String[] techArray = data.techStack().split(", ");
                project.setTechStack(techArray);
            }

            warehouseEm.persist(project);
            warehouseEm.flush();
            return project;
        }
    }

    private boolean existsMemberInformation(WarehousePerson person, Project project) {
        Long count = warehouseEm.createQuery(
                "SELECT COUNT(m) FROM ProjectMemberInformation m " +
                "WHERE m.person = :person AND m.projectId = :projectId",
                Long.class)
            .setParameter("person", person)
            .setParameter("projectId", project.getProjectId())
            .getSingleResult();
        return count > 0;
    }

    @Transactional(transactionManager = "dataWarehouseTransactionManager")
    public void clearWarehouse() {
        // Порядок важен: сначала удаляем зависимости, потом родительские таблицы
        warehouseEm.createQuery("DELETE FROM ProjectMemberInformation").executeUpdate();
        warehouseEm.createQuery("DELETE FROM Project").executeUpdate();
        warehouseEm.createQuery("DELETE FROM WarehousePerson").executeUpdate();
        warehouseEm.flush();
        log.info("Warehouse cleared");
    }

    @Transactional(readOnly = true, transactionManager = "dataWarehouseTransactionManager")
    public long getWarehouseStats() {
        try {
            return warehouseEm.createQuery("SELECT COUNT(m) FROM ProjectMemberInformation m", Long.class)
                .getSingleResult();
        } catch (Exception e) {
            log.warn("Failed to get warehouse stats: {}", e.getMessage());
            return 0;
        }
    }

}