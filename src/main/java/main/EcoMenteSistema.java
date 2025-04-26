package main;

import java.util.Scanner;

public class EcoMenteSistema {
    // public static void registrarAcaoEmpresa(Empresas empresa, String tipoAcao) {
    // if (tipoAcao.equalsIgnoreCase("descartou")) {
    // int pontosParaAdicionar = 10;
    // DatabaseConnector.adicionarPontuacao(empresa.email, pontosParaAdicionar);
    // empresa.pontuacao += pontosParaAdicionar;
    // }
    // }

    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        try {
            System.out.print("Deseja se registrar? (s/n): ");
            String resposta = scn.nextLine();

            if (resposta.equalsIgnoreCase("s")) {
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
                        DatabaseConnector.visualizarRelatorios();
                        break;
                    case 2:
                        DatabaseConnector.listarEmpresas();
                        break;
                    case 3:
                        DatabaseConnector.listarPontos();
                        break;
                    case 0:
                        System.out.println("Encerrando o programa...");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }

            } while (opcao != 0);
            System.out.println("Obrigado por usar o EcoMente!\nCriado por Grupo 10 - 2025");
        } catch (Exception e) {
            System.err.println("Ocorreu um erro inesperado: " + e.getMessage());
        } finally {
            scn.close();
        }
    }
}
