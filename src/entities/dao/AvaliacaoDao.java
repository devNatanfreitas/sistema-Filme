package entities.dao;

import entities.Avaliacao;

import java.util.List;

public interface AvaliacaoDao {
    void insert(Avaliacao avaliacao);
    void update(Avaliacao avaliacao);
    void deleteById(Integer id);
    Avaliacao findById(Integer id);
    List<Avaliacao> findAll();
}