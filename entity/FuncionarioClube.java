package club_employee_control.entity;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "funcionarios")
@DiscriminatorValue("FUNCIONARIO")
public class FuncionarioClube extends FuncionarioBase {

    protected FuncionarioClube() {}

    public FuncionarioClube(String nome, LocalDate dataAdmissao,
                            String cargo, float salario, int duracaoContrato) {
        super(nome, dataAdmissao, cargo, salario, duracaoContrato);
    }
}