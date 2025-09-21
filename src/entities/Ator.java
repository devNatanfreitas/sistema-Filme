package entities;

import java.io.Serializable;

public class Ator implements Serializable {
    private Integer id;
    private String nome;
    private Integer idFilme;

    public Ator() {}

    public Ator(String nome, Integer idFilme) {
        this.nome = nome;
        this.idFilme = idFilme;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public Integer getIdFilme() { return idFilme; }
    public void setIdFilme(Integer idFilme) { this.idFilme = idFilme; }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Ator ator = (Ator) obj;
        return id != null ? id.equals(ator.id) : ator.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
