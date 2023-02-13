package co.edu.uniandes.dse.outfits.entities;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import net.bytebuddy.implementation.bytecode.constant.IntegerConstant;

/**
 * Clase que representa un usuario en la persistencia
 * 
 * @author Daniel Pedroza
 **/

@Getter
@Setter
@Entity
public class Marca extends BaseEntity {

    private String nombre;
    private String url_sitio_web;
    private String logo; /* No es string sino es imagen */
    private String detalle_de_marca;

}