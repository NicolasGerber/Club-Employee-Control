package Classes;

import java.time.LocalDate;
import java.util.UUID;

class ComissaoTecnica extends FuncionarioBase {

    public ComissaoTecnica(UUID id, String nome, LocalDate dataAdmissao,
                           String cargo, float salario, int duracaoContrato) {
        super(id, nome, dataAdmissao, cargo, salario, duracaoContrato);
    }
}