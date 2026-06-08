package smarthirebackend.service.impl;

import smarthirebackend.dto.request.RegisterRequest;
import smarthirebackend.dto.response.AuthResponse;
import smarthirebackend.exception.EmailAlreadyExistsException;
import smarthirebackend.exception.InvalidCredentialsException;
import smarthirebackend.model.User;
import smarthirebackend.repository.UserRepository;
import smarthirebackend.service.AuthService;
import smarthirebackend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

// @Service marks this as a business logic component
// Spring will automatically create an instance of this class

@Service

// Lombok generates constructor for all final fields
// This is how we do Dependency Injection in modern Spring

@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService
{
//    final means these are injected once and never changed
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request)
    {
        // Step 1: Check if email already exists
        // Never allow two users with same email
        if(userRepository.existsByEmail(request.getEmail()))
        {
           throw new EmailAlreadyExistsException("Email Already Registered:" + request.getEmail());
        }

        // Step 2: Build User object from request
        // Notice: we encrypt the password before saving
        User user = User.builder()
                .first_name(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

//        Step 3 : save user to database
        User savedUser = userRepository.save(user);

        // Step 4: Return response
        // Notice: we never return the password
        return AuthResponse.builder()
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .message("User Registered Successfully")
                .build();

    }

    @Override
    public AuthResponse login(String email , String password)
    {
//        step 1 : Find user by email
//        if user not found , throw exception
        User user = userRepository.findByEmail(email).orElseThrow(() -> new InvalidCredentialsException("Invalid Email or Password: "+ email));

//        Step 2 : check if password matches
//        passwordEncoder.matches() compares plain text with Bcrypt hash
        if(!passwordEncoder.matches(password , user.getPassword()))
        {
            throw new InvalidCredentialsException("Invalid Email or Password");
        }

//        Generate JWT Token
//        we include role as an extra claim in the token
        Map<String , Object> extraClaims = new HashMap<>();
        extraClaims.put("role" , user.getRole().name());

        String token = jwtService.generateToken(extraClaims , user.getEmail());

//        Return Response with token
        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().name())
                .message("Login Successful")
                .build();
    }
}
