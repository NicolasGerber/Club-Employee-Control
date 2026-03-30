package club_employee_control.repository;

import club_employee_control.entity.Jogador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface JogadorRepository extends JpaRepository<Jogador, UUID> {
}