package recital;

import java.util.ArrayList;

public class ArtistaCandidato extends ArtistaBase{
	private ArrayList <Rol> rolesEntrenados = new ArrayList<Rol>();
	private double costoBasePorCancion;
	private int cantMaxCanciones;
	private int cantCancionesAsignadas;
	
	public ArtistaCandidato(String nombre, ArrayList <Rol> rolesHistoricos, ArrayList <Banda> bandasHistoricas,
			double costoBasePorCancion, int cantMaxCanciones) {
		super(nombre, rolesHistoricos, bandasHistoricas);
		this.costoBasePorCancion = costoBasePorCancion;
		this.cantMaxCanciones = cantMaxCanciones;
	}
	
	public double calcularCostoContratacion(ArrayList<ArtistaBase> artistasBase) {
		double costoIni = costoBasePorCancion;
		
		//si ya toco con un ArtistaBase en una banda, su costo se reduce al 50%
		if(compartioBandaConArtistaBase(artistasBase))
			return costoIni / 2;
		return costoIni;
	}
	@Override
	public void validarAsignacion(Cancion cancion, Rol rol) throws Exception {
	    if(!puedeTocar()) {
	        throw new Exception("El artista ha alcanzado el máximo de canciones dispuesto a tocar.");
	    }

	    if(!getRolesHistoricos().contains(rol) && !getRolesEntrenados().contains(rol)) {
	        throw new Exception("El artista nunca ha ocupado ese rol. Debe entrenarse.");
	    }
	}

	public boolean compartioBandaConArtistaBase(ArrayList<ArtistaBase> artistasBase) {
		for(ArtistaBase a : artistasBase) {
			for(Banda b: this.getBandasHistoricas()) {
				if(a.getBandasHistoricas().contains(b))
					return true;
			}
		}
		return false;
	}
	
	public void entrenarRol(Rol rol) {
		//falta verificar q el artista no este ya contratado para alguna canción
		rolesEntrenados.add(rol);
	}
	public double calcularCostoExtraEntrenado() {
		int cantidad = rolesEntrenados.size();
		if(cantidad > 0) {
			return costoBasePorCancion * (1+ cantidad * 0.5);
		}
		return costoBasePorCancion;
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
