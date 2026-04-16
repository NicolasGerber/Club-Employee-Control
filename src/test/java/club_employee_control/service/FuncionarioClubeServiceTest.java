package club_employee_control.service;

import club_employee_control.dto.AjusteSalarioRequest;
import club_employee_control.dto.DemissaoRequest;
import club_employee_control.entity.FuncionarioClube;
import club_employee_control.exception.RecursoNaoEncontradoException;
import club_employee_control.repository.FuncionarioClubeRepository;
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

@ExtendWith(MockitoExtension.class)
class FuncionarioClubeServiceTest {

    @Mock
    FuncionarioClubeRepository funcionarioClubeRepository;

    @InjectMocks
    FuncionarioClubeService funcionarioClubeService;

    private FuncionarioClube funcionario;
    private UUID id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        funcionario = new FuncionarioClube(
                "Ana",
                LocalDate.of(2021, 3, 15),
                "Coordenadora Administrativa",
                new BigDecimal("50000"),
                24
        );
    }

    // -------------------------
    // demitir()
    // -------------------------

    @Test
    void demitir_sucesso() {
        when(funcionarioClubeRepository.findById(id)).thenReturn(Optional.of(funcionario));
        when(funcionarioClubeRepository.save(any(FuncionarioClube.class))).thenAnswer(i -> i.getArgument(0));

        FuncionarioClube resultado = funcionarioClubeService.demitir(id, new DemissaoRequest(LocalDate.of(2025, 6, 1)));

        assertFalse(resultado.isAtivo());
        assertEquals(LocalDate.of(2025, 6, 1), resultado.getDataDemissao());
        verify(funcionarioClubeRepository).save(funcionario);
    }

    @Test
    void demitir_funcionarioNaoEncontrado_lancaExcecao() {
        when(funcionarioClubeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                RecursoNaoEncontradoException.class,
                () -> funcionarioClubeService.demitir(id, new DemissaoRequest(LocalDate.of(2025, 6, 1)))
        );

        verify(funcionarioClubeRepository, never()).save(any());
    }

    // -------------------------
    // aumentarSalario()
    // -------------------------

    @Test
    void aumentarSalario_sucesso() {
        when(funcionarioClubeRepository.findById(id)).thenReturn(Optional.of(funcionario));
        when(funcionarioClubeRepository.save(any(FuncionarioClube.class))).thenAnswer(i -> i.getArgument(0));

        FuncionarioClube resultado = funcionarioClubeService.aumentarSalario(id, new AjusteSalarioRequest(new BigDecimal("10")));

        assertEquals(0, new BigDecimal("55000").compareTo(resultado.getSalario()));
    }

    @Test
    void aumentarSalario_funcionarioNaoEncontrado_lancaExcecao() {
        when(funcionarioClubeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                RecursoNaoEncontradoException.class,
                () -> funcionarioClubeService.aumentarSalario(id, new AjusteSalarioRequest(new BigDecimal("10")))
        );
    }

    // -------------------------
    // diminuirSalario()
    // -------------------------

    @Test
    void diminuirSalario_sucesso() {
        when(funcionarioClubeRepository.findById(id)).thenReturn(Optional.of(funcionario));
        when(funcionarioClubeRepository.save(any(FuncionarioClube.class))).thenAnswer(i -> i.getArgument(0));

        FuncionarioClube resultado = funcionarioClubeService.diminuirSalario(id, new AjusteSalarioRequest(new BigDecimal("20")));

        assertEquals(0, new BigDecimal("40000").compareTo(resultado.getSalario()));
    }

    @Test
    void diminuirSalario_resultadoNegativo_lancaExcecao() {
        when(funcionarioClubeRepository.findById(id)).thenReturn(Optional.of(funcionario));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> funcionarioClubeService.diminuirSalario(id, new AjusteSalarioRequest(new BigDecimal("100")))
        );

        assertEquals("Redução inválida: salário não pode ser zero ou negativo", ex.getMessage());
        verify(funcionarioClubeRepository, never()).save(any());
    }

    @Test
    void diminuirSalario_funcionarioNaoEncontrado_lancaExcecao() {
        when(funcionarioClubeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                RecursoNaoEncontradoException.class,
                () -> funcionarioClubeService.diminuirSalario(id, new AjusteSalarioRequest(new BigDecimal("20")))
        );
    }
}