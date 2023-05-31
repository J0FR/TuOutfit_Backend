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
import co.edu.uniandes.dse.outfits.dto.UbicacionDTO;
import co.edu.uniandes.dse.outfits.entities.UbicacionEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.services.UbicacionService;

@RestController
@RequestMapping("/ubicaciones")
public class UbicacionController {
    @Autowired
    private UbicacionService ubicacionService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<UbicacionDTO> findAll() {
            List<UbicacionEntity> ubicaciones = ubicacionService.getUbicaciones();
            return modelMapper.map(ubicaciones, new TypeToken<List<UbicacionDTO>>() {}.getType());
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public UbicacionDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
            UbicacionEntity ubicacionEntity = ubicacionService.getUbicacion(id);
            return modelMapper.map(ubicacionEntity, UbicacionDTO.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public UbicacionDTO create(@RequestBody UbicacionDTO ubicacionDTO) throws IllegalOperationException {
            UbicacionEntity ubicacionEntity = ubicacionService.createUbicacion(modelMapper.map(ubicacionDTO, UbicacionEntity.class));
            return modelMapper.map(ubicacionEntity, UbicacionDTO.class);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public UbicacionDTO update(@PathVariable("id") Long id, @RequestBody UbicacionDTO ubicacionDTO) throws IllegalOperationException, EntityNotFoundException {
            UbicacionEntity ubicacionEntity = ubicacionService.updateUbicacion(id, modelMapper.map(ubicacionDTO, UbicacionEntity.class));
            return modelMapper.map(ubicacionEntity, UbicacionDTO.class);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) throws IllegalOperationException, EntityNotFoundException {
            ubicacionService.deleteUbicacion(id);
    }

}