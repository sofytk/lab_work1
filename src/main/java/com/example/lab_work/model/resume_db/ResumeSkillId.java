package com.example.lab_work.model.resume_db;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ResumeSkillId implements Serializable {
    private Integer resume;
    private Integer skill;
}
