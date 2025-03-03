package gui;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Usuario;
import model.dao.UsuarioDAO;
import gui.util.*;

public class LoginViewController {

	private Main mainApp;

	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	private TextField campoNome;
	@FXML
	private PasswordField campoSenha;

	private UsuarioDAO usuarioDAO = new UsuarioDAO();

	@FXML
	private void fazerLogin() {
		String nome = campoNome.getText().trim();
		String senha = campoSenha.getText().trim();

		if (nome.isEmpty() || senha.isEmpty()) {
			Alerts.showAlert("Erro", null, "Preencha todos os campos!", AlertType.ERROR);
			;
			return;
		}

		Usuario usuario = usuarioDAO.fazerLogin(nome, senha);

		if (usuario != null) {
			Alerts.showAlert("Sucesso", null, "Bem-vindo, " + usuario.getNome() + "!", AlertType.INFORMATION);
			abrirMainView();
		} else {
			Alerts.showAlert("Erro", null, "Nome ou senha incorretos!", AlertType.ERROR);
		}
	}

	private void abrirMainView() {
		try {
			mainApp.carregarTela("/gui/MainView.fxml", "Avançar Vestibular");
		} catch (Exception e) {
			Alerts.showAlert("Erro", null, "Não foi possível abrir a tela principal!", AlertType.ERROR);
			e.printStackTrace();
		}
	}

	@FXML
	private void abrirCadastro() {
		try {
			mainApp.carregarTela("/gui/CadastroView.fxml", "Cadastro");
		} catch (Exception e) {
			Alerts.showAlert("Erro", null, "Não foi possível abrir a tela de cadastro!", AlertType.ERROR);
			e.printStackTrace();
		}
	}

}
