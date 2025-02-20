package id.project.education.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import id.project.education.dto.course.CourseGetDTO;
import id.project.education.dto.course.CoursePostDTO;
import id.project.education.dto.course.CoursePutDTO;
import id.project.education.service.CourseService;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<CourseGetDTO> createCourse(@RequestBody CoursePostDTO coursePostDTO) {
        CourseGetDTO courseGetDTO = courseService.createCourse(coursePostDTO);
        return ResponseEntity.ok(courseGetDTO);
    }

    @GetMapping
    public ResponseEntity<List<CourseGetDTO>> getAllCourses() {
        List<CourseGetDTO> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseGetDTO> getCourseById(@PathVariable Long id) {
        CourseGetDTO course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseGetDTO> updateCourse(@PathVariable Long id, @RequestBody CoursePutDTO coursePutDTO) {
        CourseGetDTO courseGetDTO = courseService.updateCourse(id, coursePutDTO);
        return ResponseEntity.ok(courseGetDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}