package recital;

import java.util.ArrayList;
import java.util.List;

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

    public double calcularCostoContratacion(List<ArtistaBase> artistasBase) {
        return calcularCostoContratacion(new ArrayList<>(artistasBase));
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

    /**
     * Costo final aplicando entrenamiento (50% por rol entrenado) y luego descuento por banda (50%).
     */
    public double calcularCostoFinal(List<ArtistaBase> artistasBase) {
        double costoConEntrenamiento = calcularCostoExtraEntrenado();
        boolean desc = compartioBandaConArtistaBase(new ArrayList<>(artistasBase));
        return desc ? costoConEntrenamiento / 2.0 : costoConEntrenamiento;
    }
	
	public boolean puedeTocar() {
		return cantCancionesAsignadas < cantMaxCanciones;
	}

    public void registrarAsignacion() {
        this.cantCancionesAsignadas++;
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

    /**
     * Entrena al artista candidato para un rol nuevo, validando que no esté contratado aún.
     */
    public void entrenar(Recital recital, Rol rol) throws Exception {
        if (rol == null) throw new IllegalArgumentException("Rol requerido");
        if (getRolesHistoricos().contains(rol) || getRolesEntrenados().contains(rol)) return;
        for (Cancion c : recital.getCanciones()) {
            for (Asignacion a : c.getAsignaciones()) {
                if (a.getArtista().equals(this)) {
                    throw new Exception("No se puede entrenar: el artista ya fue contratado en alguna canción");
                }
            }
        }
        entrenarRol(rol);
    }
	
}
