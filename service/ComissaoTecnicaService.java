package club_employee_control.service;

import club_employee_control.entity.ComissaoTecnica;
import club_employee_control.exception.RecursoNaoEncontradoException;
import club_employee_control.repository.ComissaoTecnicaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ComissaoTecnicaService {

    private final ComissaoTecnicaRepository comissaoTecnicaRepository;

    public ComissaoTecnicaService(ComissaoTecnicaRepository comissaoTecnicaRepository) {
        this.comissaoTecnicaRepository = comissaoTecnicaRepository;
    }

    public ComissaoTecnica salvar(ComissaoTecnica membro) {
        return comissaoTecnicaRepository.save(membro);
    }

    public List<ComissaoTecnica> listarTodos() {
        return comissaoTecnicaRepository.findAll();
    }

    public Optional<ComissaoTecnica> buscarPorId(UUID id) {
        return comissaoTecnicaRepository.findById(id);
    }

    public ComissaoTecnica atualizar(UUID id, ComissaoTecnica dadosNovos) {
        ComissaoTecnica membro = comissaoTecnicaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Membro não encontrado: " + id));

        membro.setDuracaoContrato(dadosNovos.getDuracaoContrato());

        return comissaoTecnicaRepository.save(membro);
    }

    public void deletar(UUID id) {
        comissaoTecnicaRepository.deleteById(id);
    }
}