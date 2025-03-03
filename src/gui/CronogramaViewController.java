package gui;

import java.util.List;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Cronograma;
import model.dao.CronogramaDAO;

public class CronogramaViewController {

	private Main mainApp;

	@FXML
	private TextField campoDiaSemana;
	@FXML
	private TextField campoHorario;
	@FXML
	private TextField campoMateria;
	@FXML
	private TextField campoAssunto;
	@FXML
	private CheckBox checkConcluido;

	@FXML
	private ListView<String> listaCronograma;
	
	private CronogramaDAO cronogramaDAO = new CronogramaDAO();

	
	
	@FXML
	public void salvarBloco() {
		String diaSemana = campoDiaSemana.getText();
		String horario = campoHorario.getText();
		String materia = campoMateria.getText();
		String assunto = campoAssunto.getText();
		boolean concluido = checkConcluido.isSelected();

		if (diaSemana.isEmpty() || horario.isEmpty() || materia.isEmpty() || assunto.isEmpty()) {
            System.out.println("Preencha todos os campos!");
            return;
        }
		Cronograma cronograma = new Cronograma(diaSemana, horario, materia, assunto);
		cronograma.setConcluido(concluido);
		cronogramaDAO.inserir(cronograma);

		// Limpar campos após salvar
        System.out.println("Bloco salvo com sucesso!");
		limparCampos();
	}
	private void limparCampos() {
        campoDiaSemana.clear();
        campoHorario.clear();
        campoMateria.clear();
        campoAssunto.clear();
        checkConcluido.setSelected(false);
    }
	@FXML
    private void abrirCadastroCronograma() {
        // Abre a tela de cadastro de cronograma
		mainApp.carregarTela("/gui/CronogramaView.fxml", "Cadastro");
		}
	
	
	@FXML
    public void initialize() {
        // Carrega o cronograma ao abrir a tela
        atualizarCronograma();
    }
	 @FXML
	    private void atualizarCronograma() {
	        // Limpa a lista atual
	        listaCronograma.getItems().clear();

	        // Recupera os blocos de estudo do banco de dados
	        List<Cronograma> blocos = cronogramaDAO.listarTodos();

	        // Adiciona cada bloco à ListView
	        for (Cronograma bloco : blocos) {
	            String status = bloco.isConcluido() ? "[Concluído] " : "[Pendente] ";
	            String texto = status + bloco.getDiaSemana() + " - " + bloco.getHorario() + ": " + bloco.getMateria() + " (" + bloco.getAssunto() + ")";
	            listaCronograma.getItems().add(texto);
	        }
	    }
	
}
