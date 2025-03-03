package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DataBase;
import model.Resumo;

public class ResumoDAO {

    public void inserir(Resumo resumo, int usuarioId) {
        String sql = "INSERT INTO resumos (titulo, texto, materia, assunto, anexo, usuario_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DataBase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, resumo.getTitulo());
            pstmt.setString(2, resumo.getTexto());
            pstmt.setString(3, resumo.getMateria());
            pstmt.setString(4, resumo.getAssunto());
            pstmt.setString(5, resumo.getAnexo());
            pstmt.setInt(6, usuarioId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Resumo> listarPorMateria(String materia, int usuarioId) {
        List<Resumo> resumos = new ArrayList<>();
        String sql = "SELECT * FROM resumos WHERE materia = ? AND usuario_id = ?";

        try (Connection conn = DataBase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, materia);
            pstmt.setInt(2, usuarioId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Resumo r = new Resumo(rs.getString("titulo"), rs.getString("texto"), rs.getString("materia"),
                        rs.getString("assunto"), rs.getInt("usuario_id"));
                r.setId(rs.getInt("id"));
                r.setAnexo(rs.getString("anexo"));
                resumos.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resumos;
    }

    public List<Resumo> buscarPorTermo(String termo, int usuarioId) {
        List<Resumo> resumos = new ArrayList<>();
        String sql = "SELECT * FROM resumos WHERE usuario_id = ? AND (titulo LIKE ? OR texto LIKE ? OR materia LIKE ? OR assunto LIKE ?)";

        try (Connection conn = DataBase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            String parametro = "%" + termo + "%";
            pstmt.setString(2, parametro);
            pstmt.setString(3, parametro);
            pstmt.setString(4, parametro);
            pstmt.setString(5, parametro);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Resumo r = new Resumo(rs.getString("titulo"), rs.getString("texto"), rs.getString("materia"),
                        rs.getString("assunto"), rs.getInt("usuario_id"));
                r.setId(rs.getInt("id"));
                r.setAnexo(rs.getString("anexo"));
                resumos.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resumos;
    }
}