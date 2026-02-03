import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TelefonoDAO {
    private Connection con;

    public TelefonoDAO() {
        this.con = Conexion.getConnection();
    }

    // Insertar un telefono vinculado a una persona
    public boolean insertar(Telefono t) {
        String sql = "INSERT INTO Telefonos (personaId, telefono) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, t.getPersonaId());
            ps.setString(2, t.getNumero());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Modificar telefono
    public boolean modificar(Telefono t) {
        String sql = "UPDATE Telefonos SET telefono = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, t.getNumero());
            ps.setInt(2, t.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar telefono
    public boolean eliminar(int id) {
        String sql = "DELETE FROM Telefonos WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Obtener telefonos de una persona específica
    public List<Telefono> obtenerPorPersona(int personaId) {
        List<Telefono> lista = new ArrayList<>();
        String sql = "SELECT * FROM Telefonos WHERE personaId = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, personaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Telefono t = new Telefono(
                            rs.getInt("id"),
                            rs.getInt("personaId"),
                            rs.getString("telefono")
                    );
                    lista.add(t);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}