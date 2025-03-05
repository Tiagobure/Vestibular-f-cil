package gui;

import java.io.IOException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Questao;
import model.Simulado;
import model.dao.SimuladoDAO;

public class QuestaoSimuladoViewController {

	// Outros campos...
	private int usuarioId; // ID do usuário

	// Método para definir o usuarioId
	public void setUsuarioId(int usuarioId) {
		this.usuarioId = usuarioId;
	}

	@FXML
	private Label labelTempo; // Exibe o tempo restante
	@FXML
	private VBox containerAlternativas; // Contém as alternativas (A, B, C, D, E)
	@FXML
	private Label labelMensagem; // Exibe mensagens de feedback ao usuário

	private Simulado simulado; // Objeto Simulado atual
	private Timeline timeline; // Cronômetro do simulado
	private ToggleGroup grupoAlternativas = new ToggleGroup(); // Grupo de alternativas

	private SimuladoDAO simuladoDAO; // DAO para salvar resultados

	// Define o simulado atual e inicializa a interface
	public void setSimulado(Simulado simulado) {
		this.simulado = simulado;
		if (simulado != null) {
			carregarQuestaoAtual(); // Carrega a primeira questão
			iniciarCronometro(); // Inicia o cronômetro
		} else {
			exibirMensagem("Erro: Simulado não foi inicializado.");
		}
	}

	// Inicializa a interface
	@FXML
	public void initialize() {
		containerAlternativas.getChildren().clear(); // Limpa as alternativas
		labelMensagem.setText(""); // Inicializa a mensagem como vazia
	}

