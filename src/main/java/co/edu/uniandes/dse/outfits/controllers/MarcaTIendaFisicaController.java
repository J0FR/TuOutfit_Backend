package co.edu.uniandes.dse.outfits.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.outfits.dto.TiendaFisicaDTO;
import co.edu.uniandes.dse.outfits.entities.PrendaEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.services.MarcaPrendaService;

@RestController
@RequestMapping("/marcas")
public class MarcaTIendaFisicaController {
    @Autowired
    private MarcaPrendaService marcaPrendaService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Asocia una prenda existente con una marca existente
     *
     * @param tiendaFisicaId El ID de la prenda que se va a asociar
     * @param marcaId        El ID del marca al cual se le va a asociar la prenda
     * @return JSON {@link tiendaFisicaDTO} - La prenda asociado.
     * @throws EntityNotFoundException
     */
    @PostMapping(value = "/{marcaId}/TiendaFisicas/{tiendaFisicaId}")
    @ResponseStatus(code = HttpStatus.OK)
    public TiendaFisicaDTO addPrenda(@PathVariable("marcaId") Long marcaId,
            @PathVariable("tiendaFisicaId") Long prendaId)
            throws EntityNotFoundException {
        PrendaEntity tiendaFisicaEntity = marcaPrendaService.addPrenda(marcaId, prendaId);
        return modelMapper.map(tiendaFisicaEntity, TiendaFisicaDTO.class);
    }
}
