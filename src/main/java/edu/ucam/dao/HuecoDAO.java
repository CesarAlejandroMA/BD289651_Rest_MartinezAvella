package edu.ucam.dao;

import edu.ucam.model.Huecos;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
public class HuecoDAO {

    private DataSource ds;
    
    public HuecoDAO() {
        try {
            ds = (DataSource) new InitialContext()
                    .lookup("java:comp/env/jdbc/rest");
        } catch (NamingException e) {
            throw new RuntimeException("Error al inicializar DataSource", e);
        }
    }
    
    private Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
    
    /**
     * Recupera todos los huecos asociados a un espacio.
     */
    public List<Huecos> findByEspacio(int espacioId) {
        String sql = "SELECT id, espacio_id, inicio, fin "
                   + "FROM Hueco WHERE espacio_id = ?";
        List<Huecos> lista = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, espacioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Convertir Timestamp → LocalDateTime
                    LocalDateTime ini = rs.getTimestamp("inicio").toLocalDateTime();
                    LocalDateTime fin = rs.getTimestamp("fin").toLocalDateTime();

                    Huecos h = new Huecos(
                        rs.getInt("id"),
                        rs.getInt("espacio_id"),
                        ini,
                        fin
                    );
                    lista.add(h);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(
                "Error al leer huecos para espacio " + espacioId, ex
            );
        }
        return lista;
    }
    
    /**
     * Inserta un nuevo hueco y devuelve el objeto con ID.
     */
    public Huecos create(Huecos h) {
        String sql = "INSERT INTO Hueco (espacio_id, inicio, fin) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, h.getSpaceId());
            ps.setTimestamp(2, Timestamp.valueOf(h.getStartTime()));
            ps.setTimestamp(3, Timestamp.valueOf(h.getEndTime()));
            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new SQLException("No se insertó hueco");
            }
            try (ResultSet rk = ps.getGeneratedKeys()) {
                if (rk.next()) {
                    h.setId(rk.getInt(1));
                } else {
                    throw new SQLException("No se obtuvo ID generado");
                }
            }
            return h;
        } catch (SQLException ex) {
            throw new RuntimeException("Error al crear hueco en espacio " + h.getSpaceId(), ex);
        }
    }
    
    public boolean hasOverlap(int espacioId, LocalDateTime start, LocalDateTime end) {
        String sql = "SELECT COUNT(*) FROM Hueco "
                   + "WHERE espacio_id = ? "
                   + "  AND inicio < ? "      // inicio existente antes de nuevo fin
                   + "  AND fin > ?";         // fin existente después de nuevo inicio
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, espacioId);
            ps.setTimestamp(2, Timestamp.valueOf(end));
            ps.setTimestamp(3, Timestamp.valueOf(start));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al comprobar solapamientos", ex);
        }
        return false;
    }
    
    /**
     * Verifica solapamiento excluyendo el hueco con id = excludeId.
     */
    public boolean hasOverlapExcluding(int espacioId,
                                       LocalDateTime start,
                                       LocalDateTime end,
                                       int excludeId) {
        String sql = "SELECT COUNT(*) FROM Hueco "
                   + "WHERE espacio_id = ? "
                   + "  AND inicio < ? "
                   + "  AND fin > ? "
                   + "  AND id <> ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, espacioId);
            ps.setTimestamp(2, Timestamp.valueOf(end));
            ps.setTimestamp(3, Timestamp.valueOf(start));
            ps.setInt(4, excludeId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error comprobando solapamiento excluyendo " + excludeId, ex);
        }
    }

    /**
     * Actualiza un hueco existente.
     */
    public boolean update(Huecos h) {
        String sql = "UPDATE Hueco SET inicio = ?, fin = ? "
                   + "WHERE id = ? AND espacio_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(h.getStartTime()));
            ps.setTimestamp(2, Timestamp.valueOf(h.getEndTime()));
            ps.setInt(3, h.getId());
            ps.setInt(4, h.getSpaceId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Error al actualizar hueco " + h.getId(), ex);
        }
    }
    
    public boolean delete(int espacioId, int huecoId) {
        String sql = "DELETE FROM Hueco WHERE espacio_id = ? AND id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, espacioId);
            ps.setInt(2, huecoId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Error al eliminar hueco " + huecoId, ex);
        }
    }
}
