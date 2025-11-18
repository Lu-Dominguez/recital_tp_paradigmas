package recital;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EscenariosBordeTest {

	private Rol guitarra;
	private ArtistaBase baseSlash;
	private ArtistaCandidato candidatoAxl;

	@BeforeEach
	void setUp() {
		guitarra = new Rol("Guitarra");

		// Base: Slash en "Guns N Roses"
		baseSlash = new ArtistaBase("Slash", new ArrayList<>(List.of(guitarra)),
				new ArrayList<>(List.of(new Banda("Guns N Roses"))));

		// Candidato: Axl en "guns n roses" (minúsculas)
		// Queremos probar que el sistema detecte que es la misma banda
		candidatoAxl = new ArtistaCandidato("Axl", new ArrayList<>(),
				new ArrayList<>(List.of(new Banda("guns n roses"))), 1000.0, 5);
	}

	@Test
	@DisplayName("Case Insensitive: El descuento aplica aunque el nombre de la banda tenga mayúsculas distintas")
	void testDescuentoBandaCaseInsensitive() {
		// 1000 -> comparte 'guns n roses' vs 'Guns N Roses' -> 500
		double costo = candidatoAxl.calcularCostoFinal(List.of(baseSlash));
		assertEquals(500.0, costo, 0.01, "Debería ignorar mayúsculas/minúsculas en nombres de bandas");
	}

	@Test
	@DisplayName("Descuento NO acumulativo: Compartir banda con 2 bases no baja el precio a 25%")
	void testDescuentoNoAcumulativo() {
		// Creamos otro base de GNR
		ArtistaBase baseDuff = new ArtistaBase("Duff", new ArrayList<>(List.of(guitarra)),
				new ArrayList<>(List.of(new Banda("Guns N Roses"))));

		// Axl comparte banda con Slash Y con Duff
		List<ArtistaBase> muchasBases = List.of(baseSlash, baseDuff);

		double costo = candidatoAxl.calcularCostoFinal(muchasBases);

		// El descuento máximo es 50%, no importa cuántos amigos tenga
		assertEquals(500.0, costo, 0.01, "El descuento debe mantenerse en 50%");
	}

	@Test
	@DisplayName("Roles Repetidos: Si la canción pide 2 Guitarras y la Base cubre 1, falta 1")
	void testRolesRepetidos() {
		Cancion c = new Cancion("Patience", 4.0);
		c.agregarRolRequerido(guitarra, 2); // Pide 2 guitarras

		// Slash cubre 1 guitarra
		var faltantes = c.faltantesConBases(List.of(baseSlash));

		assertEquals(1, faltantes.get(guitarra), "Debería faltar 1 guitarra (2 requeridas - 1 base)");
	}
}