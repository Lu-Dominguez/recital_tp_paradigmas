package recital;

import java.util.ArrayList;

public class ArtistaCandidato extends ArtistaBase{
	private ArrayList <Rol> rolesEntrenados = new ArrayList<Rol>();
	private double costoBasePorCancion;
	private int cantMaxCanciones;
	private int cantCancionesAsignadas;
	
	public double calcularCostoContratacion() {
		return 0.0;
	}
	public boolean compartioBandaConArtistaBase() {
		return false;
	}
	public double calcularCostoExtraEntrenado() {
		return 0.0;
	}
	
	public boolean puedeTocar() {
		return cantCancionesAsignadas <= cantMaxCanciones;
	}
	
	public double getCostoBasePorCancion() {
		return costoBasePorCancion;
	}
	public void setCostoBasePorCancion(double costoBasePorCancion) {
		this.costoBasePorCancion = costoBasePorCancion;
	}
	public ArrayList<Rol> getRolesEntrenados() {
		return rolesEntrenados;
	}
	public int getCantMaxCanciones() {
		return cantMaxCanciones;
	}
	
}
