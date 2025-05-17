package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Criar Função para verificar empresas - dependendo do banco de dados - Empresas tabela -- completo
// Criar Função para buscar Lançamento de descartes - dependendo do banco de dados - Lançamentos tabela -- completo
// Criar Função para visualizar relatorios(Substituido pela view) - Materiais mais descartados, etc - Acesso a view -- completo
// Criar Função para listar os pontos de coletas - PontoColeta - latitude e longitude -- completo
// Criar Função para registrar relatorio - Novo relatorio - precisaria de nome, ponto de coleta, material, quantidade -- completo
// Decidir se vai manter a pontuacao/recomepensa -- decidido ; nao manter
// Adicionar Overloading para visualizarRelatorios - por empresa  -- completo

public class DatabaseConnector {

    private static final String DB_URL = "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;databaseName=DescarteInteligente;encrypt=true;trustServerCertificate=true;";
    private static final String DB_USER = "upx2025-1";
    private static final String DB_PASSWORD = "1234";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static void visualizarRelatorios() {
        String sql = "SELECT data_lancamento, nome_empresa, nome_material,quantidade,nome_ponto FROM vw_LancamentosDescarte";
        try (Connection conn = getConnection(); // conecta com banco de dados
                Statement stmt = conn.createStatement(); // verifica banco de dados
                ResultSet rs = stmt.executeQuery(sql)) {// executa codigo sql
            System.out.println("--- Relatorios de descartes ---");
            while (rs.next()) {
                System.out.println("Data de descarte: " + rs.getString("data_lancamento"));
                System.out.println("Nome da empresa: " + rs.getString("nome_empresa"));
                System.out.println("Nome do material: " + rs.getString("nome_material"));
                System.out.println("Quantidade: " + rs.getString("quantidade") + "kg");
                System.out.println("Nome do ponto de descarte: " + rs.getString("nome_ponto"));
                System.out.println("-----------------------------");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar Empresas: " + e.getMessage());
        }
    }
    public static void visualizarRelatorios(String nome_empresa) {
        String sql = "SELECT data_lancamento, nome_empresa, nome_material,quantidade,nome_ponto FROM vw_LancamentosDescarte WHERE nome_empresa = ?";
        try (Connection conn = getConnection(); // conecta com banco de dados
                PreparedStatement pstmt = conn.prepareStatement(sql)) {// executa codigo sql
            pstmt.setString(1, nome_empresa);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("--- Relatorios de descartes ---");
            while (rs.next()) {
                System.out.println("Data de descarte: " + rs.getString("data_lancamento"));
                System.out.println("Nome da empresa: " + rs.getString("nome_empresa"));
                System.out.println("Nome do material: " + rs.getString("nome_material"));
                System.out.println("Quantidade: " + rs.getString("quantidade") + "kg");
                System.out.println("Nome do ponto de descarte: " + rs.getString("nome_ponto"));
                System.out.println("-----------------------------");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar Empresas: " + e.getMessage());
        }
    }

    public static void registrarEmpresas(String nome, String cnpj, String telefone) {
        String sql = "INSERT INTO Empresas (Nome, CNPJ, Telefone ) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setString(2, cnpj);
            pstmt.setString(3, telefone);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Empresa registrado com sucesso!");
            } else {
                System.out.println("Falha ao registrar a Empresa.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao registrar Empresa: " + e.getMessage());
        }
    }

    public static void listarEmpresas() {
        String sql = "SELECT Nome, CNPJ, Telefone FROM Empresas";// codigo sql
        try (Connection conn = getConnection(); // conecta com banco de dados
                Statement stmt = conn.createStatement(); // verifica banco de dados
                ResultSet rs = stmt.executeQuery(sql)) {// executa codigo sql
            System.out.println("--- Empresas Cadastrados ---");
            while (rs.next()) {
                System.out.println("Nome: " + rs.getString("Nome"));
                System.out.println("CNPJ: " + rs.getString("CNPJ"));
                System.out.println("Telefone: " + rs.getString("Telefone"));
                System.out.println("-----------------------------");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar Empresas: " + e.getMessage());
        }
    }

    public static void listarPontos() {
        String sql = "SELECT Nome, descricao, cidade, estado ,latitude, longitude, cep  FROM PontosDescartes, Localizacoes";
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("--- Pontos de Coleta ---");
            while (rs.next()) {
                System.out.println("Nome: " + rs.getString("Nome"));
                System.out.println("Cidade: " + rs.getString("Cidade"));
                System.out.println("Estado: " + rs.getString("Estado"));
                System.out.println("Latitude: " + rs.getDouble("Latitude"));
                System.out.println("Longitude: " + rs.getDouble("Longitude"));
                System.out.println("Descrição: " + rs.getString("Descricao"));
                System.out.println("CEP: " + rs.getString("CEP"));
                System.out.println("-------------------------");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar Pontos de Coleta: " + e.getMessage());
        }
    }

    public static Empresas buscarEmpresaPorNome(String nome) {
        String sql = "SELECT Nome, Telefone, CNPJ FROM Empresas WHERE CNPJ = ?";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Empresas empresa = new Empresas();
                empresa.nome = rs.getString("Nome");
                empresa.telefone = rs.getString("Telefone");
                empresa.CNPJ = rs.getString("CNPJ");
                return empresa;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar empresa: " + e.getMessage());
        }
        return null;
    }

    public static Integer buscarIdPorNome(Connection conn, String tabela, String nome) throws SQLException {
        String idColuna;
        String nomeColuna;
        String tabelaOriginal;

        switch (tabela.toLowerCase()) {
            case "empresas" -> {
                idColuna = "id_empresa";
                nomeColuna = "nome_empresa";
                tabelaOriginal = "Empresas";
            }
            case "pontos" -> {
                idColuna = "id_ponto";
                nomeColuna = "nome_ponto";
                tabelaOriginal = "PontosDescartes";
            }
            case "materiais" -> {
                idColuna = "id_material";
                nomeColuna = "nome_material";
                tabelaOriginal = "Materiais";
            }
            default -> throw new IllegalArgumentException("Tabela desconhecida: " + tabela);
        }

        // Busca na view
        String sqlView = "SELECT DISTINCT " + idColuna + " FROM vw_LancamentosDescarte WHERE UPPER(" + nomeColuna + ") = UPPER(?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlView)) {
            pstmt.setString(1, nome);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        // Busca na tabela original
        String sqlTabela = "SELECT " + idColuna + " FROM " + tabelaOriginal + " WHERE UPPER(nome) = UPPER(?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlTabela)) {
            pstmt.setString(1, nome);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        return null;
    }

    public static void registrarRelatorio(String nome_empresa, String nome_ponto, String nome_material, double quantidade) {
        String insertSql = "INSERT INTO LancamentosDescarte (data_lancamento, id_empresa, id_ponto, id_material, quantidade) VALUES (GETDATE(), ?, ?, ?, ?)";
        try (Connection conn = getConnection()) {
            Integer idEmpresa = buscarIdPorNome(conn, "Empresas", nome_empresa);
            Integer idPonto = buscarIdPorNome(conn, "Pontos", nome_ponto);
            Integer idMaterial = buscarIdPorNome(conn, "Materiais", nome_material);

            if (idEmpresa == null || idPonto == null || idMaterial == null) {
                System.out.println("Erro: Nome de empresa, ponto ou material não encontrado no banco.");
                return;
            }

            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setInt(1, idEmpresa);
                pstmt.setInt(2, idPonto);
                pstmt.setInt(3, idMaterial);
                pstmt.setDouble(4, quantidade);

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Relatório registrado com sucesso!");
                } else {
                    System.out.println("Falha ao registrar o relatório.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao registrar relatório: " + e.getMessage());
        }
    }
}
