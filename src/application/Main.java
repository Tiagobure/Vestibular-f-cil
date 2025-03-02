package application;

import java.io.IOException;

import gui.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static Scene mainScene;
	@Override
	public void start(Stage primaryStage) {
		// inicializarBancoDeDados();
		
		
		try {
			// Carrega o arquivo FXML da View Principal
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			BorderPane root = loader.load();
			MainViewController controller = loader.getController();
		    controller.setMainApp(this);
			
		    mainScene = new Scene(root);
			// Configura o título da janela
			primaryStage.setTitle("AVançar Vestibular");
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


	// private void inicializarBancoDeDados() {
	// try (Connection conn =
	// DriverManager.getConnection("jdbc:sqlite:vestibular.db")) {
	// System.out.println("Banco de dados conectado!");
	// } catch (SQLException e) {
	// System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
	// }
	// }

	public static void main(String[] args) {
		launch(args);
	}
}
