package entities;

import java.io.Serializable;

public class Avaliacao implements Serializable {
    private int id;
    private int idUsuario;
    private int idFilme;
    private int nota;
    private String comentario;

    public Avaliacao() {}

    public Avaliacao(int id, int idUsuario, int idFilme, int nota, String comentario) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idFilme = idFilme;
        this.nota = nota;
        this.comentario = comentario;
    }
    public Avaliacao(int idUsuario, int idFilme, int nota, String comentario) {
        this.idUsuario = idUsuario;
        this.idFilme = idFilme;
        this.nota = nota;
        this.comentario = comentario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdFilme() {
        return idFilme;
    }

    public void setIdFilme(int idFilme) {
        this.idFilme = idFilme;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", id_usuario=" + idUsuario +
                ", id_filme=" + idFilme +
                ", nota=" + nota +
                ", comentario='" + comentario;
    }
}
