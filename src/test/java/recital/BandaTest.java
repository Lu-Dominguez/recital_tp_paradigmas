package recital;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BandaTest {

	@Test
	@DisplayName("Constructor: Asigna correctamente el nombre")
	void testConstructor() {
		Banda b = new Banda("Queen");
		assertEquals("Queen", b.getNombre());
	}

	@Test
	@DisplayName("Equals: Detecta igualdad ignorando mayúsculas (Case Insensitive)")
	void testEqualsCaseInsensitive() {
		Banda b1 = new Banda("Guns N Roses");
		Banda b2 = new Banda("guns n roses");
		Banda b3 = new Banda("GUNS N ROSES");

		assertEquals(b1, b2, "'Guns N Roses' debe ser igual a 'guns n roses'");
		assertEquals(b1, b3, "Debe ser igual a la versión en mayúsculas");
	}

	@Test
	@DisplayName("Equals: Diferencia bandas con distinto nombre")
	void testNotEquals() {
		Banda b1 = new Banda("Nirvana");
		Banda b2 = new Banda("Metallica");

		assertNotEquals(b1, b2);
	}

	@Test
	@DisplayName("HashCode: Debe ser consistente con equals (mismo hash para variantes de case)")
	void testHashCode() {
		Banda b1 = new Banda("Beatles");
		Banda b2 = new Banda("beatles");

		assertEquals(b1.hashCode(), b2.hashCode(), "Si son iguales por equals, deben tener el mismo hashCode");
	}

	@Test
	@DisplayName("Equals: Validaciones contra nulos y otros objetos")
	void testEqualsBorde() {
		Banda b = new Banda("Test");
		assertNotEquals(b, null);
		assertNotEquals(b, "TestString"); // No debe ser igual a un String
	}
}