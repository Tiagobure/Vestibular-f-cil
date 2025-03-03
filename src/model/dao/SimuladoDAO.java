package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DataBase;
import model.Simulado;

public class SimuladoDAO {


	// Salva os resultados do simulado no banco de dados
	public void salvarResultado(Simulado simulado, int usuarioId) {
		String sql = "INSERT INTO resultados_simulado (usuario_id, vestibular, acertos, erros, tempo_restante) VALUES (?, ?, ?, ?, ?)";

		try (Connection conn = DataBase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, usuarioId);
			pstmt.setString(2, simulado.getVestibularSelecionado());
			pstmt.setInt(3, simulado.getAcertos());
			pstmt.setInt(4, simulado.getErros());
			pstmt.setInt(5, simulado.getTempoRestante());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Recupera o histórico de simulados de um usuário
	public List<Simulado> carregarHistorico(int usuarioId) {
		List<Simulado> historico = new ArrayList<>();
		String sql = "SELECT vestibular, acertos, erros, tempo_restante FROM resultados_simulado WHERE usuario_id = ?";

		try (Connection conn = DataBase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, usuarioId);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				String vestibular = rs.getString("vestibular");
				int acertos = rs.getInt("acertos");
				int erros = rs.getInt("erros");
				int tempoRestante = rs.getInt("tempo_restante");

				// Cria um novo Simulado com os dados recuperados
				Simulado simulado = new Simulado(vestibular, new ArrayList<>(), tempoRestante / 60);
				simulado.setAcertos(acertos);
				simulado.setErros(erros);
				historico.add(simulado);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return historico;
	}
}