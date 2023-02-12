package co.edu.uniandes.dse.outfits.entities;

import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class TiendaFisicaEntity extends BaseEntity {
    private String nombre;
    private String horarios;
}