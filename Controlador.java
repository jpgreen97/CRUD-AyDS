import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TabPane;

public class Controlador {

    // --- SECCIÓN PERSONAS (IZQUIERDA) ---
    @FXML private TableView<Persona> tablaPersonas;
    @FXML private TableColumn<Persona, String> colNombre;
    @FXML private TextField txtNombre;
    @FXML private TabPane tabPaneDetalles;

    // Pestaña Teléfonos
    @FXML private TableView<Telefono> tablaTelefonos;
    @FXML private TableColumn<Telefono, String> colNumero;
    @FXML private TextField txtTelefono;

    // Pestaña Direcciones
    @FXML private TableView<Direccion> tablaDirecciones;
    @FXML private TableColumn<Direccion, String> colCalle;
    @FXML private TableColumn<Direccion, String> colCiudad;
    @FXML private TextField txtCalle;
    @FXML private TextField txtCiudad;

    // --- LÓGICA ---
    private PersonaDAO personaDAO;
    private TelefonoDAO telefonoDAO;
    private DireccionDAO direccionDAO; // NUEVO

    private ObservableList<Persona> listaPersonas;
    private ObservableList<Telefono> listaTelefonos;
    private ObservableList<Direccion> listaDirecciones; // NUEVO

    private Persona personaSeleccionada;
    private Telefono telefonoSeleccionado;

    @FXML
    public void initialize() {
        personaDAO = new PersonaDAO();
        telefonoDAO = new TelefonoDAO();
        direccionDAO = new DireccionDAO(); // Instanciar nuevo DAO

        configurarTablas();
        cargarPersonas();

        // Listener: Selección de PERSONA
        tablaPersonas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            personaSeleccionada = newSelection;
            if (personaSeleccionada != null) {
                // Rellenar campos
                txtNombre.setText(personaSeleccionada.getNombre());

                // Habilitar el panel de pestañas
                tabPaneDetalles.setDisable(false);

                // Cargar los datos de las pestañas
                cargarTelefonos(personaSeleccionada.getId());
                cargarDirecciones(personaSeleccionada.getId()); // NUEVO
            } else {
                tabPaneDetalles.setDisable(true);
                limpiarCamposPersona();
            }
        });

        // Listener: Selección de TELÉFONO
        tablaTelefonos.getSelectionModel().selectedItemProperty().addListener((obs, old, newSelection) -> {
            telefonoSeleccionado = newSelection;
            if (telefonoSeleccionado != null) {
                txtTelefono.setText(telefonoSeleccionado.getNumero());
            }
        });

    }

    private void configurarTablas() {
        // Personas
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        // Teléfonos
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));

        // Direcciones
        colCalle.setCellValueFactory(new PropertyValueFactory<>("calle"));
        colCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
    }

    // ================= GESTIÓN DE PERSONAS =================

    private void cargarPersonas() {
        listaPersonas = FXCollections.observableArrayList(personaDAO.obtenerTodas());
        tablaPersonas.setItems(listaPersonas);
    }

    @FXML
    protected void btnAgregarPersona() {
        String nombre = txtNombre.getText();

        if (nombre.isEmpty()) return;

        Persona p = new Persona();
        p.setNombre(nombre);

        if (personaDAO.insertar(p)) {
            cargarPersonas();
            limpiarCamposPersona();
        }
    }

    @FXML
    protected void btnModificarPersona() {
        if (personaSeleccionada == null) return;

        personaSeleccionada.setNombre(txtNombre.getText());
        // personaSeleccionada.setDireccion(...) // YA NO SE USA

        if (personaDAO.modificar(personaSeleccionada)) {
            cargarPersonas();
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

    // ================= GESTIÓN DE TELÉFONOS =================

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

    // ================= GESTIÓN DE DIRECCIONES (NUEVO) =================

    private void cargarDirecciones(int personaId) {
        listaDirecciones = FXCollections.observableArrayList(direccionDAO.obtenerPorPersona(personaId));
        tablaDirecciones.setItems(listaDirecciones);
    }

    @FXML
    protected void btnAgregarDireccion() {
        if (personaSeleccionada == null) return;

        String calle = txtCalle.getText();
        String ciudad = txtCiudad.getText();

        if (calle.isEmpty() || ciudad.isEmpty()) return;

        Direccion d = new Direccion(0, calle, ciudad);

        if (direccionDAO.agregarDireccion(d, personaSeleccionada.getId())) {
            cargarDirecciones(personaSeleccionada.getId());
            txtCalle.clear();
            txtCiudad.clear();
        }
    }

    // ================= UTILIDADES =================

    private void limpiarCamposPersona() {
        txtNombre.clear();
        // txtDireccion.clear(); // YA NO EXISTE
        if (listaTelefonos != null) listaTelefonos.clear();
        if (listaDirecciones != null) listaDirecciones.clear();
    }
}