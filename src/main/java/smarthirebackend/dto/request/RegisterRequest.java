package smarthirebackend.dto.request;

import jakarta.persistence.Id;
import jakarta.persistence.Index;
import smarthirebackend.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest
{
    @NotBlank(message = "First name is Required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

//    @Email validates that the format of email is valid
    @NotBlank(message = "Email is Requires")
    @Email(message = "Please provide a valid email")
    private String email;

    // @Size ensures password is at least 8 characters
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotNull(message = "Role is Required")
    private Role role;
}
