package co.edu.uniandes.dse.outfits.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import co.edu.uniandes.dse.outfits.entities.ComentarioEntity;
import co.edu.uniandes.dse.outfits.entities.PrendaEntity;
import co.edu.uniandes.dse.outfits.entities.UsuarioEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de lógica de la asociación entre Comentario y Usuario
 * 
 * @author Álvaro A. Bacca (c4ts0up)
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(
    {
        ComentarioUsuarioService.class,
        ComentarioService.class, 
        UsuarioService.class
    })
public class ComentarioUsuarioServiceTest {

    // servicio a probar
    @Autowired
    private ComentarioUsuarioService comentarioUsuarioService;

    // proveedor de herramientas para testeo de herramientas
    @Autowired
    private TestEntityManager entityManager;

    // proveedor de datos aleatorios para entidades
    private PodamFactory factory = new PodamFactoryImpl();

    private ComentarioEntity comentarioEntity = new ComentarioEntity();
    private UsuarioEntity usuarioEntity = new UsuarioEntity();
    private PrendaEntity prendaEntity = new PrendaEntity();
    

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    /**
     * Limpia las tablas que están implicadas en las pruebas
     */
    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from ComentarioEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from UsuarioEntity").executeUpdate();
    }

    /**
     * Inserta los datos iniciales para las pruebas
     */
    private void insertData() {
        // datos aleatores para entidad de usuario
        usuarioEntity = factory.manufacturePojo( UsuarioEntity.class);
        entityManager.persist(usuarioEntity);

        // datos aleatorios para entidad de comentario
        comentarioEntity = factory.manufacturePojo(ComentarioEntity.class);
        
        // datos extra para crear correctamente el comentario
        prendaEntity = factory.manufacturePojo(PrendaEntity.class);
        entityManager.persist(comentarioEntity);
        comentarioEntity.setAutor(usuarioEntity);
    }

    /**
     * Compara los atributos propios de dos entidades
     */
    private void compareUsuarioEntity(UsuarioEntity a, UsuarioEntity b) throws EntityNotFoundException {
        assertEquals(a.getId(), b.getId());
        assertEquals(a.getNombre(), b.getNombre());
        assertEquals(a.getEdad(), b.getEdad());
        assertEquals(a.getEmail(), b.getEmail());
    }

    /**
     * Prueba para asociar un Usuario existente a un Comentario
     */
    @Test
    void testSetUsuario() throws EntityNotFoundException {
        // entidad de usuario
        UsuarioEntity nuevoUsuarioEntity = factory.manufacturePojo(UsuarioEntity.class);
        entityManager.persist(nuevoUsuarioEntity);

        // actualiza el usuario del comentario
        comentarioUsuarioService.setUsuario(comentarioEntity.getId(), nuevoUsuarioEntity.getId());

        // verifica que el comentario tenga el usuario
        UsuarioEntity ultimoUsuarioEntity = comentarioUsuarioService.getUsuario(comentarioEntity.getId());

        compareUsuarioEntity(nuevoUsuarioEntity, ultimoUsuarioEntity);
    }

    /**
     * Prueba para asociar un Usuario no existente a un Comentario
     */
    @Test
    void testSetUsuarioInvalidUsuario() throws EntityNotFoundException {
        // entidad de usuario
        UsuarioEntity nuevoUsuarioEntity = factory.manufacturePojo(UsuarioEntity.class);
        entityManager.persist(nuevoUsuarioEntity);

        assertThrows(EntityNotFoundException.class, () -> {
            // actualiza el usuario del comentario
            comentarioUsuarioService.setUsuario(comentarioEntity.getId(), 0L);
        });
    }

    /**
     * Prueba para asociar un Usuario existente a un Comentario no existente
     */
    @Test
    void testSetUsuarioInvalidComentario() throws EntityNotFoundException {
        // entidad de usuario
        UsuarioEntity nuevoUsuarioEntity = factory.manufacturePojo(UsuarioEntity.class);
        entityManager.persist(nuevoUsuarioEntity);

        assertThrows(EntityNotFoundException.class, () -> {
            // actualiza el usuario del comentario
            comentarioUsuarioService.setUsuario(0L, nuevoUsuarioEntity.getId());
        });
    }

    /**
     * Prueba para obtener un Usuario asociado a un Comentario
     */
    @Test
    void testGetUsuario() throws EntityNotFoundException {
        // obtiene el usuario del comentario
        UsuarioEntity ultimoUsuarioEntity = comentarioUsuarioService.getUsuario(comentarioEntity.getId());

        compareUsuarioEntity(ultimoUsuarioEntity, usuarioEntity);
    }

    /**
     * Prueba para obtener un Usuario asociado a un
     * Comentario inexistente
     */
    @Test
    void testGetUsuarioInvalidComentario() throws EntityNotFoundException {
        assertThrows(EntityNotFoundException.class, () -> {
            // obtiene el usuario del comentario
            UsuarioEntity ultimoUsuarioEntity =     comentarioUsuarioService.getUsuario(0L);
        });
    }

    /**
     * Prueba para desasociar un Usuario existente del Comentario
     */
    @Test
    void testRemoveUsuario() throws EntityNotFoundException {
        // elimina al usuario
        comentarioUsuarioService.removeUsuario(comentarioEntity.getId());

        UsuarioEntity nuevoUsuarioEntity = comentarioUsuarioService.getUsuario(comentarioEntity.getId());

        assertNull(nuevoUsuarioEntity);
    }

    /**
     * Prueba para desasociar un Usuario existente del Comentario inexistente
     */
    @Test
    void testRemoveUsuarioInvalidComentario() throws EntityNotFoundException {
        assertThrows(EntityNotFoundException.class, () -> {
            // elimina al usuario
            comentarioUsuarioService.removeUsuario(0L);
        });
    }

    /**
     * Prueba para desasociar un Usuario inexistente del
     * Comentario
     */
    @Test
    void testRemoveUsuarioInvalidUsuario() throws EntityNotFoundException {
        assertThrows(EntityNotFoundException.class, () -> {
            // crea un nuevo comentario sin usuario
            ComentarioEntity nuevoComentarioEntity = factory.manufacturePojo(ComentarioEntity.class);

            // lo persiste
            entityManager.persist(nuevoComentarioEntity);

            // intenta eliminar el usuario
            comentarioUsuarioService.removeUsuario(nuevoComentarioEntity.getId());
        });
    }
}
