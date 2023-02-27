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
import co.edu.uniandes.dse.outfits.entities.OutfitEntity;
import co.edu.uniandes.dse.outfits.entities.UsuarioEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de lógica de la asociación entre Comentario y Outfit
 * 
 * @author Álvaro A. Bacca (c4ts0up)
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(
    {
        ComentarioOutfitService.class,
        ComentarioService.class, 
        OutfitService.class
    })
public class ComentarioOutfitServiceTest {

    // servicio a probar
    @Autowired
    private ComentarioOutfitService comentarioOutfitService;

    // proveedor de herramientas para testeo de herramientas
    @Autowired
    private TestEntityManager entityManager;

    // proveedor de datos aleatorios para entidades
    private PodamFactory factory = new PodamFactoryImpl();

    private ComentarioEntity comentarioEntity = new ComentarioEntity();
    private UsuarioEntity usuarioEntity = new UsuarioEntity();
    private OutfitEntity outfitEntity = new OutfitEntity();
    

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
        entityManager.getEntityManager().createQuery("delete from OutfitEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from UsuarioEntity");
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
        outfitEntity = factory.manufacturePojo(OutfitEntity.class);
        entityManager.persist(outfitEntity);

        comentarioEntity.setOutfit(outfitEntity);
        entityManager.persist(comentarioEntity);        
    }

    /**
     * Compara los atributos propios de dos entidades
     */
    private void compareOutfitEntity(OutfitEntity a, OutfitEntity b) throws EntityNotFoundException {
        assertEquals(a.getId(), b.getId());
        assertEquals(a.getDescripcion(), b.getDescripcion());
    }

    /**
     * Prueba para asociar un Outfit existente a un Comentario
     */
    @Test
    void testSetOutfit() throws EntityNotFoundException {
        // entidad de outfit
        OutfitEntity nuevoOutfitEntity = factory.manufacturePojo(OutfitEntity.class);
        entityManager.persist(nuevoOutfitEntity);

        // actualiza el outfit del comentario
        comentarioOutfitService.setOutfit(comentarioEntity.getId(), nuevoOutfitEntity.getId());

        // verifica que el comentario tenga el outfit
        OutfitEntity ultimoOutfitEntity = comentarioOutfitService.getOutfit(comentarioEntity.getId());

        compareOutfitEntity(nuevoOutfitEntity, ultimoOutfitEntity);
    }

    /**
     * Prueba para asociar un Outfit no existente a un Comentario
     */
    @Test
    void testSetOutfitInvalidOutfit() throws EntityNotFoundException {
        // entidad de outfit
        OutfitEntity nuevoOutfitEntity = factory.manufacturePojo(OutfitEntity.class);
        entityManager.persist(nuevoOutfitEntity);

        assertThrows(EntityNotFoundException.class, () -> {
            // actualiza el outfit del comentario
            comentarioOutfitService.setOutfit(comentarioEntity.getId(), 0L);
        });
    }

    /**
     * Prueba para asociar un Outfit existente a un Comentario no existente
     */
    @Test
    void testSetOutfitInvalidComentario() throws EntityNotFoundException {
        // entidad de outfit
        OutfitEntity nuevoOutfitEntity = factory.manufacturePojo(OutfitEntity.class);
        entityManager.persist(nuevoOutfitEntity);

        assertThrows(EntityNotFoundException.class, () -> {
            // actualiza el outfit del comentario
            comentarioOutfitService.setOutfit(0L, nuevoOutfitEntity.getId());
        });
    }

    /**
     * Prueba para obtener un Outfit asociado a un Comentario
     */
    @Test
    void testGetOutfit() throws EntityNotFoundException {
        // obtiene el outfit del comentario
        OutfitEntity ultimoOutfitEntity = comentarioOutfitService.getOutfit(comentarioEntity.getId());

        compareOutfitEntity(ultimoOutfitEntity, outfitEntity);
    }

    /**
     * Prueba para obtener un Outfit asociado a un
     * Comentario inexistente
     */
    @Test
    void testGetOutfitInvalidComentario() throws EntityNotFoundException {
        assertThrows(EntityNotFoundException.class, () -> {
            // obtiene el outfit del comentario
            OutfitEntity ultimoOutfitEntity =     comentarioOutfitService.getOutfit(0L);
        });
    }

    /**
     * Prueba para desasociar un Outfit existente del Comentario
     */
    @Test
    void testRemoveOutfit() throws EntityNotFoundException {
        // elimina al outfit
        comentarioOutfitService.removeOutfit(comentarioEntity.getId());

        OutfitEntity nuevoOutfitEntity = comentarioOutfitService.getOutfit(comentarioEntity.getId());

        assertNull(nuevoOutfitEntity);
    }

    /**
     * Prueba para desasociar un Outfit existente del Comentario inexistente
     */
    @Test
    void testRemoveOutfitInvalidComentario() throws EntityNotFoundException {
        assertThrows(EntityNotFoundException.class, () -> {
            // elimina al outfit
            comentarioOutfitService.removeOutfit(0L);
        });
    }

    /**
     * Prueba para desasociar un Outfit inexistente del
     * Comentario
     */
    @Test
    void testRemoveOutfitInvalidOutfit() throws EntityNotFoundException {
        assertThrows(EntityNotFoundException.class, () -> {
            // crea un nuevo comentario sin outfit
            ComentarioEntity nuevoComentarioEntity = factory.manufacturePojo(ComentarioEntity.class);

            // lo persiste
            entityManager.persist(nuevoComentarioEntity);

            // intenta eliminar el outfit
            comentarioOutfitService.removeOutfit(nuevoComentarioEntity.getId());
        });
    }
}
