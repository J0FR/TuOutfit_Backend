package co.edu.uniandes.dse.outfits.services;

import lombok.extern.slf4j.Slf4j;
import java.util.List;
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

@Slf4j
@Service
public class OutfitComentarioService {
    @Autowired
	private OutfitRepository outfitRepository;

	@Autowired
	private ComentarioRepository comentarioRepository;

    /**
	 * Agregar un comentario a un outfit
	 *
	 * @param ourfitId  El id premio a guardar
	 * @param comentarioId El id del autor al cual se le va a guardar el premio.
	 * @return El comentario agregado al outfit.
	 * @throws EntityNotFoundException
	 */
	@Transactional
	public ComentarioEntity addComment(Long outfitId, Long comentarioId) throws EntityNotFoundException {
		log.info("Inicia proceso de asociar el outfit con id = {0} al comentario con id = " + outfitId, comentarioId);
		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
		if (outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

		Optional<ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioId);
		if (comentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);

		outfitEntity.get().addComentario(comentarioEntity.get());
		log.info("Termina proceso de asociar el outfit con id = {0} al comentario con id = {1}", outfitId, comentarioId);
		return comentarioEntity.get();
	}

    /**
	 *
	 * Obtener los comentarios pertenecientes al id del outfit dado.
	 *
	 * @param outfitId id del premio a ser buscado.
	 * @return los comentarios asociados.
	 * @throws EntityNotFoundException
	 */
	@Transactional
	public List<ComentarioEntity> getComentarios(Long outfitId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar los comentarios del outfit con id = {0}", outfitId);
		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
		if (outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

        List<ComentarioEntity> comentarioEntity = outfitEntity.get().getComentarios();

		if (comentarioEntity == null)
			throw new EntityNotFoundException("The comment was not found");

		log.info("Termina proceso de consultar los comentarios del outfit con id = {0}", outfitId);
		return comentarioEntity;
	}

    /**
	 * Borrar el comentario de un outfit
	 *
	 * @param outfitId El id del outfit.
	 * @param comentarioId El comentario que se desea borrar del outfit.
	 * @throws EntityNotFoundException si el premio no tiene autor
	 */
	@Transactional
	public void removeComentario(Long outfitId, Long comentarioId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar el comentario del outfit con id = {0}", outfitId);
		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
		if (outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

        List<ComentarioEntity> comentariosEntity = outfitEntity.get().getComentarios();
        ComentarioEntity comentarioToFind = comentarioRepository.findById(comentarioId).get();

        if (comentarioToFind != null && comentariosEntity.contains(comentarioToFind)){
            int index = comentariosEntity.indexOf(comentarioToFind);
            comentariosEntity.remove(index);
        } else {
            throw new EntityNotFoundException("Comment not found.");
        }

		log.info("Termina proceso de borrar el autor del premio con id = " + comentarioId);
	}
}




