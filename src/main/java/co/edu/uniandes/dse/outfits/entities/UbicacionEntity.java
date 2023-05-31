package co.edu.uniandes.dse.outfits.entities;

import java.util.Objects;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UbicacionEntity extends BaseEntity {
    private float latitud;
    private float longitud;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UbicacionEntity other = (UbicacionEntity) o;

        // los IDs son únicos en la tabla de la clase
        return Objects.equals(id, other.id);      
    }


    @Override
    public int hashCode() {
        // los IDs son únicos en la tabla de la clase
        return Objects.hash(id);
    }
}
