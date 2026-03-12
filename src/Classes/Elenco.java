package Classes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

public class Elenco {
    private ArrayList<FuncionarioBase> funcionarios = new ArrayList<>();
    public void adicionarFuncionario() {
        UUID id = UUID.randomUUID();
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nArea de atuação: ");
        System.out.println("\n1 - Jogador");
        System.out.println("\n2 - Comissão Técnica");
        System.out.println("\n3 - Funcionario do Clube");
        int select = scanner.nextInt();
        scanner.nextLine();
        switch (select) {
            case 1:
                System.out.println("\nNome: ");
                String nome = scanner.nextLine();
                System.out.println("\nAno: ");
                int ano = scanner.nextInt();
                scanner.nextLine();
                System.out.println("\nMes: ");
                int mes = scanner.nextInt();
                scanner.nextLine();
                System.out.println("\nDia: ");
                int dia = scanner.nextInt();
                scanner.nextLine();
                System.out.println("\nSalário: ");
                float salario = scanner.nextFloat();
                scanner.nextLine();
                System.out.println("\nDuração do contrato: ");
                int duracaoContrato = scanner.nextInt();
                scanner.nextLine();
                System.out.println("\nPosição: ");
                String posicao = scanner.nextLine();
                System.out.println("\nNumero da Camisa: ");
                int numeroCamisa = scanner.nextInt();
                scanner.nextLine();
                Jogador j = new Jogador(id, nome,
                        LocalDate.of(ano, mes, dia),
                        posicao, salario, duracaoContrato, numeroCamisa);

                funcionarios.add(j);
                break;
            case 2:
                System.out.println("\nNome: ");
                String nome2 = scanner.nextLine();
                System.out.println("\nAno: ");
                int ano2 = scanner.nextInt();
                scanner.nextLine();
                System.out.println("\nMes: ");
                int mes2 = scanner.nextInt();
                scanner.nextLine();
                System.out.println("\nDia: ");
                int dia2 = scanner.nextInt();
                scanner.nextLine();
                System.out.println("\nSalário: ");
                float salario2 = scanner.nextFloat();
                scanner.nextLine();
                System.out.println("\nDuração do contrato: ");
                int duracaoContrato2 = scanner.nextInt();
                scanner.nextLine();
                System.out.println("\nCargo: ");
                String cargo = scanner.nextLine();
                ComissaoTecnica ct = new ComissaoTecnica(id, nome2,
                        LocalDate.of(ano2, mes2, dia2),
                        cargo, salario2, duracaoContrato2);
                funcionarios.add(ct);
                break;
            case 3:
                System.out.println("\nNome: ");
                String nome3 = scanner.nextLine();
                System.out.println("\nAno: ");
                int ano3 = scanner.nextInt();
                scanner.nextLine();
                System.out.println("\nMes: ");
                int mes3 = scanner.nextInt();
                scanner.nextLine();
                System.out.println("\nDia: ");
                int dia3 = scanner.nextInt();
                scanner.nextLine();
                System.out.println("\nSalário: ");
                float salario3 = scanner.nextFloat();
                scanner.nextLine();
                System.out.println("\nDuração do contrato: ");
                int duracaoContrato3 = scanner.nextInt();
                scanner.nextLine();
                System.out.println("\nCargo: ");
                String cargo3 = scanner.nextLine();
                FuncionarioClube fc = new FuncionarioClube(id, nome3,
                        LocalDate.of(ano3, mes3, dia3),
                        cargo3, salario3, duracaoContrato3);
                funcionarios.add(fc);
                break;

        }

        scanner.close();
    }
    public void contarSituacao(){
        int ativos = 0;
        int demitidos = 0;
        for (FuncionarioBase f : funcionarios){
            if (f.isAtivo()){
                ativos++;
            } else{
                demitidos++;
            }
        }
        System.out.println("=== Situação do Elenco ===");
        System.out.println("Ativos: "+ativos);
        System.out.println("Demitidos: "+demitidos);
    }

    public  FuncionarioBase buscarPorId(UUID id)
            throws Exception {
        for (FuncionarioBase f : funcionarios) {
            if (f.getId().equals(id)) {
                return f;
            }
        }
        throw new Exception("Funcionário com ID " + id + " não encontrado.");
    }

}