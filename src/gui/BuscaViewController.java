package gui;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.Resumo;
import model.dao.ResumoDAO;

public class BuscaViewController {
	@FXML private TextField campoBusca;
    @FXML private ListView<Resumo> listaResumos;
    @FXML private Label mensagemFeedback;

    private ResumoDAO resumoDAO = new ResumoDAO();

    @FXML
    public void initialize() {
        // Configura como os resumos serão exibidos na lista
        listaResumos.setCellFactory(param -> new ListCell<Resumo>() {
            @Override
            protected void updateItem(Resumo resumo, boolean empty) {
                super.updateItem(resumo, empty);
                if (empty || resumo == null) {
                    setText(null);
                } else {
                    setText(
                        "Título: " + resumo.getTitulo() + "\n" +
                        "Matéria: " + resumo.getMateria() + "\n" +
                        "Assunto: " + resumo.getAssunto() + "\n" +
                        "Resumo: " + resumo.getTexto().substring(0, Math.min(100, resumo.getTexto().length())) + "..." 
                    );
                }
            }
        });
    }

    @FXML
    private void buscarResumos() {
        String termo = campoBusca.getText().trim();
        listaResumos.getItems().clear();

        if (termo.isEmpty()) {
            mensagemFeedback.setText("Digite um termo para buscar!");
            return;
        }

        List<Resumo> resultados = resumoDAO.buscarPorTermo(termo); // Método que você precisa implementar no DAO

        if (resultados.isEmpty()) {
            mensagemFeedback.setText("Nenhum resumo encontrado para: " + termo);
        } else {
            listaResumos.getItems().addAll(resultados);
            mensagemFeedback.setText(resultados.size() + " resumo(s) encontrado(s)");
        }
    }

    // Métodos de navegação (voltar/avançar podem ser removidos se não forem usados)
    @FXML
    private void voltar() { /* Lógica de navegação */ }

    @FXML
    private void avancar() { /* Lógica de navegação */ }
}
