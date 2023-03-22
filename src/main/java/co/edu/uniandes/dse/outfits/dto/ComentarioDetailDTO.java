package co.edu.uniandes.dse.outfits.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// TODO: se debería borrar
public class ComentarioDetailDTO extends ComentarioDTO {
    private PrendaDTO prendas;
	private OutfitDTO outfit;
}