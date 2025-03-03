package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DataBase;
import model.PalavraChave;

public class PalavraChaveDAO {
	

	    public void inserir(PalavraChave palavraChave) {
	        String sql = "INSERT INTO palavras_chave (palavra, descricao, materia, assunto) VALUES (?, ?, ?, ?)";

	        try (Connection conn = DataBase.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setString(1, palavraChave.getPalavra());
	            pstmt.setString(2, palavraChave.getDescricao());
	            pstmt.setString(3, palavraChave.getMateria());
	            pstmt.setString(4, palavraChave.getAssunto());
	            pstmt.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    public List<PalavraChave> listarPorMateria(String materia) {
	        List<PalavraChave> palavrasChave = new ArrayList<>();
	        String sql = "SELECT * FROM palavras_chave WHERE materia = ?";

	        try (Connection conn = DataBase.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setString(1, materia);
	            ResultSet rs = pstmt.executeQuery();

	            while (rs.next()) {
	                PalavraChave pc = new PalavraChave(
	                    rs.getString("palavra"),
	                    rs.getString("descricao"),
	                    rs.getString("materia"),
	                    rs.getString("assunto")
	                );
	                pc.setId(rs.getInt("id"));
	                palavrasChave.add(pc);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return palavrasChave;
	    }
	    
	    public List<PalavraChave> buscarPorTermo(String termo) {
	        List<PalavraChave> palavrasChave = new ArrayList<>();
	        String sql = "SELECT * FROM palavras_chave WHERE " +
	                     "palavra LIKE ? OR " +
	                     "descricao LIKE ? OR " +
	                     "materia LIKE ? OR " +
	                     "assunto LIKE ?";

	        try (Connection conn = DataBase.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {

	            // Adiciona o termo de busca em cada campo
	            String termoBusca = "%" + termo + "%"; // Usamos % para buscar parcialmente
	            pstmt.setString(1, termoBusca);
	            pstmt.setString(2, termoBusca);
	            pstmt.setString(3, termoBusca);
	            pstmt.setString(4, termoBusca);

	            ResultSet rs = pstmt.executeQuery();

	            while (rs.next()) {
	                PalavraChave pc = new PalavraChave(
	                    rs.getString("palavra"),
	                    rs.getString("descricao"),
	                    rs.getString("materia"),
	                    rs.getString("assunto")
	                );
	                pc.setId(rs.getInt("id"));
	                palavrasChave.add(pc);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return palavrasChave;
	    }
	   
}
