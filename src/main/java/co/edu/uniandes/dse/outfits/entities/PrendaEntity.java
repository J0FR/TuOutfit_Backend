package co.edu.uniandes.dse.outfits.entities;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;
import uk.co.jemos.podam.common.PodamExclude;

@Getter
@Setter
@Entity

public class PrendaEntity extends ProductoEntity {

    private String url_sitio_web_compra;


   
    
    @OneToMany(mappedBy = "prenda", fetch = FetchType.LAZY)
    @PodamExclude
    private List<ComentarioEntity> comentarios= new ArrayList<>();;



    
    @ManyToMany
    @PodamExclude
    private List<OutfitEntity> outfits = new ArrayList<>();

    @ManyToMany
    @PodamExclude
    private List<PrendaEntity> prendas_asociadas = new ArrayList<>();

    @ManyToOne
    @PodamExclude
    private MarcaEntity marca;

    public void addComentario(ComentarioEntity comentario) {
        comentarios.add(comentario);
    }


    

}
