package co.edu.uniandes.dse.outfits.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OutfitDTO {
    private Long id;
    private String nombre;
    private Integer precio;
    private Color colores;
    private Genero genero;
    private Ocasion ocasiones;
    private RangoEdad rangoEdad;
    private String imagen;
    private String talla;
    private String descripcion;

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
}
