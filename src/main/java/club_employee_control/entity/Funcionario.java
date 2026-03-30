package club_employee_control.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface Funcionario {
    UUID getId();
    LocalDate getDataAdmissao();
    String getNome();
    String getCargo();
    BigDecimal getSalario();
    int getDuracaoContrato();
    void setDuracaoContrato(int meses);
    LocalDate getDataDemissao();
    boolean isAtivo();

    default void renovarContrato(int mesesExtras) {
        setDuracaoContrato(getDuracaoContrato() + mesesExtras);
        System.out.println(getNome() + " teve contrato renovado por mais " + mesesExtras + " meses.");
    }
}