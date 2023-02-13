package co.edu.uniandes.dse.outfits.entities;


import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity

public class PrendaEntity extends BaseEntity {

    private String url_sitio_web_compra;
    private String imagen;
    private String nombre;
    private Integer precio;
    private Color colores;
    private Genero genero;
    private Ocacion ocaciones;
    private RangoEdad rango_edad;
    private String foto;
    private String talla;
    

    @ManyToOne
    private ComentarioEntity commentario;
    
    @ManyToMany(mappedBy="outfit",fetch= FetchType.LAZY)
    private ArrayList<OutfitEntity> outfits = new ArrayList<>();

    @OneToMany(mappedBy="marca",fetch=FetchType.LAZY)
    private ArrayList<MarcaEntity> marcas = new ArrayList<>();

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
