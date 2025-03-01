package model;

public class Cronograma {
	private int id;
	private String diaSemana; // Ex.: "Segunda-feira"
	private String horario; // Ex.: "14:00 - 16:00"
	private String materia; // Ex.: "Matemática"
	private String assunto; // Ex.: "Geometria Analítica"
	private boolean concluido; // Indica se o bloco foi concluído

	// Construtor, getters e setters
	public Cronograma(String diaSemana, String horario, String materia, String assunto) {
		this.diaSemana = diaSemana;
		this.horario = horario;
		this.materia = materia;
		this.assunto = assunto;
		this.concluido = false; // Por padrão, o bloco não está concluído
	}

	// Getters e setters...
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(String diaSemana) {
		this.diaSemana = diaSemana;
	}

	public String getHorario() {
		return horario;
	}

	public void setHorario(String horario) {
		this.horario = horario;
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

	public boolean isConcluido() {
		return concluido;
	}

	public void setConcluido(boolean concluido) {
		this.concluido = concluido;
	}
}