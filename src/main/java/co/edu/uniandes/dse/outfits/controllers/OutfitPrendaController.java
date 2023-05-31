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
import co.edu.uniandes.dse.outfits.dto.PrendaDTO;
import co.edu.uniandes.dse.outfits.dto.PrendaDetailDTO;
import co.edu.uniandes.dse.outfits.entities.PrendaEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.services.OutfitPrendaService;

@RestController
@RequestMapping("/outfits")
public class OutfitPrendaController {
    @Autowired
	private OutfitPrendaService outfitPrendaService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Asocia un prenda existente con un outfit existente
	 *
	 * @param prendaId El ID del prenda que se va a asociar
	 * @param outfitId   El ID del outfit al cual se le va a asociar el prenda
	 * @return JSON {@link PrendaDetailDTO} - El prenda asociado.
	 * @throws EntityNotFoundException
	 */
	@PostMapping(value = "/{outfitId}/prendas/{prendaId}")
	@ResponseStatus(code = HttpStatus.OK)
	public PrendaDetailDTO addPrenda(@PathVariable("prendaId") Long prendaId, @PathVariable("outfitId") Long outfitId)
			throws EntityNotFoundException {
        PrendaEntity comentarioEntity = outfitPrendaService.addPrenda(outfitId, prendaId);
		return modelMapper.map(comentarioEntity, PrendaDetailDTO.class);
	}

	/**
	 * Busca y devuelve el prenda con el ID recibido en la URL, relativo a un outfit.
	 *
	 * @param prendaId El ID del prenda que se busca
	 * @param outfitId   El ID del outfit del cual se busca el prenda
	 * @return {@link PrendaDetailDTO} - El prenda encontrado en el outfit.
	 * @throws EntityNotFoundException
	 */
	@GetMapping(value = "/{outfitId}/prendas/{prendaId}")
	@ResponseStatus(code = HttpStatus.OK)
	public PrendaDetailDTO getPrenda(@PathVariable("prendaId") Long prendaId, @PathVariable("outfitId") Long outfitId)
			throws IllegalOperationException, EntityNotFoundException {
        PrendaEntity authorEntity = outfitPrendaService.getPrenda(outfitId, prendaId);
		return modelMapper.map(authorEntity, PrendaDetailDTO.class);
	}

	/**
	 * Actualiza la lista de prendas de un outfit con la lista que se recibe en el
	 * cuerpo.
	 *
	 * @param outfitId  El ID del outfit al cual se le va a asociar la lista de prendas
	 * @param prendas JSONArray {@link PrendaDTO} - La lista de prendas que se desea
	 *                guardar.
	 * @return JSONArray {@link PrendaDetailDTO} - La lista actualizada.
	 * @throws EntityNotFoundException
	 */
	@PutMapping(value = "/{outfitId}/prendas")
	@ResponseStatus(code = HttpStatus.OK)
	public List<PrendaDetailDTO> addPrendas(@PathVariable("outfitId") Long outfitId, @RequestBody List<PrendaDTO> prendas)
			throws EntityNotFoundException {
		List<PrendaEntity> entities = modelMapper.map(prendas, new TypeToken<List<PrendaEntity>>() {
		}.getType());
		List<PrendaEntity> prendasList = outfitPrendaService.replacePrendas(outfitId, entities);
		return modelMapper.map(prendasList, new TypeToken<List<PrendaDetailDTO>>() {
		}.getType());
	}


	/**
	 * Busca y devuelve todos los prendas que existen en un outfit.
	 *
	 * @param outfitId El ID del outfit del cual se buscan los prendas
	 * @return JSONArray {@link PrendaDetailDTO} - Los prendas encontrados en el
	 *         outfit. Si no hay ninguno retorna una lista vacía.
	 * @throws co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException
	 */
	@GetMapping(value = "/{outfitId}/prendas")
	@ResponseStatus(code = HttpStatus.OK)
	public List<PrendaDetailDTO> getPrendas(@PathVariable("outfitId") Long outfitId) throws EntityNotFoundException {
		List<PrendaEntity> comentarioEntity = outfitPrendaService.getPrendas(outfitId);
		return modelMapper.map(comentarioEntity, new TypeToken<List<PrendaDetailDTO>>() {
		}.getType());
	}

	/**
	 * Elimina la conexión entre el prenda y el outfit recibidos en la URL.
	 *
	 * @param outfitId   El ID del outfit al cual se le va a desasociar el prenda
	 * @param prendaId El ID del prenda que se desasocia
	 * @throws co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException
	 */
	@DeleteMapping(value = "/{outfitId}/prendas/{prendaId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removePrenda(@PathVariable("prendaId") Long prendaId, @PathVariable("outfitId") Long outfitId)
			throws EntityNotFoundException {
		outfitPrendaService.removePrenda(outfitId, prendaId);
	}
}
