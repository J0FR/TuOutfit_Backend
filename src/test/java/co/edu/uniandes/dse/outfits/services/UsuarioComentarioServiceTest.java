package co.edu.uniandes.dse.outfits.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import co.edu.uniandes.dse.outfits.entities.ComentarioEntity;
import co.edu.uniandes.dse.outfits.entities.UsuarioEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * pruebas de logica de la relacion Usuario - Comentario
 * 
 * @author Daniel Pedroza
 * 
 */

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(UsuarioComentarioService.class)
class UsuarioComentarioServiceTest {

    @Autowired
    private UsuarioComentarioService usuarioComentarioService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private UsuarioEntity usuario = new UsuarioEntity();
    private List<ComentarioEntity> comentarioList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from UsuarioEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ComentarioEntity").executeUpdate();
    }

    private void insertData() {

        usuario = factory.manufacturePojo(UsuarioEntity.class);
        entityManager.persist(usuario);

        for (int i = 0; i < 3; i++) {

            ComentarioEntity entity = factory.manufacturePojo(ComentarioEntity.class);
            entityManager.persist(entity);
            comentarioList.add(entity);
            usuario.getComentarios().add(entity);
        }
    }

    /**
     * Prueba para asociar un comentario existente a un usuario.
     * 
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @Test
    void testAddComentario() throws EntityNotFoundException, IllegalOperationException {
        UsuarioEntity newUsuario = factory.manufacturePojo(UsuarioEntity.class);
        entityManager.persist(newUsuario);
        ComentarioEntity comentario = factory.manufacturePojo(ComentarioEntity.class);
        entityManager.persist(comentario);
        usuarioComentarioService.addComentario(newUsuario.getId(), comentario.getId());

        ComentarioEntity lastComentario = usuarioComentarioService.getComentario(newUsuario.getId(),
                comentario.getId());

        assertEquals(comentario.getAutor(), lastComentario.getAutor());
        assertEquals(comentario.getCalificacion(), lastComentario.getCalificacion());
        assertEquals(comentario.getClass(), lastComentario.getClass());
        assertEquals(comentario.getId(), lastComentario.getId());
        assertEquals(comentario.getMensaje(), lastComentario.getMensaje());
        assertEquals(comentario.getTitulo(), lastComentario.getTitulo());
        assertEquals(comentario.getPrenda(), lastComentario.getPrenda());
        assertEquals(comentario.getOutfit(), lastComentario.getOutfit());

    }

}
