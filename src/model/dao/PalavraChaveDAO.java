package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class PalavraChaveDAO {
	  private static final String URL = "jdbc:sqlite:vestibular.db";

	    public static void init() {
	        try (Connection conn = DriverManager.getConnection(URL);
	             Statement stmt = conn.createStatement()) {
	            // Tabela de matérias
	            stmt.execute("CREATE TABLE IF NOT EXISTS materias (" +
	                         "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
	                         "nome TEXT NOT NULL)");

	            // Tabela de questões
	            stmt.execute("CREATE TABLE IF NOT EXISTS questoes (" +
	                         "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
	                         "pergunta TEXT NOT NULL, " +
	                         "resposta TEXT NOT NULL, " +
	                         "materia TEXT NOT NULL, " +
	                         "assunto TEXT NOT NULL)");

	            // Tabela de resumos
	            stmt.execute("CREATE TABLE IF NOT EXISTS resumos (" +
	                         "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
	                         "materia TEXT NOT NULL, " +
	                         "texto TEXT NOT NULL)");

	            // Tabela de palavras-chave
	            stmt.execute("CREATE TABLE IF NOT EXISTS palavras_chave (" +
	                         "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
	                         "palavra TEXT NOT NULL, " +
	                         "descricao TEXT, " +
	                         "materia TEXT NOT NULL, " +
	                         "assunto TEXT NOT NULL)");
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
}
