package application;

import java.io.IOException;
import java.util.Map;

import db.DataBase;
import gui.CronogramaViewController;
import gui.LoginViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Cronograma;

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
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erro");
			alert.setHeaderText("Erro ao carregar a tela de login");
			alert.setContentText("O arquivo FXML da tela de login não pôde ser carregado.");
			alert.showAndWait();
		}
	}

	public void carregarTela(String fxml, String titulo, Map<String, Object> params) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
			Parent root = loader.load();

			// Obtém o controlador da tela carregada
			Object controller = loader.getController();

			// Passa a referência do Main para todos os controladores que implementam
			// MainAppAware
			if (controller instanceof MainAppAware) {
				((MainAppAware) controller).setMainApp(this);
			}

			// Passa parâmetros específicos para o CronogramaViewController
			if (controller instanceof CronogramaViewController && params != null) {
				CronogramaViewController cronogramaController = (CronogramaViewController) controller;

				// Verifica se a chave "cronograma" existe no mapa
				if (params.containsKey("cronograma")) {
					cronogramaController.setCronogramaSelecionado((Cronograma) params.get("cronograma"));
				}

				// Verifica se a chave "usuarioId" existe no mapa
				if (params.containsKey("usuarioId")) {
					cronogramaController.setUsuarioId((int) params.get("usuarioId"));
				}
			}

			// Configura a cena e a janela
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.setTitle(titulo);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Scene getMainScene() {
		return mainScene;
	}

	public static void main(String[] args) {
		DataBase.init();

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