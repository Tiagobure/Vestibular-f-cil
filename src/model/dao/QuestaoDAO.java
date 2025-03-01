package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Questao;

public class QuestaoDAO {
	private static final String URL = "jdbc:sqlite:vestibular.db";

	// Método para listar questões por exame (vestibular)
	public List<Questao> listarPorExame(String exame) {
		List<Questao> questoes = new ArrayList<>();
		String sql = "SELECT * FROM questoes WHERE exame = ?";

		try (Connection conn = DriverManager.getConnection(URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, exame);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Questao q = new Questao(rs.getString("pergunta"), rs.getString("resposta"), rs.getString("materia"),
						rs.getString("assunto"), rs.getString("exame"));
				q.setId(rs.getInt("id"));
				questoes.add(q);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return questoes;
	}

	// Método para inserir uma nova questão
	public void inserir(Questao questao) {
		String sql = "INSERT INTO questoes (pergunta, resposta, materia, assunto, exame) VALUES (?, ?, ?, ?, ?)";

		try (Connection conn = DriverManager.getConnection(URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, questao.getPergunta());
			pstmt.setString(2, questao.getResposta());
			pstmt.setString(3, questao.getMateria());
			pstmt.setString(4, questao.getAssunto());
			pstmt.setString(5, questao.getExame());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Outros métodos (atualizar, deletar, buscar por ID, etc.) podem ser
	// adicionados aqui.
}
