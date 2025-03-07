package gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.Main;
import application.MainAppAware;
import db.DbException;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Questao;
import model.Simulado;
import model.dao.QuestaoDAO;

public class SelecaoSimuladoViewController implements MainAppAware {

	private Main mainApp; // A variável mainApp será usada para carregar outras telas
	private QuestaoDAO questaoDAO = new QuestaoDAO(); // A dependência do DAO
	@FXML
	private Button btEnem;
	@FXML
	private Button btFuvest;
	@FXML
	private Button btVoltar;

	private Label labelTempo;

	// Método para definir o QuestaoDAO (injeção de dependência)
	public void setQuestaoDAO(QuestaoDAO questaoDAO) {
		System.out.println("QuestaoDAO está sendo definido: " + questaoDAO);
		if (questaoDAO == null) {
			throw new IllegalArgumentException("QuestaoDAO não pode ser nulo.");
		}
		this.questaoDAO = questaoDAO;
	}

	// Método para definir o MainApp (injeção de dependência)
	@Override
	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	private void iniciarFUVEST(ActionEvent event) {
		// Verifica se o questaoDAO não é null antes de continuar
		if (questaoDAO == null) {
			exibirErro("Erro ao carregar as questões", "A dependência do QuestaoDAO não foi inicializada.");
			return;
		}
		List<Questao> questoesFUVEST = questaoDAO.listarPorExame("FUVEST");

		if (questoesFUVEST.isEmpty()) {
			exibirErro("Nenhuma questão encontrada", "Não há questões disponíveis para o exame FUVEST.");
			return;
		}
		iniciarSimulado("FUVEST", questoesFUVEST, event.getSource());
	}

	@FXML
	private void iniciarENEM(ActionEvent event) {
		// Verifica se o questaoDAO não é null antes de continuar
		if (questaoDAO == null) {
			exibirErro("Erro ao carregar as questões", "A dependência do QuestaoDAO não foi inicializada.");
			return;
		}
		List<Questao> questoesENEM = questaoDAO.listarPorExame("ENEM");

		if (questoesENEM.isEmpty()) {
			exibirErro("Nenhuma questão encontrada", "Não há questões disponíveis para o exame ENEM.");
			return;
		}

		iniciarSimulado("ENEM", questoesENEM, event.getSource());
	}

	@FXML
	private void voltarMenu(ActionEvent event) {
		// Obtém o Stage atual
	    fadeOutAndClose((Node) event.getSource(), () -> {
	        mainApp.carregarTela("/gui/MainView.fxml", "Menu Principal", null);
	    }); // Inicia a transição
	}



	private void iniciarSimulado(String exame, List<Questao> questoes, Object source) {
		if (questoes.isEmpty()) {
			// Exibe um alerta para o usuário
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Nenhuma questão encontrada");
			alert.setHeaderText(null);
			alert.setContentText("Não há questões disponíveis para o exame: " + exame);
			alert.showAndWait();
			return;
		}

		// Cria o objeto Simulado
		Simulado simulado = new Simulado(exame, questoes, 180); // 180 minutos = 2 horas

		// Prepara os parâmetros para a próxima tela
		Map<String, Object> params = new HashMap<>();
		params.put("simulado", simulado);
		params.put("questoes", questoes); // Passa a lista de questões, não o QuestaoDAO

		fadeOutAndClose((Node) source, () -> {
			// Abre a tela de questões do simulado
			try {
				mainApp.carregarTela("/gui/QuestaoSimuladoView.fxml", "simulado", params);
			} catch (DbException e) {
				e.printStackTrace();
				exibirErro("Erro ao carregar a tela de questões", e.getMessage());
			}
		});

	}

	// Método auxiliar para exibir erros
	private void exibirErro(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private void fadeOutAndClose(Node node, Runnable onFinished) {
		Stage stage = (Stage) node.getScene().getWindow();
        
		FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.2), stage.getScene().getRoot());
		fadeOut.setFromValue(1.0);
		fadeOut.setToValue(0.0);
		fadeOut.setOnFinished(e -> {
			onFinished.run(); // Executa a ação após o fade-out
			stage.close(); // Fecha o stage
		});
		fadeOut.play();
	}

}