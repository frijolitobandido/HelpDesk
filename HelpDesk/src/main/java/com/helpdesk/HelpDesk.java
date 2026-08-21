package com.helpdesk;

import com.helpdesk.modelo.*;
import com.helpdesk.persistencia.ConexionBD;
import com.helpdesk.servicio.SistemaTickets;

import java.util.List;
import java.util.Scanner;

public class HelpDesk {

    private static final Scanner sc = new Scanner(System.in);
    private static SistemaTickets sistema;

    public static void main(String[] args) {
        ConexionBD.inicializarTablas();
        sistema = new SistemaTickets();
        sistema.iniciar();

        boolean salir = false;
        while (!salir) {
            mostrarMenuPrincipal();
            int opcion = leerEntero("Elige una opcion: ");
            switch (opcion) {
                case 1 -> flujoCliente();
                case 2 -> flujoAgente();
                case 3 -> verMetricas();
                case 0 -> salir = true;
                default -> System.out.println("Opcion invalida.");
            }
        }

        ConexionBD.cerrarConexion();
        System.out.println("Sistema cerrado.");
    }

    

    private static void mostrarMenuPrincipal() {
        System.out.println("\n===== HelpDeskPro =====");
        System.out.println("1. Soy cliente (reportar un problema)");
        System.out.println("2. Soy agente (atender tickets)");
        System.out.println("3. Ver metricas");
        System.out.println("0. Salir");
    }

    

    private static void flujoCliente() {
        System.out.print("Ingresa tu numero de documento/celular: ");
        String clienteId = sc.nextLine();

        if (!sistema.historialDeCliente(clienteId).isEmpty() || clienteExisteEnBD(clienteId)) {
            System.out.println("Bienvenido de nuevo.");
        } else {
            System.out.print("Eres nuevo, ingresa tu nombre: ");
            String nombre = sc.nextLine();
            sistema.registrarClienteSiNoExiste(clienteId, nombre, "");
        }

        System.out.println("\nSelecciona tu problema:");
        TipoProblema[] tipos = TipoProblema.values();
        for (int i = 0; i < tipos.length; i++) {
            System.out.println((i + 1) + ". " + tipos[i].getDescripcion());
        }
        int opcion = leerEntero("Opcion: ") - 1;
        if (opcion < 0 || opcion >= tipos.length) {
            System.out.println("Opcion invalida.");
            return;
        }

        System.out.print("Describe brevemente tu problema (opcional): ");
        String descripcion = sc.nextLine();

        Ticket ticket = sistema.registrarTicket(clienteId, tipos[opcion], descripcion);
        System.out.println("\nTicket #" + ticket.getId() + " registrado con prioridad " + ticket.getPrioridad());
    }

    private static boolean clienteExisteEnBD(String id) {
        return sistema.historialDeCliente(id) != null;
    }

    

    private static void flujoAgente() {
        System.out.println("\nAgentes disponibles:");
        for (Agente a : sistema.verTodosLosAgentes()) {
            System.out.println("- " + a);
        }
        System.out.print("Ingresa tu ID de agente (ej. A001): ");
        String agenteId = sc.nextLine();

        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- Bandeja de tickets (ordenada por prioridad) ---");
            List<Ticket> bandeja = sistema.verBandejaOrdenada();
            if (bandeja.isEmpty()) {
                System.out.println("No hay tickets pendientes.");
            } else {
                for (Ticket t : bandeja) {
                    System.out.println("#" + t.getId() + " " + t);
                }
            }

            System.out.println("\n1. Tomar un ticket");
            System.out.println("2. Cambiar estado de un ticket");
            System.out.println("3. Deshacer ultimo cambio de un ticket");
            System.out.println("0. Volver al menu principal");
            int opcion = leerEntero("Opcion: ");

            switch (opcion) {
                case 1 -> {
                    int id = leerEntero("ID del ticket a tomar: ");
                    Ticket t = sistema.tomarTicket(id, agenteId);
                    System.out.println(t != null ? "Ticket tomado: " + t : "No se pudo tomar el ticket.");
                }
                case 2 -> {
                    int id = leerEntero("ID del ticket: ");
                    System.out.println("Estados: 1-EN_PROGRESO 2-ESCALADO 3-RESUELTO");
                    int est = leerEntero("Nuevo estado: ");
                    Estado nuevo = switch (est) {
                        case 1 -> Estado.EN_PROGRESO;
                        case 2 -> Estado.ESCALADO;
                        case 3 -> Estado.RESUELTO;
                        default -> null;
                    };
                    System.out.println(nuevo != null ? "Estado actualizado." : "Opcion invalida.");
                }
                case 3 -> {
                    int id = leerEntero("ID del ticket: ");
                    System.out.println("Deshacer no implementado en este menu todavia (falta buscar Ticket por id).");
                }
                case 0 -> volver = true;
                default -> System.out.println("Opcion invalida.");
            }
        }
    }

    //  Métricas

    private static void verMetricas() {
        System.out.println("\n--- Metricas ---");
        for (Agente a : sistema.verTodosLosAgentes()) {
            System.out.println("- " + a);
        }
        Agente menosCargado = sistema.verAgenteMenosCargado();
        if (menosCargado != null) {
            System.out.println("\nAgente con menos carga: " + menosCargado);
        }
    }

    

    private static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        while (!sc.hasNextInt()) {
            sc.next();
            System.out.print("Ingresa un numero valido: ");
        }
        int valor = sc.nextInt();
        sc.nextLine(); 
        return valor;
    }
}