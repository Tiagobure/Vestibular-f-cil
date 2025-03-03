package gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class MostrarCronogramaViewController {

	@FXML
	private ListView<String> listaCronograma; // ListView para exibir os cronogramas

	@FXML
	private Button botaoAtualizar; // Botão para atualizar a lista

	@FXML
	private Button botaoNovoCronograma; // Botão para abrir a tela de cadastro

	@FXML
	private void initialize() {
		// Inicializa a lista de cronogramas (pode ser carregada de um banco de dados ou
		// serviço)
		atualizarListaCronograma();
	}

	// Método para atualizar a lista de cronogramas
	@FXML
	private void atualizarCronograma() {
		System.out.println("Atualizando cronograma...");
		atualizarListaCronograma();
	}

	// Método para abrir a tela de cadastro de novo cronograma
	@FXML
	private void abrirCadastroCronograma() {
		try {
			// Carrega o arquivo FXML da tela de cadastro
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/CronogramaView.fxml"));
			Parent root = loader.load();

			// Cria uma nova cena
			Scene scene = new Scene(root);

			// Cria um novo estágio (janela) para a tela de cadastro
			Stage stage = new Stage();
			stage.setTitle("Cronograma de Estudos");
			stage.setScene(scene);

			// Exibe a tela de cadastro
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Erro ao carregar a tela de cadastro de cronograma.");
		}
	}

	// Método auxiliar para atualizar a lista de cronogramas
	private void atualizarListaCronograma() {
		// Limpa a lista atual
		listaCronograma.getItems().clear();

		// Adiciona itens de exemplo à lista (substitua por dados reais)
		listaCronograma.getItems().addAll("Cronograma 1: Matemática - 10 horas", "Cronograma 2: Física - 8 horas",
				"Cronograma 3: Química - 6 horas");

		System.out.println("Lista de cronogramas atualizada.");
	}

}
