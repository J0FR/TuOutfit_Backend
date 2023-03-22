package co.edu.uniandes.dse.outfits.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/*
 * DTO de Detalles de Prenda
 * @author 
 */
@Getter
@Setter
public class PrendaDetailDTO extends PrendaDTO {
    private List <OutfitDTO> outfits = new ArrayList<>();
    private List <ComentarioDTO> comentarios = new ArrayList<>();
}
