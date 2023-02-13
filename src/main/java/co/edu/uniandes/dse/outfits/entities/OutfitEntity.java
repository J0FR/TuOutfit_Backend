package co.edu.uniandes.dse.outfits.entities;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class OutfitEntity extends BaseEntity {
    private String descripcion;
}






