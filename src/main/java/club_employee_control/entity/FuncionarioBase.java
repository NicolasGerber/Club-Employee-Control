package club_employee_control.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@Entity
@Table(name = "funcionarios")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING)
public abstract class FuncionarioBase implements Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected UUID id;

    @Column(name = "nome", nullable = false, length = 100)
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    protected String nome;

    @Column(name = "data_admissao", nullable = false)
    @NotNull(message = "Data de admissão é obrigatória")
    protected LocalDate dataAdmissao;

    @Column(name = "cargo", nullable = false, length = 100)
    @NotBlank(message = "Cargo é obrigatório")
    @Size(max = 100, message = "Cargo deve ter no máximo 100 caracteres")
    protected String cargo;

    @Column(name = "salario", nullable = false, precision = 15, scale = 2)
    @Positive(message = "Salário deve ser positivo")
    @NotNull(message = "Salário é obrigatório")
    protected BigDecimal salario;

    @Column(name = "duracao_contrato", nullable = false)
    @Min(value = 1, message = "Duração do contrato deve ser de pelo menos 1 mês")
    protected int duracaoContrato;

    @Column(name = "data_demissao")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected LocalDate dataDemissao;

    @Column(name = "ativo", nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected boolean ativo;

    protected FuncionarioBase() {
        this.ativo = true;
        this.dataDemissao = null;
    }

    public FuncionarioBase(String nome, LocalDate dataAdmissao,
                           String cargo, BigDecimal salario, int duracaoContrato) {
        this.nome = nome;
        this.dataAdmissao = dataAdmissao;
        this.cargo = cargo;
        this.salario = salario;
        this.duracaoContrato = duracaoContrato;
        this.ativo = true;
        this.dataDemissao = null;
    }

    public String getTempoDeCasa() {
        Period periodo = Period.between(this.dataAdmissao, LocalDate.now());
        return nome + " está no clube há " + periodo.getYears() + " anos e " + periodo.getMonths() + " meses.";
    }

    public void demitir(LocalDate data) {
        if (!ativo) return;
        this.ativo = false;
        this.dataDemissao = data;
    }

    public void aumentarSalario(BigDecimal percentual) {
        BigDecimal fator = percentual.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
        this.salario = this.salario.add(this.salario.multiply(fator))
                .setScale(2, RoundingMode.HALF_UP); // ✅ garante escala 2 na resposta
    }

    public void diminuirSalario(BigDecimal percentual) {
        BigDecimal fator = percentual.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
        this.salario = this.salario.subtract(this.salario.multiply(fator))
                .setScale(2, RoundingMode.HALF_UP); // ✅ garante escala 2 na resposta
    }

    @Override
    public String toString() {
        return "\nID: "        + id +
                "\nNome: "      + nome +
                "\nCargo: "     + cargo +
                "\nSalário: R$" + salario +
                "\nAdmissão: "  + dataAdmissao +
                "\nContrato: "  + duracaoContrato + " meses" +
                "\nStatus: "    + (ativo ? "Ativo" : "Demitido em " + dataDemissao);
    }

    // Setters
    public void setNome(String nome)                { this.nome = nome; }
    public void setCargo(String cargo)              { this.cargo = cargo; }
    public void setDataAdmissao(LocalDate data)     { this.dataAdmissao = data; }

    // Getters
    @Override public UUID getId()                   { return id; }
    @Override public String getNome()               { return nome; }
    @Override public LocalDate getDataAdmissao()    { return dataAdmissao; }
    @Override public String getCargo()              { return cargo; }
    @Override public BigDecimal getSalario()        { return salario; }
    @Override public int getDuracaoContrato()       { return duracaoContrato; }
    @Override public void setDuracaoContrato(int m) { this.duracaoContrato = m; }
    @Override public LocalDate getDataDemissao()    { return dataDemissao; }
    @Override public boolean isAtivo()              { return ativo; }
}