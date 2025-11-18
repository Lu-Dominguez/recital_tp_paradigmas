package recital;

import java.util.ArrayList;


/**
 * Artista base : es el músico “de la casa” que no genera costo por canción y no tiene tope de canciones
 */

public class ArtistaBase {
	private String nombre;
	private ArrayList <Rol> rolesHistoricos = new ArrayList<Rol>();
	private ArrayList <Banda> bandasHistoricas = new ArrayList <Banda>();
	
	public ArtistaBase(String nombre, ArrayList <Rol> rolesHistoricos, ArrayList <Banda> bandasHistoricas) {
		if(rolesHistoricos == null || bandasHistoricas == null) {
	        throw new IllegalArgumentException("Las listas de roles y bandas no pueden ser null");
	    }
		this.nombre = nombre;
		this.bandasHistoricas = bandasHistoricas;
		this.rolesHistoricos = rolesHistoricos;
	}
	public void validarAsignacion(recital.Cancion cancion, recital.Rol rol) throws Exception {
	    //un artista base puede tocar cualquier rol que haya hecho históricamente
	    if (!getRolesHistoricos().contains(rol)) {
	        throw new Exception("El artista base no ha ocupado el rol " + rol.getNombreRol());
	    }
	}
	public void agregarRol(recital.Rol nuevoRol) {
		rolesHistoricos.add(nuevoRol);
	}

	public String getNombre() {
		return nombre;
	}
	public ArrayList<Rol> getRolesHistoricos() {
		return rolesHistoricos;
	}
	public ArrayList<Banda> getBandasHistoricas() {
		return bandasHistoricas;
	}
	public void agregarBanda(recital.Banda bandaNueva) {
		bandasHistoricas.add(bandaNueva);
	}
}

