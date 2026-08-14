package com.helpdesk.modelo;

import java.time.LocalDateTime;

public class CambioEstado {
    private final Estado estadoAnterior;
    private final Estado estadoNuevo;
    private final LocalDateTime fechaCambio;

    public CambioEstado(Estado estadoAnterior, Estado estadoNuevo) {
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.fechaCambio = LocalDateTime.now();
    }

    public Estado getEstadoAnterior() { return estadoAnterior; }
    public Estado getEstadoNuevo() { return estadoNuevo; }
    public LocalDateTime getFechaCambio() { return fechaCambio; }

    @Override
    public String toString() {
        return String.format("%s -> %s (%s)", estadoAnterior, estadoNuevo, fechaCambio);
    }
}