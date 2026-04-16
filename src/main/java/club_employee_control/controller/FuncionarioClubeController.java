package club_employee_control.controller;

import club_employee_control.dto.AjusteSalarioRequest;
import club_employee_control.dto.DemissaoRequest;
import club_employee_control.entity.FuncionarioClube;
import club_employee_control.exception.RecursoNaoEncontradoException;
import club_employee_control.service.FuncionarioClubeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import java.util.UUID;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioClubeController {

    private final FuncionarioClubeService funcionarioClubeService;

    public FuncionarioClubeController(FuncionarioClubeService funcionarioClubeService) {
        this.funcionarioClubeService = funcionarioClubeService;
    }

    @GetMapping
    public ResponseEntity<Page<FuncionarioClube>> listarTodos(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(funcionarioClubeService.listarTodos(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioClube> buscarPorId(@PathVariable UUID id) {
        return funcionarioClubeService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionário não encontrado: " + id));
    }

    @PostMapping
    public ResponseEntity<FuncionarioClube> salvar(@Valid @RequestBody FuncionarioClube funcionarioClube) {
        return ResponseEntity.ok(funcionarioClubeService.salvar(funcionarioClube));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioClube> atualizar(@PathVariable UUID id,
                                                      @Valid @RequestBody FuncionarioClube dadosNovos) {
        return ResponseEntity.ok(funcionarioClubeService.atualizar(id, dadosNovos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        funcionarioClubeService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/demitir")
    public ResponseEntity<FuncionarioClube> demitir(@PathVariable UUID id,
                                                    @Valid @RequestBody DemissaoRequest request) {
        return ResponseEntity.ok(funcionarioClubeService.demitir(id, request));
    }

    @PatchMapping("/{id}/aumentar-salario")
    public ResponseEntity<FuncionarioClube> aumentarSalario(@PathVariable UUID id,
                                                            @Valid @RequestBody AjusteSalarioRequest request) {
        return ResponseEntity.ok(funcionarioClubeService.aumentarSalario(id, request));
    }

    @PatchMapping("/{id}/diminuir-salario")
    public ResponseEntity<FuncionarioClube> diminuirSalario(@PathVariable UUID id,
                                                            @Valid @RequestBody AjusteSalarioRequest request) {
        return ResponseEntity.ok(funcionarioClubeService.diminuirSalario(id, request));
    }
}