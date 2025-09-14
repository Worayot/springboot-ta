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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import ta.sf212.demo.model.Student;
import ta.sf212.demo.model.data.StudentData;
import ta.sf212.demo.model.request.StudentRequest;
import ta.sf212.demo.model.response.common.Header;
import ta.sf212.demo.model.response.common.Response;
import ta.sf212.demo.repository.StudentRepository;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @PostMapping
    @Operation(summary = "Create or update a student", security = @SecurityRequirement(name = "bearerAuth"))
    public Response<StudentData> saveOrUpdateStudent(@RequestBody StudentRequest studentRequest) {
        Student studentToSave;
        String message;

        if (studentRequest.getId() != null) {
            Optional<Student> existingStudent = studentRepository.findById(studentRequest.getId());
            if (existingStudent.isPresent()) {
                Student updated = existingStudent.get();
                updated.setStudentId(studentRequest.getStudentId());
                updated.setName(studentRequest.getName());
                updated.setSurname(studentRequest.getSurname());
                studentToSave = updated;
                message = "Student updated successfully";
            } else {
                Header notFoundHeader = new Header(HttpStatus.OK.value(), "Student not found");
                return new Response<>(notFoundHeader, null);
            }
        } else {
            Student newStudent = new Student();
            newStudent.setStudentId(studentRequest.getStudentId());
            newStudent.setName(studentRequest.getName());
            newStudent.setSurname(studentRequest.getSurname());
            studentToSave = newStudent;
            message = "Student created successfully";
        }
        
        Student savedStudent = studentRepository.save(studentToSave);
        StudentData studentData = new StudentData(savedStudent);

        Header header = new Header(HttpStatus.OK.value(), message);
        return new Response<>(header, studentData);
    }

    @GetMapping
    @Operation(summary = "Get all students", security = @SecurityRequirement(name = "bearerAuth"))
    public Response<List<StudentData>> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        List<StudentData> studentDataList = students.stream()
            .map(StudentData::new)
            .collect(Collectors.toList());
        
        Header header = new Header(HttpStatus.OK.value(), "Success");
        return new Response<>(header, studentDataList);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get student by ID", security = @SecurityRequirement(name = "bearerAuth"))
    public Response<StudentData> getStudentById(@PathVariable Long id) {
        Optional<Student> studentOptional = studentRepository.findById(id);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            StudentData studentData = new StudentData(student);
            Header header = new Header(HttpStatus.OK.value(), "Student found");
            return new Response<>(header, studentData);
        } else {
            Header header = new Header(HttpStatus.OK.value(), "Student not found");
            return new Response<>(header, null);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete student by ID", security = @SecurityRequirement(name = "bearerAuth"))
    public Response<Void> deleteStudent(@PathVariable Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            Header header = new Header(HttpStatus.OK.value(), "Student deleted successfully");
            return new Response<>(header, null);
        } else {
            Header header = new Header(HttpStatus.OK.value(), "Student not found");
            return new Response<>(header, null);
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Get students with pagination, search, and sorting", security = @SecurityRequirement(name = "bearerAuth"))
    public Response<List<StudentData>> getStudents(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "id,asc") String sortBy) {

        Sort.Direction direction = Sort.Direction.fromString(sortBy.split(",")[1]);
        String property = sortBy.split(",")[0];

        int page = offset / limit;
        
        Sort sort = Sort.by(direction, property);
        Pageable pageable = PageRequest.of(page, limit, sort);

        Specification<Student> spec = null;
        
        if (q != null && !q.isEmpty()) {
            spec = parseQuery(q);
        }

        Page<Student> studentPage = studentRepository.findAll(spec, pageable);
        List<StudentData> studentDataList = studentPage.getContent().stream()
            .map(StudentData::new)
            .collect(Collectors.toList());

        Header header = new Header(HttpStatus.OK.value(), "Success");
        return new Response<>(header, studentDataList);
    }

    private Specification<Student> parseQuery(String q) {
        Specification<Student> spec = null;
        
        String[] criteria = q.split(",");
        for (String criterion : criteria) {
            String[] parts = criterion.split(":");
            if (parts.length == 2) {
                String key = parts[0].trim();
                String value = parts[1].trim();
                
                Specification<Student> currentSpec = null;

                switch (key.toLowerCase()) {
                    case "name" -> currentSpec = (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + value.toLowerCase() + "%");
                    case "surname" -> currentSpec = (root, query, cb) -> cb.like(cb.lower(root.get("surname")), "%" + value.toLowerCase() + "%");
                    case "studentid" -> currentSpec = (root, query, cb) -> cb.like(cb.lower(root.get("studentId")), "%" + value.toLowerCase() + "%");
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