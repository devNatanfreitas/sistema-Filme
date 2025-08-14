package jdbc;

import db.DB;
import db.DbException;
import entities.Genero;
import entities.dao.GeneroDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GeneroDaoJDBC implements GeneroDao {
    private Connection conn;

    public GeneroDaoJDBC(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(Genero genero) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO genero (nome,id_filme) VALUES (?,?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            st.setString(1, genero.getNome());
            st.setInt(2, genero.getIdFilme());
            int rows = st.executeUpdate();
            System.out.println(rows);
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Genero genero) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "UPDATE genero SET nome = ?, id_filme = ? WHERE id = ?"
            );
            st.setString(1, genero.getNome());
            st.setInt(2, genero.getId());
            st.setInt(3, genero.getIdFilme());
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
            st = conn.prepareStatement("DELETE FROM genero WHERE id = ?");
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
    public Genero findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT g.* FROM genero g WHERE g.id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                Genero genero = new Genero();
                genero.setId(rs.getInt("id"));
                genero.setNome(rs.getString("nome"));
                genero.setIdFilme(rs.getInt("id_filme"));
                return genero;
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
    public List<Genero> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Genero> list = new ArrayList<>();
        try {
            st = conn.prepareStatement("SELECT * FROM genero");
            rs = st.executeQuery();

            while (rs.next()) {
                Genero genero = new Genero();
                genero.setId(rs.getInt("id"));
                genero.setNome(rs.getString("nome"));
                genero.setIdFilme(rs.getInt("id_filme"));
                list.add(genero);
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