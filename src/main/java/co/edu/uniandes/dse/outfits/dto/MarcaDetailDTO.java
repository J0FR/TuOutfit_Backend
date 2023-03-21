package co.edu.uniandes.dse.outfits.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/*
 * DTO de Detalles de Marca
 * @author 
 */
@Getter
@Setter
public class MarcaDetailDTO extends MarcaDTO {
    private List <TiendaFisicaDTO> tiendasFisicas = new ArrayList<>();
    private List <PrendaDTO> prendas = new ArrayList<>();
}
