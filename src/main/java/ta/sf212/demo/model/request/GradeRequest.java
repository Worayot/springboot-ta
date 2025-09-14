package ta.sf212.demo.model.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request DTO for creating or updating a Grade")
public class GradeRequest {

    @Schema(description = "The ID of the grade, optional for creation", example = "1")
    private Long id;

    @Schema(description = "The name of the grade (e.g., 'Midterm Exam')", example = "Midterm Exam")
    private String name;
    
    @Schema(description = "The score for the grade", example = "85")
    private int score;
    
    @Schema(description = "The ID of the student this grade belongs to", example = "123")
    private Long studentId;
    
    @Schema(description = "The ID of the user who submitted this grade", example = "456")
    private Long userId;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
