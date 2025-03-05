package gui;

import java.io.IOException;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Simulado;
import model.dao.SimuladoDAO;

public class ResultadoViewController {

	@FXML
	private Label labelAcertos; // Exibe o número de acertos
	@FXML
	private Label labelErros; // Exibe o número de erros
	@FXML
	private Label labelMensagem; // Exibe uma mensagem personalizada
	@FXML
	private Button botaoVoltar;

	private Simulado simulado; // Objeto Simulado
	private int usuarioId; // ID do usuário
	private SimuladoDAO simuladoDAO; // DAO para salvar resultados

	// Método para definir o simulado
	public void setSimulado(Simulado simulado) {
		if (simulado == null) {
			throw new IllegalArgumentException("Simulado não pode ser nulo.");
		}
		this.simulado = simulado;
		exibirResultados(); // Exibe os resultados assim que o simulado é definido
	}

	// Método para definir o ID do usuário
	public void setUsuarioId(int usuarioId) {
		if (usuarioId <= 0) {
			throw new IllegalArgumentException("ID do usuário inválido.");
		}
		this.usuarioId = usuarioId;
	}

	// Método para definir o DAO
	public void setSimuladoDAO(SimuladoDAO simuladoDAO) {
		if (simuladoDAO == null) {
			throw new IllegalArgumentException("SimuladoDAO não pode ser nulo.");
		}
		this.simuladoDAO = simuladoDAO;
	}

	// Método para exibir os resultados na tela
	private void exibirResultados() {
		if (simulado == null) {
			labelAcertos.setText("Erro: Simulado não carregado.");
			return;
		}

		// Atualiza os labels com os resultados
		labelAcertos.setText("Acertos: " + simulado.getAcertos());
		labelErros.setText("Erros: " + simulado.getErros());

		// Mensagem personalizada com base no desempenho
		if (simulado.getAcertos() > simulado.getErros()) {
			labelMensagem.setText("Parabéns! Você foi muito bem!");
		} else if (simulado.getAcertos() == simulado.getErros()) {
			labelMensagem.setText("Você foi bem, mas pode melhorar!");
		} else {
			labelMensagem.setText("Não desanime! Pratique mais e tente novamente.");
		}

		// Salva os resultados no banco de dados
		salvarResultado();
	}

	// Salva o resultado do simulado no banco de dados
	private void salvarResultado() {
		if (simuladoDAO != null && simulado != null && usuarioId > 0) {
			simuladoDAO.salvarResultado(simulado, usuarioId);
		} else {
			System.out.println("Erro: SimuladoDAO, Simulado ou ID do usuário não foi inicializado.");
		}
	}

	@FXML
	private void voltarParaSelecaoSimulado() {
		// Obtém o Stage atual
		Stage stage = (Stage) labelAcertos.getScene().getWindow();

		// Cria uma transição de fade-out
		FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), stage.getScene().getRoot());
		fadeOut.setFromValue(1.0); // Opacidade inicial (totalmente visível)
		fadeOut.setToValue(0.0); // Opacidade final (totalmente invisível)
		fadeOut.setOnFinished(event -> {
			// Após a transição, carrega a SelecaoSimuladoView
			carregarSelecaoSimuladoView(stage);
		});
		fadeOut.play(); // Inicia a transição
	}

	private void carregarSelecaoSimuladoView(Stage stage) {
		try {
			// Carrega a SelecaoSimuladoView
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SelecaoSimuladoView.fxml"));
			Parent root = loader.load();

			// Cria uma transição de fade-in para a nova cena
			FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), root);
			fadeIn.setFromValue(0.0); // Opacidade inicial (totalmente invisível)
			fadeIn.setToValue(1.0); // Opacidade final (totalmente visível)

			// Configura a cena e exibe a tela
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Seleção de Simulado");
			stage.show();

			// Inicia a transição de fade-in
			fadeIn.play();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erro ao carregar a tela de seleção de simulado.");
		}
	}
}