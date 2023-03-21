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

import co.edu.uniandes.dse.outfits.dto.UsuarioDTO;
import co.edu.uniandes.dse.outfits.dto.UsuarioDetailDTO;
import co.edu.uniandes.dse.outfits.entities.UsuarioEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.services.UsuarioService;

/**
 * Controlador de Usuario
 * @author Álvaro Bacca (c4ts0up)
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Encuentra todos los ususarios almacenados en la base de datos
     * @return      Todos los usuarios almacenados en la base de datos
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List <UsuarioDetailDTO> findAll() {
        List <UsuarioEntity> usuarios = usuarioService.getUsuarios();
        return modelMapper.map(usuarios, new TypeToken <List <UsuarioDetailDTO> > () {}.getType());
    }

    /**
     * Encuentra un usuario específico en la base de datos
     * @param id    Id del usuario que se busca
     * @return      Usuario buscado
     * @throws      EntityNotFoundException
     */
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public UsuarioDetailDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
        UsuarioEntity usuarioEntity = usuarioService.getUsuario(id);
        return modelMapper.map(usuarioEntity, UsuarioDetailDTO.class);
    }

    /**
     * Crea un usuario en la base de datos
     * @param usuarioDTO    DTO con info para crear el usuario
     * @return              Usuario creado
     * @throws              IllegalOperationException
     * @throws              EntityNotFoundException
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public UsuarioDTO create(@RequestBody UsuarioDTO usuarioDTO) 
        throws IllegalOperationException, EntityNotFoundException {
        UsuarioEntity usuarioEntity = usuarioService.createUsuario
            (modelMapper.map(usuarioDTO, UsuarioEntity.class));
        return modelMapper.map(usuarioEntity, UsuarioDTO.class);
    }

    /**
     * Actualiza un usuario especificado de la base de datos
     * @param id            Id del usuario a actualizar
     * @param usuarioDTO    Información para actualizar al usuario
     * @return              Usuario actualizado
     * @throws              IllegalOperationException
     * @throws              EntityNotFoundException
     */
    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public UsuarioDTO update(@PathVariable("id") Long id, @RequestBody UsuarioDTO usuarioDTO) 
        throws IllegalOperationException, EntityNotFoundException {
        UsuarioEntity usuarioEntity = usuarioService.updateUsuario
            (id, modelMapper.map(usuarioDTO, UsuarioEntity.class));
        return modelMapper.map(usuarioEntity, UsuarioDTO.class);
    }

    /**
     * Elimina un usuario especificado de la base de datos
     * @param id    Id del usuario a eliminar
     * @throws      IllegalOperationException
     * @throws      EntityNotFoundException
     */
    @DeleteMapping
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) 
        throws IllegalOperationException, EntityNotFoundException {
        usuarioService.deleteUsuario(id);
    }
}

