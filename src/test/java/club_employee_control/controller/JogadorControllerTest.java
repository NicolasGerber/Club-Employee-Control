package club_employee_control.controller;

import club_employee_control.entity.Jogador;
import club_employee_control.entity.Role;
import club_employee_control.entity.Usuario;
import club_employee_control.repository.JogadorRepository;
import club_employee_control.repository.UsuarioRepository;
import club_employee_control.service.JwtService;                  // (1) pacote correto
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class JogadorControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private JogadorRepository jogadorRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private JwtService jwtService;
    @Autowired private PasswordEncoder passwordEncoder;

    private String token;
    private UUID jogadorId;

    @BeforeEach
    void setup() {
        // (2) Construtor público: Usuario(email, senha, role)
        // senha precisa de hash BCrypt — igual ao que UsuarioService faz
        String senhaCriptografada = passwordEncoder.encode("senha123");
        Usuario usuario = new Usuario("teste@clube.com", senhaCriptografada, Role.USER);
        usuarioRepository.save(usuario);

        // (3) gerarToken recebe UserDetails — Usuario já implementa UserDetails
        token = jwtService.gerarToken(usuario);

        // (4) Jogador para os testes de PATCH
        Jogador jogador = new Jogador(
                "Carlos Tevez",
                LocalDate.of(2023, 1, 15),
                "Atacante",
                new BigDecimal("50000.00"),
                12,
                10
        );
        jogadorId = jogadorRepository.save(jogador).getId();
    }

    @AfterEach
    void limpar() {
        jogadorRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    // ── DEMITIR ──────────────────────────────────────

    @Test
    void demitir_sucesso() throws Exception {
        mockMvc.perform(patch("/jogadores/" + jogadorId + "/demitir")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "dataDemissao": "2024-06-01" }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ativo").value(false))
                .andExpect(jsonPath("$.dataDemissao").value("2024-06-01"));
    }

    @Test
    void demitir_jogadorNaoEncontrado() throws Exception {
        mockMvc.perform(patch("/jogadores/" + UUID.randomUUID() + "/demitir")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "dataDemissao": "2024-06-01" }
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void demitir_semToken_retorna401() throws Exception {
        mockMvc.perform(patch("/jogadores/" + jogadorId + "/demitir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "dataDemissao": "2024-06-01" }
                                """))
                .andExpect(status().isUnauthorized());
    }

    // ── AUMENTAR SALÁRIO ─────────────────────────────

    @Test
    void aumentarSalario_sucesso() throws Exception {
        mockMvc.perform(patch("/jogadores/" + jogadorId + "/aumentar-salario")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "percentual": 10 }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.salario").value(55000.00));
    }

    @Test
    void aumentarSalario_percentualZero_retorna400() throws Exception {
        mockMvc.perform(patch("/jogadores/" + jogadorId + "/aumentar-salario")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "percentual": 0 }
                                """))
                .andExpect(status().isBadRequest());
    }

    // ── DIMINUIR SALÁRIO ─────────────────────────────

    @Test
    void diminuirSalario_sucesso() throws Exception {
        mockMvc.perform(patch("/jogadores/" + jogadorId + "/diminuir-salario")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "percentual": 20 }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.salario").value(40000.00));
    }

    @Test
    void diminuirSalario_resultadoNegativo_retorna400() throws Exception {
        mockMvc.perform(patch("/jogadores/" + jogadorId + "/diminuir-salario")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "percentual": 100 }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void diminuirSalario_semToken_retorna401() throws Exception {
        mockMvc.perform(patch("/jogadores/" + jogadorId + "/diminuir-salario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "percentual": 10 }
                                """))
                .andExpect(status().isUnauthorized());
    }
}