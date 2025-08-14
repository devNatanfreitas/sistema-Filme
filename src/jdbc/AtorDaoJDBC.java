package jdbc;

import db.DB;
import db.DbException;
import entities.Ator;
import entities.dao.AtorDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AtorDaoJDBC implements AtorDao {
    private Connection conn;

    public AtorDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Ator ator) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO ator (nome, id_filme) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            st.setString(1, ator.getNome());
            st.setInt(2, ator.getIdFilme());

            int rows = st.executeUpdate();
            System.out.println(rows);
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Ator ator) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "UPDATE ator SET nome = ?, id_filme = ? WHERE id = ?"
            );
            st.setString(1, ator.getNome());
            st.setInt(2, ator.getIdFilme());
            st.setInt(3, ator.getId());

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
            st = conn.prepareStatement("DELETE FROM ator WHERE id = ?");
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
    public Ator findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT a.* FROM ator a WHERE a.id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                Ator ator = new Ator();
                ator.setId(rs.getInt("id"));
                ator.setNome(rs.getString("nome"));
                ator.setIdFilme(rs.getInt("id_filme"));
                return ator;
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
    public List<Ator> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Ator> list = new ArrayList<>();
        try {
            st = conn.prepareStatement("SELECT * FROM ator");
            rs = st.executeQuery();

            while (rs.next()) {
                Ator ator = new Ator();
                ator.setId(rs.getInt("id"));
                ator.setNome(rs.getString("nome"));
                ator.setIdFilme(rs.getInt("id_filme"));
                list.add(ator);
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