package recital;

import java.time.LocalDate;
import java.util.ArrayList;

public class Recital {
	private String nombre;
	private LocalDate fecha;
	private ArrayList<Cancion> canciones = new ArrayList<Cancion>();
	private ArrayList<ArtistaBase> artistasBase = new ArrayList<ArtistaBase>();
	private ArrayList<ArtistaCandidato> artistaContratados = new ArrayList<ArtistaCandidato>();
	
	public double costoTotal() {
		return 0.0;
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

	public ArrayList<Cancion> getCanciones() {
		return canciones;
	}

	public ArrayList<ArtistaBase> getArtistasBase() {
		return artistasBase;
	}

	public ArrayList<ArtistaCandidato> getArtistaContratados() {
		return artistaContratados;
	}
	

}
