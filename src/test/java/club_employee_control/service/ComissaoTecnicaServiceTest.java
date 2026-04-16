package club_employee_control.service;

import club_employee_control.dto.AjusteSalarioRequest;
import club_employee_control.dto.DemissaoRequest;
import club_employee_control.entity.ComissaoTecnica;
import club_employee_control.exception.RecursoNaoEncontradoException;
import club_employee_control.repository.ComissaoTecnicaRepository;
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
class ComissaoTecnicaServiceTest {

    @Mock
    ComissaoTecnicaRepository comissaoTecnicaRepository;

    @InjectMocks
    ComissaoTecnicaService comissaoTecnicaService;

    private ComissaoTecnica membro;
    private UUID id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        membro = new ComissaoTecnica(
                "Roberto",
                LocalDate.of(2020, 1, 1),
                "Assistente Técnico",
                new BigDecimal("50000"),
                12
        );
    }

    // -------------------------
    // demitir()
    // -------------------------

    @Test
    void demitir_sucesso() {
        when(comissaoTecnicaRepository.findById(id)).thenReturn(Optional.of(membro));
        when(comissaoTecnicaRepository.save(any(ComissaoTecnica.class))).thenAnswer(i -> i.getArgument(0));

        ComissaoTecnica resultado = comissaoTecnicaService.demitir(id, new DemissaoRequest(LocalDate.of(2025, 1, 1)));

        assertFalse(resultado.isAtivo());
        assertEquals(LocalDate.of(2025, 1, 1), resultado.getDataDemissao());
        verify(comissaoTecnicaRepository).save(membro);
    }

    @Test
    void demitir_membroNaoEncontrado_lancaExcecao() {
        when(comissaoTecnicaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                RecursoNaoEncontradoException.class,
                () -> comissaoTecnicaService.demitir(id, new DemissaoRequest(LocalDate.of(2025, 1, 1)))
        );

        verify(comissaoTecnicaRepository, never()).save(any());
    }

    // -------------------------
    // aumentarSalario()
    // -------------------------

    @Test
    void aumentarSalario_sucesso() {
        when(comissaoTecnicaRepository.findById(id)).thenReturn(Optional.of(membro));
        when(comissaoTecnicaRepository.save(any(ComissaoTecnica.class))).thenAnswer(i -> i.getArgument(0));

        ComissaoTecnica resultado = comissaoTecnicaService.aumentarSalario(id, new AjusteSalarioRequest(new BigDecimal("10")));

        assertEquals(0, new BigDecimal("55000").compareTo(resultado.getSalario()));
    }

    @Test
    void aumentarSalario_membroNaoEncontrado_lancaExcecao() {
        when(comissaoTecnicaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                RecursoNaoEncontradoException.class,
                () -> comissaoTecnicaService.aumentarSalario(id, new AjusteSalarioRequest(new BigDecimal("10")))
        );
    }

    // -------------------------
    // diminuirSalario()
    // -------------------------

    @Test
    void diminuirSalario_sucesso() {
        when(comissaoTecnicaRepository.findById(id)).thenReturn(Optional.of(membro));
        when(comissaoTecnicaRepository.save(any(ComissaoTecnica.class))).thenAnswer(i -> i.getArgument(0));

        ComissaoTecnica resultado = comissaoTecnicaService.diminuirSalario(id, new AjusteSalarioRequest(new BigDecimal("20")));

        assertEquals(0, new BigDecimal("40000").compareTo(resultado.getSalario()));
    }

    @Test
    void diminuirSalario_resultadoNegativo_lancaExcecao() {
        when(comissaoTecnicaRepository.findById(id)).thenReturn(Optional.of(membro));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> comissaoTecnicaService.diminuirSalario(id, new AjusteSalarioRequest(new BigDecimal("100")))
        );

        assertEquals("Redução inválida: salário não pode ser zero ou negativo", ex.getMessage());
        verify(comissaoTecnicaRepository, never()).save(any());
    }

    @Test
    void diminuirSalario_membroNaoEncontrado_lancaExcecao() {
        when(comissaoTecnicaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                RecursoNaoEncontradoException.class,
                () -> comissaoTecnicaService.diminuirSalario(id, new AjusteSalarioRequest(new BigDecimal("20")))
        );
    }
}