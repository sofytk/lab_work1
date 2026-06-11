package com.example.lab_work.model.projects_db;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity(name = "ProjectDbProject")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project")
public class Project {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "project_name", length = 255, nullable = false)
    private String projectName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<ProjectTechStack> techStacks = new HashSet<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<ProjectMember> members = new HashSet<>();

    @Transient
    public String[] getTechStack() {
        return techStacks.stream()
                .map(ProjectTechStack::getTechnology)
                .toArray(String[]::new);
    }

    public void setTechStack(String[] techStack) {
        this.techStacks.clear();
        if (techStack != null) {
            for (String tech : techStack) {
                this.techStacks.add(new ProjectTechStack(this, tech));
            }
        }
    }

}