package com.example.lab_work.dto;

public record MatchedPersonData(
        String normalizedName,
        Short age,
        Integer projectId,
        String projectName,
        String role,
        Integer salary,
        Integer bonus,
        String[] skills,
        String techStack
) {}