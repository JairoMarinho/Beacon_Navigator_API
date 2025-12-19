package com.beaconnavigator.api.controllers;

import com.beaconnavigator.api.services.AvatarService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/usuarios/me")
public class UsuarioMeController {

    private final AvatarService avatarService;

    public UsuarioMeController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "/avatar", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadAvatar(
            Authentication authentication,
            @RequestPart("file") MultipartFile file
    ) throws Exception {

        // No seu filtro JWT, principal = email (subject)
        String email = (String) authentication.getPrincipal();

        String avatarUrl = avatarService.uploadForUser(email, file);
        return ResponseEntity.ok(Map.of("avatarUrl", avatarUrl));
    }
}
