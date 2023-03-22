package co.edu.uniandes.dse.outfits.services;

import java.util.List;
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

@Slf4j
@Service
public class PrendaComentarioService {

    @Autowired
    private PrendaRepository prendaRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    /**
	 * Agregar un comentario a una prenda
	 *
	 * @param prendaId  El id premio a guardar
	 * @param comentarioId El id del autor al cual se le va a guardar el premio.
	 * @return El premio que fue agregado al autor.
	 * @throws EntityNotFoundException
	 */

    @Transactional
    public ComentarioEntity addComentario(Long comentarioId, Long prendaId) throws EntityNotFoundException {
        log.info("Inicia proceso de asociarle el comentario co id ={0} a la prenda con id = " + prendaId, comentarioId);
        Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);
        Optional<ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioId);

        if (prendaEntity.isEmpty()) 
            throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);
        
        if (comentarioEntity.isEmpty()) 
            throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);

        prendaEntity.get().addComentario(comentarioEntity.get());
        log.info("Termina proceso de asociar el comentario co id ={0} a la prenda con id = {1}", comentarioId, prendaId);
        return comentarioEntity.get();
    }
    /**
	 *
	 * Obtener un author por medio del id del premio.
	 *
	 * @param prendaId id del premio a ser buscado.
	 * @return el autor solicitado.
	 * @throws EntityNotFoundException
	 */

     @Transactional
     public List<ComentarioEntity> getComentarios(Long prendaId)  throws EntityNotFoundException {
		log.info("Inicia proceso de consultar el usuario del outfit con id = {0}", prendaId);
		Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);
		if (prendaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);

		List<ComentarioEntity> comentarioEntity = prendaEntity.get().getComentarios();

		if (comentarioEntity.isEmpty())
			    throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);

		log.info("Termina proceso de consultar el comentario de la prenda con id = {0}", prendaId);
		return comentarioEntity;
	}
    /**
	 *
	 * Obtener un author por medio del id del premio.
	 *
	 * @param prendaId id de la prenda
	 * @param comentarioId el cometario que desea borrar de prenda
	 * @throws EntityNotFoundException
	 */
    @Transactional
	public void removeComentario(Long prendaId, Long comentarioId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar el usuario del outfit con id = {0}", prendaId);
		Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);
		if (prendaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);

        List<ComentarioEntity> comentarioEntity = prendaEntity.get().getComentarios();
        Optional<ComentarioEntity> comentarioToFind = comentarioRepository.findById(comentarioId);

        if (comentarioToFind.isEmpty()) {
			throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);
		}

        if (comentarioEntity.contains(comentarioToFind.get())){
            int index = comentarioEntity.indexOf(comentarioToFind.get());
            comentarioEntity.remove(index);
        } else {
            throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);
        }

		log.info("Termina proceso de borrar el comentario de la prenda con id = " + comentarioId);
	}



    
}
