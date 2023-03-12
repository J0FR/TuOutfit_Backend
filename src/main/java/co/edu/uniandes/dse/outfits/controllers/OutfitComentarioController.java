package co.edu.uniandes.dse.outfits.controllers;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.outfits.dto.ComentarioDTO;
import co.edu.uniandes.dse.outfits.dto.ComentarioDetailDTO;
import co.edu.uniandes.dse.outfits.entities.ComentarioEntity;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.services.OutfitComentarioService;

@RestController
@RequestMapping("/outfits")
public class OutfitComentarioController {
    @Autowired
	private OutfitComentarioService outfitComentarioService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Asocia un comentario existente con un outfit existente
	 *
	 * @param comentarioId El ID del comentario que se va a asociar
	 * @param outfitId   El ID del outfit al cual se le va a asociar el comentario
	 * @return JSON {@link ComentarioDetailDTO} - El comentario asociado.
	 * @throws co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException
	 */
	@PostMapping(value = "/{outfitId}/comentarios/{comentarioId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ComentarioDetailDTO addComentario(@PathVariable("comentarioId") Long comentarioId, @PathVariable("outfitId") Long outfitId)
			throws EntityNotFoundException, co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException {
		ComentarioEntity comentarioEntity = outfitComentarioService.addComentario(outfitId, comentarioId);
		return modelMapper.map(comentarioEntity, ComentarioDetailDTO.class);
	}

	/**
	 * Busca y devuelve el comentario con el ID recibido en la URL, relativo a un outfit.
	 *
	 * @param comentarioId El ID del comentario que se busca
	 * @param outfitId   El ID del outfit del cual se busca el comentario
	 * @return {@link ComentarioDetailDTO} - El comentario encontrado en el outfit.
	 * @throws co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException
	 */
	@GetMapping(value = "/{outfitId}/comentarios/{comentarioId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ComentarioDetailDTO getComentario(@PathVariable("comentarioId") Long comentarioId, @PathVariable("outfitId") Long outfitId)
			throws EntityNotFoundException, IllegalOperationException, co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException {
        ComentarioEntity authorEntity = outfitComentarioService.getComentario(outfitId, comentarioId);
		return modelMapper.map(authorEntity, ComentarioDetailDTO.class);
	}

	/**
	 * Actualiza la lista de comentarios de un outfit con la lista que se recibe en el
	 * cuerpo.
	 *
	 * @param outfitId  El ID del outfit al cual se le va a asociar la lista de comentarios
	 * @param comentarios JSONArray {@link ComentarioDTO} - La lista de comentarios que se desea
	 *                guardar.
	 * @return JSONArray {@link ComentarioDetailDTO} - La lista actualizada.
	 * @throws co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException
	 */
	@PutMapping(value = "/{outfitId}/comentarios")
	@ResponseStatus(code = HttpStatus.OK)
	public List<ComentarioDetailDTO> addComentarios(@PathVariable("outfitId") Long outfitId, @RequestBody List<ComentarioDTO> comentarios)
			throws EntityNotFoundException, co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException {
		List<ComentarioEntity> entities = modelMapper.map(comentarios, new TypeToken<List<ComentarioEntity>>() {
		}.getType());
		List<ComentarioEntity> comentariosList = outfitComentarioService.replaceComentarios(outfitId, entities);
		return modelMapper.map(comentariosList, new TypeToken<List<ComentarioDetailDTO>>() {
		}.getType());
	}

	/**
	 * Busca y devuelve todos los comentarios que existen en un outfit.
	 *
	 * @param outfitId El ID del outfit del cual se buscan los comentarios
	 * @return JSONArray {@link ComentarioDetailDTO} - Los comentarios encontrados en el
	 *         outfit. Si no hay ninguno retorna una lista vacía.
	 * @throws co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException
	 */
	@GetMapping(value = "/{outfitId}/comentarios")
	@ResponseStatus(code = HttpStatus.OK)
	public List<ComentarioDetailDTO> getComentarios(@PathVariable("outfitId") Long outfitId) throws EntityNotFoundException, co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException {
		List<ComentarioEntity> comentarioEntity = outfitComentarioService.getComentarios(outfitId);
		return modelMapper.map(comentarioEntity, new TypeToken<List<ComentarioDetailDTO>>() {
		}.getType());
	}

	/**
	 * Elimina la conexión entre el comentario y el outfit recibidos en la URL.
	 *
	 * @param outfitId   El ID del outfit al cual se le va a desasociar el comentario
	 * @param comentarioId El ID del comentario que se desasocia
	 * @throws co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException
	 */
	@DeleteMapping(value = "/{outfitId}/comentarios/{comentarioId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeComentario(@PathVariable("comentarioId") Long comentarioId, @PathVariable("outfitId") Long outfitId)
			throws EntityNotFoundException, co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException {
		outfitComentarioService.removeComentario(outfitId, comentarioId);
	}
}
