package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {
	private static final String URL = "jdbc:sqlite:vestibular.db";

	public static void init() {
		try (Connection conn = DriverManager.getConnection(URL); Statement stmt = conn.createStatement()) {
//	            // Tabela de matérias
//	            stmt.execute("CREATE TABLE IF NOT EXISTS materias (" +
//	                         "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//	                         "nome TEXT NOT NULL)");
//
//	            Tabela de questões
            stmt.execute("CREATE TABLE IF NOT EXISTS questoes ("
	            	+"id INTEGER PRIMARY KEY AUTOINCREMENT, " 
	            	+"pergunta TEXT NOT NULL, " 
	            	+"resposta TEXT NOT NULL, " 
	           		+"materia TEXT NOT NULL, " 
	            	+"assunto TEXT NOT NULL)");
			
			  //tabela de questões:
			stmt.execute("CREATE TABLE IF NOT EXISTS questoes ("
					+"id INTEGER PRIMARY KEY AUTOINCREMENT, " 
					+"pergunta TEXT NOT NULL, "
					+"resposta TEXT NOT NULL, "
					+"materia TEXT NOT NULL, "
					+"assunto TEXT NOT NULL, "
					+"exame TEXT NOT NULL)"); // Novo campo "exame"
//
//	            // Tabela de resumos
            stmt.execute("CREATE TABLE IF NOT EXISTS resumos (" 
            		+"id INTEGER PRIMARY KEY AUTOINCREMENT, " 
            		+"titulo TEXT NOT NULL, " 
            		+"texto TEXT NOT NULL, "
            		+ "materia TEXT NOT NULL, "
            		+ "assunto TEXT NOT NULL, " 
            		+"anexo TEXT)");

			// Tabela de palavras-chave
			stmt.execute("CREATE TABLE IF NOT EXISTS palavras_chave (" 
					+ "id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "palavra TEXT NOT NULL, "
					+ "descricao TEXT, " 
					+ "materia TEXT NOT NULL, "
					+ "assunto TEXT NOT NULL)");

			stmt.execute("CREATE TABLE IF NOT EXISTS cronograma (" 
					+ "id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "diaSemana TEXT NOT NULL, "
					+ "horario TEXT NOT NULL, "
					+ "materia TEXT NOT NULL, "
					+ "assunto TEXT NOT NULL, " 
					+ "concluido BOOLEAN NOT NULL)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
