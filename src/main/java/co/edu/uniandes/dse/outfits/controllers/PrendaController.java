package co.edu.uniandes.dse.outfits.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
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
import org.modelmapper.TypeToken;

import co.edu.uniandes.dse.outfits.dto.PrendaDTO;
import co.edu.uniandes.dse.outfits.dto.PrendaDetailDTO;
import co.edu.uniandes.dse.outfits.entities.PrendaEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.services.PrendaService;

@RestController
@RequestMapping("/prenda")
public class PrendaController {
        
        @Autowired
        private PrendaService prendaService;

        @Autowired
        private ModelMapper modelMapper;
        
        @GetMapping
        @ResponseStatus(code = HttpStatus.OK)
        public List<PrendaDetailDTO> findAll() {
            List<PrendaEntity> prendas = prendaService.getPrendas();
            return modelMapper.map(prendas, new TypeToken<List<PrendaDetailDTO>>() {
            }.getType());
        }

        
        @GetMapping(value = "/{id}")
        @ResponseStatus(code = HttpStatus.OK)
        public PrendaDetailDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
            PrendaEntity prendaEntity = prendaService.getPrenda(id);
            return modelMapper.map(prendaEntity, PrendaDetailDTO.class);
        }
        
        @PostMapping
        @ResponseStatus(code = HttpStatus.CREATED)
        public PrendaDTO create(@RequestBody PrendaDTO prendaDTO) throws IllegalOperationException, EntityNotFoundException{
            PrendaEntity prendaEntity = prendaService.createPrenda(modelMapper.map(prendaDTO, PrendaEntity.class));
            return modelMapper.map(prendaEntity, PrendaDTO.class);   
        } 

        @PutMapping(value = "/{id}")
        @ResponseStatus(code = HttpStatus.OK)
        public PrendaDTO update(@PathVariable("id") Long id, @RequestBody PrendaDTO prendaDTO) throws EntityNotFoundException, IllegalOperationException {
            PrendaEntity prendaEntity = prendaService.updatePrenda(id, modelMapper.map(prendaDTO, PrendaEntity.class));
            return modelMapper.map(prendaEntity, PrendaDTO.class);
        }

        @DeleteMapping(value = "/{id}")
        @ResponseStatus(code = HttpStatus.NO_CONTENT)
        public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
            prendaService.deletePrenda(id);
        }

}
