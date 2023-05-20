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

import co.edu.uniandes.dse.outfits.dto.TiendaFisicaDTO;
import co.edu.uniandes.dse.outfits.entities.TiendaFisicaEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.services.MarcaTiendaFisicaService;

@RestController
@RequestMapping("/marcas")
public class MarcaTIendaFisicaController {
    @Autowired
    private MarcaTiendaFisicaService marcaTiendaFisicaService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Asocia una prenda existente con una marca existente
     *
     * @param tiendaFisicaId El ID de la tienda fisica que se va a asociar
     * @param marcaId        El ID del marca al cual se le va a asociar la prenda
     * @return JSON {@link tiendaFisicaDTO} - La prenda asociado.
     * @throws EntityNotFoundException
     */
    @PostMapping(value = "/{marcaId}/tiendasFisicas/{tiendaFisicaId}")
    @ResponseStatus(code = HttpStatus.OK)
    public TiendaFisicaDTO addTiendaFisica(@PathVariable("marcaId") Long marcaId,
            @PathVariable("tiendaFisicaId") Long tiendasFisicaId)
            throws EntityNotFoundException {
        TiendaFisicaEntity tiendaFisicaEntity = marcaTiendaFisicaService.addTiendaFisica(marcaId, tiendasFisicaId);
        return modelMapper.map(tiendaFisicaEntity, TiendaFisicaDTO.class);
    }

    @GetMapping(value = "/{marcaId}/tiendasFisicas/{tiendaFisicaId}")
    @ResponseStatus(code = HttpStatus.OK)
    public TiendaFisicaDTO getTiendaFisica(@PathVariable("marcaId") Long marcaId,
            @PathVariable("tiendaFisicaId") Long tiendaFisicaId)
            throws IllegalOperationException, EntityNotFoundException {
        TiendaFisicaEntity tiendaFisicaEntity = marcaTiendaFisicaService.getTiendaFisica(marcaId, tiendaFisicaId);
        return modelMapper.map(tiendaFisicaEntity, TiendaFisicaDTO.class);
    }

    @PutMapping(value = "/{marcaId}/tiendasFisicas")
    @ResponseStatus(code = HttpStatus.OK)
    public List<TiendaFisicaDTO> addTiendasFisicas(@PathVariable("marcaId") Long marcaId,
            @RequestBody List<TiendaFisicaDTO> tiendaFisicas)
            throws EntityNotFoundException {
        List<TiendaFisicaEntity> entities = modelMapper.map(tiendaFisicas, new TypeToken<List<TiendaFisicaEntity>>() {
        }.getType());
        List<TiendaFisicaEntity> tiendaFisicaList = marcaTiendaFisicaService.updateTiendasFisicas(marcaId, entities);
        return modelMapper.map(tiendaFisicaList, new TypeToken<List<TiendaFisicaDTO>>() {
        }.getType());
    }

    @GetMapping(value = "/{marcaId}/tiendasFisicas")
    @ResponseStatus(code = HttpStatus.OK)
    public List<TiendaFisicaDTO> getTiendasFisicas(@PathVariable("marcaId") Long marcaId)
            throws EntityNotFoundException {
        List<TiendaFisicaEntity> prendaEntity = marcaTiendaFisicaService.getTiendasFisicas(marcaId);
        return modelMapper.map(prendaEntity, new TypeToken<List<TiendaFisicaDTO>>() {
        }.getType());
    }

    @DeleteMapping(value = "/{marcaId}/tiendasFisicas/{tiendaFisicaId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removePrenda(@PathVariable("marcaId") Long marcaId,
            @PathVariable("tiendaFisicaId") Long tiendaFisicaId)
            throws EntityNotFoundException {
        marcaTiendaFisicaService.removeTiendaFisica(marcaId, tiendaFisicaId);
    }

}
