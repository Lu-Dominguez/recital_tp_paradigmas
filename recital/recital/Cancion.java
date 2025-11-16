package recital;

import java.util.*;


/**
 * Cancion: Representa una canción del recital, sus roles requeridos , y las asignaciones de artistas para cubrirlos.
 */


public class Cancion {
	private String titulo;
	private double duracion;
	private HashMap<Rol, Integer> rolesRequeridos = new HashMap<Rol, Integer>();
	private ArrayList<Asignacion> asignaciones = new ArrayList<>();
		
	public Cancion(String titulo, double duracion) {
		this.titulo = titulo;
		this.duracion = duracion;
	}
	
	public ArrayList<Asignacion> getAsignaciones() {
		return asignaciones;
	}
	public Rol rolesFaltantes() {
	    for(Rol rol : rolesRequeridos.keySet()) {
	        int cantidadRequerida = rolesRequeridos.get(rol);
	        int cantidadAsignada = 0;
	        //cuento cuantos artistas ya tienen este rol
	        for(Asignacion a : asignaciones) {
	            if(a.getRol().equals(rol)) {
	                cantidadAsignada++;
	            }
	        }
	        if(cantidadAsignada < cantidadRequerida) {
	            return rol;
	        }
	    }
	    return null;
	}
	
	public boolean estaCompleta() {
		return rolesFaltantes()==null;
	}

    /**
     * Calcula faltantes con cantidades, descontando asignaciones actuales y
     * considerando artistas base disponibles (cada base puede cubrir 1 rol por canción).
     */
    public Map<Rol, Integer> faltantesConBases(List<ArtistaBase> artistasBase) {
        Map<Rol, Integer> faltantes = new HashMap<>();
        for (Map.Entry<Rol, Integer> e : rolesRequeridos.entrySet()) {
            faltantes.put(e.getKey(), e.getValue());
        }

        // Descontar asignaciones existentes
        for (Asignacion a : asignaciones) {
            Rol rol = a.getRol();
            if (faltantes.containsKey(rol)) {
                faltantes.put(rol, Math.max(0, faltantes.get(rol) - 1));
            }
        }

        // Base ya usados en esta canción
        Set<ArtistaBase> baseUsados = new HashSet<>();
        for (Asignacion a : asignaciones) {
            ArtistaBase ar = a.getArtista();
            if (!(ar instanceof ArtistaCandidato)) {
                baseUsados.add(ar);
            }
        }

        // Disponibles
        List<ArtistaBase> baseDisponibles = new ArrayList<>();
        if (artistasBase != null) {
            for (ArtistaBase b : artistasBase) {
                if (!baseUsados.contains(b)) baseDisponibles.add(b);
            }
        }

        // Asignación ávida: cada base cubre a lo sumo 1 rol
        Set<ArtistaBase> usadosAhora = new HashSet<>();
        for (Map.Entry<Rol, Integer> e : new ArrayList<>(faltantes.entrySet())) {
            Rol rol = e.getKey();
            int necesarios = e.getValue();
            if (necesarios <= 0) continue;
            for (ArtistaBase b : baseDisponibles) {
                if (necesarios == 0) break;
                if (usadosAhora.contains(b)) continue;
                if (b.getRolesHistoricos().contains(rol)) {
                    usadosAhora.add(b);
                    necesarios--;
                }
            }
            faltantes.put(rol, Math.max(0, necesarios));
        }

        // Limpieza
        faltantes.entrySet().removeIf(e -> e.getValue() == null || e.getValue() <= 0);
        return faltantes;
    }
    public double calcularCosto() {
        double total = 0.0;
        for (Asignacion a : asignaciones) {
            total += a.getCosto();
        }
        return total;
    }
    public void agregarAsignacion(Asignacion a) {
        asignaciones.add(a);
    }
    public int removerAsignacionesDe(ArtistaBase artista) {
        int removidas = 0;
        java.util.Iterator<Asignacion> it = asignaciones.iterator();
        while (it.hasNext()) {
            Asignacion a = it.next();
            if (a.getArtista().equals(artista)) {
                if (artista instanceof ArtistaCandidato) {
                    ((ArtistaCandidato) artista).anularAsignacion();
                }
                it.remove();
                removidas++;
            }
        }
        return removidas;
    }
	public void agregarRolRequerido(Rol rol, int cantidad) {
	    if(rol == null || cantidad <= 0) {
	        throw new IllegalArgumentException("Rol inválido o cantidad inválida");
	    }
	    //si ya existe el rol, sumamos la cantidad
	    rolesRequeridos.put(rol, rolesRequeridos.getOrDefault(rol, 0) + cantidad);
	}

	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public double getDuracion() {
		return duracion;
	}
	public void setDuracion(double duracion) {
		this.duracion = duracion;
	}
	public HashMap<Rol, Integer> getRolesRequeridos() {
		return rolesRequeridos;
	}
}
