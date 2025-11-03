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
		for(ArtistaBase base : artistasBase) {
			for(Banda b : this.getBandasHistoricas()) {
				if(base.getBandasHistoricas().contains(b)) {
					return costoIni / 2;
				}
			}
		}
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

