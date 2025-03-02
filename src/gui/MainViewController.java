package gui;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainViewController {

	private Main mainApp;
	
	public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
	@FXML
	private LineChart<String, Number> graficoHoras;

	@FXML
	private VBox containerCalendario;

	@FXML
	private ComboBox<String> comboVisualizacao;

	@FXML
	private Label labelMesAtual;

	@FXML
	private VBox diasDoMes;

	private YearMonth mesAtual; // Para controle do calendário

	@FXML
	public void initialize() {
		// Inicializa o mês atual
		mesAtual = YearMonth.now();

		// Configura o ComboBox de visualização
		comboVisualizacao.getItems().addAll("Gráfico de Horas", "Calendário");
		comboVisualizacao.getSelectionModel().selectFirst();

		// Inicializa o gráfico de horas
		carregarDadosGrafico();

		// Configura o listener para mudar a visualização
		comboVisualizacao.setOnAction(event -> mudarVisualizacao());

		// Atualiza o calendário com o mês atual
		atualizarCalendario();
	}

	// Alterna entre gráfico e calendário
	@FXML
	private void mudarVisualizacao() {
		String selecao = comboVisualizacao.getSelectionModel().getSelectedItem();
		System.out.println("Opção selecionada: " + selecao);
		if (selecao.equals("Gráfico de Horas")) {
			graficoHoras.setVisible(true);
			containerCalendario.setVisible(false);
		} else if (selecao.equals("Calendário")) {
			graficoHoras.setVisible(false);
			containerCalendario.setVisible(true);
		}
	}

	// Carrega dados fictícios no gráfico (substitua com dados reais)
	private void carregarDadosGrafico() {
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.setName("Horas Estudadas");
		series.getData().add(new XYChart.Data<>("Seg", 4));
		series.getData().add(new XYChart.Data<>("Ter", 3));
		series.getData().add(new XYChart.Data<>("Qua", 6));
		series.getData().add(new XYChart.Data<>("Qui", 2));
		series.getData().add(new XYChart.Data<>("Sex", 5));
		graficoHoras.getData().add(series);
	}

	// Atualiza o calendário com os dias do mês
	private void atualizarCalendario() {
		diasDoMes.getChildren().clear(); // Limpa os dias anteriores
		labelMesAtual.setText(mesAtual.format(DateTimeFormatter.ofPattern("MMMM yyyy")));

		LocalDate primeiroDia = mesAtual.atDay(1);
		int diasNoMes = mesAtual.lengthOfMonth();
		int diaSemana = primeiroDia.getDayOfWeek().getValue() % 7; // Ajuste para Domingo=0

		HBox semanaAtual = new HBox(5);
		semanaAtual.setAlignment(Pos.CENTER);

		// Preenche os dias vazios no início do mês
		for (int i = 0; i < diaSemana; i++) {
			semanaAtual.getChildren().add(new Label(""));
		}

		// Preenche os dias do mês
		for (int dia = 1; dia <= diasNoMes; dia++) {
			Button botaoDia = new Button(String.valueOf(dia));
			botaoDia.setOnAction(this::selecionarDia);
			semanaAtual.getChildren().add(botaoDia);

			// Inicia uma nova semana após o sábado
			if ((diaSemana + dia) % 7 == 0) {
				diasDoMes.getChildren().add(semanaAtual);
				semanaAtual = new HBox(5);
				semanaAtual.setAlignment(Pos.CENTER);
			}
		}

		// Adiciona a última semana (se necessário)
		if (!semanaAtual.getChildren().isEmpty()) {
			diasDoMes.getChildren().add(semanaAtual);
		}
	}

	// Navegação do calendário
	@FXML
	private void mesAnterior() {
		mesAtual = mesAtual.minusMonths(1);
		atualizarCalendario();
	}

	@FXML
	private void proximoMes() {
		mesAtual = mesAtual.plusMonths(1);
		atualizarCalendario();
	}

	// Métodos de navegação para outras telas
	@FXML
	private void abrirSimulados() {
		mainApp.carregarTela("/gui/SelecaoSimuladoView.fxml", "Simulados");
	}

	@FXML
	private void abrirResumos() {
		mainApp.carregarTela("/gui/ResumosView.fxml", "Resumos");
	}

	@FXML
	private void abrirPalavrasChave() {
		mainApp.carregarTela("/gui/PalavrasChaveView.fxml", "Palavras-Chave");
	}

	@FXML
	private void abrirCronograma() {
		mainApp.carregarTela("/gui/CronogramaView.fxml", "Cronograma");
	}

	// Método genérico para carregar telas

	@FXML
	private void selecionarDia(ActionEvent event) {
		// Obtém o botão que foi clicado
		Button botaoClicado = (Button) event.getSource();
		// Obtém o texto do botão (número do dia)
		String diaSelecionado = botaoClicado.getText();

		// Exibe o dia selecionado no console (ou faz outra ação)
		System.out.println("Dia selecionado: " + diaSelecionado);
	}
}