package co.edu.uniandes.dse.outfits.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.outfits.entities.OutfitEntity;
import co.edu.uniandes.dse.outfits.entities.UsuarioEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.ErrorMessage;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.repositories.OutfitRepository;
import co.edu.uniandes.dse.outfits.repositories.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UsuarioOutfitService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private OutfitRepository outfitRepository;

    /**
     * Asocia un Usuario existente a un outfit
     *
     * @param usuarioId Identificador de la instancia de Usuario
     * @param outfitId  Identificador de la instancia de Outfit
     * @return Instancia de UsuarioEntity que fue asociada a Outfit
     */
    @Transactional
    public OutfitEntity addOutfit(Long usuarioId, Long outfitId) throws EntityNotFoundException {
        log.info("Inicia proceso de asociarle un outfit al usuario con id = {0}", usuarioId);
        Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
        if (outfitEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

        Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findById(usuarioId);
        if (usuarioEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);

        usuarioEntity.get().getFavoritos().add(outfitEntity.get());
        log.info("Termina proceso de asociarle un outfit al usuario con id = {0}", usuarioId);
        return outfitEntity.get();
    }

    @Transactional
    public List<OutfitEntity> getOutfits(Long usuarioId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar todos los outfits favoritos del usuario con id = {0}", usuarioId);
        Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findById(usuarioId);
        if (usuarioEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);
        log.info("Finaliza proceso de consultar todos los outfits favoritos del usuario con id = {0}", usuarioId);
        return usuarioEntity.get().getFavoritos();
    }

    @Transactional
    public OutfitEntity getOutfit(Long usuarioId, Long outfitId)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de consultar un outfit favorito del usuario con id = {0}", usuarioId);
        Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
        Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findById(usuarioId);

        if (outfitEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

        if (usuarioEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);
        log.info("Termina proceso de consultar un outfit del usuario con id = {0}", usuarioId);
        if (!usuarioEntity.get().getFavoritos().contains(outfitEntity.get()))
            throw new IllegalOperationException("El outfit no esta asociado con el usuarioz`");

        return outfitEntity.get();
    }

    @Transactional
    public void removeOutfit(Long usuarioId, Long outfitId) throws EntityNotFoundException {
        log.info("Inicia proceso de borrar un outfit favorito del usuario con id = {0}", usuarioId);
        Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
        Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findById(usuarioId);

        if (outfitEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

        if (usuarioEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);

        usuarioEntity.get().getFavoritos().remove(outfitEntity.get());

        log.info("Termina proceso de borrar un outfit favorito del usuario con id = {0}", usuarioId);
    }

}
