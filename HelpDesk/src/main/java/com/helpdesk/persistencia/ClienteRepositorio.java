package com.helpdesk.persistencia;

import com.helpdesk.modelo.Cliente;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteRepositorio {

    public void insertar(Cliente cliente) {
        String sql = "INSERT INTO clientes (id, nombre, correo) VALUES (?, ?, ?)";
        try (PreparedStatement ps = ConexionBD.obtenerConexion().prepareStatement(sql)) {
            ps.setString(1, cliente.getId());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getCorreo());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al insertar cliente: " + e.getMessage());
        }
    }

    public Cliente buscarPorId(String id) {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        try (PreparedStatement ps = ConexionBD.obtenerConexion().prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Cliente(rs.getString("id"), rs.getString("nombre"), rs.getString("correo"));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar cliente: " + e.getMessage());
        }
        return null;
    }

    public boolean existe(String id) {
        return buscarPorId(id) != null;
    }
}