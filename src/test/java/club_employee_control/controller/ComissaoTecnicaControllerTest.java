package club_employee_control.controller;

import club_employee_control.dto.DemissaoRequest;
import club_employee_control.entity.ComissaoTecnica;
import club_employee_control.entity.Role;
import club_employee_control.entity.Usuario;
import club_employee_control.repository.ComissaoTecnicaRepository;
import club_employee_control.repository.UsuarioRepository;
import club_employee_control.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ComissaoTecnicaControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ComissaoTecnicaRepository comissaoTecnicaRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private JwtService jwtService;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private ObjectMapper objectMapper;

    private String token;
    private UUID comissaoId;

    @BeforeEach
    void setUp() {
        String senhaCriptografada = passwordEncoder.encode("senha123");
        Usuario usuario = new Usuario("admin@teste.com", senhaCriptografada, Role.ADMIN);
        usuarioRepository.save(usuario);
        token = jwtService.gerarToken(usuario);

        ComissaoTecnica membro = new ComissaoTecnica(
                "Carlos Técnico",
                LocalDate.of(2022, 1, 10),
                "Assistente Técnico",
                new BigDecimal("50000.00"),
                24
        );
        comissaoId = comissaoTecnicaRepository.save(membro).getId();
    }

    @AfterEach
    void tearDown() {
        comissaoTecnicaRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    // ─── DEMITIR ─────────────────────────────────────────────────────────────────

    @Test
    void demitir_sucesso() throws Exception {
        String jsonRequest = "{\"dataDemissao\": \"2026-04-07\"}";

        mockMvc.perform(patch("/comissao-tecnica/" + comissaoId + "/demitir")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    void demitir_membroNaoEncontrado() throws Exception {
        String jsonRequest = "{\"dataDemissao\": \"2026-04-07\"}";

        UUID idInexistente = UUID.randomUUID();

        mockMvc.perform(patch("/comissao-tecnica/" + idInexistente + "/demitir")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON) // <- Falta isso
                        .content(jsonRequest))                   // <- Falta enviar o corpo
                .andExpect(status().isNotFound());       // Agora sim ele vai retornar 404!
    }

    // ─── AUMENTAR SALÁRIO ─────────────────────────────────────────────────────────

    @Test
    void aumentarSalario_sucesso() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("percentual", 10));

        mockMvc.perform(patch("/comissao-tecnica/" + comissaoId + "/aumentar-salario")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.salario").value(55000.00));
    }

    @Test
    void aumentarSalario_percentualZero_retorna400() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("percentual", 0));

        mockMvc.perform(patch("/comissao-tecnica/" + comissaoId + "/aumentar-salario")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    // ─── DIMINUIR SALÁRIO ─────────────────────────────────────────────────────────

    @Test
    void diminuirSalario_sucesso() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("percentual", 20));

        mockMvc.perform(patch("/comissao-tecnica/" + comissaoId + "/diminuir-salario")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.salario").value(40000.00));
    }

    @Test
    void diminuirSalario_resultadoNegativo_retorna400() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("percentual", 100));

        mockMvc.perform(patch("/comissao-tecnica/" + comissaoId + "/diminuir-salario")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void diminuirSalario_semToken_retorna401() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("percentual", 10));

        mockMvc.perform(patch("/comissao-tecnica/" + comissaoId + "/diminuir-salario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized());
    }
}