package com.helpdesk.servicio;

import com.helpdesk.estructuras.ArbolAgentes;
import com.helpdesk.estructuras.ColaTickets;
import com.helpdesk.estructuras.HistorialEstados;
import com.helpdesk.estructuras.RegistroClientes;
import com.helpdesk.modelo.*;
import com.helpdesk.persistencia.AgenteRepositorio;
import com.helpdesk.persistencia.ClienteRepositorio;
import com.helpdesk.persistencia.TicketRepositorio;

import java.util.List;

public class SistemaTickets {

    private final ColaTickets colaTickets;
    private final ArbolAgentes arbolAgentes;
    private final RegistroClientes registroClientes;
    private final HistorialEstados historialEstados;

    private final TicketRepositorio ticketRepositorio;
    private final AgenteRepositorio agenteRepositorio;
    private final ClienteRepositorio clienteRepositorio;

    public SistemaTickets() {
        this.colaTickets = new ColaTickets();
        this.arbolAgentes = new ArbolAgentes();
        this.registroClientes = new RegistroClientes();
        this.historialEstados = new HistorialEstados();

        this.ticketRepositorio = new TicketRepositorio();
        this.agenteRepositorio = new AgenteRepositorio();
        this.clienteRepositorio = new ClienteRepositorio();
    }

    //  Arranque 

    public void iniciar() {
        cargarAgentes();
        cargarTicketsPendientesALaCola();
    }

    private void cargarAgentes() {
        if (!agenteRepositorio.existeAlgunAgente()) {
            crearAgentesPorDefecto();
        }
        for (Agente a : agenteRepositorio.obtenerTodos()) {
            arbolAgentes.insertar(a);
        }
    }

    private void crearAgentesPorDefecto() {
        Agente a1 = new Agente("A001", "Camila Torres");
        Agente a2 = new Agente("A002", "Diego Ramirez");
        Agente a3 = new Agente("A003", "Valeria Chavez");
        agenteRepositorio.insertar(a1);
        agenteRepositorio.insertar(a2);
        agenteRepositorio.insertar(a3);
    }

    private void cargarTicketsPendientesALaCola() {
        List<Ticket> pendientes = ticketRepositorio.obtenerPendientes();
        for (Ticket t : pendientes) {
            colaTickets.agregar(t);
        }
    }

    //  Fase 1: Registro (cliente) 

    public void registrarClienteSiNoExiste(String id, String nombre, String correo) {
        if (!clienteRepositorio.existe(id)) {
            Cliente cliente = new Cliente(id, nombre, correo);
            clienteRepositorio.insertar(cliente);
            registroClientes.registrarCliente(cliente);
        }
    }

    public Ticket registrarTicket(String clienteId, TipoProblema tipo, String descripcion) {
        Ticket ticket = new Ticket(clienteId, tipo.getDescripcion(), descripcion, tipo.getPrioridadAsociada());
        ticketRepositorio.insertar(ticket); // aquí recupera el id autoincremental
        registroClientes.agregarTicketAHistorial(ticket);
        colaTickets.agregar(ticket); // entra a la cola de prioridad
        return ticket;
    }

    //  Fase 2: Bandeja para agentes 

    public List<Ticket> verBandejaOrdenada() {
        return colaTickets.verComoListaOrdenada();
    }

    public Ticket tomarTicket(int ticketId, String agenteId) {
        Ticket ticket = colaTickets.extraerPorId(ticketId);
        if (ticket == null) return null;

        Agente agente = arbolAgentes.buscarPorId(agenteId);
        if (agente == null) return null;

        Estado anterior = ticket.getEstado();
        ticket.setAgenteAsignado(agenteId);
        ticket.setEstado(Estado.EN_PROGRESO);
        agente.incrementarCarga();
        arbolAgentes.reordenar();

        historialEstados.registrarCambio(ticket.getId(), anterior, Estado.EN_PROGRESO);

        ticketRepositorio.actualizar(ticket);
        agenteRepositorio.actualizar(agente);

        return ticket;
    }

    public void cambiarEstado(Ticket ticket, Estado nuevoEstado) {
        Estado anterior = ticket.getEstado();
        ticket.setEstado(nuevoEstado);
        historialEstados.registrarCambio(ticket.getId(), anterior, nuevoEstado);
        ticketRepositorio.actualizar(ticket);
    }

    public CambioEstado deshacerUltimoCambio(Ticket ticket) {
        CambioEstado cambio = historialEstados.deshacerUltimoCambio(ticket.getId());
        if (cambio != null) {
            ticket.setEstado(cambio.getEstadoAnterior());
            ticketRepositorio.actualizar(ticket);
        }
        return cambio;
    }

    // Consultas 

    public List<Ticket> historialDeCliente(String clienteId) {
        return ticketRepositorio.obtenerPorCliente(clienteId);
    }

    public Agente verAgenteMenosCargado() {
        return arbolAgentes.encontrarMenosCargado();
    }

    public List<Agente> verTodosLosAgentes() {
        return arbolAgentes.obtenerTodosInOrder();
    }
}