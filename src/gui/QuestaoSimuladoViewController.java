package gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import model.Questao;
import model.Simulado;

public class QuestaoSimuladoViewController {
	@FXML
	private Label labelTempo;
	@FXML
	private Label labelQuestao;
	@FXML
	private VBox containerAlternativas;

	private Simulado simulado;
	private Timeline timeline;
	private ToggleGroup grupoAlternativas = new ToggleGroup();

	public void setSimulado(Simulado simulado) {
		this.simulado = simulado;
		if (simulado != null) {
			carregarQuestaoAtual();
			iniciarCronometro();
		} else {
			System.out.println("Erro: Simulado não foi inicializado.");
		}
	}

	@FXML
	public void initialize() {
		containerAlternativas.getChildren().clear();
	}

	private void iniciarCronometro() {
		timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> Platform.runLater(this::atualizarTempo)));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}

	private void atualizarTempo() {
		if (simulado == null)
			return;

		simulado.atualizarTempo();
		int minutos = simulado.getTempoRestante() / 60;
		int segundos = simulado.getTempoRestante() % 60;
		labelTempo.setText(String.format("Tempo Restante: %02d:%02d", minutos, segundos));

		if (simulado.isFinalizado()) {
			timeline.stop();
			mostrarResultados();
		}
	}

	private void carregarQuestaoAtual() {
		if (simulado == null)
			return;

		Questao questao = simulado.getQuestaoAtual();
		if (questao == null) {
			System.out.println("Erro: Nenhuma questão disponível.");
			return;
		}

		labelQuestao.setText(questao.getPergunta());
		containerAlternativas.getChildren().clear();
		grupoAlternativas.getToggles().clear();

		for (String alternativa : questao.getResposta().split(";")) {
			RadioButton radio = new RadioButton(alternativa.trim());
			radio.setToggleGroup(grupoAlternativas);
			containerAlternativas.getChildren().add(radio);
		}
	}

	@FXML
	private void proximaQuestao() {
		if (grupoAlternativas.getSelectedToggle() instanceof RadioButton radioSelecionado) {
			simulado.registrarResposta(radioSelecionado.getText().substring(0, 1));
		}

		if (simulado.isFinalizado()) {
			mostrarResultados();
		} else {
			simulado.avancarQuestao();
			carregarQuestaoAtual();
		}
	}

	private void mostrarResultados() {
		System.out.println("Acertos: " + simulado.getAcertos());
		System.out.println("Erros: " + simulado.getErros());
	}
}
