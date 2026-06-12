package smarthirebackend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// OncePerRequestFilter = runs exactly once per request
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // Step 1: Get the Authorization header
        // JWT tokens are sent like: "Bearer eyJhbGci..."
        final String authHeader = request.getHeader("Authorization");

        // Step 2: If no token present, skip this filter
        // The request will be handled by Spring Security normally
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Step 3: Extract the token (remove "Bearer " prefix)
        final String jwt = authHeader.substring(7);

        // Step 4: Extract email from token
        final String userEmail = jwtService.extractEmail(jwt);

        // Step 5: If email found and user not already authenticated
        if (userEmail != null &&
                SecurityContextHolder.getContext()
                        .getAuthentication() == null) {

            // Step 6: Validate the token
            if (jwtService.isTokenValid(jwt, userEmail)) {

                // Step 7: Extract role from token
                String role = jwtService.extractRole(jwt);

                // Step 8: Create authentication token for Spring Security
                // This is how we tell Spring "this user is authenticated"
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userEmail,
                                null,
                                // Set the user's role as authority
                                List.of(new SimpleGrantedAuthority(
                                        "ROLE_" + role))
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request));

                // Step 9: Save authentication in Security Context
                // Now Principal.getName() will return the email
                SecurityContextHolder.getContext()
                        .setAuthentication(authToken);
            }
        }

        // Step 10: Continue to the next filter/controller
        filterChain.doFilter(request, response);
    }
}