package recital;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;

public class CancionTest {

	@Test
	public void testRolesFaltantesSinBases() {
		Rol voz = new Rol("Voz");
		Cancion c = new Cancion("Tema", 3.0);
		c.agregarRolRequerido(voz, 2);

		Map<Rol, Integer> falt = c.faltantesConBases(List.of());

		assertEquals(2, falt.get(voz));
	}

	@Test
	public void testRolesFaltantesConBases() {
		Rol voz = new Rol("Voz");
		Cancion c = new Cancion("Tema", 3.0);
		c.agregarRolRequerido(voz, 1);

		ArtistaBase base = new ArtistaBase("Juan", new ArrayList<>(List.of(voz)), new ArrayList<>());

		Map<Rol, Integer> falt = c.faltantesConBases(List.of(base));

		assertTrue(falt.isEmpty()); // base cubre el rol
	}

	@Test
	public void testCancelarAsignaciones() throws Exception {
		Rol voz = new Rol("Voz");

		ArtistaCandidato ac = new ArtistaCandidato("Ana", new ArrayList<>(List.of(voz)), new ArrayList<>(), 1000, 3);

		Cancion c = new Cancion("Tema", 3.0);
		c.agregarRolRequerido(voz, 1);

		new Asignacion(ac, c, voz);

		int removed = c.removerAsignacionesDe(ac);

		assertEquals(1, removed);
		assertEquals(0, c.getAsignaciones().size());
	}
}
