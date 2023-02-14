package co.edu.uniandes.dse.outfits.entities;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity

public class PrendaEntity extends ProductoEntity {

    private String url_sitio_web_compra;

    @ManyToOne
    private ComentarioEntity commentario;
    
    @ManyToMany
    private List<OutfitEntity> outfits = new ArrayList<>();

    @ManyToOne
    private MarcaEntity marca;


    

}
