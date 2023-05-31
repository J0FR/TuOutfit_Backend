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
import co.edu.uniandes.dse.outfits.entities.MarcaEntity;
import co.edu.uniandes.dse.outfits.entities.PrendaEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(
    {
        PrendaMarcaService.class,
        MarcaService.class, 
        PrendaService.class
    })
public class PrendaMarcaServiceTest {

    // servicio a probar
    @Autowired
    private PrendaMarcaService prendaMarcaService;

    // proveedor de herramientas para testeo de herramientas
    @Autowired
    private TestEntityManager entityManager;

    // proveedor de datos aleatorios para entidades
    private PodamFactory factory = new PodamFactoryImpl();

    private MarcaEntity marcaEntity = new MarcaEntity();
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
        entityManager.getEntityManager().createQuery("delete from MarcaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PrendaEntity").executeUpdate();
    }

    /**
     * Inserta los datos iniciales para las pruebas
     */
    private void insertData() {

        // datos aleatorios para entidad de marca
        marcaEntity = factory.manufacturePojo(MarcaEntity.class);

        prendaEntity = factory.manufacturePojo(PrendaEntity.class);
        entityManager.persist(prendaEntity);

        prendaEntity.setMarca(marcaEntity);
        entityManager.persist(prendaEntity);        
    }
    private void compareMarcaEntity(MarcaEntity a, MarcaEntity b) throws EntityNotFoundException {
        assertEquals(a.getId(), b.getId());
        assertEquals(a.getNombre(), b.getNombre());
        assertEquals(a.getUrlSitioWeb(), b.getUrlSitioWeb());

    }

    /**
     * Prueba para actualizar un Comentario
     */
    /**
     * Prueba para asociar una marca no existente a una prenda
     */
    @Test
    void testAddMarcaInvalidMarca() throws EntityNotFoundException {
        // entidad de prenda
        MarcaEntity nuevaMarcaEntity = factory.manufacturePojo(MarcaEntity.class);
        entityManager.persist(nuevaMarcaEntity);

        assertThrows(EntityNotFoundException.class, () -> {
            // actualiza el marca de la prenda
            prendaMarcaService.addMarca(prendaEntity.getId(), 0L);
        });
    }

    /**
     * Prueba para asociar una Prenda existente a un Comentario no existente
     */
    @Test
    void testAddMarcaInvalidPrenda() throws EntityNotFoundException {
        // entidad de prenda
        MarcaEntity nuevaMarcaEntity = factory.manufacturePojo(MarcaEntity.class);
        entityManager.persist(nuevaMarcaEntity);

        assertThrows(EntityNotFoundException.class, () -> {
            // actualiza el prenda del comentario
            prendaMarcaService.addMarca(0L, nuevaMarcaEntity.getId());
        });
    }

    /**
     * Prueba para obtener una Prenda asociado a un Comentario
     */
    @Test
    void testGetMarca() throws EntityNotFoundException {
        // obtiene el prenda del comentario
        MarcaEntity ultimaMarcaEntity = prendaMarcaService.getMarca(prendaEntity.getId());

        compareMarcaEntity(ultimaMarcaEntity, marcaEntity);
    }

    /**
     * Prueba para obtener una Prenda asociado a un
     * Comentario inexistente
     */
    @Test
    void testGetMarcaInvalidPrenda() throws EntityNotFoundException {
        assertThrows(EntityNotFoundException.class, () -> {
            // obtiene el prenda del comentario
            MarcaEntity ultimaMarcaEntity = prendaMarcaService.getMarca(0L);
        });
    }

    /**
     * Prueba para desasociar una Prenda existente del Comentario
     */
    @Test
    void testRemoveMarca() throws EntityNotFoundException {
        // elimina al marca
        prendaMarcaService.removeMarca(prendaEntity.getId());
        

        MarcaEntity nuevaMarcaEntity = prendaMarcaService.getMarca(prendaEntity.getId());   
        assertNull(nuevaMarcaEntity);
    }

    /**
     * Prueba para desasociar una Prenda existente del Comentario inexistente
     */
    @Test
    void testRemovePrendaInvalidComentario() throws EntityNotFoundException {
        assertThrows(EntityNotFoundException.class, () -> {
            // elimina al prenda
            prendaMarcaService.removeMarca(0L);
        });
    }

    /**
     * Prueba para desasociar una Prenda inexistente del
     * Comentario
     */
    @Test
    void testRemoveMarcaInvalidMarca() throws EntityNotFoundException {
        assertThrows(EntityNotFoundException.class, () -> {
            // crea un nueva prenda sin marca
            PrendaEntity nuevPrendaEntity = factory.manufacturePojo(PrendaEntity.class);

            // lo persiste
            entityManager.persist(nuevPrendaEntity);

            // intenta eliminar la marca
            prendaMarcaService.removeMarca(nuevPrendaEntity.getId());
        });
    }
}