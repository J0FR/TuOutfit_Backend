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

import co.edu.uniandes.dse.outfits.dto.OutfitDTO;
import co.edu.uniandes.dse.outfits.dto.OutfitDetailDTO;
import co.edu.uniandes.dse.outfits.entities.OutfitEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.services.UsuarioOutfitService;

/**
 * Controlador de asocación Usuario Outfit
 * @author Álvaro Bacca (c4ts0up)
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioOutfitController {

    @Autowired
    private UsuarioOutfitService usuarioOutfitService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Agrega un outfit a un usuario
     * 
     * @param usuarioId Id del usuario al que se le agrega el outfit
     * @param outfitId  Id del outfit que se agrega al usuario
     * @return          Outfit agregado
     * @throws          EntityNotFoundException
     */
    @PutMapping(value = "/{usuarioId}/outfits/{outfitId}")
    @ResponseStatus(code = HttpStatus.OK)
    public OutfitDTO addOutfit(
        @PathVariable("usuarioId") Long usuarioId, 
        @PathVariable("outfitId") Long outfitId
    ) throws EntityNotFoundException {
        OutfitEntity outfitEntity = usuarioOutfitService.addOutfit(usuarioId, outfitId);
        return modelMapper.map(outfitEntity, OutfitDTO.class);
    }

    /**
     * Obtiene los outfits favoritos de un usuario
     * 
     * @param usuarioId Id del usuario del que se obtienen los outfits
     * @return          Lista de outfits favoritos del usuario
     */
    @GetMapping(value = "/{usuarioId}/outfits")
    @ResponseStatus(code = HttpStatus.OK)
    public List <OutfitDetailDTO> getOutfits(@PathVariable("usuarioId") Long usuarioId) 
        throws EntityNotFoundException {
            List <OutfitEntity> outfitEntities = usuarioOutfitService.getOutfits(usuarioId);
            return modelMapper.map(
                outfitEntities, 
                new TypeToken <List <OutfitDetailDTO> > () {}.getType()
            );
    }

    /**
     * Obtiene un outfit favorito de un usuario
     * 
     * @param usuarioId Id del usuario del que se obtiene el outfit
     * @param outfitId  Id del outfit que se obtiene
     * @return          Outfit favorito del usuario
     */
    @GetMapping(value = "/{usuarioId}/outfits/{outfitId}")
    @ResponseStatus(code = HttpStatus.OK)
    public OutfitDetailDTO getOutfit(
        @PathVariable("usuarioId") Long usuarioId, 
        @PathVariable("outfitId") Long outfitId
    ) throws EntityNotFoundException, IllegalOperationException {
        OutfitEntity outfitEntity = usuarioOutfitService.getOutfit(usuarioId, outfitId);
        return modelMapper.map(
            outfitEntity, 
            OutfitDetailDTO.class
        );
    }

    /**
     * Elimina un outfit de los favoritos del usuario
     * 
     * @param usuarioId Id del usuario del que se elimina el outfit
     * @param outfitId  Id del outfit que se elimina
     * @throws          EntityNotFoundException
     * @throws          IllegalOperationException
     */
    @DeleteMapping(value = "/{usuarioId}/outfits/{outfitId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeOutfit(
        @PathVariable("usuarioId") Long usuarioId, 
        @PathVariable("outfitId") Long outfitId
    ) throws EntityNotFoundException {
        usuarioOutfitService.removeOutfit(usuarioId, outfitId);
    }
}