package recital;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;

public class ArtistaCandidatoTest {

	@Test
	public void testEntrenarRolOK() throws Exception {
		Rol voz = new Rol("Voz");
		Rol piano = new Rol("Piano");

		ArtistaCandidato ac = new ArtistaCandidato("Ana", new ArrayList<>(List.of(voz)), new ArrayList<>(), 1000, 3);

		Recital r = new Recital("Recital", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

		ac.entrenar(r, piano);

		assertTrue(ac.getRolesEntrenados().contains(piano));
	}

	@Test
	public void testEntrenarErrorYaAsignado() throws Exception {
		Rol voz = new Rol("Voz");
		Rol piano = new Rol("Piano");

		ArtistaCandidato ac = new ArtistaCandidato("Ana", new ArrayList<>(List.of(voz)), new ArrayList<>(), 1000, 3);

		Cancion c = new Cancion("Tema1", 3.0);
		c.agregarRolRequerido(voz, 1);

		new Asignacion(ac, c, voz); // ya asignado

		Recital r = new Recital("Recital", List.of(c), new ArrayList<>(), new ArrayList<>(List.of(ac)));

		Exception ex = assertThrows(Exception.class, () -> ac.entrenar(r, piano));

		assertTrue(ex.getMessage().contains("ya fue contratado"));
	}

	@Test
	public void testCostoConEntrenamientoYDescuento() {
		Rol voz = new Rol("Voz");
		Rol piano = new Rol("Piano");
		Banda b = new Banda("Banda");

		ArtistaBase base = new ArtistaBase("Juan", new ArrayList<>(List.of(voz)), new ArrayList<>(List.of(b)));

		ArtistaCandidato ac = new ArtistaCandidato("Ana", new ArrayList<>(List.of(voz)), new ArrayList<>(List.of(b)), // comparte
																														// banda
																														// →
																														// descuento
				1000, 3);

		ac.entrenarRol(piano); // +50%

		double costo = ac.calcularCostoFinal(List.of(base));

		// costo base 1000 → entrenado 1000 * 1.5 = 1500 → descuento 50% = 750
		assertEquals(750, costo);
	}
}
