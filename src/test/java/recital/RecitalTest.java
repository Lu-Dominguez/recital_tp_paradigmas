package recital;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;

public class RecitalTest {

	@Test
	public void testFaltantesGlobales() {
		Rol voz = new Rol("Voz");

		Cancion c1 = new Cancion("A", 3);
		Cancion c2 = new Cancion("B", 3);

		c1.agregarRolRequerido(voz, 1);
		c2.agregarRolRequerido(voz, 2);

		Recital r = new Recital("Test", List.of(c1, c2), new ArrayList<>(), new ArrayList<>());

		Map<Rol, Integer> falt = r.faltantesGlobales();

		assertEquals(3, falt.get(voz));
	}

	@Test
	public void testCostoTotal() throws Exception {
		Rol voz = new Rol("Voz");
		ArtistaCandidato ac = new ArtistaCandidato("Ana", new ArrayList<>(List.of(voz)), new ArrayList<>(), 1000, 2);

		Cancion c = new Cancion("Tema", 3);
		c.agregarRolRequerido(voz, 1);

		Asignacion a = new Asignacion(ac, c, voz);
		a.setCosto(1000);

		Recital r = new Recital("Test", List.of(c), new ArrayList<>(), List.of(ac));

		assertEquals(1000, r.costoTotal());
	}
}
