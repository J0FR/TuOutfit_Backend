package co.edu.uniandes.dse.outfits.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;
import uk.co.jemos.podam.common.PodamExclude;

@Getter
@Setter
@Entity
public class OutfitEntity extends ProductoEntity {
    private String descripcion;

    @OneToMany(mappedBy = "outfit", fetch = FetchType.LAZY)
    @PodamExclude
    private List<ComentarioEntity> comentarios = new ArrayList<>();

    @ManyToMany(mappedBy="outfits",fetch = FetchType.LAZY)
    @PodamExclude
    private List<PrendaEntity> prendas = new ArrayList<>();

    @ManyToMany
    @PodamExclude
    private List<UsuarioEntity> usuarios = new ArrayList<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OutfitEntity other = (OutfitEntity) o;

        // los IDs son únicos en la tabla de la clase
        return Objects.equals(id, other.id);      
    }

    
    @Override
    public int hashCode() {
        // los IDs son únicos en la tabla de la clase
        return Objects.hash(id);
    }
}



