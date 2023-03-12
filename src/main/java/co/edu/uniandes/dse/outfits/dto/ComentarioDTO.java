package co.edu.uniandes.dse.outfits.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComentarioDTO {
    private Long id;
    private String titulo;
    private Integer calificacion;
    private String mensaje;
}
