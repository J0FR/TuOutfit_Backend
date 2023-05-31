package co.edu.uniandes.dse.outfits.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Clase que representa un marca en la persistencia
 * 
 * @author Daniel Pedroza
 **/

@Getter
@Setter
@Entity
public class MarcaEntity extends BaseEntity {

    private String nombre;
    private String urlSitioWeb;
    private String logo; /* No es string sino es imagen */
    private String detalleDeMarca;


    @OneToMany(mappedBy = "marca", fetch = FetchType.LAZY)
    @PodamExclude
    private List<PrendaEntity> prendas = new ArrayList<>();

    @OneToMany(mappedBy = "marca", fetch = FetchType.LAZY)
    @PodamExclude
    private List<TiendaFisicaEntity> tiendasFisicas = new ArrayList<>();

    public void addPrenda(PrendaEntity prenda) {
        prendas.add(prenda);
    }
}
