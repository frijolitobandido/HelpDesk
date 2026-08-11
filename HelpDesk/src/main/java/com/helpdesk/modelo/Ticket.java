package com.helpdesk.modelo;

import java.time.LocalDateTime;

public class Ticket {
    private int id;
    private String clienteId;
    private String asunto;
    private String descripcion;
    private Prioridad prioridad;
    private Estado estado;
    private String agenteAsignado; // id del agente, null hasta que se asigna
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaLimiteSla;

    public Ticket(String clienteId, String asunto, String descripcion, Prioridad prioridad) {
        this.clienteId = clienteId;
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.estado = Estado.ABIERTO;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaLimiteSla = calcularFechaLimite(prioridad);
    }

    private LocalDateTime calcularFechaLimite(Prioridad p) {
        return switch (p) {
            case CRITICO -> fechaCreacion.plusMinutes(15);
            case ALTO -> fechaCreacion.plusHours(4);
            case MEDIO -> fechaCreacion.plusHours(24);
            case BAJO -> fechaCreacion.plusHours(72);
        };
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getClienteId() { return clienteId; }

    public String getAsunto() { return asunto; }

    public String getDescripcion() { return descripcion; }

    public Prioridad getPrioridad() { return prioridad; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public String getAgenteAsignado() { return agenteAsignado; }
    public void setAgenteAsignado(String agenteAsignado) { this.agenteAsignado = agenteAsignado; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }

    public LocalDateTime getFechaLimiteSla() { return fechaLimiteSla; }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s (%s)", prioridad, asunto, estado, clienteId);
    }
}