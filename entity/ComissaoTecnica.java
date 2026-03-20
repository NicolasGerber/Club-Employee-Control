package club_employee_control.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "funcionarios")
@DiscriminatorValue("COMISSAO")
public class ComissaoTecnica extends FuncionarioBase {

    protected ComissaoTecnica() {}

    public ComissaoTecnica(String nome, LocalDate dataAdmissao,
                           String cargo, float salario, int duracaoContrato) {
        super(nome, dataAdmissao, cargo, salario, duracaoContrato);
    }
}