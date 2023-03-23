package co.edu.uniandes.dse.outfits.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.uniandes.dse.outfits.entities.UsuarioEntity;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {}
