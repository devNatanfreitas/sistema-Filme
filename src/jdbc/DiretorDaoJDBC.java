package jdbc;

import db.DB;
import db.DbException;
import entities.Diretor;
import entities.dao.DiretorDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiretorDaoJDBC implements DiretorDao {
    private Connection conn;

    public DiretorDaoJDBC(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(Diretor diretor) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO diretor (nome, id_filme) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            st.setString(1, diretor.getNome());
            st.setInt(2, diretor.getIdFilme());

            int rows = st.executeUpdate();
            System.out.println(rows);
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Diretor diretor) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "UPDATE diretor SET nome = ?, id_filme = ? WHERE id = ?"
            );
            st.setString(1, diretor.getNome());
            st.setInt(2, diretor.getIdFilme());
            st.setInt(3, diretor.getId());

            int rows = st.executeUpdate();
            System.out.println(rows);
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM diretor WHERE id = ?");
            st.setInt(1, id);

            int rows = st.executeUpdate();
            System.out.println(rows);
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Diretor findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM diretor WHERE id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                Diretor diretor = new Diretor();
                diretor.setId(rs.getInt("id"));
                diretor.setNome(rs.getString("nome"));
                diretor.setIdFilme(rs.getInt("id_filme"));
                return diretor;
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
    public List<Diretor> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM diretor");
            rs = st.executeQuery();

            List<Diretor> list = new ArrayList<>();

            while (rs.next()) {
                Diretor diretor = new Diretor();
                diretor.setId(rs.getInt("id"));
                diretor.setNome(rs.getString("nome"));
                diretor.setIdFilme(rs.getInt("id_filme"));
                list.add(diretor);
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
