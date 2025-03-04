package gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.Main;
import application.MainAppAware;
import gui.util.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import model.Cronograma;
import model.dao.CronogramaDAO;

public class MostrarCronogramaViewController implements MainAppAware {

	private Main mainApp;

	// Lista de cronogramas (strings para exibição)
	private ObservableList<String> cronogramas = FXCollections.observableArrayList();

	// Lista de objetos Cronograma
	private List<Cronograma> listaCronogramaObjetos;

	// FXML
	@FXML
	private ListView<String> listaCronograma;

	@FXML
	private Button novoCronogramaButton;

	@FXML
	private Button editarCronogramaButton;

	@FXML
	private Button deletarCronogramaButton;

	private CronogramaDAO cronogramaDAO = new CronogramaDAO();

	private int usuarioId;

	public void setUsuarioId(int usuarioId) {
		this.usuarioId = usuarioId;
	}

	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
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
		int usuarioId = obterUsuarioId(); // Obtém o ID do usuário logado
		listaCronogramaObjetos = cronogramaDAO.listarTodos(usuarioId);

		// Limpa a lista atual e adiciona os novos cronogramas
		cronogramas.clear();
		for (Cronograma c : listaCronogramaObjetos) {
			cronogramas.add(c.toString()); // Converte Cronograma para String
		}
	}

	// Obtém o ID do usuário logado
	private int obterUsuarioId() {
		// Implemente a lógica para obter o ID do usuário logado
		return usuarioId;
	}

	// Abre a janela de cadastro de um novo cronograma
	@FXML
	private void abrirCadastroCronograma() {
		try {
			mainApp.carregarTela("/gui/CronogramaView.fxml", "Novo Cronograma", null);
		} catch (Exception e) {
			Alerts.showAlert("Erro", null, "Não foi possível abrir a tela principal!", AlertType.ERROR);
			e.printStackTrace();
		}
	}

	// Edita o cronograma selecionado
	@FXML
	private void editarCronograma() {
		if (verificarSelecao()) {
			int indiceSelecionado = listaCronograma.getSelectionModel().getSelectedIndex();
			Cronograma cronogramaSelecionado = listaCronogramaObjetos.get(indiceSelecionado);

			Map<String, Object> params = new HashMap<>();
			params.put("cronograma", cronogramaSelecionado);
			params.put("usuarioId", usuarioId);
			try {
				mainApp.carregarTela("/gui/CronogramaView.fxml", "Editar Cronograma", params);
			} catch (Exception e) {
				Alerts.showAlert("Erro", null, "não foi possivel abrir ", AlertType.ERROR);
				e.printStackTrace();
			}
		}

	}

	// Deleta o cronograma selecionado
	@FXML
	private void deletarCronograma() {
		if (verificarSelecao()) {
			int indiceSelecionado = listaCronograma.getSelectionModel().getSelectedIndex();
			Cronograma cronogramaSelecionado = listaCronogramaObjetos.get(indiceSelecionado);

			// Remove do banco de dados
			cronogramaDAO.deletar(cronogramaSelecionado.getId(), obterUsuarioId());

			// Remove da lista de objetos e da lista de strings
			listaCronogramaObjetos.remove(indiceSelecionado);
			cronogramas.remove(indiceSelecionado);
		}
	}

	// Verifica se um cronograma foi selecionado
	private boolean verificarSelecao() {
		if (listaCronograma.getSelectionModel().getSelectedItem() == null) {
			Alerts.showAlert("Selecione um cronograma", null, "Por favor, selecione um cronograma.",
					AlertType.INFORMATION);
			return false;
		}
		return true;
	}

}