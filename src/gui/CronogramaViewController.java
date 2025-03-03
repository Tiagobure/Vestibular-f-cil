package gui;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import model.Cronograma;
import model.dao.CronogramaDAO;

public class CronogramaViewController {

	private Main mainApp;
	private CronogramaDAO cronogramaDAO;

	@FXML
	private TextField campoDiaSemana; // Campo para o dia da semana
	@FXML
	private TextField campoHorario; // Campo para o horário
	@FXML
	private TextField campoMateria; // Campo para a matéria
	@FXML
	private TextField campoAssunto; // Campo para o assunto
	@FXML
	private CheckBox checkConcluido; // Checkbox para marcar como concluído

	private int usuarioId; // ID do usuário logado

	public void setUsuarioId(int usuarioId) {
		this.usuarioId = usuarioId;
	}

	public CronogramaViewController() {
		// Injeção de dependência
		this.cronogramaDAO = new CronogramaDAO();
	}

	// Método para salvar um bloco de estudo
	@FXML
	public void salvarBloco() {
		// Obtém os valores dos campos
		String diaSemana = campoDiaSemana.getText();
		String horario = campoHorario.getText();
		String materia = campoMateria.getText();
		String assunto = campoAssunto.getText();
		boolean concluido = checkConcluido.isSelected();

		// Valida se todos os campos estão preenchidos
		if (diaSemana.isEmpty() || horario.isEmpty() || materia.isEmpty() || assunto.isEmpty()) {
			Alerts.showAlert("Erro", null, "Preencha todos os campos", AlertType.ERROR);
			return;
		}

		// Cria um novo objeto Cronograma
		Cronograma cronograma = new Cronograma(diaSemana, horario, materia, assunto);
		cronograma.setConcluido(concluido);
		cronograma.setUsuarioId(usuarioId); // Define o ID do usuário

		// Insere o cronograma no banco de dados
		cronogramaDAO.inserir(cronograma);

		// mensagem de sucesso
		Alerts.showAlert("Sucesso", null, "Bloco salvo com sucesso!", AlertType.INFORMATION);

		// Limpa os campos após salvar
		limparCampos();
	}

	// Método para limpar os campos
	private void limparCampos() {
		campoDiaSemana.clear();
		campoHorario.clear();
		campoMateria.clear();
		campoAssunto.clear();
		checkConcluido.setSelected(false);
	}

	// Método para definir a referência do Main
	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}
}