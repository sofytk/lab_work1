package com.example.lab_work.model.resume_db;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "salary")
public class Salary {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id", nullable = false, unique = true)
    private ResumeDbPerson person;

    @Column(nullable = false)
    private Integer salary;
    
    @Column(nullable = false)
    private Integer bonus;

    public Salary(ResumeDbPerson person, Integer salary, Integer bonus) {
        this.person = person;
        this.salary = salary;
        this.bonus = bonus;
    }
}
