package com.example.lab_work.model.projects_db;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ProjectDbPerson")
@Table(name = "person")
public class ProjectDbPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "full_name", length = 255, nullable = false)
    private String fullName;

    @Column(nullable = true)
    private Short age;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<ProjectMember> projectMembers = new ArrayList<>();

    public ProjectDbPerson(Integer id, String fullName, Short age) {
        this.id = id;
        this.fullName = fullName;
        this.age = age;
    }
}