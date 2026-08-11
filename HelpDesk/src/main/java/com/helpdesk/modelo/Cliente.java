package com.helpdesk.modelo;

public class Cliente {
    private String id;
    private String nombre;
    private String correo;

    public Cliente(String id, String nombre, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
    }

    // Getters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }

    @Override
    public String toString() {
        return String.format("%s - %s", nombre, id);
    }
}