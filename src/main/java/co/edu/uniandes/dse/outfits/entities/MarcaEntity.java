package co.edu.uniandes.dse.outfits.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

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
    private String url_sitio_web;
    private String logo; /* No es string sino es imagen */
    private String detalle_de_marca;


    @OneToMany(mappedBy = "marca", fetch = FetchType.LAZY)
    private List<PrendaEntity> prendas = new ArrayList<>();

    @OneToMany(mappedBy = "marca", fetch = FetchType.LAZY)
    private List<TiendaFisicaEntity> tiendas_fisicas = new ArrayList<>();


}
