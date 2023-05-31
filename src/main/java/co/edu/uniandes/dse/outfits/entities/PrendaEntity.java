package co.edu.uniandes.dse.outfits.entities;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    private String urlSitioWebCompra;

    @OneToMany(mappedBy = "prenda", fetch = FetchType.LAZY)
    @PodamExclude
    private List<ComentarioEntity> comentarios= new ArrayList<>();

    @ManyToMany
    @PodamExclude
    private List<OutfitEntity> outfits = new ArrayList<>();

    @ManyToMany
    @PodamExclude
    private List<PrendaEntity> prendasAsociadas = new ArrayList<>();

    @ManyToOne
    @PodamExclude
    private MarcaEntity marca;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PrendaEntity other = (PrendaEntity) o;

        // los IDs son únicos en la tabla de la clase
        return Objects.equals(id, other.id);      
    }

    
    @Override
    public int hashCode() {
        // los IDs son únicos en la tabla de la clase
        return Objects.hash(id);
    }


    public void addComentario(ComentarioEntity comentario) {
        comentarios.add(comentario);
    }
}
