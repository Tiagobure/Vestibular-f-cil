package model;

import java.util.List;

public class Simulado {
	private String vestibularSelecionado; // Ex: "FUVEST", "ENEM"
	private List<Questao> questoes; // Lista de questões do simulado
	private int questaoAtualIndex; // Índice da questão atual
	private int tempoRestante; // Tempo restante em segundos
	private int acertos; // Número de acertos
	private int erros; // Número de erros
	private int usuarioId; // ID do usuário realizando o simulado

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
		if (questaoAtualIndex >= 0 && questaoAtualIndex < questoes.size()) {
			return questoes.get(questaoAtualIndex);
		}
		return null; // Retorna null se não houver questões
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

	public int getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(int usuarioId) {
		this.usuarioId = usuarioId;
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

	// Registra a resposta do usuário
	public void registrarResposta(char respostaUsuario) {
		Questao questao = getQuestaoAtual();
		if (questao != null && questao.getRespostaCorreta() == respostaUsuario) {
			acertos++;
		} else {
			erros++;
		}
	}

	// Atualiza o tempo restante
	public void atualizarTempo() {
		if (tempoRestante > 0) {
			tempoRestante--;
		}
	}

	// Verifica se o simulado foi finalizado
	public boolean isFinalizado() {
		return tempoRestante <= 0 || questaoAtualIndex >= questoes.size();
	}

	// Define o número de acertos (útil para testes)
	public void setAcertos(int acertos) {
		this.acertos = acertos;
	}

	// Define o número de erros (útil para testes)
	public void setErros(int erros) {
		this.erros = erros;
	}
}