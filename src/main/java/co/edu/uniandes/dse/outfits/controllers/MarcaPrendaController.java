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
import co.edu.uniandes.dse.outfits.services.MarcaPrendaService;

@RestController
@RequestMapping("/marcas")
public class MarcaPrendaController {
    @Autowired
    private MarcaPrendaService marcaPrendaService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Asocia una prenda existente con una marca existente
     *
     * @param prendaId El ID de la prenda que se va a asociar
     * @param marcaId  El ID del marca al cual se le va a asociar la prenda
     * @return JSON {@link PrendaDetailDTO} - La prenda asociado.
     * @throws EntityNotFoundException
     */
    @PostMapping(value = "/{marcaId}/prenda/{prendaId}")
    @ResponseStatus(code = HttpStatus.OK)
    public PrendaDetailDTO addPrenda(@PathVariable("marcaId") Long marcaId, @PathVariable("prendaId") Long prendaId)
            throws EntityNotFoundException {
        PrendaEntity prendaEntity = marcaPrendaService.addPrenda(marcaId, prendaId);
        return modelMapper.map(prendaEntity, PrendaDetailDTO.class);
    }

    /**
     * Busca y devuelve el comentario con el ID recibido en la URL, relativo a un
     * outfit.
     *
     * @param prendaId El ID de la prenda que se busca
     * @param marcaId  El ID de la marca del cual se busca la prenda
     * @return {@link PrendaDetailDTO} - La prenda se encontrado en la marca.
     * @throws EntityNotFoundException
     */
    @GetMapping(value = "/{marcaId}/prenda/{prendaId}")
    @ResponseStatus(code = HttpStatus.OK)
    public PrendaDetailDTO getPrenda(@PathVariable("marcaId") Long marcaId,
            @PathVariable("prendaId") Long prendaId)
            throws IllegalOperationException, EntityNotFoundException {
        PrendaEntity prendaEntity = marcaPrendaService.getPrenda(marcaId, prendaId);
        return modelMapper.map(prendaEntity, PrendaDetailDTO.class);
    }

    /**
     * Actualiza la lista de prendas de una prenda con la lista que se recibe en
     * el
     * cuerpo.
     *
     * @param marcaId El ID de la marca al cual se le va a asociar la lista de
     *                prendas
     * @param prendas JSONArray {@link PrendaDTO} - La lista de prendas
     *                que se desea
     *                guardar.
     * @return JSONArray {@link PrendaDetailDTO} - La lista actualizada.
     * @throws EntityNotFoundException
     */
    @PutMapping(value = "/{marcaId}/prenda")
    @ResponseStatus(code = HttpStatus.OK)
    public List<PrendaDetailDTO> addPrendas(@PathVariable("marcaId") Long marcaId,
            @RequestBody List<PrendaDTO> prendas)
            throws EntityNotFoundException {
        List<PrendaEntity> entities = modelMapper.map(prendas, new TypeToken<List<PrendaEntity>>() {
        }.getType());
        List<PrendaEntity> prendaList = marcaPrendaService.updatePrendas(marcaId, entities);
        return modelMapper.map(prendaList, new TypeToken<List<PrendaDetailDTO>>() {
        }.getType());
    }

    /**
     * Busca y devuelve todos las prendas que existen en una marca.
     *
     * @param marcaId El ID de la marca del cual se buscan las prendas
     * @return JSONArray {@link PrendaDetailDTO} - Las marcas encontradas
     *         en la
     *         marca. Si no hay ninguno retorna una lista vacía.
     * @throws co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException
     */
    @GetMapping(value = "/{marcaId}/marcas")
    @ResponseStatus(code = HttpStatus.OK)
    public List<PrendaDetailDTO> getPrendas(@PathVariable("marcaId") Long marcaId)
            throws EntityNotFoundException {
        List<PrendaEntity> prendaEntity = marcaPrendaService.getPrendas(marcaId);
        return modelMapper.map(prendaEntity, new TypeToken<List<PrendaDetailDTO>>() {
        }.getType());
    }

    /**
     * Elimina la conexión entre el comentario y el outfit recibidos en la URL.
     *
     * @param marcaId  El ID de la marca al cual se le va a desasociar la
     *                 prenda
     * @param prendaId El ID de la prenda que se desasocia
     * @throws co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException
     */
    @DeleteMapping(value = "/{marcaId}/prenda/{prendaId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removePrenda(@PathVariable("marcaId") Long marcaId,
            @PathVariable("prendaId") Long prendaId)
            throws EntityNotFoundException {
        marcaPrendaService.removePrenda(marcaId, prendaId);
    }

}