	// Inicia o cronômetro do simulado
	private void iniciarCronometro() {
		timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> Platform.runLater(this::atualizarTempo)));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}

	// Atualiza o tempo restante
	private void atualizarTempo() {
		if (simulado == null) {
			return;
		}

		simulado.atualizarTempo(); // Atualiza o tempo restante
		int minutos = simulado.getTempoRestante() / 60;
		int segundos = simulado.getTempoRestante() % 60;
		labelTempo.setText(String.format("Tempo Restante: %02d:%02d", minutos, segundos));

		// Verifica se o simulado foi finalizado
		if (simulado.isFinalizado()) {
			pararCronometro();
			mostrarResultados();
		}
	}

	// Para o cronômetro
	private void pararCronometro() {
		if (timeline != null) {
			timeline.stop();
		}
	}

	// Carrega a questão atual
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

		// Limpa as alternativas anteriores
		containerAlternativas.getChildren().clear();
		grupoAlternativas.getToggles().clear();

		// Divide os caminhos das imagens
		String[] imagens = questao.getImagemQuestao().split(";");

		// Exibe cada imagem
		for (String imagem : imagens) {
			try {
				// Remove espaços em branco e carrega a imagem
				Image img = new Image(imagem.trim());
				if (img.isError()) {
					throw new Exception("Erro ao carregar a imagem: " + imagem);
				}

				// Cria um ImageView para exibir a imagem
				ImageView imageView = new ImageView(img);
				imageView.setFitWidth(600); // Ajuste o tamanho conforme necessário
				imageView.setPreserveRatio(true);

				// Adiciona a imagem ao container
				containerAlternativas.getChildren().add(imageView);
			} catch (Exception e) {
				exibirMensagem("Erro ao carregar a imagem: " + imagem);

				// Exibe uma imagem padrão em caso de erro
				ImageView imageView = new ImageView(new Image("caminho/para/imagem_padrao.jpg"));
				imageView.setFitWidth(600);
				imageView.setPreserveRatio(true);
				containerAlternativas.getChildren().add(imageView);
			}
		}

		// Adiciona as alternativas (A, B, C, D, E)
		String[] alternativas = { "A", "B", "C", "D", "E" };
		for (String alternativa : alternativas) {
			RadioButton radio = new RadioButton(alternativa);
			radio.setToggleGroup(grupoAlternativas);
			radio.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
			radio.selectedProperty().addListener((obs, oldValue, newValue) -> {
				if (newValue) {
					radio.setStyle("-fx-text-fill: yellow; -fx-font-size: 14px;"); // Destaque visual
				} else {
					radio.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
				}
			});
			containerAlternativas.getChildren().add(radio);
		}
	}

	// Avança para a próxima questão
	@FXML
	private void proximaQuestao() {
		if (grupoAlternativas.getSelectedToggle() == null) {
			exibirMensagem("Selecione uma alternativa antes de prosseguir.");
			return;
		}

		// Registra a resposta do usuário
		if (grupoAlternativas.getSelectedToggle() instanceof RadioButton radioSelecionado) {
			char respostaUsuario = radioSelecionado.getText().charAt(0); // A, B, C, D ou E
			simulado.registrarResposta(respostaUsuario);
		}

		// Verifica se o simulado foi finalizado
		if (simulado.isFinalizado()) {
			pararCronometro();
			mostrarResultados();
		} else {
			simulado.avancarQuestao(); // Avança para a próxima questão
			carregarQuestaoAtual(); // Carrega a nova questão
			labelMensagem.setText(""); // Limpa a mensagem ao avançar
		}
	}

	// Exibe os resultados finais
	private void mostrarResultados() {
		exibirMensagem(String.format("Simulado finalizado! Acertos: %d | Erros: %d", simulado.getAcertos(),
				simulado.getErros()));

		// Verifica se o simuladoDAO e o usuarioId estão definidos
		if (simuladoDAO == null || usuarioId <= 0) {
			exibirMensagem("Erro: Dados do usuário não foram configurados corretamente.");
			return;
		}

		// Abre a tela de resultados
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ResultadoView.fxml"));
			Parent root = loader.load();

			// Passa os resultados para o controlador da tela de resultados
			ResultadoViewController resultadoController = loader.getController();
			resultadoController.setResultados(simulado, usuarioId, simuladoDAO); // Passa o simulado, usuarioId e
																					// simuladoDAO

			// Configura a cena e exibe a tela
			Scene scene = new Scene(root);
			Stage resultadoStage = new Stage();
			resultadoStage.setScene(scene);
			resultadoStage.setTitle("Resultados do Simulado");
			resultadoStage.show();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erro ao carregar a tela de resultados.");
		}
	}

	@FXML
	private void confirmarResposta() {
		if (grupoAlternativas.getSelectedToggle() == null) {
			exibirMensagem("Selecione uma alternativa antes de confirmar.");
			return;
		}

		// Registra a resposta do usuário
		if (grupoAlternativas.getSelectedToggle() instanceof RadioButton radioSelecionado) {
			char respostaUsuario = radioSelecionado.getText().charAt(0); // A, B, C, D ou E
			simulado.registrarResposta(respostaUsuario);
			exibirMensagem("Resposta confirmada!");
		}
	}

	@FXML
	private void sairSimulado() {
		// Cria um diálogo de confirmação
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirmação de Saída");
		alert.setHeaderText("Tem certeza que deseja sair do simulado?");
		alert.setContentText("Se você sair, o progresso atual será perdido.");

		// Define os botões do diálogo
		ButtonType botaoSim = new ButtonType("Sim");
		ButtonType botaoNao = new ButtonType("Não");
		alert.getButtonTypes().setAll(botaoSim, botaoNao);

		// Exibe o diálogo e aguarda a resposta do usuário
		alert.showAndWait().ifPresent(resposta -> {
			if (resposta == botaoSim) {
				// Se o usuário escolher "Sim", volta para a tela de seleção de simulado
				pararCronometro();
				voltarParaSelecaoSimulado();
			} else {
				// Se o usuário escolher "Não", o simulado continua de onde parou
				exibirMensagem("Simulado retomado.");
			}
		});
	}

	private void voltarParaSelecaoSimulado() {
		try {
			// Carrega a tela de seleção de simulado
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SelecaoSimuladoView.fxml"));
			Parent root = loader.load();

			// Obtém o Stage atual
			Stage stage = (Stage) labelTempo.getScene().getWindow();

			// Configura a cena e exibe a tela
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Seleção de Simulado");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erro ao carregar a tela de seleção de simulado.");
		}
	}

	// Exibe uma mensagem de feedback ao usuário
	private void exibirMensagem(String mensagem) {
		labelMensagem.setText(mensagem);
	}

	// Define o SimuladoDAO (injeção de dependência)
	public void setSimuladoDAO(SimuladoDAO simuladoDAO) {
		this.simuladoDAO = simuladoDAO;
	}
}