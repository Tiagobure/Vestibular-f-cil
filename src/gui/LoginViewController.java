package gui;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Usuario;
import model.dao.UsuarioDAO;

public class LoginViewController {

	@FXML
	private TextField campoNome;
	@FXML
	private PasswordField campoSenha;

	private UsuarioDAO usuarioDAO = new UsuarioDAO();

	@FXML
	private void fazerLogin() {
		String nome = campoNome.getText().trim();
		String senha = campoSenha.getText().trim();

		Usuario usuario = usuarioDAO.fazerLogin(nome, senha);
		if (usuario != null) {
			System.out.println("Login bem-sucedido! Bem-vindo, " + usuario.getNome());
			// Aqui você pode abrir a tela principal do sistema
		} else {
			System.out.println("Nome ou senha incorretos.");
			return;
		}
	}

	@FXML
	private void abrirCadastro() {
		// Aqui você pode abrir a tela de cadastro
	}
}