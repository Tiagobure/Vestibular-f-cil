package model;

public class Resumo {
	private int id;
	private String titulo;
	private String texto;
	private String materia;
	private String assunto;
	private String anexo; // Caminho do arquivo ou link

	// Construtor, getters e setters
	public Resumo(String titulo, String texto, String materia, String assunto) {
		this.titulo = titulo;
		this.texto = texto;
		this.materia = materia;
		this.assunto = assunto;
	}

	// Getters e setters...
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
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

	public String getAnexo() {
		return anexo;
	}

	public void setAnexo(String anexo) {
		this.anexo = anexo;
	}
}