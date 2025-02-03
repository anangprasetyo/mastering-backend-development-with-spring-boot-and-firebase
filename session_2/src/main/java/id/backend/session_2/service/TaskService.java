package id.backend.session_2.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.backend.session_2.dto.task.TaskGetRequestDTO;
import id.backend.session_2.dto.task.TaskPostRequestDTO;
import id.backend.session_2.dto.task.TaskPutRequestDTO;
import id.backend.session_2.repository.TaskRepository;
import id.backend.session_2.repository.UserRepository;
import jakarta.transaction.Transactional;
import id.backend.session_2.model.Priority;
import id.backend.session_2.model.Task;
import id.backend.session_2.model.User;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    // convert Task to get request DTO
    public TaskGetRequestDTO convertToGetRequestDTO(Task task) {
        return TaskGetRequestDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .priority(task.getPriority().name())
                .status(task.getStatus())
                .build();
    }

    // convert Task to post request DTO
    public TaskPostRequestDTO convertToPostRequestDTO(Task task) {
        return TaskPostRequestDTO.builder().title(task.getTitle()).description(task.getDescription())
                .priority(task.getDescription()).status(task.getStatus()).user_id(task.getUser().getId()).build();
    }

    // convert Task to put request DTO
    public TaskPutRequestDTO convertToPutRequestDTO(Task task) {
        return TaskPutRequestDTO.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .priority(task.getPriority().name())
                .status(task.getStatus()).build();
    }

    // get all task
    public List<TaskGetRequestDTO> getAllTask() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(this::convertToGetRequestDTO).collect(Collectors.toList());
    }

    // get task by id
    public TaskGetRequestDTO getTaskById(int id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task dengan id: " + id + " tidak ditemukan"));
        return this.convertToGetRequestDTO(task);
    }

    // get task by user id
    public List<TaskGetRequestDTO> getTastByUserId(int user_id) {
        List<Task> tasks = taskRepository.findTaskByUser(user_id);
        return tasks.stream().map(this::convertToGetRequestDTO).collect(Collectors.toList());
    }

    // create new task
    @Transactional
    public TaskGetRequestDTO createTask(TaskPostRequestDTO taskDto) {
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setPriority(Priority.valueOf(taskDto.getPriority()));
        task.setStatus(taskDto.getStatus());

        User user = userRepository.findById(taskDto.getUser_id()).orElseThrow(
                () -> new RuntimeException("User dengan id: " + taskDto.getUser_id() + " tidak ditemukan"));
        task.setUser(user);
        Task savedTask = taskRepository.save(task);
        return convertToGetRequestDTO(savedTask);
    }

    // update task
    @Transactional
    public TaskGetRequestDTO updateTask(TaskPutRequestDTO taskDto, int id) {
        Task selectedTask = taskRepository.findById(id).get();
        if (Objects.nonNull(taskDto.getTitle())) {
            selectedTask.setTitle(taskDto.getTitle());
        }

        if (Objects.nonNull(taskDto.getDescription())) {
            selectedTask.setDescription(taskDto.getDescription());
        }

        if (Objects.nonNull(taskDto.getPriority())) {
            selectedTask.setPriority(Priority.valueOf(taskDto.getPriority()));
        }

        if (Objects.nonNull(taskDto.getStatus())) {
            selectedTask.setStatus(taskDto.getStatus());
        }

        Task updateTask = taskRepository.save(selectedTask);
        return convertToGetRequestDTO(updateTask);
    }

    // delete task
    public Map<String, Object> deleteTask(int id) {
        try {
            Task deteletedTask = taskRepository.findById(id).get();
            taskRepository.delete(deteletedTask);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Berhasil menghapus task");
            return response;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Gagal menghapus task");
            return response;
        }
    }
}
