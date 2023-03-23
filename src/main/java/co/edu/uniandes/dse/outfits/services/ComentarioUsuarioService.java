package co.edu.uniandes.dse.outfits.services;

import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.uniandes.dse.outfits.entities.ComentarioEntity;
import co.edu.uniandes.dse.outfits.entities.UsuarioEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.ErrorMessage;
import co.edu.uniandes.dse.outfits.repositories.ComentarioRepository;
import co.edu.uniandes.dse.outfits.repositories.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase que implementa la conexión con la persistencia 
 * para la relación entre la entidad de Comentario y 
 * Usuario.
 * 
 * @author Álvaro A. Bacca (c4ts0up)
 */

@Slf4j
@Service
public class ComentarioUsuarioService {
    
    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Asocia un Usuario existente a un Comentario
     * 
     * @param comentarioId Identificador del Comentario al cual se le va a asociar el Usuario
     * @param usuarioId Identificador del Usuario que se asocia
     * @return Instancia de UsuarioEntity que fue asociada a Comentario
     */
    @Transactional
    public UsuarioEntity setUsuario(Long comentarioId, Long usuarioId) throws EntityNotFoundException {
        log.info("Inicia asociación de usuario con id = {0}, a comentario con id = {1}", usuarioId, comentarioId);

        Optional <ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioId);
        Optional <UsuarioEntity> usuarioEntity = usuarioRepository.findById(usuarioId);

        // no se encontró el comentario
        if (comentarioEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);
        }
        // no se encontró el usuario
        if (usuarioEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);
        }

        comentarioEntity.get().setAutor(usuarioEntity.get());
        log.info("Finaliza asociación de usuario a comentario");
        return usuarioEntity.get();
    }


    /**
     * Obtiene el Usuario asociado al Comentario
     * 
     * @param comentarioId Identificador de la instancia de Comentario
     * @return Instancia de UsuarioEntity asociada a la instancia de Comentario
     */
    @Transactional
    public UsuarioEntity getUsuario(Long comentarioId) throws EntityNotFoundException {
        log.info("Inicia consulta de usuario autor del comentario con id = {0}", comentarioId);

        Optional <ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioId);

        // no encontró el comentario
        if (comentarioEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);
        }

        log.info("Finaliza consulta de usuario autor del comentario");
        return comentarioEntity.get().getAutor();
    }

    /**
     * Desasocia el Usuario existente del Comentario Existente
     * @param comentarioId Comentario que tiene asociado un autor (Usuario)
     */
    @Transactional
    public void removeUsuario(Long comentarioId) throws EntityNotFoundException {
        log.info("Inicia borrado de usuario autor del comentario con id = {0}", comentarioId);

        Optional <ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioId);

        // no encontró el comentario
        if (comentarioEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);
        }

        UsuarioEntity usuarioEntity = comentarioEntity.get().getAutor();

        // el comentario no tiene un usuario asociado
        if (usuarioEntity == null) {
            throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);
        }

        // desasocia el usuario del comentario
        comentarioEntity.get().setAutor(null);
        log.info("Finaliza borrado de usuario autor del comentario");
    }
}
