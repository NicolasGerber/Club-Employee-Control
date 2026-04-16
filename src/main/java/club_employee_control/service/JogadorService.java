package club_employee_control.service;

import club_employee_control.dto.AjusteSalarioRequest;
import club_employee_control.dto.DemissaoRequest;
import club_employee_control.entity.Jogador;
import club_employee_control.exception.RecursoNaoEncontradoException;
import club_employee_control.repository.JogadorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.UUID;

@Service
public class JogadorService {

    private final JogadorRepository jogadorRepository;

    public JogadorService(JogadorRepository jogadorRepository) {
        this.jogadorRepository = jogadorRepository;
    }

    public Jogador salvar(Jogador jogador) {
        return jogadorRepository.save(jogador);
    }

    public Page<Jogador> listarTodos(Pageable pageable) {
        return jogadorRepository.findByAtivoTrue(pageable);
    }
    public Optional<Jogador> buscarPorId(UUID id) {
        return jogadorRepository.findById(id);
    }
    @Transactional
    public void deletar(UUID id) {
        jogadorRepository.deleteById(id);
    }
    @Transactional
    public Jogador atualizar(UUID id, Jogador dadosNovos) {
        Jogador jogador = jogadorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Jogador não encontrado: " + id));

        jogador.setNome(dadosNovos.getNome());
        jogador.setCargo(dadosNovos.getCargo());
        jogador.setDataAdmissao(dadosNovos.getDataAdmissao());
        jogador.setDuracaoContrato(dadosNovos.getDuracaoContrato());
        jogador.setLiberadoPeloDM(dadosNovos.isLiberadoPeloDM());

        return jogadorRepository.save(jogador);
    }
    @Transactional
    public Jogador demitir(UUID id, DemissaoRequest request) {
        Jogador jogador = jogadorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Jogador não encontrado: " + id));
        jogador.demitir(request.dataDemissao());
        return jogadorRepository.save(jogador);
    }
    @Transactional
    public Jogador aumentarSalario(UUID id, AjusteSalarioRequest request) {
        Jogador jogador = jogadorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Jogador não encontrado: " + id));
        jogador.aumentarSalario(request.percentual());
        return jogadorRepository.save(jogador);
    }
    @Transactional
    public Jogador diminuirSalario(UUID id, AjusteSalarioRequest request) {
        Jogador jogador = jogadorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Jogador não encontrado: " + id));

        BigDecimal novoSalario = jogador.getSalario()
                .subtract(jogador.getSalario()
                        .multiply(request.percentual().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)));

        if (novoSalario.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Redução inválida: salário não pode ser zero ou negativo");
        }

        jogador.diminuirSalario(request.percentual());
        return jogadorRepository.save(jogador);
    }

}