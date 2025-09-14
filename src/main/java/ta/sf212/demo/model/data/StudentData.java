package ta.sf212.demo.model.data;

import java.time.LocalDate;
import java.util.List;

import ta.sf212.demo.model.Grade;
import ta.sf212.demo.model.Student;

public class StudentData {
    final private Long id;
    final private String studentId;
    final private String studentName;
    final private String studentSurname;
    final private LocalDate dateCreated;
    final private List<Grade> grades;

    public StudentData(Student student) {
        this.id = student.getId();
        this.studentId = student.getStudentId();
        this.studentName = student.getName();
        this.studentSurname = student.getSurname();
        this.dateCreated = student.getDateCreated();
        this.grades = student.getGrades();
    }

    public Long getId() {
        return id;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentSurname() {
        return studentSurname;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public List<Grade> getGrades() {
        return grades;
    }
}
