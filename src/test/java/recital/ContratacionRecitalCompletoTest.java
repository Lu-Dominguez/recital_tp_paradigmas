package recital;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;

public class ContratacionRecitalCompletoTest {

	@Test
	public void testContratarTodoRecitalRolesCubiertos() throws Exception {
		Rol voz = new Rol("Voz");
		Rol guitarra = new Rol("Guitarra");

		ArtistaBase base = new ArtistaBase("Juan", new ArrayList<>(List.of(voz)), new ArrayList<>());

		ArtistaCandidato ac1 = new ArtistaCandidato("Ana", new ArrayList<>(List.of(voz)), new ArrayList<>(), 600, 3);

		ArtistaCandidato ac2 = new ArtistaCandidato("Pablo", new ArrayList<>(List.of(guitarra)), new ArrayList<>(), 900,
				3);

		Cancion c1 = new Cancion("Tema 1", 3);
		Cancion c2 = new Cancion("Tema 2", 3);

		c1.agregarRolRequerido(voz, 1);
		c1.agregarRolRequerido(guitarra, 1);

		c2.agregarRolRequerido(voz, 1);

		List<Cancion> canciones = new ArrayList<>(List.of(c1, c2));
		List<ArtistaCandidato> candidatos = new ArrayList<>(List.of(ac1, ac2));

		for (Cancion cx : canciones) {
			Map<Rol, Integer> falt = cx.faltantesConBases(List.of(base));

			for (var e : falt.entrySet()) {
				Rol rol = e.getKey();
				int cantidad = e.getValue();

				for (int i = 0; i < cantidad; i++) {
					ArtistaCandidato elegido = candidatos.stream().filter(ac -> ac.getRolesHistoricos().contains(rol))
							.findFirst().orElseThrow(() -> new RuntimeException("Sin candidatos"));

					Asignacion a = new Asignacion(elegido, cx, rol);
					a.setCosto(elegido.calcularCostoFinal(List.of(base)));
				}
			}
		}

		assertTrue(c1.faltantesConBases(List.of(base)).isEmpty());
		assertTrue(c2.faltantesConBases(List.of(base)).isEmpty());
	}
}
