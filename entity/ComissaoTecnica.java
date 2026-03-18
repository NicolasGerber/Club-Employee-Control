package club_employee_control.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "funcionarios")
@DiscriminatorValue("COMISSAO")
public class ComissaoTecnica extends FuncionarioBase {

    protected ComissaoTecnica() {}

    public ComissaoTecnica(UUID id, String nome, LocalDate dataAdmissao,
                           String cargo, float salario, int duracaoContrato) {
        super(id, nome, dataAdmissao, cargo, salario, duracaoContrato);
    }
}