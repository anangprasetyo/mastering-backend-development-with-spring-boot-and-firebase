package id.backend.session2_2.controller;

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

import id.backend.session2_2.dto.lesson.LessonGetRequestDTO;
import id.backend.session2_2.dto.lesson.LessonPostRequestDTO;
import id.backend.session2_2.dto.lesson.LessonPutRequestDTO;
import id.backend.session2_2.service.LessonService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/lessons")
public class LessonController {
    @Autowired
    private LessonService lessonService;

    // endpoint get all lessons
    @GetMapping
    public List<LessonGetRequestDTO> getAllLessons() {
        return lessonService.getAllLessons();
    }

    // endpoint get lesson by id
    @GetMapping("/{id}")
    public LessonGetRequestDTO getLessonById(@PathVariable int id) {
        return lessonService.getLessonById(id);
    }

    // endpoint get lessons by student id
    @GetMapping("/student/{id}")
    public List<LessonGetRequestDTO> getLessonsByStudentId(@PathVariable int id) {
        return lessonService.getLessonsByStudentId(id);
    }

    // endpoint create lesson
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public LessonGetRequestDTO createLesson(@Valid @RequestBody LessonPostRequestDTO lessonDto) {
        return lessonService.createLesson(lessonDto);
    }

    // endpoint update lesson
    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public LessonGetRequestDTO updateLesson(@PathVariable int id, @RequestBody LessonPutRequestDTO lessonDto) {
        return lessonService.updateLesson(lessonDto, id);
    }

    // endpoint delete lesson
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteLesson(@PathVariable int id) {
        return lessonService.deleteLesson(id);
    }
}