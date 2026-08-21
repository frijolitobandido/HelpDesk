package com.helpdesk.persistencia;

import com.helpdesk.modelo.Agente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AgenteRepositorio {

    public void insertar(Agente agente) {
        String sql = "INSERT INTO agentes (id, nombre, tickets_asignados, activo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = ConexionBD.obtenerConexion().prepareStatement(sql)) {
            ps.setString(1, agente.getId());
            ps.setString(2, agente.getNombre());
            ps.setInt(3, agente.getTicketsAsignados());
            ps.setInt(4, agente.isActivo() ? 1 : 0);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al insertar agente: " + e.getMessage());
        }
    }

    public void actualizar(Agente agente) {
        String sql = "UPDATE agentes SET tickets_asignados = ?, activo = ? WHERE id = ?";
        try (PreparedStatement ps = ConexionBD.obtenerConexion().prepareStatement(sql)) {
            ps.setInt(1, agente.getTicketsAsignados());
            ps.setInt(2, agente.isActivo() ? 1 : 0);
            ps.setString(3, agente.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar agente: " + e.getMessage());
        }
    }

    public List<Agente> obtenerTodos() {
        List<Agente> agentes = new ArrayList<>();
        String sql = "SELECT * FROM agentes";
        try (PreparedStatement ps = ConexionBD.obtenerConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Agente a = new Agente(rs.getString("id"), rs.getString("nombre"));
                int cargaGuardada = rs.getInt("tickets_asignados");
                for (int i = 0; i < cargaGuardada; i++) {
                    a.incrementarCarga(); // reconstruye la carga guardada
                }
                a.setActivo(rs.getInt("activo") == 1);
                agentes.add(a);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener agentes: " + e.getMessage());
        }
        return agentes;
    }

    public boolean existeAlgunAgente() {
        String sql = "SELECT COUNT(*) as total FROM agentes";
        try (PreparedStatement ps = ConexionBD.obtenerConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar agentes: " + e.getMessage());
        }
        return false;
    }
}