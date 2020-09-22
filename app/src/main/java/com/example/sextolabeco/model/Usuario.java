package com.example.sextolabeco.model;

public class Usuario {


    private String name;
    private String contrasena;
    private String id;

    public Usuario(String name, String contrasena, String id) {
        this.name = name;
        this.contrasena = contrasena;
        this.id = id;
    }

    public Usuario() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}



