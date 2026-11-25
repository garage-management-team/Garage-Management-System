package org.example.garageservicebackend;

import org.example.garageservicebackend.repository.GaragePersonnelRepository;
import org.example.garageservicebackend.repository.GarageManagerRepository;
import org.example.garageservicebackend.service.ManagerAuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManagerAuthServiceTest {

    @Mock
    private GaragePersonnelRepository personnelRepository;

    @Mock
    private GarageManagerRepository managerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private ManagerAuthService managerAuthService;

    @Test
    void shouldAuthenticateManager() {

        Authentication expectedAuthentication =
                mock(Authentication.class);

        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        )).thenReturn(expectedAuthentication);

        Authentication result =
                managerAuthService.authenticateManager(
                        "manager@example.com",
                        "Manager@123"
                );

        assertSame(expectedAuthentication, result);

        verify(authenticationManager).authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        );
    }
}