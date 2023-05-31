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
import java.util.List;
import co.edu.uniandes.dse.outfits.dto.TiendaFisicaDTO;
import co.edu.uniandes.dse.outfits.entities.TiendaFisicaEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.services.TiendaFisicaService;

@RestController
@RequestMapping("/tiendasFisicas")
public class TiendaFisicaController {
    @Autowired
    private TiendaFisicaService tiendaFisicaService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<TiendaFisicaDTO> findAll() {
            List<TiendaFisicaEntity> tiendasFisicas = tiendaFisicaService.getTiendasFisicas();
            return modelMapper.map(tiendasFisicas, new TypeToken<List<TiendaFisicaDTO>>() {}.getType());
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public TiendaFisicaDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
            TiendaFisicaEntity tiendaFisicaEntity = tiendaFisicaService.getTiendaFisica(id);
            return modelMapper.map(tiendaFisicaEntity, TiendaFisicaDTO.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public TiendaFisicaDTO create(@RequestBody TiendaFisicaDTO tiendaFisicaDTO) throws IllegalOperationException {
            TiendaFisicaEntity tiendaFisicaEntity = tiendaFisicaService.createTiendaFisica(modelMapper.map(tiendaFisicaDTO, TiendaFisicaEntity.class));
            return modelMapper.map(tiendaFisicaEntity, TiendaFisicaDTO.class);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public TiendaFisicaDTO update(@PathVariable("id") Long id, @RequestBody TiendaFisicaDTO tiendaFisicaDTO) throws IllegalOperationException, EntityNotFoundException {
            TiendaFisicaEntity tiendaFisicaEntity = tiendaFisicaService.updateTiendaFisica(id, modelMapper.map(tiendaFisicaDTO, TiendaFisicaEntity.class));
            return modelMapper.map(tiendaFisicaEntity, TiendaFisicaDTO.class);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) throws IllegalOperationException, EntityNotFoundException {
            tiendaFisicaService.deleteTiendaFisica(id);
    }

}