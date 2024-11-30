package com.persona2.persona2.ClasesDeEntidad;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class LibroEntidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private int numeroDescargas;
    private String language;
    private String author;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(int numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    // Getters y Setters
    // ...
}

