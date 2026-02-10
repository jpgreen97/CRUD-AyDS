import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DireccionDAO {
    private Connection con;

    public DireccionDAO() {
        this.con = Conexion.getConnection();
    }

    // Metodo para AGREGAR una dirección y ASOCIARLA a una persona
    public boolean agregarDireccion(Direccion d, int personaId) {
        String sqlDireccion = "INSERT INTO Direcciones (calle, ciudad) VALUES (?, ?)";
        String sqlPuente = "INSERT INTO PersonaDireccion (persona_id, direccion_id) VALUES (?, ?)";

        try {
            //  Inserta la dirección
            // Usa RETURN_GENERATED_KEYS para saber qué ID le puso MySQL
            PreparedStatement ps = con.prepareStatement(sqlDireccion, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, d.getCalle());
            ps.setString(2, d.getCiudad());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int idDireccionNueva = 0;
            if (rs.next()) {
                idDireccionNueva = rs.getInt(1);
            }

            // Inserta el vínculo en la tabla puente
            PreparedStatement psPuente = con.prepareStatement(sqlPuente);
            psPuente.setInt(1, personaId);
            psPuente.setInt(2, idDireccionNueva);
            psPuente.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Obtener todas las direcciones de UNA persona
    public List<Direccion> obtenerPorPersona(int personaId) {
        List<Direccion> lista = new ArrayList<>();
        // Hacemos un JOIN para cruzar las 3 tablas
        String sql = "SELECT d.* FROM Direcciones d " +
                "JOIN PersonaDireccion pd ON d.id = pd.direccion_id " +
                "WHERE pd.persona_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, personaId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Direccion(rs.getInt("id"), rs.getString("calle"), rs.getString("ciudad")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}