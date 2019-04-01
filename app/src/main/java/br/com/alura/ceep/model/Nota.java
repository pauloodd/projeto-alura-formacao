package br.com.alura.ceep.model;

import java.io.Serializable;

public class Nota implements Serializable{

    private Long id;
    private String titulo;
    private String descricao;
    private String cor;
    private int posicao;

    public Nota(){}

    public Nota(String titulo, String descricao, String cor) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.cor = cor;
    }

    public Nota(String titulo, String descricao, String cor, Long id) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.cor = cor;
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getCor() {
        return cor;
    }

    public Long getId(){return id;}

    public void setId(Long id){this.id = id;}

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }
}