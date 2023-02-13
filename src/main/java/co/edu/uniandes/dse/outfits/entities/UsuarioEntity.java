package co.edu.uniandes.dse.outfits.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
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
public class UsuarioEntity extends BaseEntity {

    private String nombre;
    private String genero;
    private int edad;
    private String email;

    @OneToMany
    private List<ComentarioEntity> comentarios = new ArrayList<>();

    @OneToMany
    private List<OutfitEntity> favoritos = new ArrayList<>();

}
