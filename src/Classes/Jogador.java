package Classes;

import java.time.LocalDate;
import java.util.UUID;

class Jogador extends FuncionarioBase {

    private int numeroCamisa;
    private boolean liberadoPeloDM;

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
                "\nNº Camisa: "       + numeroCamisa +
                "\nLiberado pelo DM: "+ (liberadoPeloDM ? "Sim" : "Não");
    }
}