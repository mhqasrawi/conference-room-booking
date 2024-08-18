package com.mashreq.room.conference.booking.api;

import com.mashreq.room.conference.booking.dto.AuthenticationRequest;
import com.mashreq.room.conference.booking.dto.LoginResponse;
import com.mashreq.room.conference.booking.exceptions.UnauthorizedException;
import com.mashreq.room.conference.booking.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;

    @PostMapping(produces = "application/json",consumes = "application/json")
    public LoginResponse authenticate(@RequestBody AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        if(!authentication.isAuthenticated())
            throw new UnauthorizedException();
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        return new LoginResponse(jwtUtil.generateToken(userDetails.getUsername()));
    }
}
