package entities.dao;

import entities.Diretor;
import java.util.List;

public interface DiretorDao {
    void insert(Diretor diretor);
    void update(Diretor diretor);
    void deleteById(Integer id);
    Diretor findById(Integer id);
    List<Diretor> findAll();
}
 
