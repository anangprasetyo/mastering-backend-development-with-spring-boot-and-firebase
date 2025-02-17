package id.backend.session5_2.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import id.backend.session5_2.dto.student.StudentGetRequestDTO;
import id.backend.session5_2.dto.student.StudentPostRequestDTO;
import id.backend.session5_2.dto.student.StudentPutRequestDTO;
import id.backend.session5_2.dto.student.StudentTokenGetRequestDTO;
import id.backend.session5_2.dto.student.StudentTokenPostRequestDTO;
import id.backend.session5_2.service.StudentService;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // endpoint get all students
    @GetMapping
    public List<StudentGetRequestDTO> getAllStudents() {
        return studentService.getAllStudents();
    }

    // endpoint get student by id
    @GetMapping("/{id}")
    public StudentGetRequestDTO getStudentById(@PathVariable int id) {
        return studentService.getStudentById(id);
    }

    // endpoint create new student
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public StudentGetRequestDTO createStudent(@RequestBody StudentPostRequestDTO studentDto) {
        return studentService.createStudent(studentDto);
    }

    // endpoint update student
    // patch request digunakan untuk update sebagian data
    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public StudentGetRequestDTO updateStudent(@PathVariable int id, @RequestBody StudentPutRequestDTO studentDto) {
        return studentService.updateStudent(id, studentDto);
    }

    // endpoint delete student
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteStudent(@PathVariable int id) {
        return studentService.deleteStudent(id);
    }

    // endpoint add student token
    @PostMapping(path = "/token", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public StudentTokenGetRequestDTO saveToken(@RequestBody StudentTokenPostRequestDTO studentTokenDto) {
        return studentService.saveToken(studentTokenDto.getStudent_id(), studentTokenDto.getToken());
    }
}