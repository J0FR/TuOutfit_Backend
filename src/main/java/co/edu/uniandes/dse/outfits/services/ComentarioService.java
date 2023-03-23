package co.edu.uniandes.dse.outfits.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import co.edu.uniandes.dse.outfits.entities.ComentarioEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.ErrorMessage;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.repositories.ComentarioRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ComentarioService {
    @Autowired
    ComentarioRepository comentarioRepository;

    private boolean validarReglasDeNegocio(ComentarioEntity comentarioEntity) {
        // la calificación está entre 1 y 5
        return (1 <= comentarioEntity.getCalificacion() && comentarioEntity.getCalificacion() <= 5);
    }

    @Transactional
    public ComentarioEntity createComentario(ComentarioEntity comentarioEntity) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia creación comentario");

        // Revisa que ninguno de los atributos sea nulo.
        // Si no tienen valor, deben estar vacíos, no nulos.
        if (comentarioEntity.getCalificacion() == null) {
            throw new IllegalOperationException("Calificación faltante");
        }
        else if (comentarioEntity.getMensaje() == null) {
            throw new IllegalOperationException("Mensaje faltante");
        }
        // TODO: ambos podrían no ser nulos
        else if (comentarioEntity.getOutfit() != null && comentarioEntity.getPrenda() != null) {
            throw new IllegalOperationException("Solo se puede comentar una prenda o un outfit");
        }
        else if (comentarioEntity.getTitulo() == null) {
            throw new IllegalOperationException("Titulo faltante");
        }

        // valida las reglas de negocio
        if (!validarReglasDeNegocio(comentarioEntity)) {
            throw new IllegalOperationException("Reglas de negocio no cumplidas");
        }

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
            throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);
        }

        log.info("Finaliza consulta del comentario con id = {0}", comentarioID);
        return comentarioEntity.get();
    }

    @Transactional
    public ComentarioEntity updateComentario(Long comentarioID, ComentarioEntity comentario) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia actualización del comentario con id = {0}", comentarioID);

        Optional <ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioID);
        if (comentarioEntity.isEmpty()) {
            throw new EntityNotFoundException (ErrorMessage.COMENTARIO_NOT_FOUND);
        }

        // Revisa que ninguno de los atributos sea nulo.
        // Si no tienen valor, deben estar vacíos, no nulos.
        if (comentario.getAutor() == null) {
            throw new IllegalOperationException(ErrorMessage.USUARIO_NOT_FOUND);
        }
        else if (comentario.getCalificacion() == null) {
            throw new IllegalOperationException("Calificación faltante");
        }
        else if (comentario.getMensaje() == null) {
            throw new IllegalOperationException("Mensaje faltante");
        }
        else if (comentario.getOutfit() == null && comentario.getPrenda() == null) {
            throw new IllegalOperationException("Outfit o prenda faltante");
        }
        else if (comentario.getOutfit() != null && comentario.getPrenda() != null) {
            throw new IllegalOperationException("Solo se puede comentar una prenda o un outfit");
        }
        else if (comentario.getTitulo() == null) {
            throw new IllegalOperationException("Titulo faltante");
        }

        // valida las reglas de negocio
        if (!validarReglasDeNegocio(comentario)) {
            throw new IllegalOperationException("Reglas de negocio no cumplidas");
        }

        comentario.setId(comentarioID);
        log.info("Finaliza actualización del comentario con id = {0}", comentarioID);
        return comentarioRepository.save(comentario);
    }

    @Transactional
    public void deleteComentario(Long comentarioID) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia borrado del comentario con id = {0}", comentarioID);

        Optional <ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioID);

        if (comentarioEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);
        }

        // borra el comentario de la prenda
        if (comentarioEntity.get().getPrenda() != null) {
            comentarioEntity.get().getPrenda().getComentarios().remove(comentarioEntity.get());
        }

        // borra el comentario del outfit
        if (comentarioEntity.get().getOutfit() != null) {
            comentarioEntity.get().getOutfit().getComentarios().remove(comentarioEntity.get());
        }

        // borra el comentario del autor
        comentarioEntity.get().getAutor().getComentarios().remove(comentarioEntity.get());

        comentarioRepository.deleteById(comentarioID);
        log.info("Finaliza borrado del comentario con id = {0}", comentarioID);
        
    }
}