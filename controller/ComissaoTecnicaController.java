package club_employee_control.controller;

import club_employee_control.entity.ComissaoTecnica;
import club_employee_control.exception.RecursoNaoEncontradoException;
import club_employee_control.service.ComissaoTecnicaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/comissao")
public class ComissaoTecnicaController {

    private final ComissaoTecnicaService comissaoTecnicaService;

    public ComissaoTecnicaController(ComissaoTecnicaService comissaoTecnicaService) {
        this.comissaoTecnicaService = comissaoTecnicaService;
    }

    @GetMapping
    public ResponseEntity<List<ComissaoTecnica>> listarTodos() {
        return ResponseEntity.ok(comissaoTecnicaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComissaoTecnica> buscarPorId(@PathVariable UUID id) {
        return comissaoTecnicaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Membro não encontrado: " + id));
    }

    @PostMapping
    public ResponseEntity<ComissaoTecnica> salvar(@RequestBody ComissaoTecnica membro) {
        return ResponseEntity.ok(comissaoTecnicaService.salvar(membro));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComissaoTecnica> atualizar(@PathVariable UUID id,
                                                     @RequestBody ComissaoTecnica dadosNovos) {
        return ResponseEntity.ok(comissaoTecnicaService.atualizar(id, dadosNovos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        comissaoTecnicaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}