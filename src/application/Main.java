package application;

import java.io.IOException;

import db.DataBase;
import gui.LoginViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Usuario;
import model.dao.UsuarioDAO;

public class Main extends Application {

	private static Scene mainScene;

	
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

	public void carregarTela(String fxml, String titulo) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
			Parent root = loader.load();
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
