package co.edu.uniandes.dse.outfits.controllers;

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

import co.edu.uniandes.dse.outfits.dto.UbicacionDTO;
import co.edu.uniandes.dse.outfits.entities.TiendaFisicaEntity;
import co.edu.uniandes.dse.outfits.entities.UbicacionEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.services.TiendaFisicaUbicacionService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/tiendasFisicas")
public class TiendaFisicaUbicacionController {
    @Autowired
    private TiendaFisicaUbicacionService tiendaFisicaUbicacionService;

    @Autowired
    private ModelMapper modelMapper;

    @PutMapping(value = "/{idTiendaFisica}/ubicacion")
    @ResponseStatus(code = HttpStatus.OK)
    public UbicacionDTO addUbicacion(@PathVariable("idTiendaFisica") Long idTiendaFisica, @RequestBody Long idUbicacion)
            throws EntityNotFoundException {
        UbicacionEntity tiendaFisicaEntity = tiendaFisicaUbicacionService.addUbicacion(idTiendaFisica, idUbicacion);
        return modelMapper.map(tiendaFisicaEntity, UbicacionDTO.class);
    }

    @GetMapping(value = "/{idTiendaFisica}/ubicacion")
    @ResponseStatus(code = HttpStatus.OK)
    public UbicacionDTO getUbicacion(@PathVariable("idTiendaFisica") Long idTiendaFisica, @RequestBody Long idUbicacion)
            throws EntityNotFoundException, IllegalOperationException {
        UbicacionEntity tiendaFisicaEntity = tiendaFisicaUbicacionService.getUbicacion(idTiendaFisica, idUbicacion);
        return modelMapper.map(tiendaFisicaEntity, UbicacionDTO.class);
    }
}
