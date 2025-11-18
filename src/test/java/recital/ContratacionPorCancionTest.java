package recital;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;

public class ContratacionPorCancionTest {

	@Test
	public void testContratarParaUnaCancionOK() throws Exception {
		Rol voz = new Rol("Voz");

		ArtistaBase base = new ArtistaBase("Juan", new ArrayList<>(List.of(voz)), new ArrayList<>());

		ArtistaCandidato ac1 = new ArtistaCandidato("Ana", new ArrayList<>(List.of(voz)), new ArrayList<>(), 1000, 2);

		ArtistaCandidato ac2 = new ArtistaCandidato("Pablo", new ArrayList<>(List.of(voz)), new ArrayList<>(), 800, 2);

		Cancion c = new Cancion("Tema", 3);
		c.agregarRolRequerido(voz, 2); // uno lo cubre base, otro candidato

		Map<Rol, Integer> falt = c.faltantesConBases(List.of(base));
		assertEquals(1, falt.get(voz));

		// Elegimos el más barato
		ArtistaCandidato elegido = Arrays.asList(ac1, ac2).stream()
				.min(Comparator.comparing(a -> a.calcularCostoFinal(List.of(base)))).get();

		Asignacion as = new Asignacion(elegido, c, voz);
		as.setCosto(elegido.calcularCostoFinal(List.of(base)));

		assertEquals(1, c.getAsignaciones().size());
		assertEquals("Pablo", elegido.getNombre());
	}

	@Test
	public void testContratarSinCandidatosDisponibles() {
		Rol voz = new Rol("Voz");

		Cancion c = new Cancion("Tema", 3);
		c.agregarRolRequerido(voz, 1);

		Map<Rol, Integer> falt = c.faltantesConBases(List.of());

		assertEquals(1, falt.get(voz));
	}
}
