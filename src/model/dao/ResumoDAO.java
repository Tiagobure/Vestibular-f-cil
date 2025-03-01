package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Resumo;

public class ResumoDAO {
	private static final String URL = "jdbc:sqlite:vestibular.db";

	public void inserir(Resumo resumo) {
		String sql = "INSERT INTO resumos (titulo, texto, materia, assunto, anexo) VALUES (?, ?, ?, ?, ?)";

		try (Connection conn = DriverManager.getConnection(URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, resumo.getTitulo());
			pstmt.setString(2, resumo.getTexto());
			pstmt.setString(3, resumo.getMateria());
			pstmt.setString(4, resumo.getAssunto());
			pstmt.setString(5, resumo.getAnexo());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Resumo> listarPorMateria(String materia) {
		List<Resumo> resumos = new ArrayList<>();
		String sql = "SELECT * FROM resumos WHERE materia = ?";

		try (Connection conn = DriverManager.getConnection(URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, materia);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Resumo r = new Resumo(rs.getString("titulo"), rs.getString("texto"), rs.getString("materia"),
						rs.getString("assunto"));
				r.setId(rs.getInt("id"));
				r.setAnexo(rs.getString("anexo"));
				resumos.add(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resumos;
	}

	// Outros métodos como buscar por assunto, deletar, atualizar, etc.
}
