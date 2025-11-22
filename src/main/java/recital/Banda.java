package recital;

/**
 * Banda: es el que identifica las bandas y ademas, permite que se haga la funcion de comparar banddas historicas de artista Candidato
 */

public class Banda {
    private String nombre;

    public Banda(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Banda banda = (Banda) o;
        return nombre != null ? nombre.equalsIgnoreCase(banda.nombre) : banda.nombre == null;
    }

    @Override
    public int hashCode() {
        return nombre != null ? nombre.toLowerCase().hashCode() : 0;
    }
}
