package co.edu.uniandes.dse.outfits.dto;

import lombok.Getter;
import lombok.Setter;

/*
 * DTO de Usuario
 * @author Álvaro Bacca (c4ts0up)
 */
@Getter
@Setter
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String genero;
    private Integer edad;
    private String email;
}
