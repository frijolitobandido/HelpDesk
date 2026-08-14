package com.helpdesk.estructuras;

import com.helpdesk.modelo.CambioEstado;
import com.helpdesk.modelo.Estado;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class HistorialEstados {

    private final Map<Integer, Deque<CambioEstado>> pilasPorTicket;

    public HistorialEstados() {
        this.pilasPorTicket = new HashMap<>();
    }

    public void registrarCambio(int ticketId, Estado anterior, Estado nuevo) {
        CambioEstado cambio = new CambioEstado(anterior, nuevo);
        pilasPorTicket
            .computeIfAbsent(ticketId, k -> new ArrayDeque<>())
            .push(cambio); // push = apila al tope
    }

    public CambioEstado deshacerUltimoCambio(int ticketId) {
        Deque<CambioEstado> pila = pilasPorTicket.get(ticketId);
        if (pila == null || pila.isEmpty()) {
            return null; // no hay nada que deshacer
        }
        return pila.pop(); // saca y devuelve el tope
    }

    public CambioEstado verUltimoCambio(int ticketId) {
        Deque<CambioEstado> pila = pilasPorTicket.get(ticketId);
        if (pila == null || pila.isEmpty()) {
            return null;
        }
        return pila.peek(); // mira el tope sin sacarlo
    }

    public Deque<CambioEstado> obtenerHistorialCompleto(int ticketId) {
        return pilasPorTicket.getOrDefault(ticketId, new ArrayDeque<>());
    }
}