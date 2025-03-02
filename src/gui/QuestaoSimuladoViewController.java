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
import model.dao.SimuladoDAO;

public class QuestaoSimuladoViewController {

	@FXML
	private Label labelTempo;
	@FXML
	private Label labelQuestao;
	@FXML
	private VBox containerAlternativas;
	@FXML
	private Label labelMensagem; // Adicione uma Label para feedback ao usuário

	private Simulado simulado;
	private Timeline timeline;
	private ToggleGroup grupoAlternativas = new ToggleGroup();

	SimuladoDAO simuladoDAO = new SimuladoDAO();
	
	public void setSimulado(Simulado simulado) {
		this.simulado = simulado;
		if (simulado != null) {
			carregarQuestaoAtual();
			iniciarCronometro();
		} else {
			exibirMensagem("Erro: Simulado não foi inicializado.");
		}
	}

	@FXML
	public void initialize() {
		containerAlternativas.getChildren().clear();
		labelMensagem.setText(""); // Inicializa a mensagem como vazia
	}

	private void iniciarCronometro() {
		timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> Platform.runLater(this::atualizarTempo)));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}

	private void atualizarTempo() {
		if (simulado == null) {
			return;
		}

		simulado.atualizarTempo();
		int minutos = simulado.getTempoRestante() / 60;
		int segundos = simulado.getTempoRestante() % 60;
		labelTempo.setText(String.format("Tempo Restante: %02d:%02d", minutos, segundos));

		if (simulado.isFinalizado()) {
			pararCronometro();
			mostrarResultados();
		}
	}

	private void pararCronometro() {
		if (timeline != null) {
			timeline.stop();
		}
	}

	private void carregarQuestaoAtual() {
		if (simulado == null) {
			exibirMensagem("Erro: Simulado não foi inicializado.");
			return;
		}

		Questao questao = simulado.getQuestaoAtual();
		if (questao == null) {
			exibirMensagem("Erro: Nenhuma questão disponível.");
			return;
		}

		labelQuestao.setText(questao.getPergunta());
		containerAlternativas.getChildren().clear();
		grupoAlternativas.getToggles().clear();

		for (String alternativa : questao.getResposta().split(";")) {
			RadioButton radio = new RadioButton(alternativa.trim());
			radio.setToggleGroup(grupoAlternativas);
			radio.setStyle("-fx-text-fill: white; -fx-font-size: 14px;"); // Estilo para alternativas
			containerAlternativas.getChildren().add(radio);
		}
	}

	@FXML
	private void proximaQuestao() {
		if (grupoAlternativas.getSelectedToggle() == null) {
			exibirMensagem("Selecione uma alternativa antes de prosseguir.");
			return;
		}

		if (grupoAlternativas.getSelectedToggle() instanceof RadioButton radioSelecionado) {
			simulado.registrarResposta(radioSelecionado.getText().substring(0, 1));
		}

		if (simulado.isFinalizado()) {
			pararCronometro();
			mostrarResultados();
		} else {
			simulado.avancarQuestao();
			carregarQuestaoAtual();
			labelMensagem.setText(""); // Limpa a mensagem ao avançar
		}
	}

	private void mostrarResultados() {
		exibirMensagem(String.format("Simulado finalizado! Acertos: %d | Erros: %d", simulado.getAcertos(),
				simulado.getErros()));
		// Aqui você pode adicionar a lógica para exibir os resultados em uma nova tela
	}

	private void exibirMensagem(String mensagem) {
		labelMensagem.setText(mensagem);
	}
	
	//simuladoDAO.salvarResultado(simulado, usuarioId){
	//	
	//}
	
	
}