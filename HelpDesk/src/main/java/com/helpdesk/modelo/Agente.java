package com.helpdesk.modelo;

public class Agente {
    private String id;
    private String nombre;
    private int ticketsAsignados;
    private boolean activo;

    public Agente(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.ticketsAsignados = 0;
        this.activo = true;
    }

    public void incrementarCarga() {
        this.ticketsAsignados++;
    }

    public void decrementarCarga() {
        if (this.ticketsAsignados > 0) {
            this.ticketsAsignados--;
        }
    }

    // Getters y setters
    public String getId() { return id; }

    public String getNombre() { return nombre; }

    public int getTicketsAsignados() { return ticketsAsignados; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return String.format("%s (%s) - %d tickets", nombre, id, ticketsAsignados);
    }
}