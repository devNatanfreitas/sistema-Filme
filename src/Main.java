import db.DB;
import entities.Ator;
import entities.Diretor;
import entities.Genero;
import jdbc.AtorDaoJDBC;
import jdbc.DiretorDaoJDBC;
import jdbc.GeneroDaoJDBC;


import java.sql.*;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        Connection conn = DB.getConnection();
        AtorDaoJDBC generoDao = new AtorDaoJDBC(conn);

        try {
            conn.setAutoCommit(false);
            long inicio = System.currentTimeMillis();

            Ator genero = new Ator("Uma Thurman",3);
            generoDao.insert(genero);

            List<Ator> lista = generoDao.findAll();
            for (Ator genero1 : lista){
                System.out.println(genero1);
            }

            System.out.println();

            conn.commit();

            long fim = System.currentTimeMillis();
            long total = fim - inicio;
            System.out.println("Tempo com transação: " + total / 1000.0 + " segundos");
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
        } finally {
            DB.closeConnection();
        }



    }

}