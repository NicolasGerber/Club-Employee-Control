package Classes;

import java.time.LocalDate;
import java.util.UUID;

class FuncionarioClube extends FuncionarioBase {

    public FuncionarioClube(UUID id, String nome, LocalDate dataAdmissao,
                            String cargo, float salario, int duracaoContrato) {
        super(id, nome, dataAdmissao, cargo, salario, duracaoContrato);
    }
}