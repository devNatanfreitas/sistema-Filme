package jdbc;

import db.DbException;
import entities.Filme;
import entities.dao.FilmeDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilmeDaoJDBC implements FilmeDao {
    private Connection conn;

    public FilmeDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Filme filme) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO filmes (titulo, duracao, ano_lancamento) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            st.setString(1, filme.getTitulo());
            st.setInt(2, filme.getDuracao());
            st.setInt(3, filme.getAnoLancamento());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    filme.setId(id);
                }
                if (rs != null) rs.close();
            } else {
                throw new DbException("Erro inesperado! Nenhuma linha afetada.");
            }
        } catch (SQLException e) {
            throw new DbException("Erro ao inserir filme: " + e.getMessage());
        } finally {
            if (st != null) {
                try { st.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    @Override
    public void update(Filme filme) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "UPDATE filmes SET titulo = ?, duracao = ?, ano_lancamento = ? WHERE id = ?"
            );
            st.setString(1, filme.getTitulo());
            st.setInt(2, filme.getDuracao());
            st.setInt(3, filme.getAnoLancamento());
            st.setInt(4, filme.getId());

            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Erro ao atualizar filme: " + e.getMessage());
        } finally {
            if (st != null) {
                try { st.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM filmes WHERE id = ?");
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Erro ao deletar filme: " + e.getMessage());
        } finally {
            if (st != null) {
                try { st.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    @Override
    public Filme findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM filmes WHERE id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                return instantiateFilme(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar filme por ID: " + e.getMessage());
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
            if (st != null) {
                try { st.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    @Override
    public List<Filme> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM filmes ORDER BY titulo");
            rs = st.executeQuery();

            List<Filme> list = new ArrayList<>();
            while (rs.next()) {
                list.add(instantiateFilme(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar todos os filmes: " + e.getMessage());
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
            if (st != null) {
                try { st.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    private Filme instantiateFilme(ResultSet rs) throws SQLException {
        Filme filme = new Filme();
        filme.setId(rs.getInt("id"));
        filme.setTitulo(rs.getString("titulo"));
        filme.setDuracao(rs.getInt("duracao"));
        filme.setAnoLancamento(rs.getInt("ano_lancamento"));
        return filme;
    }
}
