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

/**
 * Clase que representa un usuario en la persistencia
 * 
 * @author Daniel Pedroza
 **/

@Getter
@Setter
@Entity
public class UsuarioEntity extends BaseEntity {

    private String nombre;
    private String genero;
    private Integer edad;
    private String email;

    @OneToMany(mappedBy = "autor", fetch = FetchType.LAZY)
    @PodamExclude
    private List<ComentarioEntity> comentarios = new ArrayList<>();

    @ManyToMany(mappedBy = "usuarios", fetch = FetchType.LAZY)
    @PodamExclude
    private List<OutfitEntity> favoritos = new ArrayList<>();

}
