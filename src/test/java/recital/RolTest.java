package recital;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RolTest {

	@Test
	@DisplayName("Constructor y Getter funcionan correctamente")
	void testCreacion() {
		Rol rol = new Rol("Batería");
		assertEquals("Batería", rol.getNombreRol());
	}

	@Test
	@DisplayName("Equals: Debe ignorar mayúsculas y minúsculas (Case Insensitive)")
	void testEqualsCaseInsensitive() {
		Rol r1 = new Rol("Guitarra");
		Rol r2 = new Rol("guitarra"); // Diferente case
		Rol r3 = new Rol("GUITARRA"); // Todo mayúsculas

		// Gracias a tu @Override de equals, esto debe dar true
		assertEquals(r1, r2, "'Guitarra' y 'guitarra' deben ser el mismo rol");
		assertEquals(r1, r3, "'Guitarra' y 'GUITARRA' deben ser el mismo rol");
	}

	@Test
	@DisplayName("Equals: Debe diferenciar nombres distintos")
	void testNotEquals() {
		Rol r1 = new Rol("Voz");
		Rol r2 = new Rol("Coros");

		assertNotEquals(r1, r2);
	}

	@Test
	@DisplayName("HashCode: Objetos iguales deben tener el mismo hash")
	void testHashCode() {
		// Esto es vital para que funcionen los Mapas y Sets en el resto del TP
		Rol r1 = new Rol("Bajo");
		Rol r2 = new Rol("bajo");

		assertEquals(r1.hashCode(), r2.hashCode(), "Si equals da true, hashCode debe ser idéntico");
	}

	@Test
	@DisplayName("Equals: Validaciones estándar (Null y otro tipo)")
	void testEqualsValidaciones() {
		Rol r = new Rol("Saxofón");

		assertNotEquals(r, null, "Un rol no puede ser igual a null");
		assertNotEquals(r, "Saxofón", "Un rol no puede ser igual a un String");
	}
}