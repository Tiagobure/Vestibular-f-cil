package model;

public class Cronograma {
	private int id;
	private String diaSemana;
	private String horario;
	private String materia;
	private String assunto;
	private int usuarioId;

	public Cronograma(String diaSemana, String horario, String materia, String assunto, int usuarioId) {
		this.diaSemana = diaSemana;
		this.horario = horario;
		this.materia = materia;
		this.assunto = assunto;
		this.usuarioId = usuarioId;
	}

	
	public Cronograma(String diaSemana, String horario, String materia, String assunto) {
		this(diaSemana, horario, materia, assunto, 0);

	}

	// Getters e Setters
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


	public int getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(int usuarioId) {
		this.usuarioId = usuarioId;
	}
	
	@Override
    public String toString() {
        return String.format("%s - %s: %s (%s)", diaSemana, horario, materia, assunto);
    }
}