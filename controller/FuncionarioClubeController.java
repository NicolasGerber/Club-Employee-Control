package club_employee_control.controller;

import club_employee_control.entity.FuncionarioClube;
import club_employee_control.exception.RecursoNaoEncontradoException;
import club_employee_control.service.FuncionarioClubeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<FuncionarioClube> salvar(@RequestBody FuncionarioClube funcionario) {
        return ResponseEntity.ok(funcionarioClubeService.salvar(funcionario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioClube> atualizar(@PathVariable UUID id,
                                                      @RequestBody FuncionarioClube dadosNovos) {
        return ResponseEntity.ok(funcionarioClubeService.atualizar(id, dadosNovos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        funcionarioClubeService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}