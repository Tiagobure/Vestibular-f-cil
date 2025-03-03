package gui;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.PalavraChave;
import model.Resumo;
import model.dao.PalavraChaveDAO;
import model.dao.ResumoDAO;

public class BuscaViewController {

	@FXML
	private TextField campoBusca;
	@FXML
	private ComboBox<String> comboTipoBusca;
	@FXML
	private ListView<Object> listaResultados; // Usamos Object para suportar Resumo e PalavraChave
	@FXML
	private Label mensagemFeedback;

	private ResumoDAO resumoDAO = new ResumoDAO();
	private PalavraChaveDAO palavraChaveDAO = new PalavraChaveDAO();

	@FXML
	public void initialize() {
		// Configura as opções do ComboBox
		comboTipoBusca.getItems().addAll("Resumos", "Palavras-Chave");
		comboTipoBusca.getSelectionModel().selectFirst(); // Seleciona a primeira opção por padrão

		// Configura como os itens serão exibidos na lista
		listaResultados.setCellFactory(param -> new ListCell<Object>() {
			@Override
			protected void updateItem(Object item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else {
					if (item instanceof Resumo) {
						Resumo resumo = (Resumo) item;
						setText("Título: " + resumo.getTitulo() + "\n" + "Matéria: " + resumo.getMateria() + "\n"
								+ "Assunto: " + resumo.getAssunto() + "\n" + "Resumo: "
								+ resumo.getTexto().substring(0, Math.min(100, resumo.getTexto().length())) + "...");
					} else if (item instanceof PalavraChave) {
						PalavraChave palavraChave = (PalavraChave) item;
						setText("Palavra-Chave: " + palavraChave.getPalavra() + "\n" + "Descrição: "
								+ palavraChave.getDescricao());
					}
				}
			}
		});
	}

	@FXML
	private void buscar() {
		String termo = campoBusca.getText().trim();
		String tipoBusca = comboTipoBusca.getValue(); // Obtém o tipo de busca selecionado
		listaResultados.getItems().clear();

		if (termo.isEmpty()) {
			mensagemFeedback.setText("Digite um termo para buscar!");
			return;
		}

		if (tipoBusca.equals("Resumos")) {
			buscarResumos(termo);
		} else if (tipoBusca.equals("Palavras-Chave")) {
			buscarPalavrasChave(termo);
		}
	}

	private void buscarResumos(String termo) {
		List<Resumo> resultados = resumoDAO.buscarPorTermo(termo); // Método que você já implementou

		if (resultados.isEmpty()) {
			mensagemFeedback.setText("Nenhum resumo encontrado para: " + termo);
		} else {
			listaResultados.getItems().addAll(resultados);
			mensagemFeedback.setText(resultados.size() + " resumo(s) encontrado(s)");
		}
	}

	private void buscarPalavrasChave(String termo) {
		List<PalavraChave> resultados = palavraChaveDAO.buscarPorTermo(termo);

		if (resultados.isEmpty()) {
			mensagemFeedback.setText("Nenhuma palavra-chave encontrada para: " + termo);
		} else {
			listaResultados.getItems().addAll(resultados);
			mensagemFeedback.setText(resultados.size() + " palavra(s)-chave encontrada(s)");
		}
	}

	@FXML
	private void voltar() {
		/* Lógica de navegação */ }

	@FXML
	private void avancar() {
		/* Lógica de navegação */ }
}