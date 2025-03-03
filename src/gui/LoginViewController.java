package gui;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Usuario;
import model.dao.UsuarioDAO;

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

		Usuario usuario = usuarioDAO.fazerLogin(nome, senha);
		
		if (usuario != null) {
			System.out.println("Login bem-sucedido! Bem-vindo, " + usuario.getNome());
			// Aqui você pode abrir a tela principal do sistema
			abrirMainView();
		} else {
			System.out.println("Nome ou senha incorretos.");
			return;
		}
	}
	
	private void abrirMainView() {
		try{
			mainApp.carregarTela("/gui/MainView.fxml", "AVançar Vestibular");
	        }catch(Exception e) {
	            System.err.println("Erro ao abrir a tela principal: " + e.getMessage());
	        e.printStackTrace();
	        }
	}

	@FXML
	private void abrirCadastro() {
		// Aqui você pode abrir a tela de cadastro
		try{
			mainApp.carregarTela("/gui/CadastroView.fxml", "Cadastro");
	        }catch(Exception e) {
	            System.err.println("Erro ao abrir a tela de cadastro: " + e.getMessage());
	        e.printStackTrace();
	        }
	}
}