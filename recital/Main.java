package recital;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final List<Cancion> canciones = new ArrayList<>();
    private static final List<ArtistaBase> artistasBase = new ArrayList<>();
    private static final List<ArtistaCandidato> candidatos = new ArrayList<>();
    private static final List<Rol> roles = new ArrayList<>();

    public static void main(String[] args) throws Exception {

        cargarEscenarioDeEjemplo();

        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Opcion: ");

            switch (opcion) {
                case 1 -> listarCanciones();
                case 2 -> listarArtistasBase();
                case 3 -> asignarArtistaACancion();
                case 4 -> listarFaltantesGlobales();
                case 5 -> contratarAutoPorCancion();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opcion invalida");
            }
            System.out.println();
        } while (opcion != 0);
    }

    private static void mostrarMenu() {
        System.out.println("===== MENU TP RECITAL =====");
        System.out.println("1 - Listar canciones y roles faltantes");
        System.out.println("2 - Listar artistas base");
        System.out.println("3 - Asignar artista a cancion y rol");
        System.out.println("4 - Ver faltantes globales (con bases)");
        System.out.println("5 - Contratar automaticamente para una cancion");
        System.out.println("0 - Salir");
    }

    private static void listarCanciones() {
        for (int i = 0; i < canciones.size(); i++) {
            Cancion c = canciones.get(i);
            System.out.print(i + " - " + c.getTitulo() + " ");

            if (c.estaCompleta()) {
                System.out.println("[COMPLETA]");
            } else {
                Rol faltante = c.rolesFaltantes();
                System.out.println("[INCOMPLETA] Falta: " +
                        (faltante != null ? faltante.getNombreRol() : "algun rol"));
            }

            System.out.println("   Roles requeridos:");
            for (Map.Entry<Rol, Integer> e : c.getRolesRequeridos().entrySet()) {
                System.out.println("      " + e.getKey().getNombreRol() + " x " + e.getValue());
            }
        }
    }

    private static void listarArtistasBase() {
        for (int i = 0; i < artistasBase.size(); i++) {
            ArtistaBase a = artistasBase.get(i);
            System.out.println(i + " - " + a.getNombre());
            System.out.print("   Roles historicos: ");
            a.getRolesHistoricos().forEach(r -> System.out.print(r.getNombreRol() + " "));
            System.out.println();
        }
    }

    private static void asignarArtistaACancion() {
        if (canciones.isEmpty() || artistasBase.isEmpty()) {
            System.out.println("No hay canciones o artistas cargados.");
            return;
        }

        System.out.println("Elegir cancion:");
        for (int i = 0; i < canciones.size(); i++) {
            System.out.println(i + " - " + canciones.get(i).getTitulo());
        }
        int idxCancion = leerEntero("Numero de cancion: ");
        if (idxCancion < 0 || idxCancion >= canciones.size()) {
            System.out.println("Cancion invalida.");
            return;
        }
        Cancion cancion = canciones.get(idxCancion);

        System.out.println("Elegir artista:");
        for (int i = 0; i < artistasBase.size(); i++) {
            System.out.println(i + " - " + artistasBase.get(i).getNombre());
        }
        int idxArtista = leerEntero("Numero de artista: ");
        if (idxArtista < 0 || idxArtista >= artistasBase.size()) {
            System.out.println("Artista invalido.");
            return;
        }
        ArtistaBase artista = artistasBase.get(idxArtista);

        var rolesDeLaCancion = new ArrayList<>(cancion.getRolesRequeridos().keySet());
        System.out.println("Elegir rol:");
        for (int i = 0; i < rolesDeLaCancion.size(); i++) {
            System.out.println(i + " - " + rolesDeLaCancion.get(i).getNombreRol());
        }
        int idxRol = leerEntero("Numero de rol: ");
        if (idxRol < 0 || idxRol >= rolesDeLaCancion.size()) {
            System.out.println("Rol invalido.");
            return;
        }
        Rol rol = rolesDeLaCancion.get(idxRol);

        try {
            new Asignacion(artista, cancion, rol);
            System.out.println("Asignacion creada correctamente.");
        } catch (Exception e) {
            System.out.println("No se pudo asignar: " + e.getMessage());
        }
    }

    private static void cargarEscenarioDeEjemplo() {
        Rol voz = new Rol("Voz");
        Rol guitarra = new Rol("Guitarra");
        roles.add(voz);
        roles.add(guitarra);

        Cancion c1 = new Cancion("Tema 1", 3.5);
        c1.agregarRolRequerido(voz, 1);
        c1.agregarRolRequerido(guitarra, 1);
        canciones.add(c1);

        ArrayList<Rol> rolesJuan = new ArrayList<>();
        rolesJuan.add(voz);
        ArrayList<Banda> bandasJuan = new ArrayList<>();
        Banda bandaCompartida = new Banda();
        bandasJuan.add(bandaCompartida);
        ArtistaBase juan = new ArtistaBase("Juan", rolesJuan, bandasJuan);
        artistasBase.add(juan);

        // Candidatos de ejemplo
        ArrayList<Rol> rolesAna = new ArrayList<>();
        rolesAna.add(voz);
        ArrayList<Banda> bandasAna = new ArrayList<>();
        // Comparte banda con Juan -> 50% descuento
        bandasAna.add(bandaCompartida);
        ArtistaCandidato ana = new ArtistaCandidato("Ana", rolesAna, bandasAna, 800.0, 2);
        candidatos.add(ana);

        ArrayList<Rol> rolesPablo = new ArrayList<>();
        rolesPablo.add(guitarra);
        ArrayList<Banda> bandasPablo = new ArrayList<>();
        ArtistaCandidato pablo = new ArtistaCandidato("Pablo", rolesPablo, bandasPablo, 1000.0, 3);
        candidatos.add(pablo);
    }

    private static int leerEntero(String msg) {
        System.out.print(msg);
        while (!scanner.hasNextInt()) {
            System.out.print("Ingrese un numero valido: ");
            scanner.next();
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        return n;
    }

    private static void listarFaltantesGlobales() {
        // Acumula faltantes considerando que bases pueden cubrir 1 rol por cancion
        var acumulado = new java.util.HashMap<Rol, Integer>();
        for (Cancion c : canciones) {
            var falt = c.faltantesConBases(artistasBase);
            for (var e : falt.entrySet()) {
                acumulado.put(e.getKey(), acumulado.getOrDefault(e.getKey(), 0) + e.getValue());
            }
        }
        if (acumulado.isEmpty()) {
            System.out.println("No hay faltantes globales.\n");
            return;
        }
        System.out.println("Faltantes globales (post bases):");
        for (var e : acumulado.entrySet()) {
            System.out.println(" - " + e.getKey().getNombreRol() + ": " + e.getValue());
        }
    }

    private static void contratarAutoPorCancion() {
        if (canciones.isEmpty()) {
            System.out.println("No hay canciones cargadas.");
            return;
        }
        if (candidatos.isEmpty()) {
            System.out.println("No hay candidatos disponibles para contratar.");
            return;
        }

        System.out.println("Elegir cancion:");
        for (int i = 0; i < canciones.size(); i++) {
            System.out.println(i + " - " + canciones.get(i).getTitulo());
        }
        int idx = leerEntero("Numero de cancion: ");
        if (idx < 0 || idx >= canciones.size()) {
            System.out.println("Cancion invalida.");
            return;
        }
        Cancion cancion = canciones.get(idx);

        var faltantes = cancion.faltantesConBases(artistasBase);
        if (faltantes.isEmpty()) {
            System.out.println("La cancion ya esta completa (considerando bases).");
            return;
        }

        int asignadas = 0;
        for (var entry : faltantes.entrySet()) {
            Rol rol = entry.getKey();
            int cantidad = entry.getValue();
            for (int i = 0; i < cantidad; i++) {
                ArtistaCandidato mejor = null;
                double mejorCosto = Double.POSITIVE_INFINITY;

                // evitar reutilizar el mismo artista en la misma cancion
                java.util.Set<ArtistaBase> yaUsados = new java.util.HashSet<>();
                for (Asignacion a : cancion.getAsignaciones()) yaUsados.add(a.getArtista());

                for (ArtistaCandidato cand : candidatos) {
                    boolean sabeRol = cand.getRolesHistoricos().contains(rol) || cand.getRolesEntrenados().contains(rol);
                    if (!sabeRol) continue;
                    if (!cand.puedeTocar()) continue;
                    if (yaUsados.contains(cand)) continue;
                    double costo = cand.calcularCostoFinal(artistasBase);
                    if (costo < mejorCosto) {
                        mejorCosto = costo;
                        mejor = cand;
                    }
                }

                if (mejor == null) {
                    System.out.println("No hay candidatos disponibles para el rol '" + rol.getNombreRol() + "'.");
                    // Sugerencia de entrenamiento
                    java.util.List<ArtistaCandidato> sugeribles = new java.util.ArrayList<>();
                    for (ArtistaCandidato cand : candidatos) {
                        boolean yaAsignado = false;
                        for (Cancion c : canciones) {
                            for (Asignacion a : c.getAsignaciones()) if (a.getArtista().equals(cand)) yaAsignado = true;
                        }
                        if (!yaAsignado && !cand.getRolesHistoricos().contains(rol) && !cand.getRolesEntrenados().contains(rol)) {
                            sugeribles.add(cand);
                        }
                    }
                    if (!sugeribles.isEmpty()) {
                        System.out.println("Candidatos entrenables (no contratados aun):");
                        for (ArtistaCandidato s : sugeribles) {
                            double costoTrasEntrenar = s.getCostoBasePorCancion() * (1 + (s.getRolesEntrenados().size() + 1) * 0.5);
                            boolean desc = s.compartioBandaConArtistaBase(new java.util.ArrayList<>(artistasBase));
                            double costoFinal = desc ? costoTrasEntrenar / 2.0 : costoTrasEntrenar;
                            System.out.println(" - " + s.getNombre() + ": estimado $" + costoFinal);
                        }
                    }
                    continue;
                }

                try {
                    Asignacion nueva = new Asignacion(mejor, cancion, rol);
                    double costoConEntrenamiento = mejor.calcularCostoExtraEntrenado();
                    double costoFinal = mejor.calcularCostoFinal(artistasBase);
                    nueva.setCosto(costoFinal);
                    nueva.setDescuentos(costoConEntrenamiento - costoFinal);
                    asignadas++;
                    System.out.println("Asignado '" + rol.getNombreRol() + "' a " + mejor.getNombre() + " por $" + costoFinal);
                } catch (Exception e) {
                    System.out.println("No se pudo asignar: " + e.getMessage());
                }
            }
        }

        if (asignadas == 0) {
            System.out.println("No se realizaron asignaciones.");
        } else {
            System.out.println("Asignaciones realizadas: " + asignadas + ". Costo cancion: $" + cancion.calcularCosto());
        }
    }
}
