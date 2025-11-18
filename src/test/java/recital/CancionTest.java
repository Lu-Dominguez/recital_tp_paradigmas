package recital;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class CancionTest {

	private Rol voz, guitarra;
	private Cancion cancion;
	private ArtistaBase baseJuan;

	@BeforeEach
	void setUp() {
		voz = new Rol("Voz");
		guitarra = new Rol("Guitarra");
		cancion = new Cancion("Tema", 4.0);
		baseJuan = new ArtistaBase("Juan", new ArrayList<>(List.of(voz)), new ArrayList<>());
	}

	@Test
	@DisplayName("Faltantes: Descuenta rol si la Base lo cubre")
	void testFaltantesConBase() {
		cancion.agregarRolRequerido(voz, 2); // Pide 2 voces

		Map<Rol, Integer> faltantes = cancion.faltantesConBases(List.of(baseJuan));

		// 2 necesarios - 1 base = 1 faltante
		assertEquals(1, faltantes.get(voz));
	}

	@Test
	@DisplayName("Faltantes: Si pide roles distintos, solo descuenta el que la base sabe")
	void testFaltantesRolesMixtos() {
		cancion.agregarRolRequerido(voz, 1);
		cancion.agregarRolRequerido(guitarra, 1);

		Map<Rol, Integer> faltantes = cancion.faltantesConBases(List.of(baseJuan));

		// Voz cubierta (no está o es 0), Guitarra falta
		assertNull(faltantes.get(voz));
		assertEquals(1, faltantes.get(guitarra));
	}

	@Test
	@DisplayName("Roles Repetidos: Si pide 2 y hay 1 base, falta 1")
	void testRolesRepetidos() {
		cancion.agregarRolRequerido(voz, 2);
		var falt = cancion.faltantesConBases(List.of(baseJuan));
		assertEquals(1, falt.get(voz));
	}
}