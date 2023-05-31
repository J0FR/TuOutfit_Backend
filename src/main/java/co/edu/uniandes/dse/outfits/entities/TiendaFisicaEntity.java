package co.edu.uniandes.dse.outfits.entities;

import java.util.Objects;

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

    @OneToOne
    @PodamExclude
    private UbicacionEntity ubicacion;

    @ManyToOne
    @PodamExclude
    private MarcaEntity marca;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TiendaFisicaEntity other = (TiendaFisicaEntity) o;

        // los IDs son únicos en la tabla de la clase
        return Objects.equals(id, other.id);      
    }

    
    @Override
    public int hashCode() {
        // los IDs son únicos en la tabla de la clase
        return Objects.hash(id);
    }
}