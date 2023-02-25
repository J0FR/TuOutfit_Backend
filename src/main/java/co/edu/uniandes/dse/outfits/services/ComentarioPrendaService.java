package co.edu.uniandes.dse.outfits.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.outfits.entities.ComentarioEntity;
import co.edu.uniandes.dse.outfits.entities.PrendaEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.ErrorMessage;
import co.edu.uniandes.dse.outfits.repositories.ComentarioRepository;
import co.edu.uniandes.dse.outfits.repositories.PrendaRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase que implementa la conexión con la persistencia 
 * para la relación entre la entidad de Comentario y 
 * Prenda.
 * 
 * @author Álvaro A. Bacca (c4ts0up)
 */

@Slf4j
@Service
public class ComentarioPrendaService {
    
    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private PrendaRepository prendaRepository;

    /**
     * Asocia una Prenda existente a un Comentario
     * 
     * @param comentarioId Identificador del Comentario al cual se le va a asociar el Prenda
     * @param prendaId Identificador de la Prenda que se asocia
     * @return Instancia de PrendaEntity que fue asociada a Comentario
     */
    @Transactional
    public PrendaEntity setPrenda(Long comentarioId, Long prendaId) throws EntityNotFoundException {
        log.info("Inicia asociación de prenda con id = {0}, a comentario con id = {1}", prendaId, comentarioId);

        Optional <ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioId);
        Optional <PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);

        // no se encontró el comentario
        if (comentarioEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);
        }
        // no se encontró la prenda
        if (prendaEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);
        }

        prendaEntity.get().getComentarios().add(comentarioEntity.get());
        log.info("Finaliza asociación de prenda a comentario");
        return prendaEntity.get();
    }


    /**
     * Obtiene la Prenda asociada al Comentario
     * 
     * @param comentarioId Identificador de la instancia de Comentario
     * @return Instancia de PrendaEntity asociada a la instancia de Comentario
     */
    @Transactional
    public PrendaEntity getPrenda(Long comentarioId) throws EntityNotFoundException {
        log.info("Inicia consulta de prenda del comentario con id = {0}", comentarioId);

        Optional <ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioId);

        // no encontró el comentario
        if (comentarioEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);
        }

        log.info("Finaliza consulta de prenda del comentario");
        return comentarioEntity.get().getPrenda();
    }

    /**
     * Desasocia la Prenda existente del Comentario Existente
     * @param comentarioId Comentario que tiene asociado una Prenda
     */
    @Transactional
    public void removePrenda(Long comentarioId) throws EntityNotFoundException {
        log.info("Inicia borrado de prenda del comentario con id = {0}", comentarioId);

        Optional <ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioId);

        // no encontró el comentario
        if (comentarioEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);
        }

        PrendaEntity prendaEntity = comentarioEntity.get().getPrenda();

        // el comentario no tiene un prenda asociado
        if (prendaEntity == null) {
            throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);
        }

        // desasocia el prenda del comentario
        comentarioEntity.get().setAutor(null);
        log.info("Finaliza borrado de prenda del comentario");
        return;
    }
}
