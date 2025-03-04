package gui;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class MostrarCronogramaViewController {

	private Main mainApp;

	// Lista de cronogramas
	private ObservableList<String> cronogramas;

	// FXML
	@FXML
	private ListView<String> listaCronograma;

	@FXML
	private Button novoCronogramaButton;

	@FXML
	private Button editarCronogramaButton;

	@FXML
	private Button deletarCronogramaButton;
	
	@FXML
	private

	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
		cronogramas = FXCollections.observableArrayList(); // Inicializa a lista de cronogramas
		carregarCronogramas(); // Carrega cronogramas ao iniciar
	}

	@FXML
	public void initialize() {
		listaCronograma.setItems(cronogramas); // Vincula a lista ao ListView

		// Adiciona ações para os botões
		novoCronogramaButton.setOnAction(event -> abrirCadastroCronograma());
		editarCronogramaButton.setOnAction(event -> editarCronograma());
		deletarCronogramaButton.setOnAction(event -> deletarCronograma());
	}

	// Carrega os cronogramas
	private void carregarCronogramas() {
		// Aqui você carrega os cronogramas de um banco de dados ou arquivo.
		// Como exemplo, adicionamos alguns cronogramas fictícios:
		cronogramas.addAll("Cronograma 1", "Cronograma 2", "Cronograma 3");
	}

	// Abre a janela de cadastro de um novo cronograma
	@FXML
	private void abrirCadastroCronograma() {
		mainApp.carregarTela("/gui/CronogramaView.fxml", "Novo Cronograma");
	}

	// Edita o cronograma selecionado
	@FXML
	private void editarCronograma() {
		String cronogramaSelecionado = listaCronograma.getSelectionModel().getSelectedItem();
		if (cronogramaSelecionado != null) {
			mainApp.carregarTela("/gui/CronogramaView.fxml", "Editar Cronograma");
			// Você pode passar o cronograma selecionado para a tela de edição.
		} else {
			alerta("Selecione um cronograma", "Por favor, selecione um cronograma para editar.");
		}
	}

	// Deleta o cronograma selecionado
	@FXML
	private void deletarCronograma() {
		String cronogramaSelecionado = listaCronograma.getSelectionModel().getSelectedItem();
		if (cronogramaSelecionado != null) {
			cronogramas.remove(cronogramaSelecionado);
		} else {
			alerta("Selecione um cronograma", "Por favor, selecione um cronograma para deletar.");
		}
	}

	// Exibe um alerta
	private void alerta(String titulo, String mensagem) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(titulo);
		alert.setHeaderText(null);
		alert.setContentText(mensagem);
		alert.showAndWait();
	}
}
