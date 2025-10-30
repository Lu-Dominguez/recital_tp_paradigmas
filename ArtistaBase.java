package recital;

import java.util.ArrayList;

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
	
	public void agregarRol(Rol nuevoRol) {
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
	public void agregarBanda(Banda bandaNueva) {
		bandasHistoricas.add(bandaNueva);
	}
}
