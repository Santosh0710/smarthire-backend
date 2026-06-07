package smarthirebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponse {

    // HTTP status code (400, 403, 404, 500 etc.)
    private int status;

    // Short error title
    private String error;

    // Detailed message
    private String message;

    // When did this error happen
    private LocalDateTime timestamp;

    // List of validation errors (when multiple fields fail)
    private List<String> errors;
}