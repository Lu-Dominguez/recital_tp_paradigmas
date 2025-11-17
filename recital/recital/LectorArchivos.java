package recital;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


/**
 * Esta clase: Carga artistas (bases y candidatos) y canciones desde JSON usando la libreria Gson.
 */

public class LectorArchivos {

    private static class ArtistaJson {
        String nombre;
        List<String> roles;
        List<String> bandas;
        Double costo;
        Integer maxCanciones;
    }

    private static class CancionJson {
        String titulo;
        List<String> rolesRequeridos;
    }

    public static void cargarArtistas(String archivoArtistas,
                                      String archivoArtistasBase,
                                      List<ArtistaBase> artistasBase,
                                      List<ArtistaCandidato> artistasCandidatos,
                                      List<Rol> roles) throws Exception {

        Gson gson = new Gson();
        String txtArtistas = leerArchivo(archivoArtistas);
        Type tipoListaArt = new TypeToken<List<ArtistaJson>>(){}.getType();
        List<ArtistaJson> lista = gson.fromJson(txtArtistas, tipoListaArt);
        if (lista == null) lista = new ArrayList<>();

        String txtBase = leerArchivo(archivoArtistasBase);
        Type tipoListaStr = new TypeToken<List<String>>(){}.getType();
        List<String> baseNombres = gson.fromJson(txtBase, tipoListaStr);
        if (baseNombres == null) baseNombres = new ArrayList<>();
        Set<String> nombresBase = new LinkedHashSet<>(baseNombres);

        for (ArtistaJson aj : lista) {
            if (aj == null || aj.nombre == null) continue;
            List<Rol> rolesArt = new ArrayList<>();
            if (aj.roles != null) for (String rn : aj.roles) rolesArt.add(obtenerRol(rn, roles));
            List<Banda> bandas = new ArrayList<>();
            if (aj.bandas != null) for (String bn : aj.bandas) bandas.add(new Banda(bn));

            if (nombresBase.contains(aj.nombre)) {
                artistasBase.add(new ArtistaBase(aj.nombre, new ArrayList<>(rolesArt), new ArrayList<>(bandas)));
            } else {
                double costo = aj.costo != null ? aj.costo.doubleValue() : 0.0;
                int max = aj.maxCanciones != null ? aj.maxCanciones.intValue() : 0;
                artistasCandidatos.add(new ArtistaCandidato(aj.nombre, new ArrayList<>(rolesArt), new ArrayList<>(bandas), costo, max));
            }
        }
    }

    public static void cargarRecital(String archivoRecital,
                                     List<Cancion> canciones,
                                     List<Rol> roles) throws Exception {
        Gson gson = new Gson();
        String txtRecital = leerArchivo(archivoRecital);
        Type tipoListaCancion = new TypeToken<List<CancionJson>>(){}.getType();
        List<CancionJson> arr = gson.fromJson(txtRecital, tipoListaCancion);
        if (arr == null) arr = new ArrayList<>();

        for (CancionJson cj : arr) {
            if (cj == null || cj.titulo == null) continue;
            Cancion c = new Cancion(cj.titulo, 0.0);
            Map<String,Integer> contados = new LinkedHashMap<>();
            if (cj.rolesRequeridos != null) {
                for (String rr : cj.rolesRequeridos) contados.put(rr, contados.getOrDefault(rr, 0) + 1);
            }
            for (Map.Entry<String,Integer> e : contados.entrySet()) {
                Rol rol = obtenerRol(e.getKey(), roles);
                c.agregarRolRequerido(rol, e.getValue());
            }
            canciones.add(c);
        }
    }

    // Helpers
    private static String leerArchivo(String ruta) throws Exception {
        return Files.readString(Path.of(ruta));
    }

    private static Rol obtenerRol(String nombre, List<Rol> roles) {
        for (Rol r : roles) {
            if (r.getNombreRol().equalsIgnoreCase(nombre)) return r;
        }
        Rol nuevo = new Rol(nombre);
        roles.add(nuevo);
        return nuevo;
    }
}
