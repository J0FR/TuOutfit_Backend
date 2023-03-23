package co.edu.uniandes.dse.outfits.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.outfits.entities.ComentarioEntity;
import co.edu.uniandes.dse.outfits.entities.UsuarioEntity;
import co.edu.uniandes.dse.outfits.entities.ComentarioEntity;
import co.edu.uniandes.dse.outfits.entities.UsuarioEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.ErrorMessage;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.repositories.ComentarioRepository;
import co.edu.uniandes.dse.outfits.repositories.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UsuarioComentarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    /**
     * Asocia un Usuario existente a un outfit
     *
     * @param usuarioId Identificador de la instancia de Usuario
     * @param outfitId  Identificador de la instancia de Outfit
     * @return Instancia de UsuarioEntity que fue asociada a Outfit
     */
    @Transactional
    public ComentarioEntity addComentario(Long usuarioId, Long comentarioId) throws EntityNotFoundException {
        log.info("Inicia proceso de asociarle un comentario al usuario con id = {0}", usuarioId);
        Optional<ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioId);
        if (comentarioEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);

        Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findById(usuarioId);
        if (usuarioEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);

        usuarioEntity.get().getComentarios().add(comentarioEntity.get());
        log.info("Termina proceso de asociarle un comentario al usuario con id = {0}", usuarioId);
        return comentarioEntity.get();
    }

    @Transactional
    public List<ComentarioEntity> getComentarios(Long usuarioId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar todos los comentarios del usuario con id = {0}", usuarioId);
        Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findById(usuarioId);
        if (usuarioEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);
        log.info("Finaliza proceso de consultar todos los comentarios del usuario con id = {0}", usuarioId);
        return usuarioEntity.get().getComentarios();
    }

    @Transactional
    public ComentarioEntity getComentario(Long usuarioId, Long comentarioId)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de consultar un comentario del usuario con id = {0}", usuarioId);
        Optional<ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioId);
        Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findById(usuarioId);

        if (comentarioEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);

        if (usuarioEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);
        log.info("Termina proceso de consultar un comentario del usuario con id = {0}", usuarioId);
        if (!usuarioEntity.get().getComentarios().contains(comentarioEntity.get()))
            throw new IllegalOperationException("El comentario no esta asociado con el usuarioz`");

        return comentarioEntity.get();
    }

    @Transactional
    public void removeComentario(Long usuarioId, Long comentarioId) throws EntityNotFoundException {
        log.info("Inicia proceso de borrar un comentario del usuario con id = {0}", usuarioId);
        Optional<ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioId);
        Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findById(usuarioId);

        if (comentarioEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);

        if (usuarioEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);

        usuarioEntity.get().getComentarios().remove(comentarioEntity.get());

        log.info("Termina proceso de borrar un comentario del usuario con id = {0}", usuarioId);
    }

}
