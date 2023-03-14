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
import co.edu.uniandes.dse.outfits.dto.OutfitDetailDTO;
import co.edu.uniandes.dse.outfits.entities.OutfitEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.services.OutfitService;

@RestController
@RequestMapping("/outfits")
public class OutfitController {
    @Autowired
	private OutfitService outfitService;

	@Autowired
	private ModelMapper modelMapper;

    /**
	 * Busca y devuelve todos los outfits que existen en la aplicacion.
	 *
	 * @return JSONArray {@link OutfitDetailDTO} - Los outfits encontrados en la
	 *         aplicación. Si no hay ninguno retorna una lista vacía.
	 */
    @GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<OutfitDetailDTO> findAll() {
		List<OutfitEntity> authors = outfitService.getOutfits();
		return modelMapper.map(authors, new TypeToken<List<OutfitDetailDTO>>() {
		}.getType());
	}

    /**
	 * Busca el outfit con el id asociado recibido en la URL y lo devuelve.
	 *
	 * @param id Identificador del autor que se esta buscando. Este debe ser una
	 *           cadena de dígitos.
	 * @return JSON {@link OutfitDetailDTO} - El outfit buscado
	 */
	@GetMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public OutfitDetailDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
		OutfitEntity outfitEntity = outfitService.getOutfit(id);
		return modelMapper.map(outfitEntity, OutfitDetailDTO.class);
	}

	/**
	 * Crea un nuevo outfit con la informacion que se recibe en el cuerpo de la
	 * petición y se regresa un objeto identico con un id auto-generado por la base
	 * de datos.
	 *
	 * @param OutfitDetailDTO {@link OutfitDetailDTO} - EL outfit que se desea guardar.
	 * @return JSON {@link OutfitDetailDTO} - El outfit guardado con el atributo id
	 *         autogenerado.
	 * @throws IllegalOperationException 
	 */
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public OutfitDetailDTO create(@RequestBody OutfitDetailDTO outfitDetailDTO) throws IllegalOperationException {
		OutfitEntity outfitEntity = outfitService.createOutfit(modelMapper.map(outfitDetailDTO, OutfitEntity.class));
		return modelMapper.map(outfitEntity, OutfitDetailDTO.class);
	}

	/**
	 * Actualiza el outfit con el id recibido en la URL con la información que se
	 * recibe en el cuerpo de la petición.
	 *
	 * @param id     Identificador del outfit que se desea actualizar. Este debe ser
	 *               una cadena de dígitos.
	 * @param author {@link OutfitDetailDTO} El outfit que se desea guardar.
	 * @throws IllegalOperationException
	 */
	@PutMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public OutfitDetailDTO update(@PathVariable("id") Long id, @RequestBody OutfitDetailDTO outfitDetailDTO)
			throws EntityNotFoundException, IllegalOperationException {
            OutfitEntity outfitEntity = outfitService.updateOutfit(id, modelMapper.map(outfitDetailDTO, OutfitEntity.class));
		return modelMapper.map(outfitEntity, OutfitDetailDTO.class);
	}

	/**
	 * Borra el outfit con el id asociado recibido en la URL.
	 *
	 * @param id Identificador del outfit que se desea borrar. Este debe ser una
	 *           cadena de dígitos.
	 */
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
		outfitService.deleteOutfit(id);
	}
}
