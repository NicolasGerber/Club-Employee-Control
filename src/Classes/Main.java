package Classes;

import Classes.config.ConexaoDB;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
//            Elenco elenco = new Elenco();
//            elenco.adicionarFuncionario();
        try {
            Connection conn = ConexaoDB.conectar();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }


    }
}