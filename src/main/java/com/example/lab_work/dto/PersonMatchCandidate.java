package com.example.lab_work.dto;

import com.example.lab_work.model.resume_db.ResumeDbPerson;

public record PersonMatchCandidate(
        ResumeDbPerson person,
        double nameSimilarity,
        boolean ageMatch

) implements Comparable<PersonMatchCandidate> {

    public double getTotalScore() {
        return nameSimilarity * 0.65
                + (ageMatch ? 0.20 : 0);
    }

    @Override
    public int compareTo(PersonMatchCandidate other) {
        return Double.compare(other.getTotalScore(), this.getTotalScore());
    }

    @Override
    public String toString() {
        return String.format("PersonMatchCandidate{person='%s', nameScore=%.2f, ageMatch=%s, totalScore=%.2f}",
                person.getFullName(), nameSimilarity, ageMatch,  getTotalScore());
    }
}
