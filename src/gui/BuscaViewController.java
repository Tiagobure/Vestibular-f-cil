package gui;

import java.util.List;
import java.util.Stack;

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
	private ListView<Object> listaResultados; // Suporta Resumo e PalavraChave
	@FXML
	private Label mensagemFeedback;

	private ResumoDAO resumoDAO = new ResumoDAO();
	private PalavraChaveDAO palavraChaveDAO = new PalavraChaveDAO();

	private int usuarioId; // ID do usuário logado

	private Stack<String> historicoPesquisas = new Stack<>(); // Pilha para armazenar buscas passadas
	private Stack<String> pesquisasFuturas = new Stack<>(); // Pilha para armazenar pesquisas que podem ser avançadas

	public void setUsuarioId(int usuarioId) {
		this.usuarioId = usuarioId;
	}

	@FXML
	public void initialize() {
		comboTipoBusca.getItems().addAll("Resumos", "Palavras-Chave");
		comboTipoBusca.getSelectionModel().selectFirst();

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
		String tipoBusca = comboTipoBusca.getValue();
		listaResultados.getItems().clear();

		if (termo.isEmpty()) {
			mensagemFeedback.setText("Digite um termo para buscar!");
			return;
		}

		// Adiciona a pesquisa atual ao histórico e limpa a pilha de pesquisas futuras
		if (!historicoPesquisas.isEmpty()) {
			String ultimaBusca = historicoPesquisas.peek();
			if (!ultimaBusca.equals(termo)) { // Evita salvar buscas duplicadas seguidas
				historicoPesquisas.push(termo);
				pesquisasFuturas.clear(); // Se fizer uma nova busca, limpa os "avanços"
			}
		} else {
			historicoPesquisas.push(termo);
			pesquisasFuturas.clear();
		}

		if (tipoBusca.equals("Resumos")) {
			buscarResumos(termo);
		} else if (tipoBusca.equals("Palavras-Chave")) {
			buscarPalavrasChave(termo);
		}
	}

	private void buscarResumos(String termo) {
		List<Resumo> resultados = resumoDAO.buscarPorTermo(termo, usuarioId);

		if (resultados.isEmpty()) {
			mensagemFeedback.setText("Nenhum resumo encontrado para: " + termo);
		} else {
			listaResultados.getItems().addAll(resultados);
			mensagemFeedback.setText(resultados.size() + " resumo(s) encontrado(s)");
		}
	}

	private void buscarPalavrasChave(String termo) {
		List<PalavraChave> resultados = palavraChaveDAO.buscarPorTermo(termo, usuarioId);

		if (resultados.isEmpty()) {
			mensagemFeedback.setText("Nenhuma palavra-chave encontrada para: " + termo);
		} else {
			listaResultados.getItems().addAll(resultados);
			mensagemFeedback.setText(resultados.size() + " palavra(s)-chave encontrada(s)");
		}
	}

	@FXML
	private void voltar() {
		if (historicoPesquisas.size() > 1) {
			pesquisasFuturas.push(historicoPesquisas.pop()); // Salva a busca atual na pilha de futuros
			String ultimaBusca = historicoPesquisas.peek(); // Obtém a busca anterior
			campoBusca.setText(ultimaBusca); // Atualiza o campo de busca
			buscar(); // Refaz a busca
		} else {
			mensagemFeedback.setText("Não há pesquisas anteriores!");
		}
	}

	@FXML
	private void avancar() {
		if (!pesquisasFuturas.isEmpty()) {
			String proximaBusca = pesquisasFuturas.pop(); // Recupera a busca futura
			historicoPesquisas.push(proximaBusca); // Adiciona de volta ao histórico
			campoBusca.setText(proximaBusca); // Atualiza o campo de busca
			buscar(); // Refaz a busca
		} else {
			mensagemFeedback.setText("Não há pesquisas futuras para avançar!");
		}
	}
}
