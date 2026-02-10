import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    //  Datos de la conexion
    private static final String URL = "jdbc:mysql://localhost:3306/Agenda";
    private static final String USER = "root";
    private static final String PASSWORD = "qwertyui";

    private static Connection instance;

    public static Connection getConnection() {
        try {
            // Verifica si la conexiOn está cerrada o es nula para abrirla
            if (instance == null || instance.isClosed()) {
                // Carga el driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                instance = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexion exitosa a la base de datos!");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
            e.printStackTrace();
        }
        return instance;
    }

    //  Metodo para cerrar la conexion
    public static void cerrarConexion() {
        if (instance != null) {
            try {
                instance.close();
                instance = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}