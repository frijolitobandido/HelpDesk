package com.helpdesk.estructuras;

import com.helpdesk.modelo.Agente;
import java.util.ArrayList;
import java.util.List;

public class ArbolAgentes {

    private class Nodo {
        Agente agente;
        Nodo izquierda;
        Nodo derecha;

        Nodo(Agente agente) {
            this.agente = agente;
        }
    }

    private Nodo raiz;

    public void insertar(Agente agente) {
        raiz = insertarRecursivo(raiz, agente);
    }

    private Nodo insertarRecursivo(Nodo actual, Agente agente) {
        if (actual == null) {
            return new Nodo(agente);
        }
        if (agente.getTicketsAsignados() < actual.agente.getTicketsAsignados()) {
            actual.izquierda = insertarRecursivo(actual.izquierda, agente);
        } else {
            actual.derecha = insertarRecursivo(actual.derecha, agente);
        }
        return actual;
    }

    public Agente encontrarMenosCargado() {
        if (raiz == null) return null;
        Nodo actual = raiz;
        while (actual.izquierda != null) {
            actual = actual.izquierda;
        }
        return actual.agente;
    }

    public Agente buscarPorId(String id) {
        return buscarRecursivo(raiz, id);
    }

    private Agente buscarRecursivo(Nodo actual, String id) {
        if (actual == null) return null;
        if (actual.agente.getId().equals(id)) return actual.agente;
        Agente enIzquierda = buscarRecursivo(actual.izquierda, id);
        if (enIzquierda != null) return enIzquierda;
        return buscarRecursivo(actual.derecha, id);
    }

    public void reordenar() {
        List<Agente> todos = obtenerTodosInOrder();
        raiz = null;
        for (Agente a : todos) {
            insertar(a);
        }
    }

    public List<Agente> obtenerTodosInOrder() {
        List<Agente> resultado = new ArrayList<>();
        inOrderRecursivo(raiz, resultado);
        return resultado;
    }

    private void inOrderRecursivo(Nodo actual, List<Agente> resultado) {
        if (actual == null) return;
        inOrderRecursivo(actual.izquierda, resultado);
        resultado.add(actual.agente);
        inOrderRecursivo(actual.derecha, resultado);
    }
}