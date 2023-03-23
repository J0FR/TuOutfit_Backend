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

import co.edu.uniandes.dse.outfits.dto.MarcaDetailDTO;
import co.edu.uniandes.dse.outfits.entities.MarcaEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.services.MarcaService;

@RestController
@RequestMapping("/marcas")
public class MarcaController {
    @Autowired
    private MarcaService marcaService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Busca y devuelve todos las marcas que existen en la aplicacion.
     *
     * @return JSONArray {@link MarcaDetailDTO} - Las marcas encontradas en la
     *         aplicación. Si no hay ninguno retorna una lista vacía.
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<MarcaDetailDTO> findAll() {
        List<MarcaEntity> marca = marcaService.getMarcas();
        return modelMapper.map(marca, new TypeToken<List<MarcaDetailDTO>>() {
        }.getType());

    }

    /**
     * Busca la marca con el id asociado recibido en la URL y lo devuelve.
     *
     * @param id Identificador de la marca que se esta buscando. Este debe ser una
     *           cadena de dígitos.
     * @return JSON {@link MarcaDetailDTO} - La marca buscada
     */
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public MarcaDetailDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
        MarcaEntity marcaEntity = marcaService.getMarca(id);
        return modelMapper.map(marcaEntity, MarcaDetailDTO.class);
    }

    /**
     * Crea una nueva marca con la informacion que se recibe en el cuerpo de la
     * petición y se regresa un objeto identico con un id auto-generado por la base
     * de datos.
     *
     * @param MarcaDetailDTO {@link MarcaDetailDTO} - La marca que se desea guardar.
     * @return JSON {@link MarcaDetailDTO} - La marca guardada con el atributo id
     *         autogenerado.
     * @throws IllegalOperationException
     */

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public MarcaDetailDTO create(@RequestBody MarcaDetailDTO marcaDetailDTO) throws IllegalOperationException {
        MarcaEntity marcaEntity = marcaService.createMarca(modelMapper.map(marcaDetailDTO, MarcaEntity.class));
        return modelMapper.map(marcaEntity, MarcaDetailDTO.class);
    }

    /**
     * Actualiza la marca con el id recibido en la URL con la información que se
     * recibe en el cuerpo de la petición.
     *
     * @param id    Identificador de la marca que se desea actualizar. Este debe ser
     *              una cadena de dígitos.
     * @param marca {@link MarcaDetailDTO} El outfit que se desea guardar.
     * @throws IllegalOperationException
     */
    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public MarcaDetailDTO update(@PathVariable("id") Long id, @RequestBody MarcaDetailDTO marcaDetailDTO)
            throws EntityNotFoundException, IllegalOperationException {
        MarcaEntity marcaEntity = marcaService.updateMarca(id,
                modelMapper.map(marcaDetailDTO, MarcaEntity.class));
        return modelMapper.map(marcaEntity, MarcaDetailDTO.class);
    }

    /**
     * Borra la marca con el id asociado recibido en la URL.
     *
     * @param id Identificador de la marca que se desea borrar. Este debe ser una
     *           cadena de dígitos.
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
        marcaService.deleteMarca(id);
    }

}
