package id.backend.session_3.dto.task;

import id.backend.session_3.model.Priority;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskPutRequestDTO {

    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;
    @Size(max = 200, message = "Description must not exceed 200 characters")
    private String description;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    private Boolean status;
}