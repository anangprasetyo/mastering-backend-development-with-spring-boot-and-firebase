package id.backend.session_3.dto.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskGetRequestDTO {
    private int id;
    private String title;
    private String description;
    private String priority;
    private Boolean status;
}