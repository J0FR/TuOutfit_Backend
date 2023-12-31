package co.edu.uniandes.dse.outfits.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/*
 * DTO de Detalles de Outfit
 * @author 
 */
@Getter
@Setter
public class OutfitDetailDTO extends OutfitDTO {
    private List <PrendaDTO> prendas = new ArrayList<>();
	private List <ComentarioDTO> comentarios = new ArrayList<>();
}