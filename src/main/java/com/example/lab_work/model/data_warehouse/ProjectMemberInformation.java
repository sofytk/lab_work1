package com.example.lab_work.model.data_warehouse;


import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "project_member_information")

@IdClass(ProjectMemberInformationId.class)
public class ProjectMemberInformation {

    @Id
    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private WarehousePerson person;

    @Id
    @Column(name = "project_id", nullable = false)
    private Integer projectId;

    @Column(name = "role")
    private String role;

    public WarehousePerson getPerson() {
        return person;
    }

    public void setPerson(WarehousePerson person) {
        this.person = person;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
}

