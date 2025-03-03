package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DataBase;
import model.PalavraChave;

public class PalavraChaveDAO {

	public void inserir(PalavraChave palavraChave, int usuarioId) {
		String sql = "INSERT INTO palavras_chave (palavra, descricao, materia, assunto, usuario_id) VALUES (?, ?, ?, ?, ?)";

		try (Connection conn = DataBase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, palavraChave.getPalavra());
			pstmt.setString(2, palavraChave.getDescricao());
			pstmt.setString(3, palavraChave.getMateria());
			pstmt.setString(4, palavraChave.getAssunto());
			pstmt.setInt(5, usuarioId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<PalavraChave> listarPorMateria(String materia, int usuarioId) {
		List<PalavraChave> palavrasChave = new ArrayList<>();
		String sql = "SELECT * FROM palavras_chave WHERE materia = ? AND usuario_id = ?";

		try (Connection conn = DataBase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, materia);
			pstmt.setInt(2, usuarioId);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				PalavraChave pc = new PalavraChave(rs.getString("palavra"), rs.getString("descricao"),
						rs.getString("materia"), rs.getString("assunto"), rs.getInt("usuario_id"));
				pc.setId(rs.getInt("id"));
				palavrasChave.add(pc);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return palavrasChave;
	}

	public List<PalavraChave> buscarPorTermo(String termo, int usuarioId) {
		List<PalavraChave> palavrasChave = new ArrayList<>();
		String sql = "SELECT * FROM palavras_chave WHERE usuario_id = ? AND (" + "LOWER(palavra) LIKE LOWER(?) OR "
				+ "LOWER(descricao) LIKE LOWER(?) OR " + "LOWER(materia) LIKE LOWER(?) OR "
				+ "LOWER(assunto) LIKE LOWER(?))";

		try (Connection conn = DataBase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			String termoBusca = "%" + termo + "%";
			pstmt.setInt(1, usuarioId);
			pstmt.setString(2, termoBusca);
			pstmt.setString(3, termoBusca);
			pstmt.setString(4, termoBusca);
			pstmt.setString(5, termoBusca);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				PalavraChave pc = new PalavraChave(rs.getString("palavra"), rs.getString("descricao"),
						rs.getString("materia"), rs.getString("assunto"), rs.getInt("usuario_id"));
				pc.setId(rs.getInt("id"));
				palavrasChave.add(pc);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return palavrasChave;
	}

	public void deletar(PalavraChave palavraChave) {
		String sql = "DELETE FROM palavras_chave WHERE id = ?";
		try (Connection conn = DataBase.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, palavraChave.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
