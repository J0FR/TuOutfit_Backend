package co.edu.uniandes.dse.outfits.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.outfits.entities.UsuarioEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.ErrorMessage;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.repositories.ComentarioRepository;
import co.edu.uniandes.dse.outfits.repositories.OutfitRepository;
import co.edu.uniandes.dse.outfits.repositories.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    ComentarioRepository comentarioRepository;

    @Autowired
    OutfitRepository outfitRepository;

    @Transactional
    public UsuarioEntity createUsuario(UsuarioEntity usuario) throws IllegalOperationException {
        log.info("Inicia proceso de creación del Usuario");
        if (usuario.getNombre() == null || usuario.getNombre().equals("")) {
            throw new IllegalOperationException("El usuario no tiene nombre valido");
        }
        if (usuario.getGenero() == null) {
            throw new IllegalOperationException("El usuario no tiene genero");
        }
        if (usuario.getEmail() == null || usuario.getEmail().equals("")) {
            throw new IllegalOperationException("El usuario no tiene Email valido");
        }
        if (usuario.getEdad() == null || usuario.getEdad() < 0 || usuario.getEdad() > 100) {
            throw new IllegalOperationException("El usuario no tiene edad valida");
        }

        log.info("Termina proceso de creación del Usuario");
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public List<UsuarioEntity> getUsuarios() {
        log.info("Inicia proceso de consultar todos los usuarios");
        return usuarioRepository.findAll();
    }

    @Transactional
    public UsuarioEntity getUsuario(Long usuarioId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar el usuario con id = {0}", usuarioId);
        Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findById(usuarioId);
        if (usuarioEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);
        }
        log.info("Termina proceso de consultar la usuario con id = {0}", usuarioId);
        return usuarioEntity.get();
    }

    @Transactional
    public UsuarioEntity updateUsuario(Long usuarioId, UsuarioEntity usuario)
            throws EntityNotFoundException, IllegalOperationException {

        log.info("Inicia proceso de actualizar el usuario con id = {0}", usuarioId);
        Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findById(usuarioId);
        if (usuarioEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);

        if (usuario.getNombre() == null || usuario.getNombre().equals("")) {
            throw new IllegalOperationException("Nombre no valido");
        }
        if (usuario.getGenero() == null) {
            throw new IllegalOperationException("Genero no valido");
        }
        if (usuario.getEmail() == null || usuario.getEmail().equals("")) {
            throw new IllegalOperationException("Email no valido");
        }
        if (usuario.getEdad() == null || usuario.getEdad() < 0 || usuario.getEdad() > 100) {
            throw new IllegalOperationException("Edad no valida");
        }

        usuario.setId(usuarioId);
        log.info("Termina proceso de actualizar el usuario con id = {0}", usuarioId);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void deleteUsuario(Long usuarioId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de borrar el usuario con id = {0}", usuarioId);
        Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findById(usuarioId);
        if (usuarioEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);
        }
        if (usuarioEntity.get().getComentarios().size() != 0) {
            throw new IllegalOperationException("El usuario tiene comentarios asociados");
        }
        usuarioRepository.deleteById(usuarioId);
        log.info("Terminas proceso de borrar el usuario con id = {0}", usuarioId);
    }

}
