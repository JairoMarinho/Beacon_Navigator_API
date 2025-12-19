package com.beaconnavigator.api.services;

import com.beaconnavigator.api.models.Usuario;
import com.beaconnavigator.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.*;
import java.util.Set;
import java.util.UUID;

@Service
public class AvatarService {

    private static final Set<String> ALLOWED = Set.of("image/jpeg", "image/png", "image/webp");
    private static final long MAX_BYTES = 3L * 1024 * 1024; // 3MB

    private final UsuarioRepository usuarioRepository;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    public AvatarService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public String uploadForUser(String email, MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo inválido.");
        }
        if (file.getSize() > MAX_BYTES) {
            throw new IllegalArgumentException("Arquivo excede 3MB.");
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED.contains(contentType)) {
            throw new IllegalArgumentException("Formato inválido. Use JPG/PNG/WebP.");
        }

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        // Gera nome seguro
        String ext = switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> "";
        };

        Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(dir);

        String filename = "avatar_" + usuario.getId() + "_" + UUID.randomUUID() + ext;
        Path target = dir.resolve(filename).normalize();

        // Proteção extra contra path traversal
        if (!target.startsWith(dir)) {
            throw new IllegalStateException("Caminho inválido.");
        }

        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }

        // URL pública servida pelo ResourceHandler
        String avatarUrl = "/uploads/" + filename;

        usuario.setAvatarUrl(avatarUrl);
        usuarioRepository.save(usuario);

        return avatarUrl;
    }
}
