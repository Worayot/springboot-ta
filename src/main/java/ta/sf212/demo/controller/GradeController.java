package ta.sf212.demo.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import ta.sf212.demo.model.Grade;
import ta.sf212.demo.model.Student;
import ta.sf212.demo.model.User;
import ta.sf212.demo.model.request.GradeRequest;
import ta.sf212.demo.model.response.common.Header;
import ta.sf212.demo.model.response.common.Response;
import ta.sf212.demo.model.data.GradeData;
import ta.sf212.demo.repository.GradeRepository;
import ta.sf212.demo.repository.StudentRepository;
import ta.sf212.demo.repository.UserRepository;

@RestController
@RequestMapping("/grades")
public class GradeController {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public GradeController(GradeRepository gradeRepository, StudentRepository studentRepository, UserRepository userRepository) {
        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    @Operation(summary = "Create or update a grade", security = @SecurityRequirement(name = "bearerAuth"))
    public Response<GradeData> saveOrUpdateGrade(@RequestBody GradeRequest gradeRequest) {
        // Find Student
        Student student = studentRepository.findById(gradeRequest.getStudentId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Student not found with ID: " + gradeRequest.getStudentId()
                ));

        // Find User (grader/teacher)
        User user = userRepository.findById(gradeRequest.getUserId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found with ID: " + gradeRequest.getUserId()
                ));

        Grade grade;

        if (gradeRequest.getId() != null) {
            // Update existing grade
            grade = gradeRepository.findById(gradeRequest.getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Grade not found with ID: " + gradeRequest.getId()
                    ));
        } else {
            // Create new grade
            grade = new Grade();
        }

        // Always set new values
        grade.setName(gradeRequest.getName());
        grade.setScore(gradeRequest.getScore());
        grade.setStudent(student);
        grade.setUser(user);

        Grade saved = gradeRepository.save(grade);
        GradeData gradeData = new GradeData(saved);

        Header header = new Header(200, "Success");
        return new Response<>(header, gradeData);
    }


    @GetMapping
    @Operation(summary = "Get all grades", security = @SecurityRequirement(name = "bearerAuth"))
    public Response<List<GradeData>> getAllGrades() {
        List<Grade> grades = gradeRepository.findAll();
        List<GradeData> gradeDataList = grades.stream()
                .map(GradeData::new)
                .collect(Collectors.toList());

        Header header = new Header(200, "Success");
        return new Response<>(header, gradeDataList);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get grade by ID", security = @SecurityRequirement(name = "bearerAuth"))
    public Response<GradeData> getGradeById(@PathVariable Long id) {
        Optional<Grade> gradeOptional = gradeRepository.findById(id);

        if (gradeOptional.isPresent()) {
            Grade grade = gradeOptional.get();
            GradeData gradeData = new GradeData(grade);
            Header header = new Header(HttpStatus.OK.value(), "Grade found");
            return new Response<>(header, gradeData);
        } else {
            // Return 200 OK with a null data object
            Header header = new Header(HttpStatus.OK.value(), "Grade not found");
            return new Response<>(header, null);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete grade by ID", security = @SecurityRequirement(name = "bearerAuth"))
    public Response<Void> deleteGrade(@PathVariable Long id) {
        if (gradeRepository.existsById(id)) {
            gradeRepository.deleteById(id);
            Header header = new Header(200, "Grade deleted successfully");
            return new Response<>(header, null);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade not found with ID: " + id);
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Get grades with pagination, search, and sorting", security = @SecurityRequirement(name = "bearerAuth"))
    public Response<List<GradeData>> getGrades(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "id,asc") String sortBy) {

        Sort.Direction direction = Sort.Direction.fromString(sortBy.split(",")[1]);
        String property = sortBy.split(",")[0];
        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by(direction, property));

        Specification<Grade> spec = (q != null && !q.isEmpty()) ? parseQuery(q) : null;
        Page<Grade> page = gradeRepository.findAll(spec, pageable);

        List<GradeData> gradeDataList = page.getContent().stream()
                .map(GradeData::new)
                .collect(Collectors.toList());

        Header header = new Header(200, "Success");
        return new Response<>(header, gradeDataList);
    }

   private Specification<Grade> parseQuery(String q) {
        Specification<Grade> spec = null;
        
        String[] criteria = q.split(",");
        for (String criterion : criteria) {
            String[] parts = criterion.split(":");
            if (parts.length == 2) {
                String key = parts[0].trim();
                String value = parts[1].trim();
                
                Specification<Grade> currentSpec = null;

                switch (key.toLowerCase()) {
                    case "name" -> currentSpec = (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + value.toLowerCase() + "%");
                    case "score" -> {
                        try {
                            int scoreValue = Integer.parseInt(value);
                            currentSpec = (root, query, cb) -> cb.equal(root.get("score"), scoreValue);
                        } catch (NumberFormatException e) {
                            continue;
                        }
                    }
                    case "studentid" -> {
                        try {
                            Long studentIdValue = Long.valueOf(value);
                            currentSpec = (root, query, cb) -> cb.equal(root.get("student").get("id"), studentIdValue);
                        } catch (NumberFormatException e) {
                            continue;
                        }
                    }
                    case "userid" -> {
                        try {
                            Long userIdValue = Long.valueOf(value);
                            currentSpec = (root, query, cb) -> cb.equal(root.get("user").get("id"), userIdValue);
                        } catch (NumberFormatException e) {
                            continue;
                        }
                    }
                }
                
                if (currentSpec != null) {
                    if (spec == null) {
                        spec = currentSpec;
                    } else {
                        spec = spec.and(currentSpec);
                    }
                }
            }
        }
        return spec;
    }
}