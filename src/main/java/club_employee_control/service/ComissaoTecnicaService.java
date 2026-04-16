package club_employee_control.service;

import club_employee_control.dto.AjusteSalarioRequest;
import club_employee_control.dto.DemissaoRequest;
import club_employee_control.entity.ComissaoTecnica;
import club_employee_control.exception.RecursoNaoEncontradoException;
import club_employee_control.repository.ComissaoTecnicaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.math.RoundingMode;

@Service
public class ComissaoTecnicaService {

    private final ComissaoTecnicaRepository comissaoTecnicaRepository;

    public ComissaoTecnicaService(ComissaoTecnicaRepository comissaoTecnicaRepository) {
        this.comissaoTecnicaRepository = comissaoTecnicaRepository;
    }

    public ComissaoTecnica salvar(ComissaoTecnica membro) {
        return comissaoTecnicaRepository.save(membro);
    }

    public Page<ComissaoTecnica> listarTodos(Pageable pageable) {
        return comissaoTecnicaRepository.findByAtivoTrue(pageable);
    }
    public Optional<ComissaoTecnica> buscarPorId(UUID id) {
        return comissaoTecnicaRepository.findById(id);
    }
    @Transactional
    public ComissaoTecnica atualizar(UUID id, ComissaoTecnica dadosNovos) {
        ComissaoTecnica membro = comissaoTecnicaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Membro não encontrado: " + id));

        membro.setNome(dadosNovos.getNome());
        membro.setCargo(dadosNovos.getCargo());
        membro.setDataAdmissao(dadosNovos.getDataAdmissao());
        membro.setDuracaoContrato(dadosNovos.getDuracaoContrato());

        return comissaoTecnicaRepository.save(membro);
    }
    @Transactional
    public void deletar(UUID id) {
        comissaoTecnicaRepository.deleteById(id);
    }
    @Transactional
    public ComissaoTecnica demitir(UUID id, DemissaoRequest request) {
        ComissaoTecnica membro = comissaoTecnicaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Membro não encontrado: " + id));
        membro.demitir(request.dataDemissao());
        return comissaoTecnicaRepository.save(membro);
    }
    @Transactional
    public ComissaoTecnica aumentarSalario(UUID id, AjusteSalarioRequest request) {
        ComissaoTecnica membro = comissaoTecnicaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Membro não encontrado: " + id));
        membro.aumentarSalario(request.percentual());
        return comissaoTecnicaRepository.save(membro);
    }
    @Transactional
    public ComissaoTecnica diminuirSalario(UUID id, AjusteSalarioRequest request) {
        ComissaoTecnica membro = comissaoTecnicaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Membro não encontrado: " + id));

        BigDecimal novoSalario = membro.getSalario()
                .subtract(membro.getSalario()
                        .multiply(request.percentual().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)));

        if (novoSalario.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Redução inválida: salário não pode ser zero ou negativo");
        }

        membro.diminuirSalario(request.percentual());
        return comissaoTecnicaRepository.save(membro);
    }
}