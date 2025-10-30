package recital;

public class Asignacion {
	private Rol rol;
	private double costo;
	private double descuentos;
	private ArtistaBase artista;
	private Cancion cancion;
	
	public Asignacion(ArtistaBase ar, Cancion can, Rol rol) throws Exception {
		////solo puede desempeñar un rol por canción
		for(Asignacion a: can.getAsignaciones()) {
			if(a.artista.equals(ar) && a.cancion.equals(can))
				throw new Exception("El artista ya tiene asignado un rol para la cancion " + can.getTitulo());
		}
		//si es artista candidato, verifico que no se pase del max de canciones y que ya haya ocupado el rol pedido
		if(ar instanceof ArtistaCandidato candidato) {
			if(candidato.puedeTocar()) {
				if(!candidato.getRolesHistoricos().contains(rol)) {
					throw new Exception("El artista nunca ha ocupado ese rol. Debe entrenarse.");
				}
			}
			else throw new Exception("El artista ha alcanzado el máximo de canciones dispuesto a tocar.");
		}
		this.artista = ar;
		this.cancion = can;
		this.rol = rol;
		can.getAsignaciones().add(this);
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
}
