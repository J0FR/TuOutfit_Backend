package co.edu.uniandes.dse.outfits.entities;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UbicacionEntity extends BaseEntity {
    private float latitud;
    private float longitud;
}
