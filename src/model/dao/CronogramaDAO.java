package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DataBase;
import model.Cronograma;

public class CronogramaDAO {

	public void inserir(Cronograma cronograma) {
		String sql = "INSERT INTO cronograma (diaSemana, horario, materia, assunto, concluido, usuario_id) VALUES (?, ?, ?, ?, ?)";

		try (Connection conn = DataBase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, cronograma.getDiaSemana());
			pstmt.setString(2, cronograma.getHorario());
			pstmt.setString(3, cronograma.getMateria());
			pstmt.setString(4, cronograma.getAssunto());
			pstmt.setBoolean(5, cronograma.isConcluido());
	        pstmt.setInt(6, cronograma.getUsuarioId()); 
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Cronograma> listarPorDia(String diaSemana, int usuarioId) {
	    List<Cronograma> cronogramas = new ArrayList<>();
	    String sql = "SELECT * FROM cronograma WHERE diaSemana = ? AND usuario_id = ?";

	    try (Connection conn = DataBase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setString(1, diaSemana);
	        pstmt.setInt(2, usuarioId); // Filtro por usuario_id
	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            Cronograma c = new Cronograma(rs.getString("diaSemana"), rs.getString("horario"),
	                    rs.getString("materia"), rs.getString("assunto"));
	            c.setId(rs.getInt("id"));
	            c.setConcluido(rs.getBoolean("concluido"));
	            c.setUsuarioId(rs.getInt("usuario_id")); // Definindo usuario_id
	            cronogramas.add(c);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return cronogramas;
	}
	
	// Método para listar todos os blocos de estudo
	public List<Cronograma> listarTodos(int usuarioId) {
	    List<Cronograma> blocos = new ArrayList<>();
	    String sql = "SELECT * FROM cronograma WHERE usuario_id = ?";

	    try (Connection conn = DataBase.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, usuarioId); // Filtro por usuario_id
	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            Cronograma bloco = new Cronograma(
	                rs.getString("diaSemana"),
	                rs.getString("horario"),
	                rs.getString("materia"),
	                rs.getString("assunto")
	            );
	            bloco.setId(rs.getInt("id"));
	            bloco.setConcluido(rs.getBoolean("concluido"));
	            bloco.setUsuarioId(rs.getInt("usuario_id")); // Definindo usuario_id
	            blocos.add(bloco);
	        }
	    } catch (SQLException e) {
	        System.err.println("Erro ao listar cronograma: " + e.getMessage());
	        e.printStackTrace();
	    }

	    return blocos;
	}
    
    public void marcarComoConcluido(int id, int usuarioId) {
        String sql = "UPDATE cronograma SET concluido = true WHERE id = ? AND usuario_id = ?";

        try (Connection conn = DataBase.getConnection();
           PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setInt(2, usuarioId); // Garantir que apenas o usuário correto possa marcar como concluído
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    public void deletar(int id, int usuarioId) {
        String sql = "DELETE FROM cronograma WHERE id = ? AND usuario_id = ?";

        try (Connection conn = DataBase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setInt(2, usuarioId); // Garantir que apenas o usuário correto possa deletar
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	// Outros métodos como marcar como concluído, atualizar, deletar, etc.
}