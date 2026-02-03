
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;


public class Controlador {

    @FXML private TableView<Persona> tablaPersonas;
    @FXML private TableColumn<Persona, String> colNombre;
    @FXML private TableColumn<Persona, String> colDireccion;
    @FXML private TextField txtNombre;
    @FXML private TextField txtDireccion;
    @FXML private VBox boxTelefonos; // Para habilitar/deshabilitar
    @FXML private TableView<Telefono> tablaTelefonos;
    @FXML private TableColumn<Telefono, String> colNumero;
    @FXML private TextField txtTelefono;

    private PersonaDAO personaDAO;
    private TelefonoDAO telefonoDAO;
    private ObservableList<Persona> listaPersonas;
    private ObservableList<Telefono> listaTelefonos;

    private Persona personaSeleccionada;
    private Telefono telefonoSeleccionado;

    @FXML
    public void initialize() {
        personaDAO = new PersonaDAO();
        telefonoDAO = new TelefonoDAO();

        configurarTablas();
        cargarPersonas();

        // Listener: Cuando se selecciona una persona, carga sus telefonos
        tablaPersonas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            personaSeleccionada = newSelection;
            if (personaSeleccionada != null) {
                // Rellenar campos
                txtNombre.setText(personaSeleccionada.getNombre());
                txtDireccion.setText(personaSeleccionada.getDireccion());

                // Activar panel de telefonos y cargar datos
                boxTelefonos.setDisable(false);
                cargarTelefonos(personaSeleccionada.getId());
            } else {
                boxTelefonos.setDisable(true);
                limpiarCamposPersona();
            }
        });

        // SelecciOn de telefono
        tablaTelefonos.getSelectionModel().selectedItemProperty().addListener((obs, old, newSelection) -> {
            telefonoSeleccionado = newSelection;
            if (telefonoSeleccionado != null) {
                txtTelefono.setText(telefonoSeleccionado.getNumero());
            }
        });
    }

    private void configurarTablas() {
        // Enlazar columnas con los atributos de la clase (Getters)
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
    }

    // GESTION DE LAS PERSONAS

    private void cargarPersonas() {
        listaPersonas = FXCollections.observableArrayList(personaDAO.obtenerTodas());
        tablaPersonas.setItems(listaPersonas);
    }

    @FXML
    protected void btnAgregarPersona() {
        String nombre = txtNombre.getText();
        String direccion = txtDireccion.getText();

        if (nombre.isEmpty()) return;

        Persona p = new Persona(0, nombre, direccion);
        if (personaDAO.insertar(p)) {
            cargarPersonas();
            limpiarCamposPersona();
        }
    }

    @FXML
    protected void btnModificarPersona() {
        if (personaSeleccionada == null) return;

        personaSeleccionada.setNombre(txtNombre.getText());
        personaSeleccionada.setDireccion(txtDireccion.getText());

        if (personaDAO.modificar(personaSeleccionada)) {
            cargarPersonas(); // Refrescar tabla
            tablaPersonas.refresh();
        }
    }

    @FXML
    protected void btnEliminarPersona() {
        if (personaSeleccionada == null) return;

        if (personaDAO.eliminar(personaSeleccionada.getId())) {
            cargarPersonas();
            limpiarCamposPersona();
        }
    }

    // GESTION DE TELEFONOS

    private void cargarTelefonos(int personaId) {
        listaTelefonos = FXCollections.observableArrayList(telefonoDAO.obtenerPorPersona(personaId));
        tablaTelefonos.setItems(listaTelefonos);
    }

    @FXML
    protected void btnAgregarTelefono() {
        if (personaSeleccionada == null) return;

        String numero = txtTelefono.getText();
        if (numero.isEmpty()) return;

        Telefono t = new Telefono(0, personaSeleccionada.getId(), numero);
        if (telefonoDAO.insertar(t)) {
            cargarTelefonos(personaSeleccionada.getId());
            txtTelefono.clear();
        }
    }

    @FXML
    protected void btnModificarTelefono() {
        if (telefonoSeleccionado == null) return;

        telefonoSeleccionado.setNumero(txtTelefono.getText());
        if (telefonoDAO.modificar(telefonoSeleccionado)) {
            cargarTelefonos(personaSeleccionada.getId());
            txtTelefono.clear();
        }
    }

    @FXML
    protected void btnEliminarTelefono() {
        if (telefonoSeleccionado == null) return;

        if (telefonoDAO.eliminar(telefonoSeleccionado.getId())) {
            cargarTelefonos(personaSeleccionada.getId());
            txtTelefono.clear();
        }
    }

    private void limpiarCamposPersona() {
        txtNombre.clear();
        txtDireccion.clear();
        listaTelefonos.clear();
    }
}

