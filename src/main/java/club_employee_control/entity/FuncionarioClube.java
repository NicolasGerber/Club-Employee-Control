package club_employee_control.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "funcionarios_clube")
@DiscriminatorValue("FUNCIONARIO")
public class FuncionarioClube extends FuncionarioBase {

    protected FuncionarioClube() {}

    public FuncionarioClube(String nome, LocalDate dataAdmissao,
                            String cargo, BigDecimal salario, int duracaoContrato) {
        super(nome, dataAdmissao, cargo, salario, duracaoContrato);
    }
}