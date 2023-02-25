package co.edu.uniandes.dse.outfits.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.outfits.entities.ComentarioEntity;
import co.edu.uniandes.dse.outfits.entities.OutfitEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.ErrorMessage;
import co.edu.uniandes.dse.outfits.repositories.ComentarioRepository;
import co.edu.uniandes.dse.outfits.repositories.OutfitRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase que implementa la conexión con la persistencia 
 * para la relación entre la entidad de Comentario y 
 * Outfit.
 * 
 * @author Álvaro A. Bacca (c4ts0up)
 */

@Slf4j
@Service
public class ComentarioOutfitService {
    
    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private OutfitRepository outfitRepository;

    /**
     * Asocia un Outfit existente a un Comentario
     * 
     * @param comentarioId Identificador del Comentario al cual se le va a asociar el Outfit
     * @param outfitId Identificador del Outfit que se asocia
     * @return Instancia de OutfitEntity que fue asociada a Comentario
     */
    @Transactional
    public OutfitEntity setOutfit(Long comentarioId, Long outfitId) throws EntityNotFoundException {
        log.info("Inicia asociación del outfit con id = {0}, a comentario con id = {1}", outfitId, comentarioId);

        Optional <ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioId);
        Optional <OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);

        // no se encontró el comentario
        if (comentarioEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);
        }
        // no se encontró el outfit
        if (outfitEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);
        }

        outfitEntity.get().getComentarios().add(comentarioEntity.get());
        log.info("Finaliza asociación del outfit a comentario");
        return outfitEntity.get();
    }


    /**
     * Obtiene el Outfit asociado al Comentario
     * 
     * @param comentarioId Identificador de la instancia de Comentario
     * @return Instancia de OutfitEntity asociada a la instancia de Comentario
     */
    @Transactional
    public OutfitEntity getOutfit(Long comentarioId) throws EntityNotFoundException {
        log.info("Inicia consulta del outfit del comentario con id = {0}", comentarioId);

        Optional <ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioId);

        // no encontró el comentario
        if (comentarioEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);
        }

        log.info("Finaliza consulta de outfit del comentario");
        return comentarioEntity.get().getOutfit();
    }

    /**
     * Desasocia el Outfit existente del Comentario Existente
     * @param comentarioId Comentario que tiene asociado una Outfit
     */
    @Transactional
    public void removeOutfit(Long comentarioId) throws EntityNotFoundException {
        log.info("Inicia borrado del outfit del comentario con id = {0}", comentarioId);

        Optional <ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioId);

        // no encontró el comentario
        if (comentarioEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);
        }

        OutfitEntity outfitEntity = comentarioEntity.get().getOutfit();

        // el comentario no tiene un outfit asociado
        if (outfitEntity == null) {
            throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);
        }

        // desasocia el outfit del comentario
        comentarioEntity.get().setAutor(null);
        log.info("Finaliza borrado del outfit del comentario");
        return;
    }
}
