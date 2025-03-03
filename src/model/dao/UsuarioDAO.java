package model.dao;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DataBase;
import gui.util.Alerts;
import javafx.scene.control.Alert.AlertType;
import model.Usuario;

public class UsuarioDAO {

	// Cadastra um novo usuário com senha criptografada
	public void cadastrarUsuario(Usuario usuario) {
		String sql = "INSERT INTO usuarios (nome, senha) VALUES (?, ?)";

		try (Connection conn = DataBase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, usuario.getNome());
			pstmt.setString(2, hashSenha(usuario.getSenha())); // Armazena a senha criptografada
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Verifica as credenciais de login
	public Usuario fazerLogin(String nome, String senha) {
		String sql = "SELECT id, nome, senha FROM usuarios WHERE nome = ?";

		try (Connection conn = DataBase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, nome);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				String senhaHash = rs.getString("senha");
				if (hashSenha(senha).equals(senhaHash)) { // Compara a senha informada com o hash armazenado
					Usuario usuario = new Usuario(rs.getString("nome"), senha);
					usuario.setId(rs.getInt("id"));
					return usuario;
				}
			}

			// Mensagem genérica para evitar vazamento de informações
			Alerts.showAlert("Erro de autenticação", "Nome ou senha incorretos", "Tente novamente.",
					AlertType.INFORMATION);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null; // Retorna null se o login falhar
	}

	// Gera um hash SHA-256 para a senha
	private String hashSenha(String senha) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hashBytes = digest.digest(senha.getBytes(StandardCharsets.UTF_8));

			// Converte bytes para uma string hexadecimal
			StringBuilder hexString = new StringBuilder();
			for (byte b : hashBytes) {
				hexString.append(String.format("%02x", b));
			}

			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Erro ao gerar hash da senha", e);
		}
	}
}
