package com.example.lab_work.model.resume_db;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity(name = "ResumeDbPerson")
@Table(name = "person")
public class ResumeDbPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "full_name", length = 255, nullable = false)
    private String fullName;

    @Column(nullable = true)
    private Short age;

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL, optional = true, orphanRemoval = true)
    private Resume resume;

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL, optional = true, orphanRemoval = true)
    private Salary salary;

    public ResumeDbPerson() {
    }

    public ResumeDbPerson(Integer id, String fullName, Short age) {
        this.id = id;
        this.fullName = fullName;
        this.age = age;
    }

    @Transient
    public String[] getSkillsArray() {
        return resume != null ? resume.getSkillsArray() : new String[0];
    }
}
