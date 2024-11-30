package com.persona2.persona2.ClasesDeEntidad;

import jakarta.persistence.*;


import java.util.List;

@Entity
public class AutorEntidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private int añoNacimiento;
    private int añoDefuncion;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getAñoNacimiento() {
        return añoNacimiento;
    }

    public void setAñoNacimiento(int añoNacimiento) {
        this.añoNacimiento = añoNacimiento;
    }

    public int getAñoDefuncion() {
        return añoDefuncion;
    }

    public void setAñoDefuncion(int añoDefuncion) {
        this.añoDefuncion = añoDefuncion;
    }

    // Getters y Setters
    // ...
}

