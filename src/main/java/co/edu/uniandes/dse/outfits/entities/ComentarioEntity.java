package co.edu.uniandes.dse.outfits.entities;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
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
    
}
