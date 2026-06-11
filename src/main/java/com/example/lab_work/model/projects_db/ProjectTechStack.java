package com.example.lab_work.model.projects_db;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "project_tech_stack")
@IdClass(ProjectTechStackId.class)
public class ProjectTechStack {

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Id
    @Column(name = "technology", length = 100, nullable = false)
    private String technology;
}

