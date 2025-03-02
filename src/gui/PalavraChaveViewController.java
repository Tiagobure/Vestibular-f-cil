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

		PalavraChave palavraChave = new PalavraChave(palavra, descricao, materia, assunto);
		palavraChaveDAO.inserir(palavraChave);

		// Limpar campos após salvar
		campoPalavra.clear();
		campoDescricao.clear();
		campoMateria.clear();
		campoAssunto.clear();
	}
}
