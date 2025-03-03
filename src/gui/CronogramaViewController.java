package gui;

import java.util.List;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.Cronograma;
import model.dao.CronogramaDAO;

public class CronogramaViewController {

	private Main mainApp;
	private CronogramaDAO cronogramaDAO;
	
	@FXML
	private TextField campoDiaSemana;
	@FXML
	private TextField campoHorario;
	@FXML
	private TextField campoMateria;
	@FXML
	private TextField campoAssunto;
	@FXML
	private CheckBox checkConcluido;
	@FXML
	private ListView<String> listaCronograma;

	public CronogramaViewController() {
		// Injeção de dependência 
		this.cronogramaDAO = new CronogramaDAO(); 
	}

	@FXML
	public void salvarBloco() {
		String diaSemana = campoDiaSemana.getText();
		String horario = campoHorario.getText();
		String materia = campoMateria.getText();
		String assunto = campoAssunto.getText();
		boolean concluido = checkConcluido.isSelected();

		if (diaSemana.isEmpty() || horario.isEmpty() || materia.isEmpty() || assunto.isEmpty()) {
			Alerts.showAlert("Erro",null,"Preencha todos os campos", AlertType.ERROR);
			return;
		}

		Cronograma cronograma = new Cronograma(diaSemana, horario, materia, assunto);
		cronograma.setConcluido(concluido);
		cronogramaDAO.inserir(cronograma);

		Alerts.showAlert("Sucesso",null, "Bloco salvo com sucesso!", AlertType.INFORMATION);
		limparCampos();
		atualizarCronograma();
	}

	private void limparCampos() {
		campoDiaSemana.clear();
		campoHorario.clear();
		campoMateria.clear();
		campoAssunto.clear();
		checkConcluido.setSelected(false);
	}

	@FXML
	private void abrirCadastroCronograma() {
		try {
			mainApp.carregarTela("/gui/CronogramaView.fxml", "Cadastro");
		} catch (Exception e) {
			Alerts.showAlert("Erro",null, "Erro ao abrir a tela de cadastro: " + e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}

	@FXML
	public void initialize() {
		atualizarCronograma();
	}

	private void atualizarCronograma() {
		listaCronograma.getItems().clear();
		List<Cronograma> blocos = cronogramaDAO.listarTodos();

		for (Cronograma bloco : blocos) {
			String status = bloco.isConcluido() ? "[Concluído] " : "[Pendente] ";
			String texto = status + bloco.getDiaSemana() + " - " + bloco.getHorario() + ": " + bloco.getMateria() + " ("
					+ bloco.getAssunto() + ")";
			listaCronograma.getItems().add(texto);
		}
	}


	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}

}
