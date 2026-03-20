package club_employee_control.service;

import club_employee_control.entity.Jogador;
import club_employee_control.exception.RecursoNaoEncontradoException;
import club_employee_control.repository.JogadorRepository;
import org.springframework.stereotype.Service;
import java.util.List;
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

    public List<Jogador> listarTodos() {
        return jogadorRepository.findAll();
    }

    public Optional<Jogador> buscarPorId(UUID id) {
        return jogadorRepository.findById(id);
    }

    public void deletar(UUID id) {
        jogadorRepository.deleteById(id);
    }
    public Jogador atualizar(UUID id, Jogador dadosNovos) {
        Jogador jogador = jogadorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Jogador não encontrado: " + id));

        jogador.setDuracaoContrato(dadosNovos.getDuracaoContrato());
        jogador.setLiberadoPeloDM(dadosNovos.isLiberadoPeloDM());

        return jogadorRepository.save(jogador);
    }
}