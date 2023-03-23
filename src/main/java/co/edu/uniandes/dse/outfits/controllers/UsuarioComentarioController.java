package co.edu.uniandes.dse.outfits.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.outfits.dto.ComentarioDTO;
import co.edu.uniandes.dse.outfits.dto.ComentarioDetailDTO;
import co.edu.uniandes.dse.outfits.entities.ComentarioEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.services.UsuarioComentarioService;

/**
 * Controlador de comentarios de un usuario
 * @author Álvaro Bacca (c4ts0up)
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioComentarioController {

    @Autowired
    private UsuarioComentarioService usuarioComentarioService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Agrega un comentario a un usuario
     * 
     * @param usuarioId     Id del usuario al que se le agrega el comentario
     * @param comentarioId  Id del comentario que se agrega al usuario
     * @return              Comentario agregado
     * @throws              EntityNotFoundException
     */
    @PutMapping(value = "/{usuarioId}/comentarios/{comentarioId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ComentarioDTO addComentario(
        @PathVariable("usuarioId") Long usuarioId,
        @PathVariable("comentarioId") Long comentarioId
    ) throws EntityNotFoundException {
        ComentarioEntity comentarioEntity= usuarioComentarioService.addComentario(usuarioId, comentarioId);

        return modelMapper.map(comentarioEntity, ComentarioDTO.class);
    }

    /**
     * Obtiene los comentarios agregados a un usuario
     * 
     * @param usuarioId     Id del usuario del que se obtienen los comentarios
     * @return              Lista de comentarios del usuario
     * @throws              EntityNotFoundException
     */
    @GetMapping(value = "/{usuarioId}/comentarios")
    @ResponseStatus(code = HttpStatus.OK)
    public List <ComentarioDetailDTO> getComentarios(
        @PathVariable("usuarioId") Long usuarioId
    ) throws EntityNotFoundException {
        List <ComentarioEntity> comentarioEntities = usuarioComentarioService.getComentarios(usuarioId);

        return modelMapper.map(
            comentarioEntities, 
            new TypeToken <List <ComentarioDetailDTO> > () {}.getType()
        );
    }

    /**
     * Obtiene un comentario agregado a un usuario
     * 
     * @param usuarioId     Id del usuario del que se obtiene el comentario
     * @param comentarioId  Id del comentario que se obtiene del usuario
     * @return              Comentario del usuario
     * @throws              EntityNotFoundException
     * @throws              IllegalOperationException
     */
    @GetMapping(value = "/{usuarioId}/comentarios/{comentarioId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ComentarioDetailDTO getComentario(
        @PathVariable("usuarioId") Long usuarioId,
        @PathVariable("comentarioId") Long comentarioId
    ) throws EntityNotFoundException, IllegalOperationException {
        ComentarioEntity comentarioEntity = usuarioComentarioService.getComentario(usuarioId, comentarioId);

        return modelMapper.map(
            comentarioEntity, 
            ComentarioDetailDTO.class
        );
    }

    /**
     * Elimina un comentario agregado a un usuario
     * 
     * @param usuarioId     Id del usuario del que se elimina el comentario
     * @param comentarioId  Id del comentario que se elimina del usuario
     * @throws              EntityNotFoundException
     */
    @DeleteMapping(value = "/{usuarioId}/comentarios/{comentarioId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeComentario(
        @PathVariable("usuarioId") Long usuarioId,
        @PathVariable("comentarioId") Long comentarioId
    ) throws EntityNotFoundException {
        usuarioComentarioService.removeComentario(usuarioId, comentarioId);
    }

    /*
     * TODO: @DanielPedrozaA
     * Falta implementar la actualización de un comentario en la lógica.
     * Sin esto, no se puede desarrollar la actualización desde el API
     */
}  
