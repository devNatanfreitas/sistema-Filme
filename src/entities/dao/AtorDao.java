package entities.dao;

import entities.Ator;

import java.util.List;

public interface AtorDao {
    void insert(Ator ator);
    void update(Ator ator);
    void deleteById(Integer id);
    Ator findById(Integer id);
    List<Ator> findAll();
}