package com.helpdesk.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionBD {

    private static final String URL = "jdbc:sqlite:helpdesk.db";
    private static Connection conexion;

    private ConexionBD() {
        // constructor privado: estática
    }

    public static Connection obtenerConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                conexion = DriverManager.getConnection(URL);
                conexion.createStatement().execute("PRAGMA foreign_keys = ON;");
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }
        return conexion;
    }

    public static void inicializarTablas() {
        String sqlAgentes = """
            CREATE TABLE IF NOT EXISTS agentes (
                id TEXT PRIMARY KEY,
                nombre TEXT NOT NULL,
                tickets_asignados INTEGER DEFAULT 0,
                activo INTEGER DEFAULT 1
            );
        """;

        String sqlClientes = """
            CREATE TABLE IF NOT EXISTS clientes (
                id TEXT PRIMARY KEY,
                nombre TEXT,
                correo TEXT
            );
        """;

        String sqlTickets = """
            CREATE TABLE IF NOT EXISTS tickets (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                cliente_id TEXT NOT NULL,
                asunto TEXT NOT NULL,
                descripcion TEXT,
                prioridad TEXT NOT NULL,
                estado TEXT DEFAULT 'ABIERTO',
                agente_id TEXT,
                fecha_creacion TEXT,
                fecha_limite_sla TEXT,
                FOREIGN KEY (cliente_id) REFERENCES clientes(id),
                FOREIGN KEY (agente_id) REFERENCES agentes(id)
            );
        """;

        String sqlHistorial = """
            CREATE TABLE IF NOT EXISTS historial_estados (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                ticket_id INTEGER NOT NULL,
                estado_anterior TEXT,
                estado_nuevo TEXT,
                fecha_cambio TEXT,
                FOREIGN KEY (ticket_id) REFERENCES tickets(id)
            );
        """;

        try (Statement stmt = obtenerConexion().createStatement()) {
            stmt.execute(sqlAgentes);
            stmt.execute(sqlClientes);
            stmt.execute(sqlTickets);
            stmt.execute(sqlHistorial);
            System.out.println("Tablas inicializadas correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al crear tablas: " + e.getMessage());
        }
    }

    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}