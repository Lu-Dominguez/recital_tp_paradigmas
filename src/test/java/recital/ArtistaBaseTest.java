package recital;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ArtistaBaseTest {

	private Rol guitarra;
	private Banda banda;
	private ArtistaBase artista;

	@BeforeEach
	void setUp() {
		guitarra = new Rol("Guitarra");
		banda = new Banda("BandaTest");

		// Constructor con listas mutables
		artista = new ArtistaBase("Juan", new ArrayList<>(List.of(guitarra)), new ArrayList<>(List.of(banda)));
	}

	@Test
	@DisplayName("Constructor: Lanza excepción si recibe listas nulas")
	void testConstructorNullSafety() {
		assertThrows(IllegalArgumentException.class, () -> new ArtistaBase("Error", null, new ArrayList<>()));

		assertThrows(IllegalArgumentException.class, () -> new ArtistaBase("Error", new ArrayList<>(), null));
	}

	@Test
	@DisplayName("Validar Asignación: Éxito si tiene el rol histórico")
	void testValidarAsignacionOk() {
		Cancion c = new Cancion("Tema", 3.0); // Dummy
		assertDoesNotThrow(() -> artista.validarAsignacion(c, guitarra));
	}

	@Test
	@DisplayName("Validar Asignación: Falla si NO tiene el rol histórico")
	void testValidarAsignacionError() {
		Cancion c = new Cancion("Tema", 3.0);
		Rol bateria = new Rol("Batería");

		Exception ex = assertThrows(Exception.class, () -> artista.validarAsignacion(c, bateria));
		assertTrue(ex.getMessage().contains("no ha ocupado el rol"));
	}

	@Test
	@DisplayName("Agregar Rol: Actualiza la lista de conocimientos")
	void testAgregarRol() throws Exception {
		Rol nuevo = new Rol("Teclado");
		artista.agregarRol(nuevo);

		assertTrue(artista.getRolesHistoricos().contains(nuevo));

		// Ahora debería pasar la validación
		Cancion c = new Cancion("Tema", 3.0);
		assertDoesNotThrow(() -> artista.validarAsignacion(c, nuevo));
	}

	@Test
	@DisplayName("Agregar Banda: Actualiza el historial")
	void testAgregarBanda() {
		Banda nueva = new Banda("NuevaBanda");
		artista.agregarBanda(nueva);

		assertTrue(artista.getBandasHistoricas().contains(nueva));
	}
}