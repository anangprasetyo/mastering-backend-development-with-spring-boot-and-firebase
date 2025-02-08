package id.backend.session2_2.dto.lesson;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonPostRequestDTO {

    @NotBlank(message = "Name tidak boleh kosong")
    @Size(max = 50, message = "Jumlah karakter name tidak boleh lebih dari 50")
    private String name;

    private Integer student_id;
}
