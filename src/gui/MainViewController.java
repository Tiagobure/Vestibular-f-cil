package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainViewController implements Initializable{
	@FXML
	private LineChart<String, Number> graficoHoras;
	@FXML
	private VBox containerCalendario;
	@FXML
	private ComboBox<String> comboVisualizacao;
	@FXML
	private GridPane gridDias;
	@FXML
	private Label labelMesAtual;

	private YearMonth mesAtual; // Para controle do calendário

	@FXML
	public void initialize() {
		// Configura o ComboBox de visualização
		comboVisualizacao.getItems().addAll("Gráfico de Horas", "Calendário");
		comboVisualizacao.getSelectionModel().selectFirst();

		// Inicializa o gráfico de horas
		carregarDadosGrafico();

		// Inicializa o calendário com o mês atual
		mesAtual = YearMonth.now();
		atualizarCalendario();
	}

	// Alterna entre gráfico e calendário
	@FXML
	private void mudarVisualizacao() {
		String selecao = comboVisualizacao.getSelectionModel().getSelectedItem();
		graficoHoras.setVisible(selecao.equals("Gráfico de Horas"));
		containerCalendario.setVisible(selecao.equals("Calendário"));
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
		gridDias.getChildren().clear();
		labelMesAtual.setText(mesAtual.format(DateTimeFormatter.ofPattern("MMMM yyyy")));

		LocalDate primeiroDia = mesAtual.atDay(1);
		int diasNoMes = mesAtual.lengthOfMonth();
		int diaSemana = primeiroDia.getDayOfWeek().getValue() % 7; // Ajuste para Domingo=0

		// Preenche os dias
		for (int dia = 1, linha = 1; dia <= diasNoMes; dia++) {
			VBox dayBox = criarDayBox(dia);
			gridDias.add(dayBox, (diaSemana + dia - 1) % 7, linha);
			if ((diaSemana + dia) % 7 == 0)
				linha++;
		}
	}

	// Cria uma caixa para cada dia no calendário
	private VBox criarDayBox(int dia) {
		VBox dayBox = new VBox(5);
		dayBox.setStyle("-fx-border-color: #ccc; -fx-padding: 5;");

		Label lblDia = new Label(String.valueOf(dia));
		lblDia.setStyle("-fx-font-weight: bold;");

		// Adicione aqui eventos ou notas para o dia (exemplo fictício)
		if (dia == LocalDate.now().getDayOfMonth() && mesAtual.equals(YearMonth.now())) {
			dayBox.setStyle("-fx-background-color: #e0f7fa;");
		}

		dayBox.getChildren().add(lblDia);
		return dayBox;
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
		carregarTela("/view/SelecaoSimuladoView.fxml", "Simulados");
	}

	@FXML
	private void abrirResumos() {
		carregarTela("/view/ResumosView.fxml", "Resumos");
	}

	@FXML
	private void abrirPalavrasChave() {
		carregarTela("/view/PalavrasChaveView.fxml", "Palavras-Chave");
	}

	@FXML
	private void abrirCronograma() {
		carregarTela("/view/CronogramaView.fxml", "Cronograma");
	}

	// Método genérico para carregar telas
	private void carregarTela(String fxml, String titulo) {
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

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}
}
