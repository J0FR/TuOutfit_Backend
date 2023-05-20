package co.edu.uniandes.dse.outfits.entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;
import uk.co.jemos.podam.common.PodamExclude;

@Getter
@Setter
@Entity
public class UbicacionEntity extends BaseEntity {
    private float latitud;
    private float longitud;

    @OneToOne
    @PodamExclude
    private TiendaFisicaEntity tiendaFisica;
}
