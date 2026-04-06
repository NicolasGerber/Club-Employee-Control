package club_employee_control.controller;

import club_employee_control.entity.Jogador;
import club_employee_control.exception.RecursoNaoEncontradoException;
import club_employee_control.service.JogadorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import club_employee_control.dto.AjusteSalarioRequest;
import club_employee_control.dto.DemissaoRequest;
import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

@RestController
@RequestMapping("/jogadores")
public class JogadorController {

    private final JogadorService jogadorService;

    public JogadorController(JogadorService jogadorService) {
        this.jogadorService = jogadorService;
    }

    @GetMapping
    public ResponseEntity<Page<Jogador>> listarTodos(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(jogadorService.listarTodos(pageable));
    }

    @PostMapping
    public ResponseEntity<Jogador> salvar(@Valid @RequestBody Jogador jogador) {
        return ResponseEntity.ok(jogadorService.salvar(jogador));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Jogador> atualizar(@PathVariable UUID id,
                                             @Valid @RequestBody Jogador dadosNovos) {
        return ResponseEntity.ok(jogadorService.atualizar(id, dadosNovos));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        jogadorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/demitir")
    public ResponseEntity<Jogador> demitir(@PathVariable UUID id,
                                           @Valid @RequestBody DemissaoRequest request) {
        return ResponseEntity.ok(jogadorService.demitir(id, request));
    }

    @PatchMapping("/{id}/aumentar-salario")
    public ResponseEntity<Jogador> aumentarSalario(@PathVariable UUID id,
                                                   @Valid @RequestBody AjusteSalarioRequest request) {
        return ResponseEntity.ok(jogadorService.aumentarSalario(id, request));
    }

    @PatchMapping("/{id}/diminuir-salario")
    public ResponseEntity<Jogador> diminuirSalario(@PathVariable UUID id,
                                                   @Valid @RequestBody AjusteSalarioRequest request) {
        return ResponseEntity.ok(jogadorService.diminuirSalario(id, request));
    }
}