package id.backend.session_2.dto.user;

import java.util.List;

import id.backend.session_2.dto.task.TaskGetRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserGetRequestDTO {
    private int id;
    private String name;
    private String email;
    private String role;
    private List<TaskGetRequestDTO> tasks;
}
