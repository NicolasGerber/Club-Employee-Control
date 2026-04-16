package club_employee_control.service;

import club_employee_control.dto.AjusteSalarioRequest;
import club_employee_control.dto.DemissaoRequest;
import club_employee_control.entity.Jogador;
import club_employee_control.exception.RecursoNaoEncontradoException;
import club_employee_control.repository.JogadorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // (1)
class JogadorServiceTest {

    @Mock
    JogadorRepository jogadorRepository; // (2)

    @InjectMocks
    JogadorService jogadorService; // (3)

    private Jogador jogador;
    private UUID id;


    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        jogador = new Jogador(
                "Carlos",
                LocalDate.of(2020, 1, 1),
                "Atacante",
                new BigDecimal("50000"),
                12,
                10
        );
    }
    @Test
    void demitir_sucesso() {
        // ARRANGE
        when(jogadorRepository.findById(id)).thenReturn(Optional.of(jogador)); // (5)
        when(jogadorRepository.save(any(Jogador.class))).thenAnswer(i -> i.getArgument(0)); // (6)

        DemissaoRequest request = new DemissaoRequest(LocalDate.of(2025, 1, 1));

        Jogador resultado = jogadorService.demitir(id, request);

        assertFalse(resultado.isAtivo()); // (7)
        assertEquals(LocalDate.of(2025, 1, 1), resultado.getDataDemissao());
        verify(jogadorRepository).save(jogador); // (8)
    }

    @Test
    void demitir_jogadorNaoEncontrado_lancaExcecao() {
        when(jogadorRepository.findById(id)).thenReturn(Optional.empty()); // (9)

        DemissaoRequest request = new DemissaoRequest(LocalDate.of(2025, 1, 1));

        assertThrows(
                RecursoNaoEncontradoException.class,
                () -> jogadorService.demitir(id, request) // (10)
        );

        verify(jogadorRepository, never()).save(any()); // (11)
    }


    @Test
    void aumentarSalario_sucesso() {

        when(jogadorRepository.findById(id)).thenReturn(Optional.of(jogador));
        when(jogadorRepository.save(any(Jogador.class))).thenAnswer(i -> i.getArgument(0));

        AjusteSalarioRequest request = new AjusteSalarioRequest(new BigDecimal("10")); // 10%


        Jogador resultado = jogadorService.aumentarSalario(id, request);


        assertEquals(0, new BigDecimal("55000").compareTo(resultado.getSalario()));
    }

    @Test
    void aumentarSalario_jogadorNaoEncontrado_lancaExcecao() {

        when(jogadorRepository.findById(id)).thenReturn(Optional.empty());

        AjusteSalarioRequest request = new AjusteSalarioRequest(new BigDecimal("10"));


        assertThrows(
                RecursoNaoEncontradoException.class,
                () -> jogadorService.aumentarSalario(id, request)
        );
    }


    @Test
    void diminuirSalario_sucesso() {
        when(jogadorRepository.findById(id)).thenReturn(Optional.of(jogador));
        when(jogadorRepository.save(any(Jogador.class))).thenAnswer(i -> i.getArgument(0));

        AjusteSalarioRequest request = new AjusteSalarioRequest(new BigDecimal("20")); // 20%

        Jogador resultado = jogadorService.diminuirSalario(id, request);


        assertEquals(0, new BigDecimal("40000").compareTo(resultado.getSalario()));
    }

    @Test
    void diminuirSalario_resultadoNegativo_lancaExcecao() {

        when(jogadorRepository.findById(id)).thenReturn(Optional.of(jogador));

        AjusteSalarioRequest request = new AjusteSalarioRequest(new BigDecimal("100")); // 100% → zero


        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> jogadorService.diminuirSalario(id, request) // (14)
        );

        assertEquals("Redução inválida: salário não pode ser zero ou negativo", ex.getMessage()); // (15)
        verify(jogadorRepository, never()).save(any()); // (16)
    }

    @Test
    void diminuirSalario_jogadorNaoEncontrado_lancaExcecao() {
        when(jogadorRepository.findById(id)).thenReturn(Optional.empty());

        AjusteSalarioRequest request = new AjusteSalarioRequest(new BigDecimal("20"));

        assertThrows(
                RecursoNaoEncontradoException.class,
                () -> jogadorService.diminuirSalario(id, request)
        );
    }
}