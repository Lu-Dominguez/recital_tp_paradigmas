package recital;

/**
 * Asignacion : es lo que representa que un artista forma parte de un rol en una cancion, ademas calcula los descuentos
 */

public class Asignacion {
    private Rol rol;
    private double costo;
    private double descuentos;
    private ArtistaBase artista;
    private Cancion cancion;
	
	public Asignacion(ArtistaBase ar, Cancion can, Rol rol) throws Exception {
	    for(Asignacion a: can.getAsignaciones()) {
	        if(a.getArtista().equals(ar) && a.getCancion().equals(can)) {
	            throw new Exception("El artista ya tiene asignado un rol para la canción " + can.getTitulo());
	        }
	    }

	    ar.validarAsignacion(can, rol);

	    this.artista = ar;
	    this.cancion = can;
	    this.rol = rol;
	    if (ar instanceof ArtistaCandidato) {
	        ((ArtistaCandidato) ar).registrarAsignacion();
	    }
        can.agregarAsignacion(this);
    }
	
	public ArtistaBase getArtista() {
		return artista;
	}

	public Cancion getCancion() {
		return cancion;
	}

	public Rol getRol() {
		return rol;
	}
    public double getCosto() {
        return costo;
    }
    public double getDescuentos() {
        return descuentos;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public void setDescuentos(double descuentos) {
        this.descuentos = descuentos;
    }
}



