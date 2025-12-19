package com.beaconnavigator.api.config;

import com.beaconnavigator.api.models.Usuario;
import com.beaconnavigator.api.repository.UsuarioRepository;
import com.beaconnavigator.api.security.JwtAuthenticationFilter;
import com.beaconnavigator.api.security.JwtService;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import java.util.UUID;

@Configuration
public class SecurityConfig {

    // URL do Frontend para onde redirecionar após login com Google
    // Ajuste se seu React estiver em outra porta
    private static final String FRONTEND_URL = "http://localhost:5173";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, 
                                                   JwtAuthenticationFilter jwtFilter,
                                                   JwtService jwtService,
                                                   UsuarioRepository usuarioRepository) throws Exception {

        http
            // Se você estiver atrás de proxy (Nginx/Cloud), ajuste também forward-headers
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            
            // OAuth2 precisa de cookies temporários para o fluxo de login, mas a API em si continua Stateless
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())

            // (Opcional) FORÇAR HTTPS
            // .requiresChannel(channel -> channel.anyRequest().requiresSecure())

            // Headers de hardening
            .headers(headers -> headers
                .frameOptions(frame -> frame.deny())
                .contentTypeOptions(Customizer.withDefaults())
                .httpStrictTransportSecurity(hsts -> hsts
                    .includeSubDomains(true)
                    .preload(true)
                    .maxAgeInSeconds(31536000)
                )
            )

            // 401 e 403 separados, sempre em JSON
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, e) -> {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    res.getWriter().write("{\"message\":\"Não autenticado\"}");
                })
                .accessDeniedHandler((req, res, e) -> {
                    res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    res.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    res.getWriter().write("{\"message\":\"Acesso negado\"}");
                })
            )

            .authorizeHttpRequests(auth -> auth
                // Não mascarar exceções internas
                .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                .requestMatchers("/error").permitAll()

                // Preflight
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Público
                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/usuarios").permitAll() // Cadastro
                .requestMatchers(HttpMethod.GET, "/usuarios/teste").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/uploads/**").permitAll()
                
                // Endpoints do OAuth2
                .requestMatchers("/oauth2/**", "/login/oauth2/code/**").permitAll()

                // Todo o resto protegido
                .anyRequest().authenticated()
            )

            // CONFIGURAÇÃO DO GOOGLE OAUTH2
            .oauth2Login(oauth2 -> oauth2
                .successHandler((request, response, authentication) -> {
                    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
                    String email = oAuth2User.getAttribute("email");
                    String nome = oAuth2User.getAttribute("name");
                    // String avatarUrl = oAuth2User.getAttribute("picture"); // Se quiser salvar o avatar

                    // Lógica: Verifica se usuário existe, se não, cria.
                    Usuario usuario = usuarioRepository.findByEmail(email).orElseGet(() -> {
                        Usuario novoUsuario = new Usuario();
                        novoUsuario.setEmail(email);
                        novoUsuario.setNomeCompleto(nome != null ? nome : "Usuário Google");
                        // Gera uma senha aleatória forte pois ele loga via Google
                        novoUsuario.setSenha(UUID.randomUUID().toString()); 
                        // Defina um perfil padrão se necessário, ex: novoUsuario.setPerfil(...)
                        return usuarioRepository.save(novoUsuario);
                    });

                    // Gera o JWT para esse usuário
                    String token = jwtService.generateToken(usuario);

                    // Redireciona para o React passando o token na URL
                    // O front deve ler esse token, salvar no localStorage e limpar a URL
                    response.sendRedirect(FRONTEND_URL + "/oauth/callback?token=" + token);
                })
            )

            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}