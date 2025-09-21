package entities;

import java.io.Serializable;

public class Avaliacao implements Serializable {
    private int id;
    private String nomeAvaliador;
    private int idFilme;
    private int nota; // MUDANÇA: de String para int
    private String comentario;

    public Avaliacao() {}


    public Avaliacao(int id, String nomeAvaliador, int idFilme, int nota, String comentario) {
        this.id = id;
        this.nomeAvaliador = nomeAvaliador;
        this.idFilme = idFilme;
        this.nota = nota;
        this.comentario = comentario;
    }
    public Avaliacao(String nomeAvaliador, int idFilme, int nota, String comentario) {
        this.nomeAvaliador = nomeAvaliador;
        this.idFilme = idFilme;
        this.nota = nota;
        this.comentario = comentario;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNomeAvaliador() { return nomeAvaliador; }
    public void setNomeAvaliador(String nomeAvaliador) { this.nomeAvaliador = nomeAvaliador; }
    public int getIdFilme() { return idFilme; }
    public void setIdFilme(int idFilme) { this.idFilme = idFilme; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    // Getters e Setters atualizados para a nota
    public int getNota() { return nota; }
    public void setNota(int nota) { this.nota = nota; }

    @Override
    public String toString() {
        return "Avaliador: " + nomeAvaliador + " | Nota: " + nota + " | " + comentario;
    }
}