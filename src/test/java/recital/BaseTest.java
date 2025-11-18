package recital;

import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;
import java.util.List;

public class BaseTest {

    protected Rol voz;
    protected Rol guitarra;

    protected Banda banda1;

    protected ArtistaBase juanBase;
    protected ArtistaCandidato anaCandidata;
    protected ArtistaCandidato pabloCandidato;

    protected Cancion cancion1;
    protected List<Cancion> canciones;
    protected List<ArtistaBase> bases;
    protected List<ArtistaCandidato> candidatos;

    @BeforeEach
    public void setUp() {

        voz = new Rol("Voz");
        guitarra = new Rol("Guitarra");

        banda1 = new Banda("Banda1");

        // Artista Base
        ArrayList<Rol> rolesJuan = new ArrayList<>();
        rolesJuan.add(voz);
        ArrayList<Banda> bandasJuan = new ArrayList<>();
        bandasJuan.add(banda1);
        juanBase = new ArtistaBase("Juan", rolesJuan, bandasJuan);

        // Candidatos
        ArrayList<Rol> rolesAna = new ArrayList<>();
        rolesAna.add(voz);
        ArrayList<Banda> bandasAna = new ArrayList<>();
        bandasAna.add(banda1);
        anaCandidata = new ArtistaCandidato("Ana", rolesAna, bandasAna, 800.0, 2);

        ArrayList<Rol> rolesPablo = new ArrayList<>();
        rolesPablo.add(guitarra);
        ArrayList<Banda> bandasPablo = new ArrayList<>();
        pabloCandidato = new ArtistaCandidato("Pablo", rolesPablo, bandasPablo, 1000.0, 3);

        // Canciones
        cancion1 = new Cancion("Tema 1", 3.5);
        cancion1.agregarRolRequerido(voz, 1);
        cancion1.agregarRolRequerido(guitarra, 1);

        canciones = new ArrayList<>();
        canciones.add(cancion1);

        bases = new ArrayList<>();
        bases.add(juanBase);

        candidatos = new ArrayList<>();
        candidatos.add(anaCandidata);
        candidatos.add(pabloCandidato);
    }
}
