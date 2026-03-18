package club_employee_control.controller;

import club_employee_control.entity.Jogador;
import club_employee_control.exception.RecursoNaoEncontradoException;
import club_employee_control.service.JogadorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/jogadores")
public class JogadorController {

    private final JogadorService jogadorService;

    public JogadorController(JogadorService jogadorService) {
        this.jogadorService = jogadorService;
    }

    @GetMapping
    public ResponseEntity<List<Jogador>> listarTodos() {
        return ResponseEntity.ok(jogadorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Jogador> buscarPorId(@PathVariable UUID id) {
        return jogadorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Jogador não encontrado: " + id));
    }

    @PostMapping
    public ResponseEntity<Jogador> salvar(@RequestBody Jogador jogador) {
        return ResponseEntity.ok(jogadorService.salvar(jogador));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Jogador> atualizar(@PathVariable UUID id,
                                             @RequestBody Jogador dadosNovos) {
        return ResponseEntity.ok(jogadorService.atualizar(id, dadosNovos));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        jogadorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}