package co.edu.uniandes.dse.outfits.entities;

import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComentarioEntity other = (ComentarioEntity) o;

        // los IDs son únicos en la tabla de la clase
        return Objects.equals(id, other.id);      
    }

    
    @Override
    public int hashCode() {
        // los IDs son únicos en la tabla de la clase
        return Objects.hash(id);
    }
}
