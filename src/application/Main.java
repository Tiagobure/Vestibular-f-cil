package application;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import db.DataBase;
import gui.CronogramaViewController;
import gui.LoginViewController;
import gui.QuestaoSimuladoViewController;
import gui.util.Alerts;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Cronograma;
import model.Questao;
import model.Simulado;

public class Main extends Application {

	private static Scene mainScene;
	private int usuarioId; // ID do usuário logado

	public int getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(int usuarioId) {
		this.usuarioId = usuarioId;
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			// Carrega o arquivo FXML da View Principal
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/LoginView.fxml"));
			VBox root = loader.load();
			LoginViewController controller = loader.getController();
			controller.setMainApp(this);

			mainScene = new Scene(root);
			// Configura o título da janela
			primaryStage.setTitle("Login");
			// Define a cena no palco (Stage)
			primaryStage.setScene(mainScene);
			// Exibe a janela
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
			// Exibe uma mensagem de erro para o usuário
			Alerts.showAlert("Erro", "Erro ao carregar a tela de login",
					"O arquivo FXML da tela de login não pôde ser carregado.", AlertType.ERROR);
		}
	}

	public void carregarTela(String fxml, String titulo, Map<String, Object> params) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
			Parent root = loader.load();
			Object controller = loader.getController();

			if (controller instanceof MainAppAware) {
				((MainAppAware) controller).setMainApp(this);
			}

			if (controller instanceof CronogramaViewController && params != null) {
				CronogramaViewController cronogramaController = (CronogramaViewController) controller;

				if (params.containsKey("cronograma")) {
					cronogramaController.setCronogramaSelecionado((Cronograma) params.get("cronograma"));
				}

				if (params.containsKey("usuarioId")) {
					cronogramaController.setUsuarioId((int) params.get("usuarioId"));
				}
			}

			if (controller instanceof QuestaoSimuladoViewController && params != null) {
				QuestaoSimuladoViewController simuladoController = (QuestaoSimuladoViewController) controller;

				if (params.containsKey("simulado") && params.containsKey("questoes")) {
					Simulado simulado = (Simulado) params.get("simulado");

					// Verifique se o objeto em "questoes" é uma List<Questao>
					Object questoesObj = params.get("questoes");
					if (questoesObj instanceof List<?>) {
						List<Questao> questoes = (List<Questao>) questoesObj; // Cast seguro
						simuladoController.setSimulado(simulado, questoes);
					} else {
						throw new IllegalArgumentException("O parâmetro 'questoes' não é uma List<Questao>.");
					}
				}
			}
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.setTitle(titulo);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("Erro ao carregar a tela", null, "Não foi possível carregar a tela: " + fxml,
					AlertType.INFORMATION);

		}
	}

	public static Scene getMainScene() {
		return mainScene;
	}

	public static void main(String[] args) {
		DataBase.init();
//
//		String csvFile = "data/questoes.csv";
//		try {
//			DataBase.importarCSV(csvFile);
//		} catch (Exception e) {
//			System.err.println("Erro ao importar CSV: " + e.getMessage());
//			// Você pode decidir se quer continuar a aplicação ou encerrá-la aqui
//		}

		launch(args);
	}
}