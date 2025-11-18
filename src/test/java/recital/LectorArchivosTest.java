package recital;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;

public class LectorArchivosTest {

	@Test
	public void testCargarArtistasSeparacionBaseYcandidatos() throws Exception {
		List<ArtistaBase> bases = new ArrayList<>();
		List<ArtistaCandidato> candidatos = new ArrayList<>();
		List<Rol> roles = new ArrayList<>();

		LectorArchivos.cargarArtistas("src/test/resources/artistas_test.json",
				"src/test/resources/artistas_base_test.json", bases, candidatos, roles);

		assertEquals(1, bases.size());
		assertEquals(2, candidatos.size());

		assertTrue(bases.stream().anyMatch(a -> a.getNombre().equals("Brian")));
	}

	@Test
	public void testCargarRecitalRolesCorrectos() throws Exception {
		List<Cancion> canciones = new ArrayList<>();
		List<Rol> roles = new ArrayList<>();

		LectorArchivos.cargarRecital("src/test/resources/recital_test.json", canciones, roles);

		assertEquals(2, canciones.size());

		Cancion c = canciones.get(0);
		assertEquals(2, c.getRolesRequeridos().get(new Rol("voz principal")));
	}
}
