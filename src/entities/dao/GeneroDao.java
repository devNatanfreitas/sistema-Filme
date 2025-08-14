package entities.dao;

import entities.Genero;

import java.util.List;

public interface GeneroDao {
    void insert(Genero genero);
    void update(Genero genero);
    void deleteById(Integer id);
    Genero findById(Integer id);
    List<Genero> findAll();
}