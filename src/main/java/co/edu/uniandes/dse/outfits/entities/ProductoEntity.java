package co.edu.uniandes.dse.outfits.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public abstract class ProductoEntity extends BaseEntity {

    protected String imagen;
    protected String nombre;
    protected Integer precio;
    protected Color colores;
    protected Genero genero;
    protected Ocasion ocasiones;
    protected RangoEdad rangoEdad;
    protected String talla;

    public enum Color {
        ROJO,
        AZUL,
        DORADO,
        VERDE,
        PLATA,
        AMARILLO,
        CAFE,
        BLANCO,
        NEGRO
    }

    public enum Genero {
        HOMBRE,
        MUJER,
        UNISEX
    }

    public enum RangoEdad {
        BEBE,
        NINHO,
        ADOLECENTE,
        JOVEN,
        ADULTO,
        ABUELOS
    }

    public enum Ocasion {
        BODA,
        FIESTA,
        CENA,
        CASUAL,
        FORMAL,
        GRADO
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductoEntity other = (ProductoEntity) o;

        // los IDs son únicos en la tabla de la clase
        return Objects.equals(id, other.id);      
    }

    
    @Override
    public int hashCode() {
        // los IDs son únicos en la tabla de la clase
        return Objects.hash(id);
    }
}
