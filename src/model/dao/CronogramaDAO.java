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
		String sql = "INSERT INTO cronograma (diaSemana, horario, materia, assunto, concluido) VALUES (?, ?, ?, ?, ?)";

		try (Connection conn = DataBase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, cronograma.getDiaSemana());
			pstmt.setString(2, cronograma.getHorario());
			pstmt.setString(3, cronograma.getMateria());
			pstmt.setString(4, cronograma.getAssunto());
			pstmt.setBoolean(5, cronograma.isConcluido());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Cronograma> listarPorDia(String diaSemana) {
		List<Cronograma> cronogramas = new ArrayList<>();
		String sql = "SELECT * FROM cronograma WHERE diaSemana = ?";

		try (Connection conn = DataBase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, diaSemana);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Cronograma c = new Cronograma(rs.getString("diaSemana"), rs.getString("horario"),
						rs.getString("materia"), rs.getString("assunto"));
				c.setId(rs.getInt("id"));
				c.setConcluido(rs.getBoolean("concluido"));
				cronogramas.add(c);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return cronogramas;
	}
	// Método para listar todos os blocos de estudo
    public List<Cronograma> listarTodos() {
        List<Cronograma> blocos = new ArrayList<>();
        String sql = "SELECT * FROM cronograma";

        try (Connection conn = DataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Cronograma bloco = new Cronograma(
                    rs.getString("diaSemana"),
                    rs.getString("horario"),
                    rs.getString("materia"),
                    rs.getString("assunto")
                );
                bloco.setId(rs.getInt("id"));
                blocos.add(bloco);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar cronograma: " + e.getMessage());
            e.printStackTrace();
        }

        return blocos;
    }

	// Outros métodos como marcar como concluído, atualizar, deletar, etc.
}