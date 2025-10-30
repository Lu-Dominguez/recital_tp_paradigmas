package recital;

import java.util.ArrayList;

public class ArtistaBase {
	private String nombre;
	private ArrayList <Rol> rolesHistoricos = new ArrayList<Rol>();
	private ArrayList <Banda> bandasHistoricas = new ArrayList <Banda>();
	
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
	
}
