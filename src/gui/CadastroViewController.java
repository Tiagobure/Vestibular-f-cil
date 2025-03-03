package gui;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Usuario;
import model.dao.UsuarioDAO;

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

		if (nome.isEmpty() || senha.isEmpty()) {
			System.out.println("Preencha todos os campos!");
			return;
		}

		Usuario usuario = new Usuario(nome, senha);
		usuarioDAO.cadastrarUsuario(usuario);
		System.out.println("Usuário cadastrado com sucesso!");
		
	}
}