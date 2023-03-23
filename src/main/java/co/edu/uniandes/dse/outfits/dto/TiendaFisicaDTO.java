package co.edu.uniandes.dse.outfits.dto;

import lombok.Getter;
import lombok.Setter;

/*
 * DTO de TiendaFisica
 * @author 
 */
@Getter
@Setter
public class TiendaFisicaDTO {
    private Long id;
    private String nombre;
    private String horarios;
    private UbicacionDTO ubicacion;
    private MarcaDTO marca;
}
