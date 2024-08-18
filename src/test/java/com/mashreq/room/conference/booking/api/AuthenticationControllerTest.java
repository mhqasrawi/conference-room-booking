package com.mashreq.room.conference.booking.api;

import com.mashreq.room.conference.booking.dto.AuthenticationRequest;
import com.mashreq.room.conference.booking.dto.LoginResponse;
import com.mashreq.room.conference.booking.exceptions.UnauthorizedException;
import com.mashreq.room.conference.booking.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

class AuthenticationControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidCredentials_whenAuthenticate_thenReturnsToken() {
        AuthenticationRequest request = new AuthenticationRequest("user", "password");
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "password",
                Collections.singleton(new GrantedAuthority() {

                    @Override
                    public String getAuthority() {
                        return "USER";
                    }
                }));
        UserDetails userDetails = org.mockito.Mockito.mock(UserDetails.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(userDetailsService.loadUserByUsername("user")).thenReturn(userDetails);
        when(jwtUtil.generateToken(any())).thenReturn("token");

        LoginResponse response = authenticationController.authenticate(request);

        assertEquals("token", response.getToken());
    }

    @Test
    void givenInvalidCredentials_whenAuthenticate_thenThrowsUnauthorizedException() {
        AuthenticationRequest request = new AuthenticationRequest("user", "wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new UnauthorizedException());

        assertThrows(UnauthorizedException.class, () -> {
            authenticationController.authenticate(request);
        });
    }

    @Test
    void givenNonAuthenticatedUser_whenAuthenticate_thenThrowsUnauthorizedException() {
        AuthenticationRequest request = new AuthenticationRequest("user", "password");
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "password",
                Collections.singleton(new GrantedAuthority() {

                    @Override
                    public String getAuthority() {
                        throw new UnauthorizedException();
                    }
                }));

        when (userDetailsService.loadUserByUsername("user")).thenThrow(UnauthorizedException.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        assertThrows(UnauthorizedException.class, () -> {
            authenticationController.authenticate(request);
        });
    }
}