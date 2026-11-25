package com.gms.customer_backend.auth.security;

import com.gms.customer_backend.auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = null;

        // 1. Read cookies from the request
        Cookie[] cookies = request.getCookies();

        // 2. Find the ACCESS_TOKEN cookiex
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("ACCESS_TOKEN".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // 3. Validate the token
        if (token != null && jwtService.isTokenValid(token)) {

            // 4. Extract customer ID from the token
            String customerId = jwtService.extractCustomerId(token);

            // 5. Create authentication object
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            customerId,
                            null,
                            List.of()
                    );

            // 6. Store authentication in Spring Security context
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 7. Continue the filter chain
        filterChain.doFilter(request, response);
    }
}