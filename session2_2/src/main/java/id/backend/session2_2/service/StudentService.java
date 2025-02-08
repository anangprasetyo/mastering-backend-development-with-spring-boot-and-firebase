package id.backend.session2_2.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import id.backend.session2_2.dto.lesson.LessonGetRequestDTO;
import id.backend.session2_2.dto.student.StudentGetRequestDTO;
import id.backend.session2_2.dto.student.StudentPostRequestDTO;
import id.backend.session2_2.dto.student.StudentPutRequestDTO;
import id.backend.session2_2.model.Student;
import id.backend.session2_2.repository.StudentRepository;
import jakarta.transaction.Transactional;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private LessonService lessonService;

    // convert Student to get request DTO
    public StudentGetRequestDTO convertToGetRequestDTO(Student student) {
        List<LessonGetRequestDTO> lessonDTOs = (student.getLessons() != null)
                ? student.getLessons().stream().map(lessonService::convertToGetRequestDTO)
                        .collect(Collectors.toList())
                : Collections.emptyList();
        return StudentGetRequestDTO.builder()
                .id(student.getId())
                .name(student.getName())
                .email(student.getEmail())
                .classes(student.getClasses())
                .lessons(lessonDTOs).build();
    }

    // get all students
    public List<StudentGetRequestDTO> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students.stream().map(this::convertToGetRequestDTO).collect(Collectors.toList());
    }

    // get student by id
    public StudentGetRequestDTO getStudentById(int id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        return this.convertToGetRequestDTO(student);
    }

    // create new student
    @Transactional
    public StudentGetRequestDTO createStudent(StudentPostRequestDTO studentDTO) {
        Student student = new Student();
        student.setName(studentDTO.getName());
        student.setEmail(studentDTO.getEmail());
        student.setClasses(studentDTO.getClasses());
        Student savedStudent = studentRepository.save(student);
        return convertToGetRequestDTO(savedStudent);
    }

    // update student
    @Transactional
    public StudentGetRequestDTO updateStudent(int id, StudentPutRequestDTO studentDTO) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));

        if (Objects.nonNull(studentDTO.getName())) {
            student.setName(studentDTO.getName());
        }

        if (Objects.nonNull(studentDTO.getEmail())) {
            student.setEmail(studentDTO.getEmail());
        }

        if (Objects.nonNull(studentDTO.getClasses())) {
            student.setClasses(studentDTO.getClasses());
        }

        Student savedStudent = studentRepository.save(student);
        return convertToGetRequestDTO(savedStudent);
    }

    // delete student
    public Map<String, Object> deleteStudent(int id) {
        try {
            Student student = studentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
            studentRepository.delete(student);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Berhasil menghapus student");
            return response;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Gagal menghapus student");
            return response;
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
            super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public class InvalidDataException extends RuntimeException {
        public InvalidDataException(String message) {
            super(message);
        }
    }
}