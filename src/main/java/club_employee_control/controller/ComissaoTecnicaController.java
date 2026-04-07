package club_employee_control.controller;

import club_employee_control.dto.AjusteSalarioRequest;
import club_employee_control.dto.DemissaoRequest;
import club_employee_control.entity.ComissaoTecnica;
import club_employee_control.exception.RecursoNaoEncontradoException;
import club_employee_control.service.ComissaoTecnicaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

@RestController
@RequestMapping("/comissao-tecnica")
public class ComissaoTecnicaController {

    private final ComissaoTecnicaService comissaoTecnicaService;

    public ComissaoTecnicaController(ComissaoTecnicaService comissaoTecnicaService) {
        this.comissaoTecnicaService = comissaoTecnicaService;
    }

    @GetMapping
    public ResponseEntity<Page<ComissaoTecnica>> listarTodos(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(comissaoTecnicaService.listarTodos(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComissaoTecnica> buscarPorId(@PathVariable UUID id) {
        return comissaoTecnicaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Membro não encontrado: " + id));
    }

    @PostMapping
    public ResponseEntity<ComissaoTecnica> salvar(@Valid @RequestBody ComissaoTecnica comissaoTecnica) {
        return ResponseEntity.ok(comissaoTecnicaService.salvar(comissaoTecnica));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComissaoTecnica> atualizar(@PathVariable UUID id,
                                                     @Valid @RequestBody ComissaoTecnica dadosNovos) {
        return ResponseEntity.ok(comissaoTecnicaService.atualizar(id, dadosNovos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        comissaoTecnicaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/demitir")
    public ResponseEntity<ComissaoTecnica> demitir(@PathVariable UUID id,
                                                   @Valid @RequestBody DemissaoRequest request) {
        return ResponseEntity.ok(comissaoTecnicaService.demitir(id, request));
    }

    @PatchMapping("/{id}/aumentar-salario")
    public ResponseEntity<ComissaoTecnica> aumentarSalario(@PathVariable UUID id,
                                                           @Valid @RequestBody AjusteSalarioRequest request) {
        return ResponseEntity.ok(comissaoTecnicaService.aumentarSalario(id, request));
    }

    @PatchMapping("/{id}/diminuir-salario")
    public ResponseEntity<ComissaoTecnica> diminuirSalario(@PathVariable UUID id,
                                                           @Valid @RequestBody AjusteSalarioRequest request) {
        return ResponseEntity.ok(comissaoTecnicaService.diminuirSalario(id, request));
    }
}