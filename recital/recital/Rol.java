package recital;

public class Rol {
	private String nombreRol;
	
	public Rol(String nombre) {
		this.nombreRol = nombre;
	}

	public String getNombreRol() {
		return nombreRol;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rol rol = (Rol) o;
        return nombreRol != null ? nombreRol.equalsIgnoreCase(rol.nombreRol) : rol.nombreRol == null;
    }

    @Override
    public int hashCode() {
        return nombreRol != null ? nombreRol.toLowerCase().hashCode() : 0;
    }
}
