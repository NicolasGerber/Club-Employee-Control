package club_employee_control.entity;
import java.time.LocalDate;
import java.util.UUID;

public interface Funcionario {
    UUID getId();
    LocalDate getDataAdmissao();
    String getNome();
    String getCargo();
    float getSalario();
    int getDuracaoContrato();         // em meses
    void setDuracaoContrato(int meses);
    LocalDate getDataDemissao();
    boolean isAtivo();

    default void renovarContrato(int mesesExtras) {
        setDuracaoContrato(getDuracaoContrato() + mesesExtras);
        System.out.println(getNome() + " teve contrato renovado por mais " + mesesExtras + " meses.");
    }
}
