package gui.controller;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Questao;
import model.Simulado;
import model.dao.QuestaoDAO;

public class SelecaoSimuladoViewController {
	private Stage stage;

	// Método para definir o Stage (janela) atual
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@FXML
	private void iniciarFUVEST() {
		QuestaoDAO questaoDAO = new QuestaoDAO();
		List<Questao> questoesFUVEST = questaoDAO.listarPorExame("FUVEST");
		iniciarSimulado("FUVEST", questoesFUVEST);
	}

	@FXML
	private void iniciarENEM() {
		QuestaoDAO questaoDAO = new QuestaoDAO();
		List<Questao> questoesENEM = questaoDAO.listarPorExame("ENEM");
		iniciarSimulado("ENEM", questoesENEM);
	}

	private void iniciarSimulado(String exame, List<Questao> questoes) {
		if (questoes.isEmpty()) {
			System.out.println("Nenhuma questão encontrada para o exame: " + exame);
			return;
		}

		// Cria o objeto Simulado
		Simulado simulado = new Simulado(exame, questoes, 180); // 180 minutos = 3 horas

		// Abre a tela de questões do simulado
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/QuestaoSimuladoView.fxml"));
			Parent root = loader.load();

			// Passa o simulado para o controlador da tela de questões
			QuestaoSimuladoViewController questaoController = loader.getController();
			questaoController.setSimulado(simulado);

			// Configura a cena e exibe a tela
			Scene scene = new Scene(root);
			Stage simuladoStage = new Stage();
			simuladoStage.setScene(scene);
			simuladoStage.setTitle("Simulado - " + exame);
			simuladoStage.show();

			// Fecha a tela de seleção de vestibular (opcional)
			if (stage != null) {
				stage.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erro ao carregar a tela de questões.");
		}
	}
}
