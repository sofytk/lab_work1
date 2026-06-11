package com.example.lab_work.model.projects_db;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "project_member")
@IdClass(ProjectMemberId.class)
public class ProjectMember {

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id", nullable = false)
    private ProjectDbPerson person;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "role_name", length = 100)
    private String roleName;

    public ProjectMember(ProjectDbPerson person, Project project, String roleName) {
        this.person = person;
        this.project = project;
        this.roleName = roleName;
    }
}