package recital;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.nio.file.*;
import java.util.*;

public class PersistenciaTest {

	@Test
	public void testGuardarYCargarEstado() throws Exception {

		// --- Crear objetos ---
		Rol voz = new Rol("Voz");
		Rol piano = new Rol("Piano");

		ArtistaBase base = new ArtistaBase("Carlos", new ArrayList<>(List.of(voz)), new ArrayList<>());

		ArtistaCandidato ac = new ArtistaCandidato("Ana", new ArrayList<>(List.of(voz)), new ArrayList<>(), 1000, 3);
		ac.entrenarRol(piano);

		Cancion c = new Cancion("Tema1", 3);
		c.agregarRolRequerido(voz, 1);
		Asignacion as = new Asignacion(ac, c, voz);
		as.setCosto(500);
		as.setDescuentos(500);

		// --- Guardar ---
		String ruta = "src/test/resources/estado_guardado.json";
		Persistencia.guardarEstado(ruta, List.of(base), List.of(ac), List.of(c));

		assertTrue(Files.exists(Path.of(ruta)));

		// --- Cargar ---
		List<ArtistaBase> bases = new ArrayList<>();
		List<ArtistaCandidato> candidatos = new ArrayList<>();
		List<Rol> roles = new ArrayList<>();
		List<Cancion> canciones = new ArrayList<>();

		Persistencia.cargarEstado(ruta, bases, candidatos, roles, canciones);

		assertEquals(1, bases.size());
		assertEquals(1, candidatos.size());
		assertEquals(1, canciones.size());

		ArtistaCandidato cargado = candidatos.get(0);
		assertEquals("Ana", cargado.getNombre());
		assertEquals(1, cargado.getRolesEntrenados().size());
	}
}
