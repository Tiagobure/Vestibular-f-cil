package gui;

import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Resumo;
import model.dao.ResumoDAO;

public class ResumosViewController {
	@FXML
	private TextField campoTitulo;
	@FXML
	private TextArea campoTexto;
	@FXML
	private TextField campoMateria;
	@FXML
	private TextField campoAssunto;
	@FXML
	private TextField campoAnexo;

	private ResumoDAO resumoDAO = new ResumoDAO();
	private int usuarioId; // ID do usuário logado

	public void setUsuarioId(int usuarioId) {
		this.usuarioId = usuarioId;
	}

	@FXML
	public void initialize() {
		// Limitar o tamanho do texto
		campoTexto.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() > 5000) { // Limite de 5000 caracteres
				campoTexto.setText(oldValue);
			}
		});
	}

	@FXML
	public void salvarResumo() {

		String titulo = campoTitulo.getText();
		String texto = campoTexto.getText();
		String materia = campoMateria.getText();
		String assunto = campoAssunto.getText();
		String anexo = campoAnexo.getText();

		if (titulo.isEmpty() || texto.isEmpty() || materia.isEmpty() || assunto.isEmpty()) {
			Alerts.showAlert("Aviso", null, "Campos em branco", AlertType.WARNING);
			return;
		}

		Resumo resumo = new Resumo(titulo, texto, materia, assunto);
		resumo.setAnexo(anexo);
		resumoDAO.inserir(resumo, usuarioId);

		// Limpar campos após salvar
		System.out.println("Resumo salvo com sucesso!");
		limparCampos();

	}

	private void limparCampos() {
		campoTitulo.clear();
		campoTexto.clear();
		campoMateria.clear();
		campoAssunto.clear();
		campoAnexo.clear();
	}
}