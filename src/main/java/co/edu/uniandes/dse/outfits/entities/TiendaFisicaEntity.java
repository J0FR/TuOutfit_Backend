package co.edu.uniandes.dse.outfits.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import uk.co.jemos.podam.common.PodamExclude;

@Getter
@Setter
@Entity
public class TiendaFisicaEntity extends BaseEntity {
    private String nombre;
    private String horarios;

    @OneToOne(cascade = CascadeType.ALL)
    @PodamExclude
    private UbicacionEntity ubicacion;

    @ManyToOne
    @PodamExclude
    private MarcaEntity marca;
}