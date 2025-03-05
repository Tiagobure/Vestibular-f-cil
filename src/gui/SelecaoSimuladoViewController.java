package gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.Main;
import application.MainAppAware;
import db.DbException;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Questao;
import model.Simulado;
import model.dao.QuestaoDAO;

public class SelecaoSimuladoViewController implements MainAppAware {
	private Main mainApp; // A variável mainApp será usada para carregar outras telas
	private QuestaoDAO questaoDAO; // A dependência do DAO

	@FXML
	private Label labelTempo;

	// Método para definir o QuestaoDAO (injeção de dependência)
	public void setQuestaoDAO(QuestaoDAO questaoDAO) {
		this.questaoDAO = questaoDAO;
	}

	// Método para definir o MainApp (injeção de dependência)
	@Override
	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	private void iniciarFUVEST() {
		// Verifica se o questaoDAO não é null antes de continuar
		if (questaoDAO == null) {
			exibirErro("Erro ao carregar as questões", "A dependência do QuestaoDAO não foi inicializada.");
			return;
		}
		List<Questao> questoesFUVEST = questaoDAO.listarPorExame("FUVEST");
		iniciarSimulado("FUVEST", questoesFUVEST);
	}

	@FXML
	private void iniciarENEM() {
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
	    
	    iniciarSimulado("ENEM", questoesENEM);
	}

	@FXML
	private void voltarMenu() {
		// Obtém o Stage atual
		Stage stage = (Stage) labelTempo.getScene().getWindow();

		// Cria uma transição de fade-out
		FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), stage.getScene().getRoot());
		fadeOut.setFromValue(1.0); // Opacidade inicial (totalmente visível)
		fadeOut.setToValue(0.0); // Opacidade final (totalmente invisível)
		fadeOut.setOnFinished(event -> {
			// Após a transição, carrega a MainView
			carregarMainView(stage);
		});
		fadeOut.play(); // Inicia a transição
	}

	private void carregarMainView(Stage stage) {
		// Usando o mainApp para carregar a tela
		mainApp.carregarTela("/gui/MainView.fxml", "Menu Principal", null);
	}

	private void iniciarSimulado(String exame, List<Questao> questoes) {
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
		Simulado simulado = new Simulado(exame, questoes, 180); // 180 minutos = 3 horas
		Map<String, Object> params = new HashMap<>();
		params.put("simulado", simulado);

		// Abre a tela de questões do simulado
		try {
			// Usando o mainApp para carregar a tela de questões
			mainApp.carregarTela("/view/QuestaoSimuladoView.fxml", "Questões do Simulado", params);

			// Fecha a tela de seleção de vestibular (opcional)
			Stage stage = (Stage) labelTempo.getScene().getWindow();
			if (stage != null) {
				stage.close();
			}
		} catch (DbException e) {
			e.printStackTrace();
			System.out.println("Erro ao carregar a tela de questões.");
		}
	}

	// Método auxiliar para exibir erros
	private void exibirErro(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
