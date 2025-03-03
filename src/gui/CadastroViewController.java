package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Usuario;
import model.dao.UsuarioDAO;
import gui.util.*;
public class CadastroViewController {

	@FXML
	private TextField campoNome;
	@FXML
	private PasswordField campoSenha;

	private UsuarioDAO usuarioDAO = new UsuarioDAO();

	@FXML
	private void cadastrarUsuario() {
		String nome = campoNome.getText().trim();
		String senha = campoSenha.getText().trim();

		// Validação básica
		if (nome.isEmpty() || senha.isEmpty()) {
			Alerts.showAlert("Error", null, "Preencha todos os campos!", AlertType.ERROR);
			return;
		}

		if (senha.length() < 6) {
			Alerts.showAlert("Error", null,"A senha deve ter pelo menos 6 caracteres!", AlertType.WARNING);
			return;
		}

		Usuario usuario = new Usuario(nome, senha);

		try {
			usuarioDAO.cadastrarUsuario(usuario);
			Alerts.showAlert("Sucesso", null,"Usuário cadastrado com sucesso!", AlertType.INFORMATION);
			limparCampos();
		} catch (Exception e) {
			Alerts.showAlert("Erro", null, "Nome de usuário já cadastrado!", AlertType.ERROR);
		}
	}

	

	private void limparCampos() {
		campoNome.clear();
		campoSenha.clear();
	}
}
