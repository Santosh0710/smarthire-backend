package smarthirebackend.service;

import smarthirebackend.dto.request.RegisterRequest;
import smarthirebackend.dto.response.AuthResponse;

public interface AuthService
{
    // Contract - we promise this method will exist
    // Implementation will be in AuthServiceImpl
    AuthResponse register(RegisterRequest request);

    // New - login method
    AuthResponse login(String email, String password);
}
