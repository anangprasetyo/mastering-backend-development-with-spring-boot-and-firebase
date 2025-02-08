package id.backend.session2_2.service;

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
import id.backend.session2_2.dto.lesson.LessonPostRequestDTO;
import id.backend.session2_2.dto.lesson.LessonPutRequestDTO;
import id.backend.session2_2.model.Lesson;
import id.backend.session2_2.model.Student;
import id.backend.session2_2.repository.LessonRepository;
import id.backend.session2_2.repository.StudentRepository;
import jakarta.transaction.Transactional;

@Service
public class LessonService {
    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private StudentRepository studentRepository;

    // convert Lesson to get request DTO
    public LessonGetRequestDTO convertToGetRequestDTO(Lesson lesson) {
        return LessonGetRequestDTO.builder()
                .id(lesson.getId())
                .name(lesson.getName())
                .build();
    }

    // get all lessons
    public List<LessonGetRequestDTO> getAllLessons() {
        List<Lesson> lessons = lessonRepository.findAll();
        return lessons.stream().map(this::convertToGetRequestDTO).collect(Collectors.toList());
    }

    // get lesson by id
    public LessonGetRequestDTO getLessonById(int id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson", "id", id));
        return this.convertToGetRequestDTO(lesson);
    }

    // get lessons by student id
    public List<LessonGetRequestDTO> getLessonsByStudentId(int student_id) {
        List<Lesson> lessons = lessonRepository.findLessonByStudent(student_id);
        return lessons.stream().map(this::convertToGetRequestDTO).collect(Collectors.toList());
    }

    // create new lesson
    @Transactional
    public LessonGetRequestDTO createLesson(LessonPostRequestDTO lessonDto) {
        if (lessonDto.getName() == null || lessonDto.getName().isEmpty()) {
            throw new InvalidDataException("Lesson name cannot be null or empty");
        }
        if (lessonDto.getStudent_id() == null || lessonDto.getStudent_id() == 0) {
            throw new InvalidDataException("Lesson name cannot be null or zero");
        }

        Lesson lesson = new Lesson();
        lesson.setName(lessonDto.getName());

        Student student = studentRepository.findById(lessonDto.getStudent_id()).orElseThrow(
                () -> new ResourceNotFoundException("Student", "id", lessonDto.getStudent_id()));
        lesson.setStudent(student);
        Lesson savedLesson = lessonRepository.save(lesson);
        return convertToGetRequestDTO(savedLesson);
    }

    // update lesson
    @Transactional
    public LessonGetRequestDTO updateLesson(LessonPutRequestDTO lessonDto, int id) {
        Lesson selectedLesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson", "id", id));

        if (Objects.nonNull(lessonDto.getName())) {
            selectedLesson.setName(lessonDto.getName());
        }

        if (Objects.nonNull(lessonDto.getStudent_id())) {
            Student student = studentRepository.findById(lessonDto.getStudent_id()).orElseThrow(
                    () -> new ResourceNotFoundException("Student", "id", lessonDto.getStudent_id()));
            selectedLesson.setStudent(student);
        }

        Lesson updatedLesson = lessonRepository.save(selectedLesson);
        return convertToGetRequestDTO(updatedLesson);
    }

    // delete lesson
    public Map<String, Object> deleteLesson(int id) {
        try {
            Lesson deletedLesson = lessonRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Lesson", "id", id));
            lessonRepository.delete(deletedLesson);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Berhasil menghapus lesson");
            return response;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Gagal menghapus lesson");
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