package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		// inicializarBancoDeDados();
		try {
			// Carrega o arquivo FXML da View Principal
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			BorderPane root = loader.load();
			// Configura a cena
			Scene scene = new Scene(root);
			// Configura o título da janela
			primaryStage.setTitle("AVançar Vestibular");
			// Define a cena no palco (Stage)
			primaryStage.setScene(scene);
			// Exibe a janela
			primaryStage.show();
		} catch (IOException e) {
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
