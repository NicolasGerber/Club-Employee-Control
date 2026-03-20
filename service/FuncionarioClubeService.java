package club_employee_control.service;

import club_employee_control.entity.FuncionarioClube;
import club_employee_control.exception.RecursoNaoEncontradoException;
import club_employee_control.repository.FuncionarioClubeRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FuncionarioClubeService {

    private final FuncionarioClubeRepository funcionarioClubeRepository;

    public FuncionarioClubeService(FuncionarioClubeRepository funcionarioClubeRepository) {
        this.funcionarioClubeRepository = funcionarioClubeRepository;
    }

    public FuncionarioClube salvar(FuncionarioClube funcionario) {
        return funcionarioClubeRepository.save(funcionario);
    }

    public List<FuncionarioClube> listarTodos() {
        return funcionarioClubeRepository.findAll();
    }

    public Optional<FuncionarioClube> buscarPorId(UUID id) {
        return funcionarioClubeRepository.findById(id);
    }

    public FuncionarioClube atualizar(UUID id, FuncionarioClube dadosNovos) {
        FuncionarioClube funcionario = funcionarioClubeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionário não encontrado: " + id));

        funcionario.setDuracaoContrato(dadosNovos.getDuracaoContrato());

        return funcionarioClubeRepository.save(funcionario);
    }

    public void deletar(UUID id) {
        funcionarioClubeRepository.deleteById(id);
    }
}