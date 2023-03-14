package co.edu.uniandes.dse.outfits.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComentarioDetailDTO extends ComentarioDTO {
    private PrendaDTO prendas;
	private OutfitDTO outfit;
}