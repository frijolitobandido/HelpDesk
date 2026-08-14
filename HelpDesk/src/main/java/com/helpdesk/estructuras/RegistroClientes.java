package com.helpdesk.estructuras;

import com.helpdesk.modelo.Cliente;
import com.helpdesk.modelo.Ticket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistroClientes {

    private final Map<String, Cliente> clientes;
    private final Map<String, List<Ticket>> historialPorCliente;

    public RegistroClientes() {
        this.clientes = new HashMap<>();
        this.historialPorCliente = new HashMap<>();
    }

    public void registrarCliente(Cliente cliente) {
        clientes.put(cliente.getId(), cliente);
        historialPorCliente.putIfAbsent(cliente.getId(), new ArrayList<>());
    }

    public boolean existeCliente(String id) {
        return clientes.containsKey(id);
    }

    public Cliente buscarCliente(String id) {
        return clientes.get(id);
    }

    public void agregarTicketAHistorial(Ticket ticket) {
        historialPorCliente
            .computeIfAbsent(ticket.getClienteId(), k -> new ArrayList<>())
            .add(ticket);
    }

    public List<Ticket> obtenerHistorial(String clienteId) {
        return historialPorCliente.getOrDefault(clienteId, new ArrayList<>());
    }
}