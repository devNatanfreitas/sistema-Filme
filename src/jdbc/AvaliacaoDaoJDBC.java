package jdbc;

import db.DB;
import db.DbException;
import entities.Avaliacao;
import entities.dao.AvaliacaoDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvaliacaoDaoJDBC implements AvaliacaoDao {
    private Connection conn;

    public AvaliacaoDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Avaliacao avaliacao) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO avaliacao (id_usuario, id_filme, nota, comentario) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            st.setInt(1, avaliacao.getIdUsuario());
            st.setInt(2, avaliacao.getIdFilme());
            st.setInt(3, avaliacao.getNota());
            st.setString(4, avaliacao.getComentario());

            int rows = st.executeUpdate();
            System.out.println(rows);
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Avaliacao avaliacao) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "UPDATE avaliacao SET id_usuario = ?, id_filme = ?, nota = ?, comentario = ? WHERE id = ?"
            );
            st.setInt(1, avaliacao.getIdUsuario());
            st.setInt(2, avaliacao.getIdFilme());
            st.setInt(3, avaliacao.getNota());
            st.setString(4, avaliacao.getComentario());
            st.setInt(5, avaliacao.getId());

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
            st = conn.prepareStatement("DELETE FROM avaliacao WHERE id = ?");
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
    public Avaliacao findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT a.* FROM avaliacao a WHERE a.id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                Avaliacao avaliacao = new Avaliacao();
                avaliacao.setId(rs.getInt("id"));
                avaliacao.setIdUsuario(rs.getInt("id_usuario"));
                avaliacao.setIdFilme(rs.getInt("id_filme"));
                avaliacao.setNota(rs.getInt("nota"));
                avaliacao.setComentario(rs.getString("comentario"));
                return avaliacao;
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
    public List<Avaliacao> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Avaliacao> list = new ArrayList<>();
        try {
            st = conn.prepareStatement("SELECT * FROM avaliacao");
            rs = st.executeQuery();

            while (rs.next()) {
                Avaliacao avaliacao = new Avaliacao();
                avaliacao.setId(rs.getInt("id"));
                avaliacao.setIdUsuario(rs.getInt("id_usuario"));
                avaliacao.setIdFilme(rs.getInt("id_filme"));
                avaliacao.setNota(rs.getInt("nota"));
                avaliacao.setComentario(rs.getString("comentario"));
                list.add(avaliacao);
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