package com.desafio;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;

public class Main {
    private static final String USUARIO = "root";
    private static final String SENHA = "";
    private static final String BANCO = "jdbc:mysql://localhost:3306/banco_desafio_backend";

    // Metodo auxiliar para geração de "linhas" para a tabela
    // Executa um loop n vezes, adicionando um caracter fornecido a uma string temporária, posteriormente retornando a mesma
    private static String repeat(Character repeatChar, int count) {
        String repeated = "";
        for (int i = 0; i < count; i++) {
            repeated += repeatChar;
        }
        return repeated;
    }

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver"); // Chamada para o driver do mysql
            Connection conexao = DriverManager.getConnection(BANCO, USUARIO, SENHA); //Conexão com o banco de dados
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); //Entrada de dados
            int id, ativo, quantidade;
            String cpf_cnpj, nome;
            double valor;

            if (args.length == 0) { // Executa quando não forem fornecidos os parametros de linha de comando
                System.out.print("Seja bem vindo ao sistema.\nQuantos registros você deseja inserir? ");
                quantidade = Integer.parseInt(reader.readLine()); //Lê o dado do reader
                if (quantidade > 0) {
                    for (int i = 0; i < quantidade; i++) {
                        System.out.print("Identificação do cliente: ");
                        id = Integer.parseInt(reader.readLine());
                        System.out.print("CPF/CNPJ do cliente: ");
                        cpf_cnpj = reader.readLine();
                        System.out.print("Nome do cliente: ");
                        nome = reader.readLine();
                        System.out.print("Ativo? (s/n): ");
                        if (reader.readLine().contains("s")) {
                            ativo = 1;
                        } else {
                            ativo = 0;
                        }
                        System.out.print("Valor total: ");
                        valor = Double.parseDouble(reader.readLine());
                        PreparedStatement id_stmt = conexao.prepareStatement("SELECT COUNT(id_customer) FROM tb_customer_account WHERE id_customer = ?"); // Verifica se já existe o id informado
                        id_stmt.setInt(1, id); // Alimenta a query com o parametro 'id'
                        ResultSet id_result = id_stmt.executeQuery(); // Executa a query e registra num resultset
                        id_result.next(); // Avança para a primeira linha (unica, no caso)
                        if (id_result.getInt(1) == 0) { // Checa se o valor da primeira coluna (unica, novamente) é = 0, ou seja, não existem registros com o mesmo id
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
            } else if (args.length == 5) { // Executa quando os 5 parametros estão presentes (automatiza a entrada de dados)
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
            System.out.println("+" + repeat('=', 94) + "+"); // Chamada a função auxiliar para gerar a linha superior da tabela
            System.out.format("| %4s | %14s | %45s | %6s | %11s |\n", "ID", "CPF/CNPJ", "Nome", "Ativo?", "Valor total"); // Realiza a formatação das "colunas" da tabela
            // Separando pela quantidade de caracteres para cada coluna
            System.out.println("+" + repeat('-', 94) + "+");
            while (select_result.next()) { // Loop pelas linhas do resultset
                System.out.format("| %4d | %14s | %45s | %6s | %11.2f |\n", select_result.getInt(1), select_result.getString(2),
                        select_result.getString(3), select_result.getInt(4) == 1 ? "sim" : "não", select_result.getDouble(5));
            }
            System.out.println("+" + repeat('=', 94) + "+");
            conexao.close();
        } catch (Exception ex) { //Tratamento de exceção
           System.out.println("Erro encontrado: " + ex.toString());
        }
    }
}
