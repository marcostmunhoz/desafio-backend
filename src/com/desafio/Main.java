package com.desafio;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;

public class Main {
    private static final String USUARIO = "marcos";
    private static final String SENHA = "98564831";
    private static final String URL = "jdbc:mysql://localhost:3306/banco_desafio_backend";
    private static final String DRIVER = "com.mysql.jdbc.Driver";

    private static String repeat(String repeatChar, int count) {
        return String.format("%0" + count + "d", 0).replace("0", repeatChar);
    }

    public static void main(String[] args) {
        try {
            Class.forName(DRIVER);
            Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            int id, ativo, quantidade;
            String cpf_cnpj, nome;
            double valor;

            if (args.length == 0) {
                System.out.print("Seja bem vindo ao sistema.\nQuantos registros você deseja inserir? ");
                quantidade = Integer.parseInt(reader.readLine());
                if (quantidade > 0) {
                    for (int i = 0; i < quantidade; i++) {
                        System.out.print("Identificação do cliente: ");
                        id = Integer.parseInt(reader.readLine());
                        System.out.print("CPF/CNPJ do cliente: ");
                        cpf_cnpj = reader.readLine();
                        System.out.print("Nome do cliente: ");
                        nome = reader.readLine();
                        System.out.print("Consumidor ativo? (s/n): ");
                        if (reader.readLine().contains("s")) {
                            ativo = 1;
                        } else {
                            ativo = 0;
                        }
                        System.out.print("Valor total: ");
                        valor = Double.parseDouble(reader.readLine());
                        PreparedStatement id_stmt = conexao.prepareStatement("SELECT COUNT(id_customer) FROM tb_customer_account WHERE id_customer = ?");
                        id_stmt.setInt(1, id);
                        ResultSet id_result = id_stmt.executeQuery();
                        id_result.next();
                        if (id_result.getInt(1) == 0) {
                            PreparedStatement insert_stmt = conexao.prepareStatement("INSERT INTO tb_customer_account VALUES (?, ?, ?, ?, ?)");
                            insert_stmt.setInt(1, id);
                            insert_stmt.setString(2, cpf_cnpj);
                            insert_stmt.setString(3, nome);
                            insert_stmt.setInt(4, ativo);
                            insert_stmt.setDouble(5, valor);
                            insert_stmt.execute();
                        } else {
                            System.out.println("Identificação '" + Integer.toString(id) + "' já utilizada.");
                        }
                        System.out.println();
                    }
                }
            } else if (args.length == 5) {
                PreparedStatement id_stmt = conexao.prepareStatement("SELECT COUNT(id_customer) FROM tb_customer_account WHERE id_customer = ?");
                id_stmt.setInt(1, Integer.parseInt(args[0]));
                ResultSet id_result = id_stmt.executeQuery();
                id_result.next();
                if (id_result.getInt(1) == 0) {
                    PreparedStatement insert_stmt = conexao.prepareStatement("INSERT INTO tb_customer_account VALUES (?, ?, ?, ?, ?)");
                    insert_stmt.setInt(1, Integer.parseInt(args[0]));
                    insert_stmt.setString(2, args[1]);
                    insert_stmt.setString(3, args[2]);
                    insert_stmt.setInt(4, Integer.parseInt(args[3]));
                    insert_stmt.setDouble(5, Double.parseDouble(args[4]));
                    insert_stmt.execute();
                } else {
                    System.out.println("Identificação '" + Integer.toString(Integer.parseInt(args[0])) + "' já utilizada.\n");
                }
            }
            ResultSet sum_result = conexao.prepareStatement(
                    "SELECT AVG(vl_total) FROM tb_customer_account WHERE vl_total > 560 AND id_customer BETWEEN 1500 AND 2700").executeQuery();
            sum_result.next();
            double soma = sum_result.getDouble(1);
            System.out.println("\nMédia = " + Double.toString(soma) + "\n");
            ResultSet select_result = conexao.prepareStatement(
                    "SELECT * FROM tb_customer_account WHERE vl_total > 560 AND id_customer BETWEEN 1500 AND 2700 ORDER BY vl_total DESC").executeQuery();
            System.out.println("+" + repeat("=", 94) + "+");
            System.out.format("| %4s | %14s | %45s | %6s | %11s |\n", "ID", "CPF/CNPJ", "Nome", "Ativo?", "Valor total");
            System.out.println("+" + repeat("=", 94) + "-");
            while (select_result.next()) {
                System.out.format("| %4d | %14s | %45s | %6s | %11.2f |\n", select_result.getInt(1), select_result.getString(2),
                        select_result.getString(3), select_result.getInt(4) == 1 ? "sim" : "não", select_result.getDouble(5));
            }
            System.out.println("+" + repeat("=", 94) + "+");
        } catch (Exception ex) {
           System.out.println("Erro encontrado: " + ex.toString());
        }
    }
}
