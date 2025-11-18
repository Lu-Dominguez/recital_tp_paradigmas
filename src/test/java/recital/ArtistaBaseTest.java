package recital;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;

public class ArtistaBaseTest {

	@Test
	public void testValidarAsignacionOK() throws Exception {
		Rol voz = new Rol("Voz");
		ArtistaBase base = new ArtistaBase("Juan", new ArrayList<>(List.of(voz)), new ArrayList<>());

		Cancion c = new Cancion("Tema", 3.0);
		c.agregarRolRequerido(voz, 1);

		assertDoesNotThrow(() -> base.validarAsignacion(c, voz));
	}

	@Test
	public void testValidarAsignacionErrorRolNoHistorico() {
		Rol voz = new Rol("Voz");
		Rol guit = new Rol("Guitarra");

		ArtistaBase base = new ArtistaBase("Juan", new ArrayList<>(List.of(voz)), new ArrayList<>());

		Cancion c = new Cancion("Tema", 3.0);
		c.agregarRolRequerido(guit, 1);

		Exception ex = assertThrows(Exception.class, () -> base.validarAsignacion(c, guit));

		assertTrue(ex.getMessage().contains("no ha ocupado"));
	}
}
