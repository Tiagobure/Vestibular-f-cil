package gui;

import application.Main;
import application.MainAppAware;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Cronograma;
import model.dao.CronogramaDAO;

public class CronogramaViewController implements MainAppAware {

	private Main mainApp;
	private Cronograma cronogramaSelecionado; // Cronograma sendo editado
	private CronogramaDAO cronogramaDAO;

	@FXML
	private TextField campoDiaSemana; // Campo para o dia da semana
	@FXML
	private TextField campoHorario; // Campo para o horário
	@FXML
	private TextField campoMateria; // Campo para a matéria
	@FXML
	private TextField campoAssunto; // Campo para o assunto

	@FXML
	private Button btSalvar;

	private int usuarioId; // ID do usuário logado

	public void setUsuarioId(int usuarioId) {
		this.usuarioId = usuarioId;
	}

	public CronogramaViewController() {
		// Injeção de dependência
		this.cronogramaDAO = new CronogramaDAO();
	}

	// Método para definir o cronograma selecionado
	public void setCronogramaSelecionado(Cronograma cronograma) {
		this.cronogramaSelecionado = cronograma;
		preencherCampos(); // Preenche os campos com os dados do cronograma
	}

	// Preenche os campos da tela com os dados do cronograma
	private void preencherCampos() {
		if (cronogramaSelecionado != null) {
			campoDiaSemana.setText(cronogramaSelecionado.getDiaSemana());
			campoHorario.setText(cronogramaSelecionado.getHorario());
			campoMateria.setText(cronogramaSelecionado.getMateria());
			campoAssunto.setText(cronogramaSelecionado.getAssunto());
		}
	}

	@FXML
	public void salvarBloco() {
		// Obtém os valores dos campos
		String diaSemana = campoDiaSemana.getText();
		String horario = campoHorario.getText();
		String materia = campoMateria.getText();
		String assunto = campoAssunto.getText();

		// Valida se todos os campos estão preenchidos
		if (diaSemana.isEmpty() || horario.isEmpty() || materia.isEmpty() || assunto.isEmpty()) {
			Alerts.showAlert("Erro", null, "Preencha todos os campos", AlertType.ERROR);
			return;
		}

		// Validação de formato de horário (opcional, exemplo simples)
		if (!horario.matches("\\d{2}:\\d{2}")) { // Verifica se o horário está no formato HH:mm
			Alerts.showAlert("Erro", null, "Formato de horário inválido. Use o formato HH:mm.", AlertType.ERROR);
			return;
		}

		if (cronogramaSelecionado == null) {
			// Cria um novo objeto Cronograma
			Cronograma cronograma = new Cronograma(diaSemana, horario, materia, assunto);
			cronograma.setUsuarioId(usuarioId); // Define o ID do usuário

			try {
				// Insere o cronograma no banco de dados
				cronogramaDAO.inserir(cronograma);

				// Mensagem de sucesso
				Alerts.showAlert("Sucesso", null, "Bloco salvo com sucesso!", AlertType.INFORMATION);

				// Limpa os campos após salvar
				limparCampos();

				// Fecha a janela
				Stage stage = (Stage) btSalvar.getScene().getWindow();
				stage.close();
			} catch (Exception e) {
				// Caso ocorra um erro ao salvar no banco de dados
				Alerts.showAlert("Erro ao salvar", "Erro ao tentar salvar o cronograma no banco de dados.",
						e.getMessage(), AlertType.ERROR);
			}
		} else {
			// Atualiza os dados do cronograma selecionado
			cronogramaSelecionado.setDiaSemana(diaSemana);
			cronogramaSelecionado.setHorario(horario);
			cronogramaSelecionado.setMateria(materia);
			cronogramaSelecionado.setAssunto(assunto);

			try {
				// Atualiza o cronograma no banco de dados
				cronogramaDAO.editarCronograma(cronogramaSelecionado);

				// Mensagem de sucesso
				Alerts.showAlert("Sucesso", null, "Bloco atualizado com sucesso!", AlertType.INFORMATION);

				// Fecha a janela
				Stage stage = (Stage) btSalvar.getScene().getWindow();
				stage.close();
			} catch (Exception e) {
				// Caso ocorra um erro ao atualizar no banco de dados
				Alerts.showAlert("Erro ao atualizar", "Erro ao tentar atualizar o cronograma no banco de dados.",
						e.getMessage(), AlertType.ERROR);
			}
		}
	}

	// Método para limpar os campos
	private void limparCampos() {
		campoDiaSemana.clear();
		campoHorario.clear();
		campoMateria.clear();
		campoAssunto.clear();
	}

	// Método para definir a referência do Main
	@Override
	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}
}