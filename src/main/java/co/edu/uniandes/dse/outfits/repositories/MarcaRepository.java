package co.edu.uniandes.dse.outfits.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.outfits.entities.MarcaEntity;
import co.edu.uniandes.dse.outfits.entities.TiendaFisicaEntity;
import co.edu.uniandes.dse.outfits.entities.PrendaEntity;

@Repository

public interface MarcaRepository extends JpaRepository<MarcaEntity, Long> {

}