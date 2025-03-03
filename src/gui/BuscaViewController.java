package gui;

import java.util.List;
import java.util.Stack;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
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
					setGraphic(null);
				} else {
					Label label = new Label();
					Button btnVerMais = new Button("Ver Mais");

					btnVerMais.setStyle("-fx-background-color: #ff5252; -fx-text-fill: white; -fx-font-size: 12px;");
					if (item instanceof Resumo) {
						Resumo resumo = (Resumo) item;
						String resumoCurto = resumo.getTexto().substring(0, Math.min(100, resumo.getTexto().length()))
								+ "...";
						label.setText("Título: " + resumo.getTitulo() + "\n" + "Matéria: " + resumo.getMateria() + "\n"
								+ "Assunto: " + resumo.getAssunto() + "\n" + "Resumo: " + resumoCurto);

						btnVerMais.setOnAction(event -> mostrarPopupResumo(resumo));

					} else if (item instanceof PalavraChave) {

						PalavraChave palavraChave = (PalavraChave) item;
						String descricaoCurta = palavraChave.getDescricao().substring(0,
								Math.min(100, palavraChave.getDescricao().length())) + "...";
						label.setText("Palavra-Chave: " + palavraChave.getPalavra() + "\n" + "Matéria: "
								+ palavraChave.getMateria() + "\n" + "Assunto: " + palavraChave.getAssunto() + "\n"
								+ "Descrição: " + descricaoCurta);
						btnVerMais.setOnAction(event -> mostrarPopupPalavraChave(palavraChave));
					}

					// Criando o botão com um ícone de lixeira
					Button btnDeletar = new Button("🗑");
					btnDeletar.setStyle(
							"-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-size: 14px; -fx-min-width: 30px; -fx-min-height: 30px; -fx-background-radius: 15px;");

					// Efeito de sombra para destacar o botão
					DropShadow shadow = new DropShadow();
					shadow.setRadius(5.0);
					shadow.setOffsetX(2.0);
					shadow.setOffsetY(2.0);
					shadow.setColor(Color.rgb(0, 0, 0, 0.3));
					btnDeletar.setEffect(shadow);

					// Efeito de hover (muda de cor ao passar o mouse)
					btnDeletar.setOnMouseEntered(e -> btnDeletar.setStyle(
							"-fx-background-color: #c62828; -fx-text-fill: white; -fx-font-size: 14px; -fx-min-width: 30px; -fx-min-height: 30px; -fx-background-radius: 15px;"));
					btnDeletar.setOnMouseExited(e -> btnDeletar.setStyle(
							"-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-size: 14px; -fx-min-width: 30px; -fx-min-height: 30px; -fx-background-radius: 15px;"));

					btnDeletar.setOnAction(event -> deletarItem(item));

					// Layout horizontal (Texto + Botão)
					if ((item instanceof Resumo && ((Resumo) item).getTexto().length() > 100)
							|| (item instanceof PalavraChave && ((PalavraChave) item).getDescricao().length() > 10)) {
						HBox hbox = new HBox(10, label, btnVerMais, btnDeletar);
						setGraphic(hbox);
					} else {
						HBox hbox = new HBox(10, label, btnDeletar);
						setGraphic(hbox);
					}
				}
			}
		});
	}

	// Método para deletar um item da lista e do banco de dados
	private void deletarItem(Object item) {
		if (item instanceof Resumo) {
			resumoDAO.deletar((Resumo) item);
		} else if (item instanceof PalavraChave) {
			palavraChaveDAO.deletar((PalavraChave) item);
		}
		listaResultados.getItems().remove(item);
		mensagemFeedback.setText("Item deletado com sucesso!");
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

	private void mostrarPopupResumo(Resumo resumo) {
		Stage popupStage = new Stage();
		popupStage.setTitle("Resumo Completo");

		VBox vbox = new VBox(10);
		vbox.setStyle("-fx-padding: 20; -fx-background-color: #ffebee;");

		Label lblTitulo = new Label("Título: " + resumo.getTitulo());
		lblTitulo.setStyle("-fx-font-weight: bold; -fx-text-fill: #d32f2f; -fx-font-size: 16px;");

		Label lblMateria = new Label("Matéria: " + resumo.getMateria());
		Label lblAssunto = new Label("Assunto: " + resumo.getAssunto());

		Label lblTexto = new Label(resumo.getTexto());
		lblTexto.setWrapText(true);

		Button btnFechar = new Button("Fechar");
		btnFechar.setStyle("-fx-background-color: #ff5252; -fx-text-fill: white;");
		btnFechar.setOnAction(event -> popupStage.close());

		vbox.getChildren().addAll(lblTitulo, lblMateria, lblAssunto, lblTexto, btnFechar);

		Scene scene = new Scene(vbox, 400, 300);
		popupStage.setScene(scene);
		popupStage.show();
	}

	private void mostrarPopupPalavraChave(PalavraChave palavraChave) {
		Stage popupStage = new Stage();
		popupStage.setTitle("Detalhes da Palavra-Chave");

		VBox vbox = new VBox(10);
		vbox.setStyle("-fx-padding: 20; -fx-background-color: #ffebee;");

		Label lblPalavra = new Label("Palavra-Chave: " + palavraChave.getPalavra());
		lblPalavra.setStyle("-fx-font-weight: bold; -fx-text-fill: #d32f2f; -fx-font-size: 16px;");

		Label lblMateria = new Label("Matéria: " + palavraChave.getMateria());
		lblMateria.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");

		Label lblAssunto = new Label("Assunto: " + palavraChave.getAssunto());
		lblAssunto.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");

		Label lblDescricao = new Label(palavraChave.getDescricao());
		lblDescricao.setWrapText(true);

		Button btnFechar = new Button("Fechar");
		btnFechar.setStyle("-fx-background-color: #ff5252; -fx-text-fill: white;");
		btnFechar.setOnAction(event -> popupStage.close());

		vbox.getChildren().addAll(lblPalavra, lblMateria, lblAssunto, lblDescricao, btnFechar);

		Scene scene = new Scene(vbox, 400, 250);
		popupStage.setScene(scene);
		popupStage.show();
	}

}
