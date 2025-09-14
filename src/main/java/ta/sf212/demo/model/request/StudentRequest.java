package ta.sf212.demo.model.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request DTO for creating or updating a Student")
public class StudentRequest {

    @Schema(description = "The ID of the student, optional for creation", example = "1234")
    private Long id;

    @Schema(description = "The student's unique identifier", example = "S1234567")
    private String studentId;

    @Schema(description = "The student's first name", example = "John")
    private String name;

    @Schema(description = "The student's surname", example = "Doe")
    private String surname;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
