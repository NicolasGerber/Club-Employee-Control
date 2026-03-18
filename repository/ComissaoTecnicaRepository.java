package club_employee_control.repository;

import club_employee_control.entity.ComissaoTecnica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ComissaoTecnicaRepository extends JpaRepository<ComissaoTecnica, UUID> {
}