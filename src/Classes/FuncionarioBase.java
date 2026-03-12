package Classes;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.UUID;


public abstract class FuncionarioBase implements Funcionario {
    protected UUID id;
    protected String nome;
    protected LocalDate dataAdmissao;
    protected String cargo;
    protected float salario;
    protected int duracaoContrato;
    protected LocalDate dataDemissao;
    protected boolean ativo;

    public FuncionarioBase(UUID id, String nome, LocalDate dataAdmissao,
                           String cargo, float salario, int duracaoContrato) {
        this.id = id;
        this.nome = nome;
        this.dataAdmissao = dataAdmissao;
        this.cargo = cargo;
        this.salario = salario;
        this.duracaoContrato = duracaoContrato;
        this.ativo = true;
        this.dataDemissao = null;
    }
    public void tempoDeCasa(){
        LocalDate dataAtual = LocalDate.now();
        Period periodo = Period.between(this.dataAdmissao,dataAtual);
        System.out.printf("%s está no clube há "+ periodo.getYears()+" anos e "+ periodo.getMonths()+" meses.",nome);
    }
    public void demitir(LocalDate data) {
        if (!ativo) {
            System.out.println(nome + " já foi demitido.");
            return;
        }
        this.ativo = false;
        this.dataDemissao = data;
        System.out.println(nome + " foi demitido em " + data);
    }
    private ArrayList<Float> historicoSalario = new ArrayList<>();
    public void aumentarSalario(float percentual){
        historicoSalario.add(this.salario);
        float oldSalario = this.salario;
        this.salario = this.salario + (this.salario * percentual/100);
        System.out.printf("\nSalario de %s atualizado, %.2f -> %.2f",this.nome,oldSalario,this.salario);
    }
    public void diminuirSalario(float percentual){
        historicoSalario.add(this.salario);
        float oldSalario = this.salario;
        this.salario = this.salario - (this.salario * percentual/100);
        System.out.printf("\nSalario de %s atualizado, %.2f -> %.2f",this.nome,oldSalario,this.salario);
    }
    public void exibirHistoricoSalario(){
        System.out.println("\n=== Histórico de Salário - "+this.nome+" ===");
        for (int i = 0; i<historicoSalario.size(); i++){
            System.out.println((i+1) + "º R$" + historicoSalario.get(i));
            if (i != historicoSalario.size() -1) {
                float val1 = historicoSalario.get(i);
                float val2 = historicoSalario.get(i + 1);
                float result = ((val2 - val1) / val1) * 100;
                String sinal = result > 0 ? "📈 +" : "📉 ";
                System.out.println(sinal + result + "%");
            }


        }
    }
    @Override
    public String toString() {
        return "\nID: "         + id +
                "\nNome: "       + nome +
                "\nCargo: "      + cargo +
                "\nSalário: R$"  + salario +
                "\nAdmissão: "   + dataAdmissao +
                "\nContrato: "   + duracaoContrato + " meses" +
                "\nStatus: "     + (ativo ? "Ativo"
                : "Demitido em " + dataDemissao);
    }
    @Override public UUID getId()                    { return id; }
    @Override public String getNome()               { return nome; }
    @Override public LocalDate getDataAdmissao()    { return dataAdmissao; }
    @Override public String getCargo()              { return cargo; }
    @Override public float getSalario()             { return salario; }
    @Override public int getDuracaoContrato()       { return duracaoContrato; }
    @Override public void setDuracaoContrato(int m) { this.duracaoContrato = m; }
    @Override public LocalDate getDataDemissao()    { return dataDemissao; }
    @Override public boolean isAtivo()              { return ativo; }
}