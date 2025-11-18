package recital;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class AsignacionTest {

	private Rol voz, piano;
	private ArtistaCandidato candidato;
	private Cancion cancion;

	@BeforeEach
	void setUp() {
		voz = new Rol("Voz");
		piano = new Rol("Piano");
		candidato = new ArtistaCandidato("Cantante", new ArrayList<>(List.of(voz)), new ArrayList<>(), 500.0, 1); // Max
																													// 1
																													// canción
		cancion = new Cancion("Tema", 3.0);
	}

	@Test
	@DisplayName("Asignación exitosa vincula artista y canción")
	void testCrearAsignacion() throws Exception {
		cancion.agregarRolRequerido(voz, 1);
		Asignacion a = new Asignacion(candidato, cancion, voz);

		assertEquals(candidato, a.getArtista());
		assertTrue(cancion.getAsignaciones().contains(a));
	}

	@Test
	@DisplayName("Falla si el artista no conoce el rol")
	void testRolDesconocido() {
		cancion.agregarRolRequerido(piano, 1);
		assertThrows(Exception.class, () -> new Asignacion(candidato, cancion, piano));
	}

	@Test
	@DisplayName("Falla si supera el tope de canciones")
	void testTopeCanciones() throws Exception {
		// 1. Llenamos su cupo (Max 1)
		cancion.agregarRolRequerido(voz, 1);
		new Asignacion(candidato, cancion, voz);

		// 2. Intentamos asignar a otra canción
		Cancion c2 = new Cancion("Otra", 3.0);
		c2.agregarRolRequerido(voz, 1);

		Exception ex = assertThrows(Exception.class, () -> new Asignacion(candidato, c2, voz));
		assertTrue(ex.getMessage().contains("máximo de canciones"));
	}
}