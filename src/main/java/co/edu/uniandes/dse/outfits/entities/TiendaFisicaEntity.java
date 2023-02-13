package co.edu.uniandes.dse.outfits.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class TiendaFisicaEntity extends BaseEntity {
    private String nombre;
    private String horarios;

    @OneToOne
    private UbicacionEntity ubicacion;

    @ManyToOne
    private MarcaEntity marca;
}