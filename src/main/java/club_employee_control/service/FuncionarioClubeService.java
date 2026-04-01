package club_employee_control.service;

import club_employee_control.dto.AjusteSalarioRequest;
import club_employee_control.dto.DemissaoRequest;
import club_employee_control.entity.FuncionarioClube;
import club_employee_control.exception.RecursoNaoEncontradoException;
import club_employee_control.repository.FuncionarioClubeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
        return funcionarioClubeRepository.findByAtivoTrue();
    }
    public Optional<FuncionarioClube> buscarPorId(UUID id) {
        return funcionarioClubeRepository.findById(id);
    }
    @Transactional
    public FuncionarioClube atualizar(UUID id, FuncionarioClube dadosNovos) {
        FuncionarioClube funcionario = funcionarioClubeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionário não encontrado: " + id));

        funcionario.setNome(dadosNovos.getNome());
        funcionario.setCargo(dadosNovos.getCargo());
        funcionario.setDataAdmissao(dadosNovos.getDataAdmissao());
        funcionario.setDuracaoContrato(dadosNovos.getDuracaoContrato());

        return funcionarioClubeRepository.save(funcionario);
    }
    @Transactional
    public void deletar(UUID id) {
        funcionarioClubeRepository.deleteById(id);
    }
    @Transactional
    public FuncionarioClube demitir(UUID id, DemissaoRequest request) {
        FuncionarioClube funcionario = funcionarioClubeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionário não encontrado: " + id));
        funcionario.demitir(request.dataDemissao());
        return funcionarioClubeRepository.save(funcionario);
    }
    @Transactional
    public FuncionarioClube aumentarSalario(UUID id, AjusteSalarioRequest request) {
        FuncionarioClube funcionario = funcionarioClubeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionário não encontrado: " + id));
        funcionario.aumentarSalario(request.percentual());
        return funcionarioClubeRepository.save(funcionario);
    }
    @Transactional
    public FuncionarioClube diminuirSalario(UUID id, AjusteSalarioRequest request) {
        FuncionarioClube funcionario = funcionarioClubeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionário não encontrado: " + id));

        BigDecimal novoSalario = funcionario.getSalario()
                .subtract(funcionario.getSalario()
                        .multiply(request.percentual().divide(BigDecimal.valueOf(100))));

        if (novoSalario.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Redução inválida: salário não pode ser zero ou negativo");
        }

        funcionario.diminuirSalario(request.percentual());
        return funcionarioClubeRepository.save(funcionario);
    }
}