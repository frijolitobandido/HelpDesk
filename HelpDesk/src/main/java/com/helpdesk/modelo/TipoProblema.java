package com.helpdesk.modelo;

public enum TipoProblema {
    CARGO_NO_RECONOCIDO("Cargo no reconocido en mi cuenta", Prioridad.CRITICO),
    CUENTA_BLOQUEADA("Mi cuenta está bloqueada", Prioridad.CRITICO),
    APP_NO_FUNCIONA("La aplicación no carga o se cierra", Prioridad.ALTO),
    PAGO_NO_REFLEJADO("Hice un pago y no se refleja", Prioridad.ALTO),
    CAMBIO_DATOS("Quiero actualizar mis datos de contacto", Prioridad.MEDIO),
    CONSULTA_GENERAL("Tengo una pregunta sobre el servicio", Prioridad.BAJO);

    private final String descripcion;
    private final Prioridad prioridadAsociada;

    TipoProblema(String descripcion, Prioridad prioridadAsociada) {
        this.descripcion = descripcion;
        this.prioridadAsociada = prioridadAsociada;
    }

    public String getDescripcion() { return descripcion; }
    public Prioridad getPrioridadAsociada() { return prioridadAsociada; }
}