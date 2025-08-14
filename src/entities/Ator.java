package entities;

import java.io.Serializable;

public class Ator implements Serializable {
    private int id;
    private String nome;
    private int idFilme;

    public Ator() {}

    public Ator(int id, String nome, int idFilme) {
        this.id = id;
        this.nome = nome;
        this.idFilme = idFilme;
    }
    public Ator(String nome, int idFilme) {
        this.nome = nome;
        this.idFilme = idFilme;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdFilme() {
        return idFilme;
    }

    public void setIdFilme(int idFilme) {
        this.idFilme = idFilme;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", nome='" + nome + '\'' +
                ", id_filme=" + idFilme;
    }
}
