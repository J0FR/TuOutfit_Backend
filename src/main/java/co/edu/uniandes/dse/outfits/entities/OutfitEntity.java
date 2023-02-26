package co.edu.uniandes.dse.outfits.entities;

import java.util.ArrayList;
import java.util.List;

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

    public void addComentario(ComentarioEntity comentario) {
        comentarios.add(comentario);
    }
}



