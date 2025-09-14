package ta.sf212.demo.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;

@Entity
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key, auto-generated
    private String name;     
    private int score;
    
    @jakarta.persistence.Column(updatable = false)
    private LocalDate dateCreated;  

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false) // FK column in grade table
    @JsonBackReference
    private Student student;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // FK column in grade table
    @JsonBackReference
    private User user;

    // Constructors
    public Grade() {}

    public Grade(String name, int score, Student student, User user) {
        this.name = name;
        this.score = score;
        this.student = student;
        this.user = user;
    }

    @PrePersist
    protected void onCreate() {
        this.dateCreated = LocalDate.now();
    }

    // Getters & Setters
    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDate getDateCreated() { return dateCreated; }

}
