package model;

public class PalavraChave {
	private int id;
	private String palavra;
	private String descricao;
	private String materia;
	private String assunto;
	private int usuarioId;

	// Construtor, getters e setters
	public PalavraChave(String palavra, String descricao, String materia, String assunto, int usuarioId) {
		this.palavra = palavra;
		this.descricao = descricao;
		this.materia = materia;
		this.assunto = assunto;
		this.usuarioId = usuarioId;
	}

	public PalavraChave(String palavra, String descricao, String materia, String assunto) {
		this.palavra = palavra;
		this.descricao = descricao;
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

	public String getPalavra() {
		return palavra;
	}

	public void setPalavra(String palavra) {
		this.palavra = palavra;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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
	
	
}