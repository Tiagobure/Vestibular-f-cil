package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DataBase;
import model.Questao;

public class QuestaoDAO {

    // Método para listar questões por exame (vestibular)
    public List<Questao> listarPorExame(String exame) {
        List<Questao> questoes = new ArrayList<>();
        String sql = "SELECT * FROM questoes WHERE exame = ?";

        try (Connection conn = DataBase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, exame);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Questao q = new Questao(
                    rs.getInt("id"),
                    rs.getString("exame"),
                    rs.getString("materia"),
                    rs.getString("assunto"),
                    rs.getString("imagem_questao"),
                    rs.getString("resposta_correta").charAt(0) // Converte String para char
                );
                questoes.add(q);
            }
        } catch (SQLException e) {
        	 System.err.println("Erro ao listar questões por exame: " + e.getMessage());
             e.printStackTrace();
        }

        return questoes;
    }

    // Método para inserir uma nova questão
    public void inserir(Questao questao) {
        String sql = "INSERT INTO questoes (exame, materia, assunto, imagem_questao, resposta_correta) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DataBase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, questao.getExame());
            pstmt.setString(2, questao.getMateria());
            pstmt.setString(3, questao.getAssunto());
            pstmt.setString(4, questao.getImagemQuestao());
            pstmt.setString(5, String.valueOf(questao.getRespostaCorreta())); // Converte char para String
            pstmt.executeUpdate();
        } catch (SQLException e) {
        	  System.err.println("Erro ao inserir questão: " + e.getMessage());
              e.printStackTrace();;
        }
    }

    // Método para atualizar uma questão existente
    public void atualizar(Questao questao) {
        String sql = "UPDATE questoes SET exame = ?, materia = ?, assunto = ?, imagem_questao = ?, resposta_correta = ? WHERE id = ?";

        try (Connection conn = DataBase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, questao.getExame());
            pstmt.setString(2, questao.getMateria());
            pstmt.setString(3, questao.getAssunto());
            pstmt.setString(4, questao.getImagemQuestao());
            pstmt.setString(5, String.valueOf(questao.getRespostaCorreta())); // Converte char para String
            pstmt.setInt(6, questao.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para deletar uma questão pelo ID
    public void deletar(int id) {
        String sql = "DELETE FROM questoes WHERE id = ?";

        try (Connection conn = DataBase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
        	 System.err.println("Erro ao deletar questão: " + e.getMessage());
             e.printStackTrace();
        }
    }

    // Método para buscar uma questão pelo ID
    public Questao buscarPorId(int id) {
        String sql = "SELECT * FROM questoes WHERE id = ?";
        Questao questao = null;

        try (Connection conn = DataBase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                questao = new Questao(
                    rs.getInt("id"),
                    rs.getString("exame"),
                    rs.getString("materia"),
                    rs.getString("assunto"),
                    rs.getString("imagem_questao"),
                    rs.getString("resposta_correta").charAt(0) // Converte String para char
                );
            }
        } catch (SQLException e) {
        	 System.err.println("Erro ao buscar questão por ID: " + e.getMessage());
             e.printStackTrace();
        }

        return questao;
    }
}