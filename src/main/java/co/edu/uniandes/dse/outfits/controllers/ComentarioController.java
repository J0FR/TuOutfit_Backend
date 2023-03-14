package co.edu.uniandes.dse.outfits.controllers;

import java.util.List;
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
import co.edu.uniandes.dse.outfits.dto.ComentarioDetailDTO;
import co.edu.uniandes.dse.outfits.entities.ComentarioEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.services.ComentarioService;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {
    @Autowired
	private ComentarioService comentarioService;

	@Autowired
	private ModelMapper modelMapper;

    /**
	 * Busca y devuelve todos los comentarios que existen en la aplicacion.
	 *
	 * @return JSONArray {@link ComentarioDetailDTO} - Los comentarios encontrados en la
	 *         aplicación. Si no hay ninguno retorna una lista vacía.
	 */
    @GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<ComentarioDetailDTO> findAll() {
		List<ComentarioEntity> comentarios = comentarioService.getComentarios();
		return modelMapper.map(comentarios, new TypeToken<List<ComentarioDetailDTO>>() {
		}.getType());
	}

    /**
	 * Busca el comentario con el id asociado recibido en la URL y lo devuelve.
	 *
	 * @param id Identificador del autor que se esta buscando. Este debe ser una
	 *           cadena de dígitos.
	 * @return JSON {@link ComentarioDetailDTO} - El comentario buscado
	 */
	@GetMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public ComentarioDetailDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
		ComentarioEntity comentarioEntity = comentarioService.getComentario(id);
		return modelMapper.map(comentarioEntity, ComentarioDetailDTO.class);
	}

	/**
	 * Crea un nuevo comentario con la informacion que se recibe en el cuerpo de la
	 * petición y se regresa un objeto identico con un id auto-generado por la base
	 * de datos.
	 *
	 * @param ComentarioDetailDTO {@link ComentarioDetailDTO} - EL comentario que se desea guardar.
	 * @return JSON {@link ComentarioDetailDTO} - El comentario guardado con el atributo id
	 *         autogenerado.
	 * @throws IllegalOperationException 
	 * @throws EntityNotFoundException
	 */
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public ComentarioDetailDTO create(@RequestBody ComentarioDetailDTO comentarioDetailDTO) throws IllegalOperationException, EntityNotFoundException {
		ComentarioEntity comentarioEntity = comentarioService.createComentario(modelMapper.map(comentarioDetailDTO, ComentarioEntity.class));
		return modelMapper.map(comentarioEntity, ComentarioDetailDTO.class);
	}

	/**
	 * Actualiza el comentario con el id recibido en la URL con la información que se
	 * recibe en el cuerpo de la petición.
	 *
	 * @param id     Identificador del comentario que se desea actualizar. Este debe ser
	 *               una cadena de dígitos.
	 * @param comentario {@link ComentarioDetailDTO} El comentario que se desea guardar.
	 * @throws IllegalOperationException
	 */
	@PutMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public ComentarioDetailDTO update(@PathVariable("id") Long id, @RequestBody ComentarioDetailDTO comentarioDetailDTO)
			throws EntityNotFoundException, IllegalOperationException {
            ComentarioEntity comentarioEntity = comentarioService.updateComentario(id, modelMapper.map(comentarioDetailDTO, ComentarioEntity.class));
		return modelMapper.map(comentarioEntity, ComentarioDetailDTO.class);
	}

	/**
	 * Borra el comentario con el id asociado recibido en la URL.
	 *
	 * @param id Identificador del comentario que se desea borrar. Este debe ser una
	 *           cadena de dígitos.
	 */
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
		comentarioService.deleteComentario(id);
	}
}
