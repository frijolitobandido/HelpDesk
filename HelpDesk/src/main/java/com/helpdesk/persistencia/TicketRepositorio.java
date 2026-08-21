package com.helpdesk.persistencia;

import com.helpdesk.modelo.Estado;
import com.helpdesk.modelo.Prioridad;
import com.helpdesk.modelo.Ticket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketRepositorio {

    public void insertar(Ticket ticket) {
        String sql = """
            INSERT INTO tickets 
            (cliente_id, asunto, descripcion, prioridad, estado, agente_id, fecha_creacion, fecha_limite_sla)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement ps = ConexionBD.obtenerConexion()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, ticket.getClienteId());
            ps.setString(2, ticket.getAsunto());
            ps.setString(3, ticket.getDescripcion());
            ps.setString(4, ticket.getPrioridad().name());
            ps.setString(5, ticket.getEstado().name());
            ps.setString(6, ticket.getAgenteAsignado());
            ps.setString(7, ticket.getFechaCreacion().toString());
            ps.setString(8, ticket.getFechaLimiteSla().toString());
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                ticket.setId(keys.getInt(1)); // recupera el id autoincremental
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar ticket: " + e.getMessage());
        }
    }

    public void actualizar(Ticket ticket) {
        String sql = "UPDATE tickets SET estado = ?, agente_id = ? WHERE id = ?";
        try (PreparedStatement ps = ConexionBD.obtenerConexion().prepareStatement(sql)) {
            ps.setString(1, ticket.getEstado().name());
            ps.setString(2, ticket.getAgenteAsignado());
            ps.setInt(3, ticket.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar ticket: " + e.getMessage());
        }
    }

    public List<Ticket> obtenerPendientes() {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM tickets WHERE estado != 'RESUELTO' AND agente_id IS NULL";
        try (PreparedStatement ps = ConexionBD.obtenerConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tickets.add(mapearTicket(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener tickets pendientes: " + e.getMessage());
        }
        return tickets;
    }

    public List<Ticket> obtenerPorCliente(String clienteId) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM tickets WHERE cliente_id = ?";
        try (PreparedStatement ps = ConexionBD.obtenerConexion().prepareStatement(sql)) {
            ps.setString(1, clienteId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tickets.add(mapearTicket(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener tickets del cliente: " + e.getMessage());
        }
        return tickets;
    }

    private Ticket mapearTicket(ResultSet rs) throws SQLException {
        Ticket t = new Ticket(
            rs.getString("cliente_id"),
            rs.getString("asunto"),
            rs.getString("descripcion"),
            Prioridad.valueOf(rs.getString("prioridad"))
        );
        t.setId(rs.getInt("id"));
        t.setEstado(Estado.valueOf(rs.getString("estado")));
        t.setAgenteAsignado(rs.getString("agente_id"));
        return t;
    }
}