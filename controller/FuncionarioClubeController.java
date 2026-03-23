package club_employee_control.controller;

import club_employee_control.entity.FuncionarioClube;
import club_employee_control.exception.RecursoNaoEncontradoException;
import club_employee_control.service.FuncionarioClubeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
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
    public ResponseEntity<List<FuncionarioClube>> listarTodos() {
        return ResponseEntity.ok(funcionarioClubeService.listarTodos());
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
    public ResponseEntity<FuncionarioClube> atualizar(@PathVariable UUID id, @Valid @RequestBody FuncionarioClube dadosNovos) {
        return ResponseEntity.ok(funcionarioClubeService.atualizar(id, dadosNovos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        funcionarioClubeService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}