package model;

public class Questao {

	private int id;
	private String pergunta;
	private String resposta; // Resposta correta
	private String materia;
	private String assunto;
	private String exame; // Ex: "FUVEST", "ENEM"

	// Construtor
	public Questao(String pergunta, String resposta, String materia, String assunto, String exame) {
		this.pergunta = pergunta;
		this.resposta = resposta;
		this.materia = materia;
		this.assunto = assunto;
		this.exame = exame;
	}

	// Getters e Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPergunta() {
		return pergunta;
	}

	public void setPergunta(String pergunta) {
		this.pergunta = pergunta;
	}

	public String getResposta() {
		return resposta;
	}

	public void setResposta(String resposta) {
		this.resposta = resposta;
	}

	public String getMateria() {
		return materia;
	}

	public void setMateria(String materia) {
		this.materia = materia;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public String getExame() {
		return exame;
	}

	public void setExame(String exame) {
		this.exame = exame;
	}
}
