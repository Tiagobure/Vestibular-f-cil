package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DataBase;
import gui.util.Alerts;
import javafx.scene.control.Alert.AlertType;
import model.Usuario;

public class UsuarioDAO {

	// Cadastra um novo usuário
	public void cadastrarUsuario(Usuario usuario) {
		String sql = "INSERT INTO usuarios (nome, senha) VALUES (?, ?)";

		try (Connection conn = DataBase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, usuario.getNome());
			pstmt.setString(2, usuario.getSenha());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Verifica as credenciais de login
	public Usuario fazerLogin(String nome, String senha) {
		String sql = "SELECT id, nome, senha FROM usuarios WHERE nome = ? AND senha = ?";

		try (Connection conn = DataBase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, nome);
			pstmt.setString(2, senha);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				Usuario usuario = new Usuario(rs.getString("nome"), rs.getString("senha"));
				usuario.setId(rs.getInt("id"));
				return usuario;
			}
		} catch (SQLException e) {
			Alerts.showAlert("Usuario não encontrado","Revise os dados","Usuario não existe", AlertType.INFORMATION);
			e.printStackTrace();
		}

		return null; // Retorna null se o login falhar
	}
}