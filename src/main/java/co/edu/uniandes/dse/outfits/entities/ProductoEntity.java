package co.edu.uniandes.dse.outfits.entities;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public abstract class ProductoEntity extends BaseEntity {

    private String imagen;
    private String nombre;
    private Integer precio;
    private Color colores;
    private Genero genero;
    private Ocacion ocasiones;
    private RangoEdad rangoEdad;
    private String talla;

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
        NIÑO,
        ADOLECENTE,
        JOVEN,
        ADULTO,
        ABUELOS
    }

    public enum Ocacion {
        BODA,
        FIESTA,
        CENA,
        CASUAL,
        FORMAL,
        GRADO
    }
}
