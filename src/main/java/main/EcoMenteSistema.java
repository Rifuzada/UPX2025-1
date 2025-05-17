package main;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class EcoMenteSistema {
    public static void main(String[] args) {
        try (Scanner scn = new Scanner(System.in)) {
            System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));
            System.out.print("Deseja se registrar? (s/n): ");
            String resposta = scn.nextLine().toLowerCase();

            if (resposta.equals("s")) {
                System.out.print("Digite o nome da empresa: ");
                String nome = scn.nextLine();

                System.out.print("Digite seu CNPJ: ");
                String cnpj = scn.nextLine();

                System.out.print("Digite seu telefone: ");
                String telefone = scn.nextLine();

                Empresas empresaExistente = DatabaseConnector.buscarEmpresaPorNome(nome);
                if (empresaExistente == null) {
                    DatabaseConnector.registrarEmpresas(nome, cnpj, telefone);
                } else {
                    System.out.println("Empresa já registrada!");
                }
            }

            int opcao = -1;
            do {
                System.out.println("\n=== Menu ===");
                System.out.println("1 - Visualizar Relatórios");
                System.out.println("2 - Listar Empresas");
                System.out.println("3 - Listar Pontos de Coleta");
                System.out.println("4 - Registrar Ação");
                System.out.println("0 - Sair");
                System.out.print("Escolha uma opção: ");

                try {
                    opcao = Integer.parseInt(scn.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Por favor, digite apenas números.");
                    opcao = -1;
                }

                switch (opcao) {
                    case 1:
                        System.out.println("Deseja buscar Algum relatorio especifico? (s/n)");
                        resposta = scn.nextLine().toLowerCase();
                        if (resposta.equals("s")) {
                            System.out.print("Digite o nome da empresa: ");
                            String nome = scn.nextLine();
                            DatabaseConnector.visualizarRelatorios(nome);
                        } else {
                            DatabaseConnector.visualizarRelatorios();
                        }
                        break;
                    case 2:
                        DatabaseConnector.listarEmpresas();
                        break;
                    case 3:
                        DatabaseConnector.listarPontos();
                        break;
                    case 4:
                        System.out.print("Digite o nome da empresa: ");
                        String nome = scn.nextLine();
                        System.out.print("Digite o ponto de coleta: ");
                        String pontoColeta = scn.nextLine();
                        System.out.print("Digite o material: ");
                        String material = scn.nextLine();
                        System.out.print("Digite a quantidade: ");
                        int quantidade = Integer.parseInt(scn.nextLine());
                        DatabaseConnector.registrarRelatorio(nome, pontoColeta, material, quantidade);
                        break;
                    case 0:
                        System.out.println("Encerrando o programa...");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }

            } while (opcao != 0);
            System.out.println("Obrigado por usar o EcoMente!\nCriado por EcoMentes- 2025");
        } catch (Exception e) {
            System.err.println("Ocorreu um erro inesperado: " + e.getMessage());
        }
    }
}
