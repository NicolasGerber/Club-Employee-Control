package club_employee_control.controller;

import club_employee_control.dto.DemissaoRequest;
import club_employee_control.entity.FuncionarioClube;
import club_employee_control.entity.Role;
import club_employee_control.entity.Usuario;
import club_employee_control.repository.FuncionarioClubeRepository;
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
class FuncionarioClubeControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private FuncionarioClubeRepository funcionarioClubeRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private JwtService jwtService;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private ObjectMapper objectMapper;

    private String token;
    private UUID funcionarioId;

    @BeforeEach
    void setup() {
        String senhaCriptografada = passwordEncoder.encode("senha123");
        Usuario usuario = new Usuario("teste@clube.com", senhaCriptografada, Role.USER);
        usuarioRepository.save(usuario);

        token = jwtService.gerarToken(usuario);

        FuncionarioClube funcionario = new FuncionarioClube(
                "Carlos asdes",
                LocalDate.of(2023, 1, 15),
                "Medico",
                new BigDecimal("50000.00"),
                12
        );
        funcionarioId = funcionarioClubeRepository.save(funcionario).getId();
    }

    @AfterEach
    void tearDown() {
        funcionarioClubeRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    // ─── DEMITIR ─────────────────────────────────────────────────────────────────

    @Test
    void demitir_sucesso() throws Exception {
        String jsonRequest = "{\"dataDemissao\": \"2026-04-07\"}";

        mockMvc.perform(patch("/funcionarios/" + funcionarioId + "/demitir")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    void demitir_membroNaoEncontrado() throws Exception {
        String jsonRequest = "{\"dataDemissao\": \"2026-04-07\"}";

        UUID idInexistente = UUID.randomUUID();

        mockMvc.perform(patch("/funcionarios/" + idInexistente + "/demitir")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());
    }

    // ─── AUMENTAR SALÁRIO ─────────────────────────────────────────────────────────

    @Test
    void aumentarSalario_sucesso() throws Exception {
        // Aumentando 10% de 50000 = 55000
        String body = objectMapper.writeValueAsString(Map.of("percentual", 10));

        mockMvc.perform(patch("/funcionarios/" + funcionarioId + "/aumentar-salario")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.salario").value(55000.00));
    }

    @Test
    void aumentarSalario_percentualZero_retorna400() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("percentual", 0));

        mockMvc.perform(patch("/funcionarios/" + funcionarioId + "/aumentar-salario")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    // ─── DIMINUIR SALÁRIO ─────────────────────────────────────────────────────────

    @Test
    void diminuirSalario_sucesso() throws Exception {
        // Diminuindo 20% de 50000 = 40000
        String body = objectMapper.writeValueAsString(Map.of("percentual", 20));

        mockMvc.perform(patch("/funcionarios/" + funcionarioId + "/diminuir-salario")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.salario").value(40000.00));
    }

    @Test
    void diminuirSalario_resultadoNegativo_retorna400() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("percentual", 100)); // Uma redução de 100% ou mais

        mockMvc.perform(patch("/funcionarios/" + funcionarioId + "/diminuir-salario")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void diminuirSalario_semToken_retorna401() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("percentual", 10));

        mockMvc.perform(patch("/funcionarios/" + funcionarioId + "/diminuir-salario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized());
    }
}