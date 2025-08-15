package jdbc;

import db.DB;
import db.DbException;
import entities.Filme;
import entities.dao.FilmeDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilmeDaoJDBC implements FilmeDao {
    private Connection conn;

    public FilmeDaoJDBC(Connection conn){
        this.conn = conn;
    }
    public FilmeDaoJDBC(){

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

            int rows =  st.executeUpdate();
            System.out.println(rows);
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
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
            int rows =  st.executeUpdate();
            System.out.println(rows);
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("delete from filmes where id = ?");
            st.setInt(1, id);
            int rows = st.executeUpdate();
            System.out.println(rows);
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Filme findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT f.* FROM filmes f WHERE f.id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                Filme filme = new Filme();
                filme.setId(rs.getInt("id"));
                filme.setTitulo(rs.getString("titulo"));
                filme.setDuracao(rs.getInt("duracao"));
                filme.setAnoLancamento(rs.getInt("ano_lancamento"));
                return filme;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
        return null;
    }

    @Override
    public List<Filme> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Filme> list = new ArrayList<>();
        try {
            st = conn.prepareStatement("SELECT * FROM filmes");
            rs = st.executeQuery();

            while (rs.next()) {
                Filme filme = new Filme();
                filme.setId(rs.getInt("id"));
                filme.setTitulo(rs.getString("titulo"));
                filme.setDuracao(rs.getInt("duracao"));
                filme.setAnoLancamento(rs.getInt("ano_lancamento"));
                list.add(filme);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
        return null;
    }
}
