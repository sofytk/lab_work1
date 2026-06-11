package com.example.lab_work.model.resume_db;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "resume")
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id", nullable = false, unique = true)
    private ResumeDbPerson person;

    @Column(name = "experience_years")
    private Integer experienceYears = 0;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<ResumeSkill> skills = new HashSet<>();

    @Transient
    public String[] getSkillsArray() {
        return skills.stream()
            .map(rs -> rs.getSkill().getName())
            .toArray(String[]::new);
    }
}