package club_employee_control.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "jogadores")
@DiscriminatorValue("JOGADOR")
public class Jogador extends FuncionarioBase {

    @Column(name = "numero_camisa", nullable = false)
    private int numeroCamisa;

    @Column(name = "liberado_pelo_dm", nullable = false)
    private boolean liberadoPeloDM;

    protected Jogador() {}

    public Jogador(UUID id, String nome, LocalDate dataAdmissao,
                   String posicao, float salario,
                   int duracaoContrato, int numeroCamisa) {
        super(id, nome, dataAdmissao, "Jogador - " + posicao,
                salario, duracaoContrato);
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