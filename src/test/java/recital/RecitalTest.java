package recital;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class RecitalTest {

	@Test
	@DisplayName("Simulación: Elegir candidato más barato")
	void testElegirMasBarato() throws Exception {
		Rol voz = new Rol("Voz");
		Cancion c = new Cancion("T", 3.0);
		c.agregarRolRequerido(voz, 1);

		ArtistaCandidato barato = new ArtistaCandidato("Barato", new ArrayList<>(List.of(voz)), new ArrayList<>(),
				100.0, 5);
		ArtistaCandidato caro = new ArtistaCandidato("Caro", new ArrayList<>(List.of(voz)), new ArrayList<>(), 1000.0,
				5);

		// Simulamos la lógica del Main
		List<ArtistaCandidato> candidatos = List.of(caro, barato);
		ArtistaCandidato elegido = candidatos.stream()
				.min(Comparator.comparing(a -> a.calcularCostoFinal(new ArrayList<>()))).orElseThrow();

		assertEquals("Barato", elegido.getNombre());
	}
}