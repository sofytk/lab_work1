package com.example.lab_work.model.data_warehouse;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "warehouse_person")
@Getter
@Setter
public class WarehousePerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "age")
    private Short age;

    @Column(name = "skills", columnDefinition = "TEXT[]")
    private String[] skills;

    @Column(name = "salary")
    private Integer salary;

    @Column(name = "bonus")
    private Integer bonus;
}