package com.beaconnavigator.api.controllers;

import com.beaconnavigator.api.dtos.AuthLoginRequest;
import com.beaconnavigator.api.dtos.AuthLoginResponse;
import com.beaconnavigator.api.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> login(@Valid @RequestBody AuthLoginRequest req) {
        String token = authService.login(req);
        return ResponseEntity.ok(new AuthLoginResponse(token));
    }
}
