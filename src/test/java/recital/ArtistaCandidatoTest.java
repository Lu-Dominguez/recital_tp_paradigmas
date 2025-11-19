package recital;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ArtistaCandidatoTest {

	private Rol voz, guitarra;
	private ArtistaBase baseSlash;
	private ArtistaCandidato axl, candidatoNuevo;

	@BeforeEach
	void setUp() {
		voz = new Rol("Voz");
		guitarra = new Rol("Guitarra");

		// Base: Slash en "Guns N Roses"
		baseSlash = new ArtistaBase("Slash", new ArrayList<>(List.of(guitarra)),
				new ArrayList<>(List.of(new Banda("Guns N Roses"))));

		// Candidato: Axl en "guns n roses" (minúsculas) para probar Case Insensitive
		axl = new ArtistaCandidato("Axl", new ArrayList<>(List.of(voz)),
				new ArrayList<>(List.of(new Banda("guns n roses"))), 1000.0, 5);

		candidatoNuevo = new ArtistaCandidato("Nuevo", new ArrayList<>(List.of(voz)), new ArrayList<>(), 500.0, 1);
	}

	@Test
	@DisplayName("Calcula costo con 50% descuento si comparte banda (Case Insensitive)")
	void testCostoConDescuento() {
		// 1000 -> comparte banda -> 500
		assertEquals(500.0, axl.calcularCostoFinal(List.of(baseSlash)), 0.01);
	}

	@Test
	@DisplayName("Costo completo si no comparte bandas")
	void testCostoSinDescuento() {
		assertEquals(500.0, candidatoNuevo.calcularCostoFinal(List.of(baseSlash)), 0.01);
	}

	@Test
	@DisplayName("El descuento NO es acumulativo (max 50%)")
	void testDescuentoNoAcumulativo() {
		ArtistaBase otroBase = new ArtistaBase("Duff", new ArrayList<>(),
				new ArrayList<>(List.of(new Banda("Guns N Roses"))));

		// Axl comparte con Slash Y con Duff. El precio debe ser 500, no 250.
		assertEquals(500.0, axl.calcularCostoFinal(List.of(baseSlash, otroBase)), 0.01);
	}

	@Test
	@DisplayName("Entrenamiento aumenta costo base un 50% por rol")
	void testEntrenamiento() {
		// 500 + 50% = 750
		candidatoNuevo.entrenarRol(guitarra);
		assertEquals(750.0, candidatoNuevo.calcularCostoExtraEntrenado(), 0.01);
	}

	@Test
	@DisplayName("No puede entrenar si ya está contratado en el recital")
	void testEntrenamientoBloqueado() throws Exception {
		Cancion c = new Cancion("T", 3.0);
		c.agregarRolRequerido(voz, 1);
		new Asignacion(axl, c, voz);

		Recital r = new Recital("R", List.of(c), List.of(), List.of(axl));

		assertThrows(Exception.class, () -> axl.entrenar(r, guitarra));
	}
}