package model;

import java.util.List;

public class Simulado {
	private String vestibularSelecionado; // Ex: "FUVEST", "ENEM"
	private List<Questao> questoes;
	private int questaoAtualIndex;
	private int tempoRestante; // Em segundos
	private int acertos;
	private int erros;

	// Construtor
	public Simulado(String vestibularSelecionado, List<Questao> questoes, int tempoTotal) {
		this.vestibularSelecionado = vestibularSelecionado;
		this.questoes = questoes;
		this.tempoRestante = tempoTotal * 60; // Converte minutos para segundos
		this.questaoAtualIndex = 0;
		this.acertos = 0;
		this.erros = 0;
	}

	// Getters e Setters
	public String getVestibularSelecionado() {
		return vestibularSelecionado;
	}

	public Questao getQuestaoAtual() {
		return questoes.get(questaoAtualIndex);
	}

	public int getTempoRestante() {
		return tempoRestante;
	}

	public int getAcertos() {
		return acertos;
	}

	public int getErros() {
		return erros;
	}

	// Métodos
	public void avancarQuestao() {
		if (questaoAtualIndex < questoes.size() - 1) {
			questaoAtualIndex++;
		}
	}

	public void voltarQuestao() {
		if (questaoAtualIndex > 0) {
			questaoAtualIndex--;
		}
	}

	public void registrarResposta(String respostaUsuario) {
		Questao questao = getQuestaoAtual();
		if (questao.getResposta().equalsIgnoreCase(respostaUsuario)) {
			acertos++;
		} else {
			erros++;
		}
	}

	public void atualizarTempo() {
		if (tempoRestante > 0) {
			tempoRestante--;
		}
	}

	public boolean isFinalizado() {
		return tempoRestante <= 0 || questaoAtualIndex >= questoes.size();
	}
}
