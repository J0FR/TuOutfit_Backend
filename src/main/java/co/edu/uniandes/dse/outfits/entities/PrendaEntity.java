package co.edu.uniandes.dse.outfits.entities;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity

public class PrendaEntity extends ProductoEntity {

    private String url_sitio_web_compra;

    //@ManyToOne
    //private ComentarioEntity commentario;
    @OneToMany
    private List<ComentarioEntity> comentarios = new ArrayList<>();
    
    @ManyToMany
    private List<OutfitEntity> outfits = new ArrayList<>();

    @ManyToOne
    private MarcaEntity marca;


    

}
