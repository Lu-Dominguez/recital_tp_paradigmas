package recital;

import java.util.ArrayList;
import java.util.HashMap;

public class Cancion {
	private String titulo;
	private double duracion;
	private HashMap<Rol, Integer> rolesRequeridos = new HashMap<Rol, Integer>();
	private ArrayList<Asignacion> asignaciones = new ArrayList<>();
	
	public ArrayList<Asignacion> getAsignaciones() {
		return asignaciones;
	}
	public Rol rolesFaltantes() {
		return null;
	}
	public boolean estaCompleta() {
		return false;
	}
	public double calcularCosto() {
		return 0.0;
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
