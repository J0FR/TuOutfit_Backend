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

import co.edu.uniandes.dse.outfits.dto.ComentarioDTO;
import co.edu.uniandes.dse.outfits.dto.ComentarioDetailDTO;
import co.edu.uniandes.dse.outfits.entities.ComentarioEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.services.PrendaComentarioService;

@RestController
@RequestMapping("/prenda")
public class PrendaComentarioController {

    @Autowired
    private PrendaComentarioService prendaComentarioService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Asocia un comentario existente con una prenda existente
     *
     * @param comentarioId El ID del comentario que se va a asociar
     * @param prendaId   El ID de la prenda a la cual se le va a asociar el comentario
     * @return JSON {@link ComentarioDetailDTO} - El comentario asociado.
     * @throws EntityNotFoundException
     */
    @PostMapping(value = "/{prendaId}/comentarios/{comentarioId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ComentarioDetailDTO addComentario(@PathVariable("comentarioId") Long comentarioId, @PathVariable("prendaId") Long prendaId)
            throws EntityNotFoundException {
        ComentarioEntity comentarioEntity = prendaComentarioService.addComentario(comentarioId , prendaId);
        return modelMapper.map(comentarioEntity, ComentarioDetailDTO.class);
    }

    /**
     * Busca y devuelve el comentario con el ID recibido en la URL, relativo a una prenda.
     *
     * @param comentarioId El ID del comentario que se busca
     * @param prendaId   El ID de la prenda de la cual se busca el comentario
     * @return {@link ComentarioDetailDTO} - El comentario encontrado en la prenda.
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @PutMapping(value = "/{prendaId}/comentarios")
    @ResponseStatus(code = HttpStatus.OK)
    public List<ComentarioDetailDTO> addComentarios(@PathVariable("prendaId") Long prendaId, @RequestBody List<ComentarioDTO> comentarios)
            throws EntityNotFoundException {
        List<ComentarioEntity> comentariosEntity = modelMapper.map(comentarios, new TypeToken<List<ComentarioEntity>>() {
            }.getType());
        List<ComentarioEntity> comentarioList = prendaComentarioService.replaceComentarios(prendaId, comentariosEntity);
        return modelMapper.map(comentarioList, new TypeToken<List<ComentarioDetailDTO>>() {
            }.getType());
    }
          





    /**
     * Busca y devuelve el comentario con el ID recibido en la URL, relativo a una prenda.
     *
     * @param comentarioId El ID del comentario que se busca
     * @param prendaId   El ID de la prenda de la cual se busca el comentario
     * @return {@link ComentarioDetailDTO} - El comentario encontrado en la prenda.
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @GetMapping(value = "/{prendaId}/comentarios/{comentarioId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ComentarioDetailDTO getComentario(@PathVariable("comentarioId") Long comentarioId, @PathVariable("prendaId") Long prendaId) throws EntityNotFoundException, IllegalOperationException {
        ComentarioEntity comentarioEntity = prendaComentarioService.getComentario(prendaId,comentarioId);
        return modelMapper.map(comentarioEntity, ComentarioDetailDTO.class);
    }

    /**
     * Busca y devuelve todos los comentarios que existen en una prenda.
     *
     * @param prendaId El ID de la prenda de la cual se buscan los comentarios
     * 
     * @return JSONArray {@link ComentarioDetailDTO} - Los comentarios encontrados en la
     *        prenda. Si no hay ninguno retorna una lista vacía.
     * @throws co.edu.uniandes.dse.outfits.controllers.EntityNotFoundException
     */
    @GetMapping(value = "/{prendaId}/comentarios")
    @ResponseStatus(code = HttpStatus.OK)
    public List<ComentarioDetailDTO> getComentarios(@PathVariable("prendaId") Long prendaId) throws EntityNotFoundException {
        List<ComentarioEntity> comentarios = prendaComentarioService.getComentarios(prendaId);
        return modelMapper.map(comentarios, new TypeToken<List<ComentarioDetailDTO>>() {
        }.getType());
    }

    /**
     * Elimina la conexión entre el comentario y la prenda recibidos en la URL.
     * 
     * @param prendaId   El ID de la prenda que se desea desasociar
     * @param comentarioId El ID del comentario que se desasocia
     * @throws co.edu.uniandes.dse.outfits.controllers.EntityNotFoundException
     */
    @DeleteMapping(value = "/{prendaId}/comentarios/{comentarioId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeComentario(@PathVariable("comentarioId") Long comentarioId, @PathVariable("prendaId") Long prendaId) throws EntityNotFoundException {
        prendaComentarioService.removeComentario( prendaId , comentarioId);
    }


    


    
}
