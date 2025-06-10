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
}
