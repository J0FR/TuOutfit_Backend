package co.edu.uniandes.dse.outfits.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/*
 * DTO de Detalles de Outfit
 * @author Álvaro Bacca (c4ts0up)
 */
@Getter
@Setter
public class UsuarioDetailDTO extends UsuarioDTO {
    private List <ComentarioDTO> comentarios = new ArrayList<>();
}