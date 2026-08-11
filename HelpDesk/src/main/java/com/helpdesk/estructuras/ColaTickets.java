package com.helpdesk.estructuras;

import com.helpdesk.modelo.Ticket;
import com.helpdesk.modelo.Prioridad;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class ColaTickets {

    private final PriorityQueue<Ticket> cola;

    public ColaTickets() {
        Comparator<Ticket> porPrioridad = Comparator.comparing(
            t -> t.getPrioridad().ordinal()
        );
        this.cola = new PriorityQueue<>(porPrioridad);
    }

    public void agregar(Ticket ticket) {
        cola.add(ticket);
    }

    public Ticket verSiguiente() {
        return cola.peek(); 
    }

    public Ticket extraerSiguiente() {
        return cola.poll(); 
    }

    public Ticket extraerPorId(int ticketId) {
        Ticket encontrado = null;
        for (Ticket t : cola) {
            if (t.getId() == ticketId) {
                encontrado = t;
                break;
            }
        }
        if (encontrado != null) {
            cola.remove(encontrado);
        }
        return encontrado;
    }

    public List<Ticket> verComoListaOrdenada() {
        List<Ticket> copia = new ArrayList<>(cola);
        copia.sort(Comparator.comparing(t -> t.getPrioridad().ordinal()));
        return copia;
    }

    public boolean estaVacia() {
        return cola.isEmpty();
    }

    public int tamaño() {
        return cola.size();
    }
}