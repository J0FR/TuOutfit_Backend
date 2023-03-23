package co.edu.uniandes.dse.outfits.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    /**
     * Prueba para asociar un comentario que no existe a un usuario.
     *
     */
    @Test
    void testAddInvalidComentario() {
        assertThrows(EntityNotFoundException.class, () -> {
            UsuarioEntity newUsuario = factory.manufacturePojo(UsuarioEntity.class);
            entityManager.persist(newUsuario);
            usuarioComentarioService.addComentario(newUsuario.getId(), 0L);
        });
    }

    /**
     * Prueba para asociar un comentario a un usuario que no existe.
     *
     */

    @Test
    void testAddComentarioInvalidUsuario() throws EntityNotFoundException, IllegalOperationException {
        assertThrows(EntityNotFoundException.class, () -> {
            ComentarioEntity comentario = factory.manufacturePojo(ComentarioEntity.class);
            entityManager.persist(comentario);
            usuarioComentarioService.addComentario(0L, comentario.getId());
        });
    }

    /**
     * Prueba para consultar la lista de comentario de un usuario.
     */

    @Test
    void testGetComentarios() throws EntityNotFoundException {
        List<ComentarioEntity> comentarioEntities = usuarioComentarioService.getComentarios(usuario.getId());

        assertEquals(comentarioList.size(), comentarioEntities.size());

        for (int i = 0; i < comentarioList.size(); i++) {
            assertTrue(comentarioEntities.contains(comentarioList.get(0)));
        }
    }

    /**
     * Prueba para consultar la lista de comentarios de un usuario que no existe.
     */
    @Test
    void testGetComentariosInvalidUsuario() {
        assertThrows(EntityNotFoundException.class, () -> {
            usuarioComentarioService.getComentarios(0L);
        });
    }

    /**
     * Prueba para consultar un comentario de un usuario.
     *
     * @throws throws EntityNotFoundException, IllegalOperationException
     */
    @Test
    void testGetComentario() throws EntityNotFoundException, IllegalOperationException {
        ComentarioEntity comentarioEntity = comentarioList.get(0);
        ComentarioEntity comentario = usuarioComentarioService.getComentario(usuario.getId(), comentarioEntity.getId());
        assertNotNull(usuario);

        assertEquals(comentario.getAutor(), comentarioEntity.getAutor());
        assertEquals(comentario.getCalificacion(), comentarioEntity.getCalificacion());
        assertEquals(comentario.getClass(), comentarioEntity.getClass());
        assertEquals(comentario.getId(), comentarioEntity.getId());
        assertEquals(comentario.getMensaje(), comentarioEntity.getMensaje());
        assertEquals(comentario.getTitulo(), comentarioEntity.getTitulo());
        assertEquals(comentario.getPrenda(), comentarioEntity.getPrenda());
        assertEquals(comentario.getOutfit(), comentarioEntity.getOutfit());

    }

    /**
     * Prueba para consultar un comentario que no existe de un usuario.
     *
     * @throws throws EntityNotFoundException, IllegalOperationException
     */
    @Test
    void testGetInvalidComentario() {
        assertThrows(EntityNotFoundException.class, () -> {
            usuarioComentarioService.getComentario(usuario.getId(), 0L);
        });
    }

    /**
     * Prueba para consultar un comentario de un usuario que no existe.
     *
     * @throws throws EntityNotFoundException, IllegalOperationException
     */
    @Test
    void testGetComentarioInvalidUsuario() {
        assertThrows(EntityNotFoundException.class, () -> {
            ComentarioEntity comentarioEntity = comentarioList.get(0);
            usuarioComentarioService.getComentario(0L, comentarioEntity.getId());
        });
    }

    /**
     * Prueba para obtener un comentario no asociado a un usuario.
     *
     */
    @Test
    void testGetNotAssociatedComentario() {
        assertThrows(IllegalOperationException.class, () -> {
            UsuarioEntity newUsuario = factory.manufacturePojo(UsuarioEntity.class);
            entityManager.persist(newUsuario);
            ComentarioEntity comentario = factory.manufacturePojo(ComentarioEntity.class);
            entityManager.persist(comentario);
            usuarioComentarioService.getComentario(newUsuario.getId(), comentario.getId());
        });
    }

    /**
     * Prueba desasociar un comentario con un usuario.
     *
     */
    @Test
    void testRemoveOutfit() throws EntityNotFoundException {
        for (ComentarioEntity comentario : comentarioList) {
            usuarioComentarioService.removeComentario(usuario.getId(), comentario.getId());
        }
        assertTrue(usuarioComentarioService.getComentarios(usuario.getId()).isEmpty());
    }

    /**
     * Prueba desasociar un comentario que no existe con un usuario.
     *
     */
    @Test
    void testRemoveInvalidComentario() {
        assertThrows(EntityNotFoundException.class, () -> {
            usuarioComentarioService.removeComentario(usuario.getId(), 0L);
        });
    }

    /**
     * Prueba desasociar un outfit con un usuario que no existe.
     *
     */
    @Test
    void testRemoveComentarioInvalidUsuario() {
        assertThrows(EntityNotFoundException.class, () -> {
            usuarioComentarioService.removeComentario(0L, comentarioList.get(0).getId());
        });
    }

}
