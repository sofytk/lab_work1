package com.example.lab_work;


import com.example.lab_work.service.EtlOrchestratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/etl")
@RequiredArgsConstructor
public class Controller {

    private final EtlOrchestratorService etlService;

    @PostMapping("/run")
    public ResponseEntity<Map<String, String>> runEtl() {
        long startTime = System.currentTimeMillis();
        etlService.executeFullEtl();
        long duration = System.currentTimeMillis() - startTime;

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "ETL completed",
                "duration_ms", String.valueOf(duration)
        ));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "ETL Service"
        ));
    }
}