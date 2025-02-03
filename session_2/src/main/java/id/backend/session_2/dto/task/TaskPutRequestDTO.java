package id.backend.session_2.dto.task;

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

    private String title;
    @Size(max = 200, message = "Jumlah karakter description tidak boleh lebih dari 200")
    private String description;
    private String priority;
    private boolean status;

    public boolean getStatus() {
        return status;
    }
}