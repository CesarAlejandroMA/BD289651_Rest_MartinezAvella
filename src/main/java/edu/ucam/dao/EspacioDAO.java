package edu.ucam.dao;

import edu.ucam.model.Espacio;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EspacioDAO {
    private DataSource ds;

    public EspacioDAO() {
        try {
            Context init = new InitialContext();
            ds = (DataSource) init.lookup("java:comp/env/jdbc/rest");

        } catch (NamingException e) {
            throw new RuntimeException("Error al inicializar DataSource", e);
        }
    }

    private Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    /**
     * Recupera todos los espacios de la base de datos.
     */
    public List<Espacio> findAll() {
        List<Espacio> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion FROM Espacio";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Espacio e = new Espacio(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("descripcion")
                );
                lista.add(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    // Próximos métodos: findById, create, update, delete...
    
    //Create
    public Espacio create(Espacio e) {
        String sql = "INSERT INTO Espacio (nombre, descripcion) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, e.getName());
            ps.setString(2, e.getDescription());
            int affected = ps.executeUpdate();

            if (affected == 0) {
                throw new SQLException("Crear espacio falló, no se insertó fila.");
            }

            // Recuperar el ID auto-generado
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    e.setId(keys.getInt(1));
                } else {
                    throw new SQLException("Crear espacio falló, no se obtuvo ID.");
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al crear Espacio", ex);
        }

        return e;
    }
    
    //Buscar por ID
    /**
     * Recupera un Espacio por su clave primaria.
     * @param id el identificador del espacio
     * @return el objeto Espacio si existe, o null si no se encuentra
     */
    public Espacio findById(int id) {
        String sql = "SELECT id, nombre, descripcion FROM Espacio WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Construimos y devolvemos el Espacio
                    return new Espacio(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                    );
                } else {
                    // No existe ese id
                    return null;
                }
            }
        } catch (SQLException ex) {
            // Opcional: lanzar RuntimeException para que el servicio lo convierta en 500
            throw new RuntimeException("Error al buscar Espacio con id=" + id, ex);
        }
    }
    
    /**
     * Actualiza un Espacio ya existente.
     * @param e objeto con id + nuevos valores de nombre y descripción
     * @return true si se actualizó alguna fila, false si no existía ese id
     */
    public boolean update(Espacio e) {
        String sql = "UPDATE Espacio SET nombre = ?, descripcion = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, e.getName());
            ps.setString(2, e.getDescription());
            ps.setInt(3, e.getId());
            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException ex) {
            throw new RuntimeException("Error al actualizar Espacio con id=" + e.getId(), ex);
        }
    }
    
    public boolean delete(int id) {
        String sql = "DELETE FROM Espacio WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException ex) {
            throw new RuntimeException("Error al eliminar Espacio con id=" + id, ex);
        }
    }
}
