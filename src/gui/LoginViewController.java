package gui;

import application.Main;
import application.MainAppAware;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Usuario;
import model.dao.UsuarioDAO;
import db.DbException;

public class LoginViewController implements MainAppAware {

	private Main mainApp;

	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	private TextField campoNome;
	@FXML
	private PasswordField campoSenha;
	@FXML
	private Button botaoLogin;

	private UsuarioDAO usuarioDAO = new UsuarioDAO();

	@FXML
	private void fazerLogin() {
		String nome = campoNome.getText().trim();
		String senha = campoSenha.getText().trim();

		// Validação dos campos
		if (nome.isEmpty() || senha.isEmpty()) {
			Alerts.showAlert("Erro", null, "Preencha todos os campos!", AlertType.ERROR);
			return;
		}

		try {
			// Tenta fazer o login
			Usuario usuario = usuarioDAO.fazerLogin(nome, senha);

			// Se o login for bem-sucedido, abre a tela principal
			abrirMainView();

			// Fecha a tela de login
			Stage stage = (Stage) botaoLogin.getScene().getWindow();
			stage.close();
		} catch (DbException e) {
			// Exibe mensagem de erro em caso de falha no login
			Alerts.showAlert("Erro", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void abrirMainView() {
		try {
			mainApp.carregarTela("/gui/MainView.fxml", "Avançar Vestibular", null);
		} catch (Exception e) {
			Alerts.showAlert("Erro", null, "Não foi possível abrir a tela principal!", AlertType.ERROR);
			e.printStackTrace();
		}
	}

	@FXML
	private void abrirCadastro() {
		try {
			mainApp.carregarTela("/gui/CadastroView.fxml", "Cadastro", null);
		} catch (Exception e) {
			Alerts.showAlert("Erro", null, "Não foi possível abrir a tela de cadastro!", AlertType.ERROR);
			e.printStackTrace();
		}
	}
}