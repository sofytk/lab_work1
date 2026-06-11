package com.example.lab_work.service;

import com.example.lab_work.dto.MatchedPersonData;
import com.example.lab_work.model.projects_db.*;
import com.example.lab_work.model.resume_db.ResumeDbPerson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EtlOrchestratorService {

    private final ExtractService extractService;
    private final TransformService transformService;
    private final LoadService loadService;
    private final NameNormalizationService nameNormalizationService;

    public void executeFullEtl() {
        long startTime = System.currentTimeMillis();
        log.info("========================================");
        log.info("Starting ETL Process");
        log.info("========================================");

        try {
            log.info("STEP 1: EXTRACT - Reading data from source databases");
            List<ProjectDbPerson> projectPersons = extractService.extractAllPersonsFromProjectDb();
            List<ResumeDbPerson> resumePersons = extractService.extractAllPersonsFromResumeDb();
            List<Project> projects = extractService.extractAllProjects();

            log.info("  - Project DB: {} persons, {} projects", projectPersons.size(), projects.size());
            log.info("  - Resume DB: {} persons", resumePersons.size());


            log.info("STEP 2: TRANSFORM - Matching and merging data");

            // Матчинг людей
            Map<ProjectDbPerson, ResumeDbPerson> matches =
                    transformService.matchPersons(projectPersons, resumePersons);

            // Сборка данных для хранилища
            List<MatchedPersonData> warehouseData = transformService.buildWarehouseData(matches, projects);

            log.info("  - Matched: {} persons", matches.size());
            log.info("  - Warehouse records to load: {}", warehouseData.size());

            log.info("STEP 3: LOAD - Writing to Data Warehouse");

            // Очистка хранилища
            loadService.clearWarehouse();

            // Загрузка данных
            loadService.loadToWarehouse(warehouseData);

            long duration = System.currentTimeMillis() - startTime;
            log.info("========================================");
            log.info("ETL COMPLETED SUCCESSFULLY");
            log.info("  - Duration: {} ms", duration);
            log.info("  - Records loaded: {}", warehouseData.size());
            log.info("  - Total in warehouse: {}", loadService.getWarehouseStats());
            log.info("========================================");

        } catch (Exception e) {
            log.error("ETL FAILED: {}", e.getMessage(), e);
            throw new RuntimeException("ETL execution failed", e);
        } finally {
            nameNormalizationService.clearCache();
        }
    }

}
