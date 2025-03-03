package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {
    private static final String URL = "jdbc:sqlite:vestibular.db";

   
    // Queries SQL
    private static final String CREATE_TABLE_USUARIOS = 
    	"CREATE TABLE IF NOT EXISTS usuarios ("
    	 +"  id INTEGER PRIMARY KEY AUTOINCREMENT, "
    	 +" nome TEXT NOT NULL UNIQUE, "
    	 +"senha TEXT NOT NULL CHECK (LENGTH(senha) >= 60)";

    private static final String CREATE_TABLE_QUESTOES = 
        "CREATE TABLE IF NOT EXISTS questoes ("
        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
        + "pergunta TEXT NOT NULL, "
        + "resposta TEXT NOT NULL, "
        + "materia TEXT NOT NULL, "
        + "assunto TEXT NOT NULL, "
        + "exame TEXT NOT NULL)";

    private static final String CREATE_TABLE_RESUMOS = 
        "CREATE TABLE IF NOT EXISTS resumos ("
        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
        + "titulo TEXT NOT NULL, "
        + "texto TEXT NOT NULL, "
        + "materia TEXT NOT NULL, "
        + "assunto TEXT NOT NULL, "
        + "anexo TEXT, "
        + "usuario_id INTEGER NOT NULL, "
        + "FOREIGN KEY (usuario_id) REFERENCES usuarios(id))";

    private static final String CREATE_TABLE_PALAVRAS_CHAVE = 
        "CREATE TABLE IF NOT EXISTS palavras_chave ("
        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
        + "palavra TEXT NOT NULL, "
        + "descricao TEXT, "
        + "materia TEXT NOT NULL, "
        + "assunto TEXT NOT NULL, "
        + "usuario_id INTEGER NOT NULL, "
        + "FOREIGN KEY (usuario_id) REFERENCES usuarios(id))";

    private static final String CREATE_TABLE_CRONOGRAMA = 
    	    "CREATE TABLE IF NOT EXISTS cronograma ("
    	    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
    	    + "diaSemana TEXT NOT NULL, "
    	    + "horario TEXT NOT NULL, "
    	    + "materia TEXT NOT NULL, "
    	    + "assunto TEXT NOT NULL, "
    	    + "concluido BOOLEAN NOT NULL, " 
    	    + "usuario_id INTEGER NOT NULL, "
    	    + "FOREIGN KEY (usuario_id) REFERENCES usuarios(id))";
    
    private static final String CREATE_TABLE_RESULTADOS_SIMULADO = 
    	 "CREATE TABLE IF NOT EXISTS resultados_simulado ("
    	 + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
    	 + "usuario_id INTEGER NOT NULL, "
    	 + "vestibular TEXT NOT NULL, "
    	 + "acertos INTEGER NOT NULL, "
    	 + "erros INTEGER NOT NULL, "
    	 + "tempo_restante INTEGER NOT NULL, "
    	 + "FOREIGN KEY (usuario_id) REFERENCES usuarios(id))";

    public static void init() {
        try (Connection conn = DriverManager.getConnection(URL); 
             Statement stmt = conn.createStatement()) {
            
            // Executa as queries
            System.out.println("Criando tabela 'usuarios'...");
            stmt.execute(CREATE_TABLE_USUARIOS);
            System.out.println("Criando tabela 'questoes'...");
            stmt.execute(CREATE_TABLE_QUESTOES);
            System.out.println("Criando tabela 'resumos'...");
            stmt.execute(CREATE_TABLE_RESUMOS);
            System.out.println("Criando tabela 'palavras_chave'...");
            stmt.execute(CREATE_TABLE_PALAVRAS_CHAVE);
            System.out.println("Criando tabela 'cronograma'...");
            stmt.execute(CREATE_TABLE_CRONOGRAMA);
            System.out.println("Criando tabela 'resultados_simulado'...");
            stmt.execute(CREATE_TABLE_RESULTADOS_SIMULADO); //
            
            //Verifica se as tabelas foram criadas
            ResultSet rs = conn.getMetaData().getTables(null, null, "%", null);
            while (rs.next()) {
                System.out.println("Tabela encontrada: " + rs.getString("TABLE_NAME"));
            }
            
            System.out.println("Banco de dados inicializado com sucesso!");

        } catch (SQLException e) {
        	System.err.println("Erro ao inicializar o banco de dados: " + e.getMessage());
            throw new RuntimeException("Falha na inicialização do banco de dados", e);
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}