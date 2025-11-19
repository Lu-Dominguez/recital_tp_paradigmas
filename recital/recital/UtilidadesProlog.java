package recital;

import org.jpl7.Query;
import org.jpl7.Term;
import java.util.List;
import java.util.Map;
import java.util.Locale;

public class UtilidadesProlog {

    public static void probarConexion() {
        try {
            String rutaProlog = "C:/Users/Diego/Desktop/LOS DOMINGUEZ/Luana Unlam/Paradigmas/recital_tp_paradigmas-federico-dev/recital_tp_paradigmas-federico-dev/src/prolog/recital.pl";

            String queryConsult = "consult('" + rutaProlog + "')";
            Query qConsult = new Query(queryConsult);

            if (qConsult.hasSolution()) {
                Query qTest = new Query("conectado");
                if (!qTest.hasSolution()) {
                    System.err.println("Fallo al ejecutar 'conectado'.");
                }
            } else {
                System.err.println("Fallo al cargar recital.pl. Revisar ruta.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int obtenerEntrenamientosMinimos(Recital recital) {

        // Limpia hechos previos en Prolog
        new Query("retractall(rol_faltante(_, _))").hasSolution();
        new Query("retractall(candidato(_, _, _, _))").hasSolution();

        // Afirmar roles faltantes
        Map<Rol, Integer> faltantes = recital.faltantesGlobales();
        for (Map.Entry<Rol, Integer> entry : faltantes.entrySet()) {
            String nombreRol = entry.getKey().getNombreRol().toLowerCase();
            int cantidad = entry.getValue();
            if (cantidad > 0) {
                String assertFaltante = String.format(
                        "assertz(rol_faltante('%s', %d))",
                        nombreRol, cantidad
                );
                new Query(assertFaltante).hasSolution();
            }
        }

        // Afirmar candidatos
        List<ArtistaCandidato> candidatos = recital.getArtistaContratados();
        for (ArtistaCandidato ac : candidatos) {
            String rolesProlog = formatRolesForProlog(ac.getRolesHistoricos());
            int maxCanciones = ac.getCantMaxCanciones();
            double costoBase = 1.0;

            String assertCandidato = String.format(Locale.US,
                    "assertz(candidato('%s', %s, %d, %.2f))",
                    ac.getNombre(), rolesProlog, maxCanciones, costoBase
            );
            new Query(assertCandidato).hasSolution();
        }

        // Comprobar si sin entrenamientos se cubre todo
        if (new Query("cubre_roles([])").hasSolution()) {
            System.out.println("Mínimo de Entrenamientos requeridos: 0");
            System.out.println("Detalle de entrenamientos: ninguno");
            return 0;
        }

        // Ejecutar entrenamientos_minimos
        try {
            Query qMinimos = new Query("entrenamientos_minimos(MinEntrenamientos, Solucion)");
            if (qMinimos.hasSolution()) {
                Map<String, Term> solucion = qMinimos.oneSolution();
                int minEntrenamientos = solucion.get("MinEntrenamientos").intValue();
                Term listaSolucion = solucion.get("Solucion");

                // Convertir a legible
                String solucionLegible = listaSolucion.toString()
                        .replaceAll("\\[|\\]", "")
                        .replaceAll("'-'\\(", "")
                        .replaceAll("\\)", "")
                        .replaceAll(",", " ->");

                System.out.println("Mínimo de Entrenamientos requeridos: " + minEntrenamientos);
                System.out.println("Posible entrenamiento:" + solucionLegible);

                return minEntrenamientos;
            } else {
                System.err.println("No se encontró ninguna combinación de entrenamientos.");
                return -1;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        }
    }

    private static String formatRolesForProlog(List<Rol> roles) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < roles.size(); i++) {
            sb.append("'").append(roles.get(i).getNombreRol().toLowerCase()).append("'");
            if (i < roles.size() - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
