public class Direccion {
    private int id;
    private String calle;
    private String ciudad;

    public Direccion(int id, String calle, String ciudad) {
        this.id = id;
        this.calle = calle;
        this.ciudad = ciudad;
    }

    // Getters, Setters y toString
    public int getId() { return id; }
    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    @Override
    public String toString() { return calle + ", " + ciudad; }
}