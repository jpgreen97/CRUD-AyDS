import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonaDAO {
    private Connection con;

    public PersonaDAO() {
        this.con = Conexion.getConnection();
    }

    // 1. INSERTAR (Alta)
    public boolean insertar(Persona p) {
        String sql = "INSERT INTO Personas (nombre, direccion) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDireccion());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. MODIFICAR
    public boolean modificar(Persona p) {
        String sql = "UPDATE Personas SET nombre = ?, direccion = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDireccion());
            ps.setInt(3, p.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. ELIMINAR (Baja)
    public boolean eliminar(int id) {
        String sql = "DELETE FROM Personas WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. CONSULTAR (Obtener todas para la tabla)
    public List<Persona> obtenerTodas() {
        List<Persona> lista = new ArrayList<>();
        String sql = "SELECT * FROM Personas";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Persona p = new Persona(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("direccion")
                );
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}