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
public class Usuario extends BaseEntity {

    private String nombre;
    private String genero;
    private int edad;
    private String email;

}
