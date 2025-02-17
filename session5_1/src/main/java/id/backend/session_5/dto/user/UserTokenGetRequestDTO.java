package id.backend.session_5.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserTokenGetRequestDTO {
    private int id;
    private UserGetRequestDTO user;
    private String token;
}
