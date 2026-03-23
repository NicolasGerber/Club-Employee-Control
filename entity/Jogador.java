package club_employee_control.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "jogadores")
@DiscriminatorValue("JOGADOR")
public class Jogador extends FuncionarioBase {

    @Column(name = "numero_camisa", nullable = false)
    @Min(value = 1, message = "Número da camisa deve ser pelo menos 1")
    private int numeroCamisa;


    @Column(name = "liberado_pelo_dm", nullable = false)
    private boolean liberadoPeloDM;

    protected Jogador() {}

    public Jogador(String nome, LocalDate dataAdmissao,
                   String posicao, float salario,
                   int duracaoContrato, int numeroCamisa) {
        super(nome, dataAdmissao, "Jogador - " + posicao, salario, duracaoContrato);
        this.numeroCamisa = numeroCamisa;
        this.liberadoPeloDM = false;
    }

    public int getNumeroCamisa()                  { return numeroCamisa; }
    public boolean isLiberadoPeloDM()             { return liberadoPeloDM; }
    public void setLiberadoPeloDM(boolean status) { this.liberadoPeloDM = status; }

    @Override
    public String toString() {
        return super.toString() +
                "\nNº Camisa: "        + numeroCamisa +
                "\nLiberado pelo DM: " + (liberadoPeloDM ? "Sim" : "Não");
    }
}