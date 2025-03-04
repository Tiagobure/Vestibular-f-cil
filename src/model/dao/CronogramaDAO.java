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

	// Método para inserir um cronograma na base de dados
	public void inserir(Cronograma cronograma) {
		String sql = "INSERT INTO cronograma (diaSemana, horario, materia, assunto, usuario_id) VALUES (?, ?, ?, ?, ?)";

		try (Connection conn = DataBase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, cronograma.getDiaSemana());
			pstmt.setString(2, cronograma.getHorario());
			pstmt.setString(3, cronograma.getMateria());
			pstmt.setString(4, cronograma.getAssunto());
			pstmt.setInt(5, cronograma.getUsuarioId()); // Definindo o usuario_id
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Método para listar todos os cronogramas de um usuário
	public List<Cronograma> listarTodos(int usuarioId) {
		List<Cronograma> cronogramas = new ArrayList<>();
		String sql = "SELECT * FROM cronograma WHERE usuario_id = ?";

		try (Connection conn = DataBase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, usuarioId); // Filtro por usuario_id
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Cronograma c = new Cronograma(rs.getString("diaSemana"), rs.getString("horario"),
						rs.getString("materia"), rs.getString("assunto"), rs.getInt("usuario_id"));
				c.setId(rs.getInt("id"));
				cronogramas.add(c);
			}
		} catch (SQLException e) {
			System.err.println("Erro ao listar cronogramas: " + e.getMessage());
			e.printStackTrace();
		}

		return cronogramas;
	}

	public void editarCronograma(Cronograma cronograma) {
	    String sql = "UPDATE cronograma SET diaSemana = ?, horario = ?, materia = ?, assunto = ? WHERE id = ? AND usuario_id = ?";

	    try (Connection conn = DataBase.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setString(1, cronograma.getDiaSemana());
	        pstmt.setString(2, cronograma.getHorario());
	        pstmt.setString(3, cronograma.getMateria());
	        pstmt.setString(4, cronograma.getAssunto());
	        pstmt.setInt(5, cronograma.getId());
	        pstmt.setInt(6, cronograma.getUsuarioId()); // Garantir que apenas o usuário correto possa editar
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        System.err.println("Erro ao editar cronograma: " + e.getMessage());
	        e.printStackTrace();
	    }
	}
	// Método para listar os cronogramas de um determinado dia da semana
	public List<Cronograma> listarPorDia(String diaSemana, int usuarioId) {
		List<Cronograma> cronogramas = new ArrayList<>();
		String sql = "SELECT * FROM cronograma WHERE diaSemana = ? AND usuario_id = ?";

		try (Connection conn = DataBase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, diaSemana);
			pstmt.setInt(2, usuarioId); // Filtro por usuario_id
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Cronograma c = new Cronograma(rs.getString("diaSemana"), rs.getString("horario"),
						rs.getString("materia"), rs.getString("assunto"), rs.getInt("usuario_id"));
				c.setId(rs.getInt("id"));
				cronogramas.add(c);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return cronogramas;
	}

	// Método para deletar um cronograma
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
}
