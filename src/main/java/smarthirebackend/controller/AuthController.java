package smarthirebackend.controller;

import smarthirebackend.dto.request.RegisterRequest;
import smarthirebackend.dto.response.AuthResponse;
import smarthirebackend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// @RestController = @Controller + @ResponseBody
// Means every method return JSON automatically
@RestController

// All endpoints in this class start with /api/auth
@RequestMapping("/api/auth")

// Lombok generates contructor for dependency injection
@RequiredArgsConstructor
public class AuthController
{
    private final AuthService authService;

// POST /api/auth/register
    // @Valid triggers the validation annotations in RegisterRequest
    // @RequestBody converts incoming JSON to RegisterRequest object

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request)
    {
        AuthResponse response = authService.register(request);

        // Return 201 Created status with the response body
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}
