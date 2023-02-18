package co.edu.uniandes.dse.outfits.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.outfits.entities.ComentarioEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.repositories.ComentarioRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ComentarioService {
    @Autowired
    ComentarioRepository comentarioRepository;

    @Transactional
    public ComentarioEntity createComentario(ComentarioEntity comentarioEntity) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia creación comentario");

        // TODO

        log.info("Finaliza creación comentario");

        return comentarioRepository.save(comentarioEntity);
    }

    @Transactional
    public List<ComentarioEntity> getComentarios() {
        log.info("Inicia obtención de todos los comentarios");
        return comentarioRepository.findAll();
    }

    @Transactional
    public ComentarioEntity getComentario(Long comentarioID) throws EntityNotFoundException {
        log.info("Inicia consulta del comentario con id = {0}", comentarioID);

        Optional <ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioID);

        if (comentarioEntity.isEmpty()) {
            throw new EntityNotFoundException("TODO Añadir mensaje");
        }

        log.info("Finaliza consulta del comentario con id = {0}", comentarioID);
        return comentarioEntity.get();
    }

    @Transactional
    public ComentarioEntity updateComentario(Long comentarioID, ComentarioEntity comentario) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia actualización del comentario con id = {0}", comentarioID);

        Optional <ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioID);
        if (comentarioEntity.isEmpty()) {
            throw new EntityNotFoundException ("TODO Mensaje");
        }

        // TODO: validar reglas de negocio

        comentario.setId(comentarioID);
        log.info("Finaliza actualización del comentario con id = {0}", comentarioID);
        return comentarioRepository.save(comentario);
    }

    @Transactional
    public void deleteComentario(Long comentarioID) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia borrado del comentario con id = {0}", comentarioID);

        Optional <ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioID);

        if (comentarioEntity.isEmpty()) {
            throw new EntityNotFoundException("TODO: Mensaje");
        }

        // TODO: validar reglas de negocio

        comentarioRepository.deleteById(comentarioID);
        log.info("Finaliza borrado del comentario con id = {0}", comentarioID);
        
    }
}