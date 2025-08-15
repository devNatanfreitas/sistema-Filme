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
                    // MUDANÇA AQUI: de "ator" para "atores"
                    "INSERT INTO atores (nome, id_filme) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            st.setString(1, ator.getNome());
            st.setInt(2, ator.getIdFilme());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    ator.setId(id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Erro inesperado! Nenhuma linha afetada.");
            }
        } catch (SQLException e) {
            throw new DbException("Erro ao inserir ator: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Ator ator) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    // MUDANÇA AQUI: de "ator" para "atores"
                    "UPDATE atores SET nome = ?, id_filme = ? WHERE id = ?"
            );
            st.setString(1, ator.getNome());
            st.setInt(2, ator.getIdFilme());
            st.setInt(3, ator.getId());

            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Erro ao atualizar ator: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM atores WHERE id = ?");
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Erro ao deletar ator: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Ator findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM atores WHERE id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                return instantiateAtor(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar ator por ID: " + e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    @Override
    public List<Ator> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM atores ORDER BY nome");
            rs = st.executeQuery();

            List<Ator> list = new ArrayList<>();
            while (rs.next()) {
                list.add(instantiateAtor(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar todos os atores: " + e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    private Ator instantiateAtor(ResultSet rs) throws SQLException {
        Ator ator = new Ator();
        ator.setId(rs.getInt("id"));
        ator.setNome(rs.getString("nome"));
        ator.setIdFilme(rs.getInt("id_filme"));
        return ator;
    }
}