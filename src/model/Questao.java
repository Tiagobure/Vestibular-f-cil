package model;

public class Questao {
    private int id;
    private String exame;
    private String materia;
    private String assunto;
    private String imagemQuestao;
    private char respostaCorreta;

    // Construtor
    public Questao(int id, String exame, String materia, String assunto, String imagemQuestao, char respostaCorreta) {
        this.id = id;
        this.exame = exame;
        this.materia = materia;
        this.assunto = assunto;
        this.imagemQuestao = imagemQuestao;
        this.respostaCorreta = respostaCorreta;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public String getExame() {
        return exame;
    }

    public String getMateria() {
        return materia;
    }

    public String getAssunto() {
        return assunto;
    }

    public String getImagemQuestao() {
        return imagemQuestao;
    }

    public char getRespostaCorreta() {
        return respostaCorreta;
    }
}