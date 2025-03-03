package gui;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import model.Cronograma;
import model.dao.CronogramaDAO;

public class CronogramaViewController {

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
	
}
