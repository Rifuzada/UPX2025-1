package main;

import java.sql.*;

// Criar Função para verificar empresas - dependendo do banco de dados - Empresas tabela -- completo
// Criar Função para buscar Lançamento de descartes - dependendo do banco de dados - Lançamentos tabela -- completo
// Criar Função para visualizar relatorios(Substituido pela view) - Materiais mais descartados, etc - Acesso a view -- completo
// Criar Função para listar os pontos de coletas - PontoColeta - latitude e longitude -- completo
// Criar Função para registrar relatorio - Novo relatorio - precisaria de nome, ponto de coleta, material, quantidade
// Decidir se vai manter a pontuacao/recomepensa
// Adicionar Overloading para visualizarRelatorios - por empresa 

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

    // public static void adicionarPontuacao(String email, int pontos) {
    // String sql = "UPDATE Usuarios SET Pontuacao = Pontuacao + ? WHERE Email = ?";
    // try (Connection conn = getConnection();
    // PreparedStatement pstmt = conn.prepareStatement(sql)) {
    // pstmt.setInt(1, pontos);
    // pstmt.setString(2, email);
    // int affectedRows = pstmt.executeUpdate();
    // if (affectedRows > 0) {
    // System.out.println("Pontuação adicionada com sucesso para a Empresa: " +
    // email);
    // } else {
    // System.out.println("Empresa não encontrada.");
    // }
    // } catch (SQLException e) {
    // System.err.println("Erro ao adicionar pontuação: " + e.getMessage());
    // }
    // }
}
