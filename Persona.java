public class Persona {
    private int id;
    private String nombre;
    private String direccion;

    // Constructor vacio
    public Persona() {
    }

    // Constructor con datos
    public Persona(int id, String nombre, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    //  ComboBox
    @Override
    public String toString() {
        return nombre;
    }
}