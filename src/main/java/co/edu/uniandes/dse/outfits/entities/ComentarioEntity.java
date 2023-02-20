package co.edu.uniandes.dse.outfits.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import uk.co.jemos.podam.common.PodamExclude;
/**
 * Clase que representa un comentario en la persistencia
 * 
 * @author: Álvaro Bacca
 */

@Getter
@Setter
@Entity
public class ComentarioEntity extends BaseEntity {
    private String titulo;
    private Integer calificacion;
    private String mensaje;

    @ManyToOne
    @PodamExclude
    private PrendaEntity prenda;

    @ManyToOne
    @PodamExclude
    private OutfitEntity outfit;

    @ManyToOne
    @PodamExclude
    private UsuarioEntity autor;
}
