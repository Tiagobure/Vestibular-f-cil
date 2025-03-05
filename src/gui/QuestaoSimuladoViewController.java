package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import application.Main;
import application.MainAppAware;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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

public class QuestaoSimuladoViewController implements MainAppAware {

	// Campos da interface
	@FXML
	private Button btConfirma;

	@FXML
	private Button btProxima;

	@FXML
	private Button btSair;

	@FXML
	private Label labelTempo; // Exibe o tempo restante

	@FXML
	private VBox containerAlternativas; // Contém as alternativas (A, B, C, D, E)

	@FXML
	private Label labelMensagem; // Exibe mensagens de feedback ao usuário

	@FXML
	private ImageView imagemQuestao;

	// Dependências
	private Main mainApp; // Referência para a aplicação principal
	private List<Questao> questoes;
	private Simulado simulado; // Objeto Simulado atual
	private Timeline timeline; // Cronômetro do simulado
	private ToggleGroup grupoAlternativas = new ToggleGroup(); // Grupo de alternativas
	private SimuladoDAO simuladoDAO; // DAO para salvar resultados
	private int usuarioId; // ID do usuário

	// Método para definir o MainApp (injeção de dependência)
	@Override
	public void setMainApp(Main mainApp) {
		if (mainApp == null) {
			throw new IllegalArgumentException("MainApp não pode ser nulo.");
		}
		this.mainApp = mainApp;
	}

	// Método para definir o usuarioId
	public void setUsuarioId(int usuarioId) {
		if (usuarioId <= 0) {
			throw new IllegalArgumentException("ID do usuário inválido.");
		}
		this.usuarioId = usuarioId;
	}

	// Método para definir o SimuladoDAO (injeção de dependência)
	public void setSimuladoDAO(SimuladoDAO simuladoDAO) {
		if (simuladoDAO == null) {
			throw new IllegalArgumentException("SimuladoDAO não pode ser nulo.");
		}
		this.simuladoDAO = simuladoDAO;
	}

	// Define o simulado atual e inicializa a interface
	public void setSimulado(Simulado simulado, List<Questao> questoes) {
		if (simulado == null) {
			throw new IllegalArgumentException("Simulado não pode ser nulo.");
		}
		this.simulado = simulado;
		this.questoes = questoes;
		iniciarCronometro(); // Inicia o cronômetro
		carregarQuestaoAtual(); // Carrega a primeira questão
	}

	// Inicializa a interface
	@FXML
	public void initialize() {
		if (simulado == null) {
			this.simulado = new Simulado(); // Criando um simulado vazio por padrão
			this.questoes = new ArrayList<>(); // Evita NullPointerException
		}
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

	// Constante para o caminho da imagem padrão
	private static final String IMAGEM_PADRAO_PATH = "/gui/padrao.png";

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
				// Remove espaços em branco e verifica se o caminho não está vazio
				String caminhoImagem = imagem.trim();
				if (caminhoImagem.isEmpty()) {
					throw new Exception("Caminho da imagem está vazio.");
				}

				// Carrega a imagem
				Image img = carregarImagem(caminhoImagem);

				// Exibe a imagem no container
				ImageView imageView = criarImageView(img, 600, true);
				containerAlternativas.getChildren().add(imageView);
			} catch (Exception e) {
				exibirMensagem("Erro ao carregar a imagem: " + imagem);
				exibirImagemPadrao();
			}
		}

		// Adiciona as alternativas (A, B, C, D, E)
		adicionarAlternativas();
	}

	// Método para carregar uma imagem (URL ou recurso do classpath)
	private Image carregarImagem(String caminhoImagem) throws Exception {
		if (caminhoImagem.startsWith("http://") || caminhoImagem.startsWith("https://")) {
			// Carrega a imagem de uma URL
			return new Image(caminhoImagem);
		} else {
			// Carrega a imagem do classpath (recurso interno)
			return new Image(getClass().getResourceAsStream(caminhoImagem));
		}
	}

	// Método para criar um ImageView com configurações padrão
	private ImageView criarImageView(Image imagem, double largura, boolean preservarProporcao) {
		ImageView imageView = new ImageView(imagem);
		imageView.setFitWidth(largura);
		imageView.setPreserveRatio(preservarProporcao);
		return imageView;
	}

	// Método para exibir a imagem padrão em caso de erro
	private void exibirImagemPadrao() {
		try {
			Image imagemPadrao = carregarImagem(IMAGEM_PADRAO_PATH);
			ImageView imageView = criarImageView(imagemPadrao, 600, true);
			containerAlternativas.getChildren().add(imageView);
		} catch (Exception e) {
			exibirMensagem("Erro ao carregar a imagem padrão.");
		}
	}

	// Método para adicionar as alternativas (A, B, C, D, E)
	private void adicionarAlternativas() {
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
			resultadoController.setSimulado(simulado); // Passa o simulado
			resultadoController.setUsuarioId(usuarioId); // Passa o ID do usuário
			resultadoController.setSimuladoDAO(simuladoDAO); // Passa o DAO

			// Configura a cena e exibe a tela
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.setTitle("Resultados do Simulado");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			exibirMensagem("Erro ao carregar a tela de resultados.");
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
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/SelecaoSimuladoView.fxml"));
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
			exibirMensagem("Erro ao carregar a tela de seleção de simulado.");
		}
	}

	// Exibe uma mensagem de feedback ao usuário
	private void exibirMensagem(String mensagem) {
		labelMensagem.setText(mensagem);
		labelMensagem.setStyle("-fx-text-fill: red; -fx-font-size: 14px;"); // Estilo para mensagens de erro
	}
}