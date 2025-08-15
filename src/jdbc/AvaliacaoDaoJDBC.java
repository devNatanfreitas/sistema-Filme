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
            // CORREÇÃO: "avaliacoes" (plural)
            st = conn.prepareStatement(
                    "INSERT INTO avaliacoes (id_usuario, id_filme, nota, comentario) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            st.setInt(1, avaliacao.getIdUsuario());
            st.setInt(2, avaliacao.getIdFilme());
            st.setInt(3, avaliacao.getNota()); // MUDANÇA: setInt para a nota
            st.setString(4, avaliacao.getComentario());

            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Erro ao inserir avaliação: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Avaliacao avaliacao) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "UPDATE avaliacoes SET id_usuario = ?, id_filme = ?, nota = ?, comentario = ? WHERE id = ?"
            );
            st.setInt(1, avaliacao.getIdUsuario());
            st.setInt(2, avaliacao.getIdFilme());
            st.setInt(3, avaliacao.getNota()); // MUDANÇA: setInt para a nota
            st.setString(4, avaliacao.getComentario());
            st.setInt(5, avaliacao.getId());

            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Erro ao atualizar avaliação: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM avaliacoes WHERE id = ?");
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Erro ao deletar avaliação: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    // Métodos findById e findAll também corrigidos com tratamento de erro e nome da tabela
    @Override
    public Avaliacao findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM avaliacoes WHERE id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                return instantiateAvaliacao(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar avaliação por ID: " + e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    @Override
    public List<Avaliacao> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Avaliacao> list = new ArrayList<>();
        try {
            st = conn.prepareStatement("SELECT * FROM avaliacoes ORDER BY id_filme");
            rs = st.executeQuery();
            while (rs.next()) {
                list.add(instantiateAvaliacao(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar todas as avaliações: " + e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    private Avaliacao instantiateAvaliacao(ResultSet rs) throws SQLException {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setId(rs.getInt("id"));
        avaliacao.setIdUsuario(rs.getInt("id_usuario"));
        avaliacao.setIdFilme(rs.getInt("id_filme"));
        avaliacao.setNota(rs.getInt("nota"));
        avaliacao.setComentario(rs.getString("comentario"));
        return avaliacao;
    }
}