package recital;

import java.util.ArrayList;
import java.util.HashMap;

public class Cancion {
	private String titulo;
	private double duracion;
	private HashMap<Rol, Integer> rolesRequeridos = new HashMap<Rol, Integer>();
	private ArrayList<Asignacion> asignaciones = new ArrayList<>();
	
	public Cancion(String titulo, double duracion) {
		this.titulo = titulo;
		this.duracion = duracion;
	}
	
	public ArrayList<Asignacion> getAsignaciones() {
		return asignaciones;
	}
	public Rol rolesFaltantes() {
	    for(Rol rol : rolesRequeridos.keySet()) {
	        int cantidadRequerida = rolesRequeridos.get(rol);
	        int cantidadAsignada = 0;
	        //cuento cuantos artistas ya tienen este rol
	        for(Asignacion a : asignaciones) {
	            if(a.getRol().equals(rol)) {
	                cantidadAsignada++;
	            }
	        }
	        if(cantidadAsignada < cantidadRequerida) {
	            return rol;
	        }
	    }
	    return null;
	}
	
	public boolean estaCompleta() {
		return rolesFaltantes()==null;
	}
	public double calcularCosto() {
		return 0.0;
	}
	public void agregarAsignacion(Asignacion a) {
		asignaciones.add(a);
	}
	public void agregarRolRequerido(Rol rol, int cantidad) {
	    if(rol == null || cantidad <= 0) {
	        throw new IllegalArgumentException("Rol inválido o cantidad inválida");
	    }
	    //si ya existe el rol, sumamos la cantidad
	    rolesRequeridos.put(rol, rolesRequeridos.getOrDefault(rol, 0) + cantidad);
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
