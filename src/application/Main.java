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
		// inicializarBancoDeDados();

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
		launch(args);

	}
}
