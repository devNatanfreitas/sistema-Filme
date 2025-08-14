package entities.dao;

import entities.Filme;

import java.util.List;

public interface FilmeDao {
    void insert(Filme filme);
    void update(Filme filme);
    void deleteById(Integer id);
    Filme findById(Integer id);
    List<Filme> findAll();
}
