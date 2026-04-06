package club_employee_control.repository;

import club_employee_control.entity.FuncionarioClube;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

@Repository
public interface FuncionarioClubeRepository extends JpaRepository<FuncionarioClube, UUID> {
    Page<FuncionarioClube> findByAtivoTrue(Pageable pageable);
}