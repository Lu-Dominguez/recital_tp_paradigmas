package recital;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Persistencia {

    private static class DTOArtista {
        String nombre;
        List<String> roles;
        List<String> bandas;
        Double costo;
        Integer maxCanciones;
        List<String> rolesEntrenados;
        String tipo; // "base" | "candidato"
    }

    private static class DTOAsignacion {
        String artista;
        String rol;
        Double costo;
        Double descuentos;
        String tipo; // "base" | "candidato"
    }

    private static class DTOCancion {
        String titulo;
        Double duracion;
        Map<String,Integer> rolesRequeridos;
        List<DTOAsignacion> asignaciones;
    }

    private static class DTOEstado {
        List<DTOArtista> artistas;
        List<DTOCancion> canciones;
        Map<String, Double> totales;
    }

    public static void guardarEstado(String ruta,
                                     List<ArtistaBase> bases,
                                     List<ArtistaCandidato> candidatos,
                                     List<Cancion> canciones) throws Exception {
        DTOEstado est = new DTOEstado();
        est.artistas = new ArrayList<>();
        est.canciones = new ArrayList<>();
        est.totales = new LinkedHashMap<>();

        for (ArtistaBase b : bases) {
            DTOArtista a = new DTOArtista();
            a.nombre = b.getNombre();
            a.roles = new ArrayList<>();
            for (Rol r : b.getRolesHistoricos()) a.roles.add(r.getNombreRol());
            a.bandas = new ArrayList<>();
            for (Banda ba : b.getBandasHistoricas()) a.bandas.add(ba.getNombre());
            a.costo = 0.0;
            a.maxCanciones = 0;
            a.rolesEntrenados = new ArrayList<>();
            a.tipo = "base";
            est.artistas.add(a);
        }

        for (ArtistaCandidato c : candidatos) {
            DTOArtista a = new DTOArtista();
            a.nombre = c.getNombre();
            a.roles = new ArrayList<>();
            for (Rol r : c.getRolesHistoricos()) a.roles.add(r.getNombreRol());
            a.bandas = new ArrayList<>();
            for (Banda ba : c.getBandasHistoricas()) a.bandas.add(ba.getNombre());
            a.costo = c.getCostoBasePorCancion();
            a.maxCanciones = c.getCantMaxCanciones();
            a.rolesEntrenados = new ArrayList<>();
            for (Rol r : c.getRolesEntrenados()) a.rolesEntrenados.add(r.getNombreRol());
            a.tipo = "candidato";
            est.artistas.add(a);
        }

        double total = 0.0;
        for (Cancion c : canciones) {
            DTOCancion dc = new DTOCancion();
            dc.titulo = c.getTitulo();
            dc.duracion = c.getDuracion();
            dc.rolesRequeridos = new LinkedHashMap<>();
            for (Map.Entry<Rol,Integer> e : c.getRolesRequeridos().entrySet()) {
                dc.rolesRequeridos.put(e.getKey().getNombreRol(), e.getValue());
            }
            dc.asignaciones = new ArrayList<>();
            for (Asignacion a : c.getAsignaciones()) {
                DTOAsignacion da = new DTOAsignacion();
                da.artista = a.getArtista().getNombre();
                da.rol = a.getRol().getNombreRol();
                da.costo = a.getCosto();
                da.descuentos = a.getDescuentos();
                da.tipo = (a.getArtista() instanceof ArtistaCandidato) ? "candidato" : "base";
                dc.asignaciones.add(da);
                total += a.getCosto();
            }
            est.canciones.add(dc);
        }
        est.totales.put("costoRecital", total);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(est);
        Files.writeString(Path.of(ruta), json);
    }

    public static void cargarEstado(String ruta,
                                    List<ArtistaBase> bases,
                                    List<ArtistaCandidato> candidatos,
                                    List<Rol> roles,
                                    List<Cancion> canciones) throws Exception {
        String txt = Files.readString(Path.of(ruta));
        Gson gson = new Gson();
        DTOEstado est = gson.fromJson(txt, DTOEstado.class);
        if (est == null) throw new RuntimeException("Estado invalido");

        bases.clear(); candidatos.clear(); roles.clear(); canciones.clear();

        // Crear artistas
        if (est.artistas != null) {
            for (DTOArtista a : est.artistas) {
                if (a == null || a.nombre == null) continue;
                List<Rol> rolesHist = new ArrayList<>();
                if (a.roles != null) for (String rn : a.roles) rolesHist.add(obtenerRol(roles, rn));
                List<Banda> bandas = new ArrayList<>();
                if (a.bandas != null) for (String bn : a.bandas) bandas.add(new Banda(bn));
                if ("base".equalsIgnoreCase(a.tipo)) {
                    bases.add(new ArtistaBase(a.nombre, new ArrayList<>(rolesHist), new ArrayList<>(bandas)));
                } else {
                    double costo = a.costo != null ? a.costo : 0.0;
                    int max = a.maxCanciones != null ? a.maxCanciones : 0;
                    ArtistaCandidato cand = new ArtistaCandidato(a.nombre, new ArrayList<>(rolesHist), new ArrayList<>(bandas), costo, max);
                    if (a.rolesEntrenados != null) {
                        for (String re : a.rolesEntrenados) cand.entrenarRol(obtenerRol(roles, re));
                    }
                    candidatos.add(cand);
                }
            }
        }

        // Crear canciones y asignaciones
        if (est.canciones != null) {
            for (DTOCancion dc : est.canciones) {
                if (dc == null || dc.titulo == null) continue;
                Cancion c = new Cancion(dc.titulo, dc.duracion != null ? dc.duracion : 0.0);
                if (dc.rolesRequeridos != null) {
                    for (Map.Entry<String,Integer> e : dc.rolesRequeridos.entrySet()) {
                        c.agregarRolRequerido(obtenerRol(roles, e.getKey()), e.getValue());
                    }
                }
                canciones.add(c);
            }
            // Asignaciones (segunda pasada, canciones ya creadas)
            for (int i = 0; i < est.canciones.size(); i++) {
                DTOCancion dc = est.canciones.get(i);
                Cancion c = canciones.get(i);
                if (dc.asignaciones != null) {
                    for (DTOAsignacion da : dc.asignaciones) {
                        ArtistaBase art = buscarArtista(bases, candidatos, da.artista);
                        Rol rol = obtenerRol(roles, da.rol);
                        try {
                            Asignacion as = new Asignacion(art, c, rol);
                            if (da.costo != null) as.setCosto(da.costo);
                            if (da.descuentos != null) as.setDescuentos(da.descuentos);
                        } catch (Exception ex) {
                            throw new RuntimeException("Error recreando asignacion: " + ex.getMessage());
                        }
                    }
                }
            }
        }
    }

    private static Rol obtenerRol(List<Rol> roles, String nombre) {
        for (Rol r : roles) if (r.getNombreRol().equalsIgnoreCase(nombre)) return r;
        Rol nuevo = new Rol(nombre);
        roles.add(nuevo);
        return nuevo;
    }

    private static ArtistaBase buscarArtista(List<ArtistaBase> bases, List<ArtistaCandidato> candidatos, String nombre) {
        for (ArtistaBase b : bases) if (b.getNombre().equalsIgnoreCase(nombre)) return b;
        for (ArtistaCandidato c : candidatos) if (c.getNombre().equalsIgnoreCase(nombre)) return c;
        throw new RuntimeException("Artista no encontrado: " + nombre);
    }
}

