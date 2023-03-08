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
 * Pruebas de lógica de la asociación entre Comentario y Prenda
 * 
 * @author Álvaro A. Bacca (c4ts0up)
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(
    {
        ComentarioPrendaService.class,
        ComentarioService.class, 
        PrendaService.class
    })
public class ComentarioPrendaServiceTest {

    // servicio a probar
    @Autowired
    private ComentarioPrendaService comentarioPrendaService;

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
        entityManager.getEntityManager().createQuery("delete from PrendaEntity").executeUpdate();
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
        prendaEntity = factory.manufacturePojo(PrendaEntity.class);
        entityManager.persist(prendaEntity);

        comentarioEntity.setPrenda(prendaEntity);
        entityManager.persist(comentarioEntity);        
    }

    /**
     * Compara los atributos propios de dos entidades
     */
    private void comparePrendaEntity(PrendaEntity a, PrendaEntity b) throws EntityNotFoundException {
        assertEquals(a.getId(), b.getId());
        assertEquals(a.getUrl_sitio_web_compra(), b.getUrl_sitio_web_compra());
    }

    /**
     * Prueba para asociar un Prenda existente a un Comentario
     */
    @Test
    void testSetPrenda() throws EntityNotFoundException {
        // entidad de prenda
        PrendaEntity nuevaPrendaEntity = factory.manufacturePojo(PrendaEntity.class);
        entityManager.persist(nuevaPrendaEntity);

        // actualiza la prenda del comentario
        comentarioPrendaService.setPrenda(comentarioEntity.getId(), nuevaPrendaEntity.getId());

        // verifica que el comentario tenga el prenda
        PrendaEntity ultimoPrendaEntity = comentarioPrendaService.getPrenda(comentarioEntity.getId());

        comparePrendaEntity(nuevaPrendaEntity, ultimoPrendaEntity);
    }

    /**
     * Prueba para asociar una Prenda no existente a un Comentario
     */
    @Test
    void testSetPrendaInvalidPrenda() throws EntityNotFoundException {
        // entidad de prenda
        PrendaEntity nuevaPrendaEntity = factory.manufacturePojo(PrendaEntity.class);
        entityManager.persist(nuevaPrendaEntity);

        assertThrows(EntityNotFoundException.class, () -> {
            // actualiza el prenda del comentario
            comentarioPrendaService.setPrenda(comentarioEntity.getId(), 0L);
        });
    }

    /**
     * Prueba para asociar una Prenda existente a un Comentario no existente
     */
    @Test
    void testSetPrendaInvalidComentario() throws EntityNotFoundException {
        // entidad de prenda
        PrendaEntity nuevaPrendaEntity = factory.manufacturePojo(PrendaEntity.class);
        entityManager.persist(nuevaPrendaEntity);

        assertThrows(EntityNotFoundException.class, () -> {
            // actualiza el prenda del comentario
            comentarioPrendaService.setPrenda(0L, nuevaPrendaEntity.getId());
        });
    }

    /**
     * Prueba para obtener una Prenda asociado a un Comentario
     */
    @Test
    void testGetPrenda() throws EntityNotFoundException {
        // obtiene el prenda del comentario
        PrendaEntity ultimaPrendaEntity = comentarioPrendaService.getPrenda(comentarioEntity.getId());

        comparePrendaEntity(ultimaPrendaEntity, prendaEntity);
    }

    /**
     * Prueba para obtener una Prenda asociado a un
     * Comentario inexistente
     */
    @Test
    void testGetPrendaInvalidComentario() throws EntityNotFoundException {
        assertThrows(EntityNotFoundException.class, () -> {
            // obtiene el prenda del comentario
            PrendaEntity ultimaPrendaEntity =     comentarioPrendaService.getPrenda(0L);
        });
    }

    /**
     * Prueba para desasociar una Prenda existente del Comentario
     */
    @Test
    void testRemovePrenda() throws EntityNotFoundException {
        // elimina al prenda
        comentarioPrendaService.removePrenda(comentarioEntity.getId());

        PrendaEntity nuevaPrendaEntity = comentarioPrendaService.getPrenda(comentarioEntity.getId());

        assertNull(nuevaPrendaEntity);
    }

    /**
     * Prueba para desasociar una Prenda existente del Comentario inexistente
     */
    @Test
    void testRemovePrendaInvalidComentario() throws EntityNotFoundException {
        assertThrows(EntityNotFoundException.class, () -> {
            // elimina al prenda
            comentarioPrendaService.removePrenda(0L);
        });
    }

    /**
     * Prueba para desasociar una Prenda inexistente del
     * Comentario
     */
    @Test
    void testRemovePrendaInvalidPrenda() throws EntityNotFoundException {
        assertThrows(EntityNotFoundException.class, () -> {
            // crea un nuevo comentario sin prenda
            ComentarioEntity nuevaComentarioEntity = factory.manufacturePojo(ComentarioEntity.class);

            // lo persiste
            entityManager.persist(nuevaComentarioEntity);

            // intenta eliminar el prenda
            comentarioPrendaService.removePrenda(nuevaComentarioEntity.getId());
        });
    }
}
