package gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.PalavraChave;
import model.dao.PalavraChaveDAO;

public class PalavraChaveViewController {
	@FXML
	private TextField campoPalavra;
	@FXML
	private TextArea campoDescricao;
	@FXML
	private TextField campoMateria;
	@FXML
	private TextField campoAssunto;

	private PalavraChaveDAO palavraChaveDAO = new PalavraChaveDAO();

	@FXML
	public void salvarPalavraChaveAction() {
		String palavra = campoPalavra.getText();
		String descricao = campoDescricao.getText();
		String materia = campoMateria.getText();
		String assunto = campoAssunto.getText();

		if (palavra.isEmpty() || descricao.isEmpty() || materia.isEmpty() || assunto.isEmpty()) {
			System.out.println("Preencha todos os campos!");
			return;
		}

		PalavraChave palavraChave = new PalavraChave(palavra, descricao, materia, assunto);
		palavraChaveDAO.inserir(palavraChave);

		System.out.println("Palavra-chave salva com sucesso!");
		limparCampos();
	}

	private void limparCampos() {
		campoPalavra.clear();
		campoDescricao.clear();
		campoMateria.clear();
		campoAssunto.clear();
	}
}
