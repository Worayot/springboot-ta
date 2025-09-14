package ta.sf212.demo.model.data;

import java.time.LocalDate;

import ta.sf212.demo.model.Grade;

public class GradeData {
    private Long id;
    private Long studentId;
    private String name;
    private int score;
    private LocalDate dateCreated;

    public GradeData(Grade grade) {
        this.id = grade.getId();
        this.studentId = grade.getStudent().getId();
        this.name = grade.getName();
        this.score = grade.getScore();
        this.dateCreated = grade.getDateCreated();
    }

    public Long getId() {
        return id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }
}
