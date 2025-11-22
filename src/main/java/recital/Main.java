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
    private static boolean prologConectado = false;

    public static void main(String[] args) throws Exception {

        cargarEscenarioDeEjemplo();

        int opcion;
        do {
        	if(!prologConectado) {
                UtilidadesProlog.probarConexion(); 
                prologConectado = true;
            }
            mostrarMenu();
            opcion = leerEntero("Opcion: ");

            switch (opcion) {
                case 1 -> listarCanciones();
                case 2 -> listarArtistasBase();
                case 3 -> asignarArtistaACancion();
                case 4 -> listarFaltantesGlobales();
                case 5 -> contratarAutoPorCancion();
                case 6 -> cargarDatosDesdeArchivos();
                case 7 -> listarArtistasContratados();
                case 8 -> listarCancionesEstadoYCosto();
                case 9 -> entrenarArtista();
                case 10 -> quitarArtistaDelRecital();
                case 11 -> contratarTodoElRecital();
                case 12 -> verCostoTotalRecital();
                case 13 -> guardarEstadoInteractivo();
                case 14 -> cargarEstadoInteractivo();
                case 15 -> consultarEntrenamientosMinimos();
                case 0 -> salir();
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
        System.out.println("4 - Ver faltantes globales de todo el recital");
        System.out.println("5 - Contratar automaticamente para una cancion");
        System.out.println("6 - Cargar datos desde archivos (JSON)");
        System.out.println("7 - Listar artistas contratados y costos");
        System.out.println("8 - Listar canciones con estado y costo");
        System.out.println("9 - Entrenar artista (candidato)");
        System.out.println("10 - Quitar artista del recital");
        System.out.println("11 - Contratar artistas para todo el recital");
        System.out.println("12 - Ver costo total del recital");
        System.out.println("13 - Guardar estado (recital-out.json)");
        System.out.println("14 - Cargar estado previo");
        System.out.println("15 - [PROLOG] Ver ENTRENAMIENTOS MINIMOS requeridos");
        System.out.println("0 - Salir");
    }

    private static void listarCanciones() {
        for (int i = 0; i < canciones.size(); i++) {
            Cancion c = canciones.get(i);
            System.out.print(i + " - " + c.getTitulo() + " ");

            var faltantes = c.faltantesConBases(artistasBase);
            boolean completa = faltantes.isEmpty();
            System.out.println(completa ? "[COMPLETA]" : "[INCOMPLETA]");

            System.out.println("   Roles requeridos:");
            for (Map.Entry<Rol, Integer> e : c.getRolesRequeridos().entrySet()) {
                System.out.println("      " + e.getKey().getNombreRol() + " x " + e.getValue());
            }
            if (!completa) {
                System.out.println("  Artistas bases faltantes:");
                for (Map.Entry<Rol, Integer> e : faltantes.entrySet()) {
                    System.out.println("      " + e.getKey().getNombreRol() + " x " + e.getValue());
                }
            }
        }
    }

    private static void listarArtistasBase() {
        for (int i = 0; i < artistasBase.size(); i++) {
            ArtistaBase a = artistasBase.get(i);
            System.out.println(i + " - " + a.getNombre());
            System.out.print("   Roles historicos: ");
            a.getRolesHistoricos().forEach(r -> System.out.print(r.getNombreRol() + ", "));
            System.out.println();
        }
    }

    private static void asignarArtistaACancion() {
        if (canciones.isEmpty() || candidatos.isEmpty()) {
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
        for (int i = 0; i < candidatos.size(); i++) {
            System.out.println(i + " - " + candidatos.get(i).getNombre());
        }
        int idxArtista = leerEntero("Numero de artista: ");
        if (idxArtista < 0 || idxArtista >= candidatos.size()) {
            System.out.println("Artista invalido.");
            return;
        }
        ArtistaCandidato artista = candidatos.get(idxArtista);

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
            Asignacion nuevaAsignacion = new Asignacion(artista, cancion, rol);

            double costoConEntrenamiento = artista.calcularCostoExtraEntrenado();
            double costoFinal = artista.calcularCostoFinal(artistasBase);
            
            nuevaAsignacion.setCosto(costoFinal);
            nuevaAsignacion.setDescuentos(costoConEntrenamiento - costoFinal);

            System.out.println("Asignacion creada correctamente. Costo: $" + costoFinal);

        } catch(Exception e) {
            System.out.println("No se pudo asignar: " + e.getMessage());
        }
    }
    

    private static void cargarEscenarioDeEjemplo() {
        Rol voz = new Rol("Voz");
        Rol guitarra = new Rol("Guitarra");
        Rol bateria = new Rol("bateria");
        roles.add(voz);
        roles.add(guitarra);

        Cancion c1 = new Cancion("Tema 1", 3.5);
        c1.agregarRolRequerido(voz, 1);
        c1.agregarRolRequerido(guitarra, 1);
        c1.agregarRolRequerido(bateria, 1);
        canciones.add(c1);

        ArrayList<Rol> rolesJuan = new ArrayList<>();
        rolesJuan.add(voz);
        ArrayList<Banda> bandasJuan = new ArrayList<>();
        Banda bandaCompartida = new Banda("BandaCompartida");
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
        
        ArrayList<Rol> rolesMaria = new ArrayList<>();
        //Maria no sabe Batería ni Voz ni Guitarra -> puede entrenarse
        ArtistaCandidato maria = new ArtistaCandidato("Maria", rolesMaria, new ArrayList<>(), 1200.0, 2);
        candidatos.add(maria);
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

    private static void cargarDatosDesdeArchivos() {
        System.out.println("Ruta de artistas.json [por defecto: /src/main/resources/artistas.json]:");
        String rutaArtistas = scanner.nextLine().trim();
        if (rutaArtistas.isEmpty()) rutaArtistas = "./src/main/resources/artistas.json";

        System.out.println("Ruta de artistas-discografica.json [por defecto: /src/main/resources/artistas-discografica.json]:");
        String rutaBase = scanner.nextLine().trim();
        if (rutaBase.isEmpty()) rutaBase = "./src/main/resources/artistas-discografica.json";

        System.out.println("Ruta de recital.json [por defecto: /src/main/resources/recital.json]:");
        String rutaRecital = scanner.nextLine().trim();
        if (rutaRecital.isEmpty()) rutaRecital = "./src/main/resources/recital.json";

        try {
            canciones.clear();
            artistasBase.clear();
            candidatos.clear();
            roles.clear();

            LectorArchivos.cargarArtistas(rutaArtistas, rutaBase, artistasBase, candidatos, roles);
            LectorArchivos.cargarRecital(rutaRecital, canciones, roles);
            System.out.println("Datos cargados correctamente. Canciones: " + canciones.size() + ", Bases: " + artistasBase.size() + ", Candidatos: " + candidatos.size());
        } catch (Exception e) {
            System.out.println("Error al cargar archivos: " + e.getMessage());
        }
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
        for(var entry : faltantes.entrySet()) {
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

    private static void listarArtistasContratados() {
        class Resumen { int asignaciones = 0; double costo = 0.0; }
        java.util.Map<ArtistaCandidato, Resumen> mapa = new java.util.LinkedHashMap<>();
        for (Cancion c : canciones) {
            for (Asignacion a : c.getAsignaciones()) {
                if (a.getArtista() instanceof ArtistaCandidato) {
                    ArtistaCandidato ac = (ArtistaCandidato) a.getArtista();
                    Resumen r = mapa.computeIfAbsent(ac, k -> new Resumen());
                    r.asignaciones++;
                    r.costo += a.getCosto();
                }
            }
        }
        if (mapa.isEmpty()) {
            System.out.println("No hay artistas contratados.");
            return;
        }
        System.out.println("Artistas contratados:");
        for (var e : mapa.entrySet()) {
            ArtistaCandidato a = e.getKey();
            Resumen r = e.getValue();
            System.out.println("- " + a.getNombre() + " | asignaciones: " + r.asignaciones +
                    " | costo total: $" + r.costo +
                    " | max canciones: " + a.getCantMaxCanciones());
        }
    }

    private static void listarCancionesEstadoYCosto() {
        for (Cancion c : canciones) {
            var faltantes = c.faltantesConBases(artistasBase);
            boolean completa = faltantes.isEmpty();
            System.out.println("- " + c.getTitulo() + " | " + (completa ? "COMPLETA" : "INCOMPLETA") +
                    " | costo: $" + c.calcularCosto());
            if (!completa) {
                System.out.println("  Faltantes: ");
                for (var e : faltantes.entrySet()) {
                    System.out.println("    " + e.getKey().getNombreRol() + " x " + e.getValue());
                }
            }
        }
    }

    private static void entrenarArtista() {
        if(candidatos.isEmpty()) {
            System.out.println("No hay artistas candidatos cargados.");
            return;
        }
        System.out.println("Elegir artista candidato a entrenar:");
        for(int i = 0; i < candidatos.size(); i++) {
            ArtistaCandidato a = candidatos.get(i);
            System.out.print(i + " - " + a.getNombre() + " | roles: ");
            a.getRolesHistoricos().forEach(r -> System.out.print(r.getNombreRol() + ", "));
            if (!a.getRolesEntrenados().isEmpty()) {
                System.out.print(" | entrenados: ");
                a.getRolesEntrenados().forEach(r -> System.out.print(r.getNombreRol() + ", "));
            }
            System.out.println();
        }
        int idx = leerEntero("Numero de artista: ");
        if(idx < 0 || idx >= candidatos.size()) {
            System.out.println("Artista invalido.");
            return;
        }
        ArtistaCandidato artista = candidatos.get(idx);

        System.out.print("Ingrese nombre del rol a entrenar: ");
        String nombreRol = scanner.nextLine().trim();
        if (nombreRol.isEmpty()) {
            System.out.println("Rol invalido.");
            return;
        }
        Rol rol = obtenerRolOCrear(nombreRol);

        try {
            artista.entrenar(crearRecitalActual(), rol);
            
            double costoEst = artista.calcularCostoExtraEntrenado();
            double costoEstFinal = artista.calcularCostoFinal(artistasBase);
            System.out.println("Entrenamiento realizado. Nuevo costo base entrenado: $" + costoEst +
                    " | estimado final (con posibles descuentos): $" + costoEstFinal);

        } catch(IllegalArgumentException ex) {

            System.out.println("Error de entrada: " + ex.getMessage());
        } catch(Exception ex) {
            System.out.println("No se pudo entrenar: " + ex.getMessage());
        }
    }

    private static Rol obtenerRolOCrear(String nombre) {
        for(Rol r : roles) 
        	if(r.getNombreRol().equalsIgnoreCase(nombre)) 
        		return r;
        Rol nuevo = new Rol(nombre);
        roles.add(nuevo);
        return nuevo;
    }

    private static void quitarArtistaDelRecital() {
        // Construir listado de artistas actualmente asignados en alguna cancion
        java.util.LinkedHashMap<Integer, ArtistaBase> indice = new java.util.LinkedHashMap<>();
        java.util.LinkedHashSet<ArtistaBase> set = new java.util.LinkedHashSet<>();
        for (Cancion c : canciones) for (Asignacion a : c.getAsignaciones()) set.add(a.getArtista());
        int idx = 0;
        System.out.println("Artistas actualmente asignados en el recital:");
        for (ArtistaBase a : set) {
            indice.put(idx, a);
            String tipo = (a instanceof ArtistaCandidato) ? "Candidato" : "Base";
            System.out.println(idx + " - " + a.getNombre() + " (" + tipo + ")");
            idx++;
        }
        if (indice.isEmpty()) {
            System.out.println("No hay artistas asignados.");
            return;
        }
        int sel = leerEntero("Numero de artista a quitar (todas sus asignaciones): ");
        if (!indice.containsKey(sel)) {
            System.out.println("Seleccion invalida.");
            return;
        }
        ArtistaBase elegido = indice.get(sel);
        int totalRemovidas = 0;
        for (Cancion c : canciones) {
            totalRemovidas += c.removerAsignacionesDe(elegido);
        }
        System.out.println("Se quitaron " + totalRemovidas + " asignacion(es) de '" + elegido.getNombre() + "'.");
    }

    private static Recital crearRecitalActual() {
        Recital r = new Recital("LolaPalusa", canciones, artistasBase, candidatos);
        return r;
    }

    private static void contratarTodoElRecital() {
        if(canciones.isEmpty()) {
            System.out.println("No hay canciones cargadas.");
            return;
        }
        if(candidatos.isEmpty()) {
            System.out.println("No hay candidatos disponibles para contratar.");
            return;
        }

        class Demanda {
            Cancion cancion; Rol rol; Demanda(Cancion c, Rol r){ this.cancion=c; this.rol=r; }
        }

        java.util.List<Demanda> demandas = new java.util.ArrayList<>();
        for (Cancion c : canciones) {
            var falt = c.faltantesConBases(artistasBase);
            for (var e : falt.entrySet()) {
                for (int k=0; k<e.getValue(); k++) demandas.add(new Demanda(c, e.getKey()));
            }
        }
        if (demandas.isEmpty()) {
            System.out.println("No hay demandas pendientes. Todas las canciones estan cubiertas (post bases).");
            return;
        }

        java.util.function.Function<Demanda, Integer> contarElegibles = d -> {
            int cnt = 0;
            java.util.Set<ArtistaBase> usados = new java.util.HashSet<>();
            for (Asignacion a : d.cancion.getAsignaciones()) usados.add(a.getArtista());
            for (ArtistaCandidato cand : candidatos) {
                boolean sabeRol = cand.getRolesHistoricos().contains(d.rol) || cand.getRolesEntrenados().contains(d.rol);
                if (!sabeRol) continue;
                if (!cand.puedeTocar()) continue;
                if (usados.contains(cand)) continue; // 1 rol por cancion
                cnt++;
            }
            return cnt;
        };

        demandas.sort((a,b) -> Integer.compare(contarElegibles.apply(a), contarElegibles.apply(b)));

        int asignadas = 0;
        for (Demanda d : demandas) {
            java.util.Set<ArtistaBase> yaUsados = new java.util.HashSet<>();
            for (Asignacion a : d.cancion.getAsignaciones()) yaUsados.add(a.getArtista());

            ArtistaCandidato mejor = null; double mejorCosto = Double.POSITIVE_INFINITY;
            for (ArtistaCandidato cand : candidatos) {
                boolean sabeRol = cand.getRolesHistoricos().contains(d.rol) || cand.getRolesEntrenados().contains(d.rol);
                if (!sabeRol) continue;
                if (!cand.puedeTocar()) continue;
                if (yaUsados.contains(cand)) continue;
                double costo = cand.calcularCostoFinal(artistasBase);
                if (costo < mejorCosto) { mejorCosto = costo; mejor = cand; }
            }
            if (mejor == null) {
                System.out.println("Sin candidatos para '" + d.rol.getNombreRol() + "' en '" + d.cancion.getTitulo() + "'. Considere entrenar (opcion 9) y reintentar.");
                continue;
            }
            try {
                Asignacion nueva = new Asignacion(mejor, d.cancion, d.rol);
                double costoConEntrenamiento = mejor.calcularCostoExtraEntrenado();
                double costoFinal = mejor.calcularCostoFinal(artistasBase);
                nueva.setCosto(costoFinal);
                nueva.setDescuentos(costoConEntrenamiento - costoFinal);
                asignadas++;
            } catch (Exception e) {
                System.out.println("No se pudo asignar en '" + d.cancion.getTitulo() + "': " + e.getMessage());
            }
        }

        System.out.println("Asignaciones realizadas: " + asignadas + ". Costo total recital: $" + verCostoTotalRecital(false));
    }

    private static double verCostoTotalRecital(boolean imprimir) {
        double total = 0.0;
        for (Cancion c : canciones) total += c.calcularCosto();
        if (imprimir) System.out.println("Costo total del recital: $" + total);
        return total;
    }

    private static void verCostoTotalRecital() { verCostoTotalRecital(true); }

    private static void guardarEstadoInteractivo() {
        System.out.println("Ruta de salida [por defecto: recital-out.json]:");
        String ruta = scanner.nextLine().trim();
        if (ruta.isEmpty()) ruta = "./src/main/resources/recital-out.json";
        try {
            Persistencia.guardarEstado(ruta, artistasBase, candidatos, canciones);
            System.out.println("Estado guardado en '" + ruta + "'.");
        } catch (Exception e) {
            System.out.println("No se pudo guardar el estado: " + e.getMessage());
        }
    }

    private static void cargarEstadoInteractivo() {
        System.out.println("Ruta de estado a cargar [por defecto: recital-out.json]:");
        String ruta = scanner.nextLine().trim();
        if (ruta.isEmpty()) ruta = "recital-out.json";
        try {
            Persistencia.cargarEstado(ruta, artistasBase, candidatos, roles, canciones);
            System.out.println("Estado cargado desde '" + ruta + "'.");
        } catch (Exception e) {
            System.out.println("No se pudo cargar el estado desde '" + ruta + "': " + e.getMessage());
        }
    }
    private static void consultarEntrenamientosMinimos() {
    	UtilidadesProlog.obtenerEntrenamientosMinimos(crearRecitalActual());


    }

    private static void salir() {
        try {
            Persistencia.guardarEstado("recital-out.json", artistasBase, candidatos, canciones);
            System.out.println("Saliendo... Estado guardado en recital-out.json");
        } catch (Exception e) {
            System.out.println("Saliendo... (no se pudo guardar estado: " + e.getMessage() + ")");
        }
    }
}
