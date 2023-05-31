package co.edu.uniandes.dse.outfits.dto;

import lombok.Getter;
import lombok.Setter;

/*
 * DTO de Marca
 * @author 
 */
@Getter
@Setter
public class MarcaDTO {
    private Long id;
    private String nombre;
    private String urlSitioWeb;
    private String logo;
    private String detalleDeMarca;
}
