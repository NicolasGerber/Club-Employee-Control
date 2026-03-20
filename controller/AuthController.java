package club_employee_control.controller;

import club_employee_control.dto.LoginRequest;
import club_employee_control.dto.LoginResponse;
import club_employee_control.dto.RegistroRequest;
import club_employee_control.entity.Usuario;
import club_employee_control.service.JwtService;
import club_employee_control.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UsuarioService usuarioService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.senha())
        );

        Usuario usuario = (Usuario) authentication.getPrincipal();
        String token = jwtService.gerarToken(usuario);

        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/registrar")
    public ResponseEntity<Void> registrar(@RequestBody RegistroRequest request) {
        usuarioService.registrar(request);
        return ResponseEntity.status(201).build();
    }
}