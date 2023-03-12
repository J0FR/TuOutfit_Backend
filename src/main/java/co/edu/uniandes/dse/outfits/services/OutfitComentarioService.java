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
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.repositories.ComentarioRepository;
import co.edu.uniandes.dse.outfits.repositories.OutfitRepository;

@Slf4j
@Service
public class OutfitComentarioService {
	@Autowired
	private ComentarioRepository comentarioRepository;

	@Autowired
	private OutfitRepository outfitRepository;
	
	/**
	 * Agregar un Comentario a la Outfit
	 *
	 * @param comentarioId      El id Comentario a guardar
	 * @param outfitId El id de la Outfit en la cual se va a guardar el Comentario.
	 * @return El Comentario creado.
	 * @throws EntityNotFoundException 
	 */
	@Transactional
	public ComentarioEntity addComentario(Long outfitId, Long authorId) throws EntityNotFoundException {
		log.info("Inicia proceso de asociarle un autor al libro con id = {0}", outfitId);
		Optional<ComentarioEntity> comentarioEntity = comentarioRepository.findById(authorId);
		if (comentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);

		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
		if (outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

		outfitEntity.get().getComentarios().add(comentarioEntity.get());
		comentarioEntity.get().setOutfit(outfitEntity.get());
		log.info("Termina proceso de asociarle un autor al libro con id = {0}", outfitId);
		return comentarioEntity.get();
	}

	/**
	 * Retorna todos los comentarios asociados a una Outfit
	 *
	 * @param outfitId El ID de la Outfit buscada
	 * @return La lista de Comentarios de la Outfit
	 * @throws EntityNotFoundException si la Outfit no existe
	 */
	@Transactional
	public List<ComentarioEntity> getComentarios(Long outfitId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar los Comentarios asociados a la Outfit con id = {0}", outfitId);
		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
		if(outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);
		
		return outfitEntity.get().getComentarios();
	}

	/**
	 * Retorna un comentario asociado a una Outfit
	 *
	 * @param outfitId El id de la Outfit a buscar.
	 * @param comentarioId      El id del Comentario a buscar
	 * @return El Comentario encontrado dentro de la Outfit.
	 * @throws EntityNotFoundException Si el Comentario no se encuentra en la Outfit
	 * @throws IllegalOperationException Si el Comentario no está asociado a la Outfit
	 */
	@Transactional
	public ComentarioEntity getComentario(Long outfitId, Long comentarioId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de consultar el Comentario con id = {0} de la Outfit con id = " + outfitId, comentarioId);
		
		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
		if(outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);
		
		Optional<ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioId);
		if(comentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);
				
		log.info("Termina proceso de consultar el Comentario con id = {0} de la Outfit con id = " + outfitId, comentarioId);
		
		if(!outfitEntity.get().getComentarios().contains(comentarioEntity.get()))
			throw new IllegalOperationException("The comentario is not associated to the Outfit");
		
		return comentarioEntity.get();
	}

	/**
	 * Remplazar comentarios de una Outfit
	 *
	 * @param comentarios        Lista de Comentarios que serán los de la Outfit.
	 * @param outfitId El id de la Outfit que se quiere actualizar.
	 * @return La lista de Comentarios actualizada.
	 * @throws EntityNotFoundException Si la Outfit o un Comentario de la lista no se encuentran
	 */
	@Transactional
	public List<ComentarioEntity> replaceComentarios(Long outfitId, List<ComentarioEntity> comentarios) throws EntityNotFoundException {
		log.info("Inicia proceso de actualizar la Outfit con id = {0}", outfitId);
		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
		if(outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);
		
		for(ComentarioEntity comentario : comentarios) {
			Optional<ComentarioEntity> b = comentarioRepository.findById(comentario.getId());
			if(b.isEmpty())
				throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);
			
			b.get().setOutfit(outfitEntity.get());
		}		
		return comentarios;
	}

	@Transactional
	/**
	 * Desasocia un Comentario existente de un Outfit existente
	 *
	 * @param outfitId   
	 * @param comentarioId 
	 */
	public void removeComentario(Long outfitId, Long comentarioId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar un comentario del outfit con id = {0}", outfitId);
		Optional<ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioId);
		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);

		if (comentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);

		if (outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

		outfitEntity.get().getComentarios().remove(comentarioEntity.get());

		log.info("Termina proceso de borrar un comentario del outfit con id = {0}", outfitId);
	}
}




