package recital;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;


class Pruebas {

	private ArtistaBase juan;
    private ArtistaCandidato ana;
    private ArtistaCandidato pablo;
    private Rol voz;
    private Rol guitarra;
    private Cancion c1;
    private List<ArtistaBase> artistasBase = new ArrayList<ArtistaBase>();
    private List<ArtistaCandidato> artistasCandidato = new ArrayList<ArtistaCandidato>();
    private List<Cancion> canciones = new ArrayList<Cancion>();

    @BeforeEach
    void setUp() {
        voz = new Rol("Voz");
        guitarra = new Rol("Guitarra");
        Banda bandaCompartida = new Banda("BandaCompartida");

        juan = new ArtistaBase("Juan", new ArrayList<>(List.of(voz)), new ArrayList<>(List.of(bandaCompartida)));
        artistasBase.add(juan);

        pablo = new ArtistaCandidato("Pablo", new ArrayList<>(List.of(guitarra)), new ArrayList<>(), 1000.0, 3);
        artistasCandidato.add(pablo);
        
        ana = new ArtistaCandidato("Ana", new ArrayList<>(List.of(voz)), 
                new ArrayList<>(List.of(bandaCompartida)), 
                800.0, 2);
        artistasCandidato.add(ana);
        
        c1 = new Cancion("Tema 1", 3.5);
        c1.agregarRolRequerido(voz, 1);
        c1.agregarRolRequerido(guitarra, 1);
        canciones.add(c1);
    }
	
    @Test
    void testCostoConDescuentoDel50PorCiento() {
        //Ana: Costo Base $800 pero comparte banda con juan. Su precio se reduce al 50%
        //Costo esperado: 800/2 = 400.0
        double costo = ana.calcularCostoFinal(artistasBase);
        assertEquals(400.0, costo, 0.001, "El descuento del 50% no se aplicó correctamente.");
    }

    @Test
    void testCostoSinDescuento() {
        //Pablo: Costo Base $1000.0. No comparte banda.
        double costo = pablo.calcularCostoFinal(artistasBase);
        assertEquals(1000.0, costo, 0.001, "El costo sin descuento no coincide con el costo base.");
    }


    @Test
    void testCostoConEntrenamientoYDescuento() {
        //Entrenar a Ana para Guitarra (1 rol extra)
        //Costo Base Entrenado: 800.0 * (1 + 1 * 0.5) = 1200.0
        //Costo Final (con 50% descuento): 1200.0/2 = 600.0
        ana.entrenarRol(guitarra);
        double costo = ana.calcularCostoFinal(artistasBase);
        assertEquals(600.0, costo, 0.001, "El recargo por entrenamiento o el descuento no se aplicaron correctamente.");
    }

    @Test
    void testErrorAlAsignarRolNoPoseido() {
        //Ana sabe Voz, intentamos asignarle Guitarra sin entrenar
        Exception exception = assertThrows(Exception.class, () -> {
            new Asignacion(ana, c1, guitarra);
        });
        assertTrue(exception.getMessage().contains("Debe entrenarse"), "No se lanzó la excepción esperada para rol faltante.");
    }

    @Test
    void testErrorAlEntrenarSiYaFueContratado() throws Exception {
        new Asignacion(pablo, c1, guitarra); 
        
        Recital recitalPrueba = new Recital("Recital de prueba", canciones, artistasBase, artistasCandidato);
        
        Exception exception = assertThrows(Exception.class, () -> {
            pablo.entrenar(recitalPrueba, voz); //Intentar entrenar a Pablo en Voz
        });
        
        assertTrue(exception.getMessage().contains("ya fue contratado"), "No se lanzó la excepción de artista contratado.");
    }

    @Test
    void testErrorAlExcederMaxCanciones() throws Exception {
        // Ana.cantMaxCanciones = 2
        Cancion c2 = new Cancion("T2", 1.0); 
        Cancion c3 = new Cancion("T3", 1.0);
        c2.agregarRolRequerido(voz, 1);
        c3.agregarRolRequerido(voz, 1);

        new Asignacion(ana, c1, voz);
        new Asignacion(ana, c2, voz);
        
        //la 3era asignación debe fallar
        Exception exception = assertThrows(Exception.class, () -> {
            new Asignacion(ana, c3, voz);
        });
        
        assertTrue(exception.getMessage().contains("máximo de canciones"), "No se lanzó la excepción de límite de canciones.");
    }


}
