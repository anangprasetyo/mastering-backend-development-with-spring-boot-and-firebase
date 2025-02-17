package id.backend.session_5.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import id.backend.session_5.dto.task.TaskGetRequestDTO;
import id.backend.session_5.dto.task.TaskPostRequestDTO;
import id.backend.session_5.dto.task.TaskPutRequestDTO;
import id.backend.session_5.service.TaskService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    // endpoint get all task
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TaskGetRequestDTO> getAllTask() {
        return taskService.getAllTask();
    }

    // endpoint get task by id
    @GetMapping("/{id}")
    public TaskGetRequestDTO getTaskById(@PathVariable int id) {
        return taskService.getTaskById(id);
    }

    // endpoint get tas by user id
    @GetMapping("/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskGetRequestDTO> getTaskByUserId(@PathVariable int id) {
        return taskService.getTaskByUserId(id);
    }

    // endpoint create task
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public TaskGetRequestDTO createTask(@Valid @RequestBody TaskPostRequestDTO taskDto) {
        return taskService.createTask(taskDto);
    }

    // endpoint update task
    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public TaskGetRequestDTO updateTask(@PathVariable int id, @RequestBody TaskPutRequestDTO taskDto) {
        return taskService.updateTask(taskDto, id);
    }

    // endpoint hapus task
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Map<String, Object> deleteTask(@PathVariable int id) {
        return taskService.deleteTask(id);
    }
}
