package recital;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;

public class AsignacionTest {

	@Test
	public void testAsignacionCorrecta() throws Exception {
		Rol voz = new Rol("Voz");
		ArtistaCandidato ac = new ArtistaCandidato("Ana", new ArrayList<>(List.of(voz)), new ArrayList<>(), 1000, 2);

		Cancion c = new Cancion("Tema", 3.0);
		c.agregarRolRequerido(voz, 1);

		Asignacion a = new Asignacion(ac, c, voz);

		assertEquals(1, c.getAsignaciones().size());
		assertEquals(ac, a.getArtista());
	}

	@Test
	public void testAsignacionDuplicadaError() throws Exception {
		Rol voz = new Rol("Voz");
		ArtistaCandidato ac = new ArtistaCandidato("Ana", new ArrayList<>(List.of(voz)), new ArrayList<>(), 1000, 2);

		Cancion c = new Cancion("Tema", 3.0);
		c.agregarRolRequerido(voz, 2);

		new Asignacion(ac, c, voz);

		Exception ex = assertThrows(Exception.class, () -> new Asignacion(ac, c, voz));

		assertTrue(ex.getMessage().contains("ya tiene asignado un rol"));
	}

	@Test
	public void testAsignacionSuperaMaxCanciones() throws Exception {
		Rol voz = new Rol("Voz");

		ArtistaCandidato ac = new ArtistaCandidato("Ana", new ArrayList<>(List.of(voz)), new ArrayList<>(), 1000, 1 // sólo
																													// puede
																													// 1
																													// canción
		);

		Cancion c1 = new Cancion("Tema1", 3.0);
		Cancion c2 = new Cancion("Tema2", 3.0);

		c1.agregarRolRequerido(voz, 1);
		c2.agregarRolRequerido(voz, 1);

		new Asignacion(ac, c1, voz);

		Exception ex = assertThrows(Exception.class, () -> new Asignacion(ac, c2, voz));

		assertTrue(ex.getMessage().contains("máximo de canciones"));
	}
}
