package smarthirebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AuthResponse
{
//    This token will be sent back after successful login/register
    private String token;
    private String email;
    private String role;
    private String message;
}
