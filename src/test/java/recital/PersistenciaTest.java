package recital;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersistenciaTest {

	@Test
	@DisplayName("Guardar y Cargar: El ciclo completo debe recuperar los mismos datos")
	void testGuardarYCargarRoundTrip(@TempDir Path tempDir) throws Exception {
		// 1. PREPARACIÓN DE DATOS
		Rol rolVoz = new Rol("Voz");
		Banda banda = new Banda("Queen");

		// Base
		ArtistaBase baseOriginal = new ArtistaBase("FreddieBase", new ArrayList<>(List.of(rolVoz)),
				new ArrayList<>(List.of(banda)));

		// Canción con asignación
		Cancion cancionOriginal = new Cancion("Bohemian Rhapsody", 6.0);
		cancionOriginal.agregarRolRequerido(rolVoz, 1);

		// (Opcional: asignamos algo para probar que se guarda la asignación,
		// aunque tu Persistencia guarda referencias por nombre)

		// Definimos la ruta del archivo temporal
		Path rutaArchivo = tempDir.resolve("recital-test.json");
		String pathString = rutaArchivo.toString();

		// 2. GUARDAR ESTADO
		Persistencia.guardarEstado(pathString, List.of(baseOriginal), new ArrayList<>(), // Sin candidatos
				List.of(cancionOriginal));

		// 3. LIMPIEZA (Para asegurar que cargamos desde el archivo y no de memoria)
		List<ArtistaBase> basesCargadas = new ArrayList<>();
		List<ArtistaCandidato> candidatosCargados = new ArrayList<>();
		List<Rol> rolesCargados = new ArrayList<>();
		List<Cancion> cancionesCargadas = new ArrayList<>();

		// 4. CARGAR ESTADO
		Persistencia.cargarEstado(pathString, basesCargadas, candidatosCargados, rolesCargados, cancionesCargadas);

		// 5. VERIFICACIONES
		// Verificar Artista Base
		assertEquals(1, basesCargadas.size(), "Debe haber 1 artista base cargado");
		assertEquals("FreddieBase", basesCargadas.get(0).getNombre());
		assertEquals("Queen", basesCargadas.get(0).getBandasHistoricas().get(0).getNombre());

		// Verificar Canción
		assertEquals(1, cancionesCargadas.size(), "Debe haber 1 canción cargada");
		Cancion cCargada = cancionesCargadas.get(0);
		assertEquals("Bohemian Rhapsody", cCargada.getTitulo());
		assertEquals(6.0, cCargada.getDuracion());

		// Verificar Roles (se recuperan del mapa de roles requeridos)
		assertTrue(cCargada.getRolesRequeridos().containsKey(new Rol("Voz")));
	}

	@Test
	@DisplayName("LectorArchivos: Debe cargar JSONs correctamente")
	void testLectorArchivos(@TempDir Path tempDir) throws Exception {
		// Crear archivos JSON falsos en la carpeta temporal
		Path archivoArtistas = tempDir.resolve("artistas.json");
		Path archivoBase = tempDir.resolve("bases.json");

		String jsonArtistas = "[{\"nombre\":\"Axl\",\"roles\":[\"Voz\"],\"bandas\":[\"GNR\"],\"costo\":100,\"maxCanciones\":5}]";
		String jsonBase = "[]"; // Lista vacía de nombres base, así Axl se carga como Candidato

		Files.writeString(archivoArtistas, jsonArtistas);
		Files.writeString(archivoBase, jsonBase);

		List<ArtistaBase> bases = new ArrayList<>();
		List<ArtistaCandidato> candidatos = new ArrayList<>();
		List<Rol> roles = new ArrayList<>();

		// Ejecutar carga
		LectorArchivos.cargarArtistas(archivoArtistas.toString(), archivoBase.toString(), bases, candidatos, roles);

		// Verificar
		assertEquals(1, candidatos.size());
		assertEquals("Axl", candidatos.get(0).getNombre());
		assertEquals(1, roles.size()); // Se creó el rol "Voz" automáticamente
	}
}