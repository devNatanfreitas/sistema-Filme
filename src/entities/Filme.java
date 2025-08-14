package entities;

import java.io.Serializable;

public class Filme implements Serializable {
    private int id;
    private String titulo;
    private int duracao;
    private int anoLancamento;

    public Filme() {}

    public Filme(String titulo, int duracao, int anoLancamento) {
        this.titulo = titulo;
        this.duracao = duracao;
        this.anoLancamento = anoLancamento;
    }
    public Filme(int id, String titulo, int duracao, int anoLancamento) {
        this.id = id;
        this.titulo = titulo;
        this.duracao = duracao;
        this.anoLancamento = anoLancamento;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public int getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(int anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", duracao=" + duracao +
                ", anoLancamento=" + anoLancamento
                ;
    }
}
