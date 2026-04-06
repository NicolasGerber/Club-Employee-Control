package club_employee_control.controller;

import club_employee_control.dto.RegistroRequest;
import club_employee_control.entity.Role;
import club_employee_control.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
// SEM @Transactional na classe — cada teste persiste de verdade
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @AfterEach
    void limpar() {
        usuarioRepository.deleteAll(); // limpa o banco após cada teste
    }

    // -------------------------------------------------------------------------
    // POST /auth/registrar
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Registrar usuário com sucesso retorna 201")
    void registrar_sucesso() throws Exception {
        var request = new RegistroRequest("nicolas@teste.com", "senha123", Role.USER);

        mockMvc.perform(post("/auth/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Registrar com email duplicado retorna 409")
    void registrar_emailDuplicado() throws Exception {
        var request = new RegistroRequest("duplicado@teste.com", "senha123", Role.USER);
        String json = objectMapper.writeValueAsString(request);

        // primeiro registro — sucesso
        mockMvc.perform(post("/auth/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        // segundo registro com mesmo email — conflito
        mockMvc.perform(post("/auth/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    @DisplayName("Registrar com campos inválidos retorna 400")
    void registrar_camposInvalidos() throws Exception {
        var json = """
                {
                    "email": "",
                    "senha": "",
                    "role": "USER"
                }
                """;

        mockMvc.perform(post("/auth/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    // -------------------------------------------------------------------------
    // POST /auth/login
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Login com sucesso retorna token JWT")
    void login_sucesso() throws Exception {
        var registro = new RegistroRequest("login@teste.com", "senha123", Role.USER);
        mockMvc.perform(post("/auth/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registro)))
                .andExpect(status().isCreated());

        var loginJson = """
                {
                    "email": "login@teste.com",
                    "senha": "senha123"
                }
                """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    @DisplayName("Login com credenciais erradas retorna 401")
    void login_credenciaisErradas() throws Exception {
        var loginJson = """
                {
                    "email": "naoexiste@teste.com",
                    "senha": "senhaerrada"
                }
                """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }
}