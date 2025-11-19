package recital;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recital {
	private String nombre;
	private LocalDate fecha;
	private List<Cancion> canciones = new ArrayList<Cancion>();
	private List<ArtistaBase> artistasBase = new ArrayList<ArtistaBase>();
    private List<ArtistaCandidato> artistaContratados = new ArrayList<ArtistaCandidato>();
    
    public Recital(String nombre, List<Cancion> canciones, List<ArtistaBase> artistasBase,List<ArtistaCandidato> artistaContratados) {
    	this.nombre = nombre;
    	this.canciones = canciones;
    	this.artistasBase = artistasBase;
    	this.artistaContratados = artistaContratados;
    }
        

	public double costoTotal() {
        double total = 0.0;
        for (Cancion c : canciones) {
            total += c.calcularCosto();
        }
        return total;
    }

    /**
     * Faltantes globales sumando todas las canciones, considerando que los artistas base
     * pueden cubrir 1 rol por canción (se descuenta por canción, no globalmente).
     */
    public Map<Rol, Integer> faltantesGlobales() {
        Map<Rol, Integer> total = new HashMap<>();
        for (Cancion c : canciones) {
            Map<Rol, Integer> faltantes = c.faltantesConBases(artistasBase);
            for (Map.Entry<Rol, Integer> e : faltantes.entrySet()) {
                total.put(e.getKey(), total.getOrDefault(e.getKey(), 0) + e.getValue());
            }
        }
        total.entrySet().removeIf(e -> e.getValue() == null || e.getValue() <= 0);
        return total;
    }

    public boolean estaAsignado(ArtistaBase artista) {
        for (Cancion c : canciones) {
            for (Asignacion a : c.getAsignaciones()) {
                if (a.getArtista().equals(artista)) return true;
            }
        }
        return false;
    }

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public List<Cancion> getCanciones() {
		return canciones;
	}

	public List<ArtistaBase> getArtistasBase() {
		return artistasBase;
	}

	public List<ArtistaCandidato> getArtistaContratados() {
		return artistaContratados;
	}
	

}
