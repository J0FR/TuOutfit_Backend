package co.edu.uniandes.dse.outfits.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrendaDTO {
    private Long id;
    private String url_sitio_web_compra;
    private String imagen;
    private String nombre;
    private Integer precio;
    private Color colores;
    private Genero genero;
    private Ocacion ocaciones;
    private RangoEdad rango_edad;
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
