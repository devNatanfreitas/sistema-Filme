import db.DB;
import entities.Filme;
import jdbc.FilmeDaoJDBC;

import java.sql.Connection;

public class Program {
    public static void main(String[] args) {

        FilmeDaoJDBC filmeDao = new FilmeDaoJDBC(DB.getConnection());
          long startTimeNoTransaction = System.currentTimeMillis();
          for (int i = 0; i < 10000; i++) {
             Filme filme = new Filme("Filme " + i, 120, 1996);
             filmeDao.insert(filme);
          }
          long endTimeNoTransaction = System.currentTimeMillis();
         long totalTimeNoTransaction = endTimeNoTransaction - startTimeNoTransaction;
          System.out.println("Tempo sem transação: " + totalTimeNoTransaction / 1000.0 + " segundos");
    }
}
