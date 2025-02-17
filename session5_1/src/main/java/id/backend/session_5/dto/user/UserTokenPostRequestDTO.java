package id.backend.session_5.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserTokenPostRequestDTO {
    private int id;

    @NotNull(message = "User ID is required")
    private int user_id;

    @NotBlank(message = "Token is required")
    private String token;
}