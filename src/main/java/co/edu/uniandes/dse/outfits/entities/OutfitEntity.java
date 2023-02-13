package co.edu.uniandes.dse.outfits.entities;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany
    private List<ComentarioEntity> comentarios = new ArrayList<>();


    @ManyToMany(mappedBy="outfits",fetch = FetchType.LAZY)
    private List<PrendaEntity> prendas = new ArrayList<>();

}



