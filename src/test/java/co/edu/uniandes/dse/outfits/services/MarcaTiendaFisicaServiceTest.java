package co.edu.uniandes.dse.outfits.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import co.edu.uniandes.dse.outfits.entities.MarcaEntity;
import co.edu.uniandes.dse.outfits.entities.TiendaFisicaEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(MarcaTiendaFisicaService.class)
public class MarcaTiendaFisicaServiceTest {
    private PodamFactory factory = new PodamFactoryImpl();

    @Autowired
    private MarcaTiendaFisicaService marcaTiendaFisicaService;

    @Autowired
    private TestEntityManager entityManager;

    private List<MarcaEntity> marcaList = new ArrayList<>();
    private List<TiendaFisicaEntity> tiendaFisicaList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from PrendaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from MarcaEntity").executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
     */
    private void insertData() {
        for (int i = 0; i < 3; i++) {
            TiendaFisicaEntity tiendaFisicaEntity = factory.manufacturePojo(TiendaFisicaEntity.class);
            entityManager.persist(tiendaFisicaEntity);
            tiendaFisicaList.add(tiendaFisicaEntity);
        }
        for (int i = 0; i < 3; i++) {
            MarcaEntity marcaEntity = factory.manufacturePojo(MarcaEntity.class);
            entityManager.persist(marcaEntity);
            if (i == 0) {
                marcaEntity.setTiendasFisicas(tiendaFisicaList);
            }
            marcaList.add(marcaEntity);
        }
    }

    /**
     * Prueba para asociar una tienda fisica existente a una marca.
     * 
     * @throws EntityNotFoundException
     */
    @Test
    void testAddTiendaFisica() throws EntityNotFoundException {
        MarcaEntity entity = marcaList.get(0);
        TiendaFisicaEntity tiendaFisicaEntity = tiendaFisicaList.get(1);
        TiendaFisicaEntity response = marcaTiendaFisicaService.addTiendaFisica(entity.getId(),
                tiendaFisicaEntity.getId());

        assertNotNull(response);
        assertEquals(tiendaFisicaEntity.getId(), response.getId());
    }

    /**
     * Prueba para asociar un tienda fisica existente a una marca que no existe.
     * 
     * @throws EntityNotFoundException
     */
    @Test
    void testAddInvalidTiendaFisica() {
        assertThrows(EntityNotFoundException.class, () -> {
            MarcaEntity marcaEntity = marcaList.get(1);
            marcaTiendaFisicaService.addTiendaFisica(0L, marcaEntity.getId());
        });
    }

    /**
     * Prueba para asociar un tienda fisica que no existe a una marca.
     * 
     * @throws EntityNotFoundException
     */
    @Test
    void testAddTiendaFisicaInvalidMarca() {
        assertThrows(EntityNotFoundException.class, () -> {
            TiendaFisicaEntity entity = tiendaFisicaList.get(0);
            marcaTiendaFisicaService.addTiendaFisica(entity.getId(), 0L);
        });
    }

    /**
     * Prueba para consultar todas las tiendas fisicas de una marca.
     * 
     * @throws EntityNotFoundException
     */
    @Test
    void testgetTiendasFisicas() throws EntityNotFoundException {
        MarcaEntity entity = marcaList.get(0);
        List<TiendaFisicaEntity> resultEntity = marcaTiendaFisicaService.getTiendasFisicas(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getTiendasFisicas(), resultEntity);
    }

    /**
     * Prueba para consultar una Tienda Fisica de una marca que no existe.
     * 
     * @throws EntityNotFoundException
     */
    @Test
    void testgetTiendasFisicasInvalidMarca() throws EntityNotFoundException {
        assertThrows(EntityNotFoundException.class, () -> {
            marcaTiendaFisicaService.getTiendasFisicas(0L);
        });
    }

    /**
     * Prueba para consultar un comentario de un usuario.
     *
     * @throws throws EntityNotFoundException, IllegalOperationException
     */
    @Test
    void testGetTiendaFisica() throws EntityNotFoundException, IllegalOperationException {
        TiendaFisicaEntity tiendaFisicaEntity = tiendaFisicaList.get(0);
        TiendaFisicaEntity tiendaFisica = marcaTiendaFisicaService.getTiendaFisica(marcaList.get(0).getId(),
                tiendaFisicaEntity.getId());
        assertNotNull(marcaList.get(0));

        assertEquals(tiendaFisicaEntity.getId(), tiendaFisica.getId());
    }

    /**
     * Prueba para consultar un comentario que no existe de un usuario.
     *
     * @throws throws EntityNotFoundException, IllegalOperationException
     */
    @Test
    void testGetInvalidTiendaFisica() {
        assertThrows(EntityNotFoundException.class, () -> {
            marcaTiendaFisicaService.getTiendaFisica(marcaList.get(0).getId(), 0L);
        });
    }

    /**
     * Prueba para consultar un comentario de un usuario que no existe.
     *
     * @throws throws EntityNotFoundException, IllegalOperationException
     */
    @Test
    void testGetTiendaFisicaInvalidMarca() {
        assertThrows(EntityNotFoundException.class, () -> {
            TiendaFisicaEntity tiendaFisicaEntity = tiendaFisicaList.get(0);
            marcaTiendaFisicaService.getTiendaFisica(0L, tiendaFisicaEntity.getId());
        });
    }

    /**
     * Prueba para obtener un comentario no asociado a un usuario.
     *
     */
    @Test
    void testGetNotAssociatedComentario() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity newMarca = factory.manufacturePojo(MarcaEntity.class);
            entityManager.persist(newMarca);
            TiendaFisicaEntity tiendaFisica = factory.manufacturePojo(TiendaFisicaEntity.class);
            entityManager.persist(tiendaFisica);
            marcaTiendaFisicaService.getTiendaFisica(newMarca.getId(), tiendaFisica.getId());
        });
    }

    @Test
    void testCeroTiendaFisicaMarca() throws EntityNotFoundException {
        assertThrows(EntityNotFoundException.class, () -> {
            marcaTiendaFisicaService.getTiendasFisicas(marcaList.get(1).getId());
        });
    }

    /**
     * Prueba para desasociar un tienda fisica existente de una marca existente.
     * 
     * @throws EntityNotFoundException
     *
     * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
     */
    @Test
    public void testRemoveTiendaFisica() throws EntityNotFoundException {
        marcaTiendaFisicaService.removeTiendaFisica(marcaList.get(0).getId(), tiendaFisicaList.get(0).getId());
        MarcaEntity marca = entityManager.find(MarcaEntity.class, marcaList.get(0).getId());
        assertEquals(tiendaFisicaList.get(0).getId(), marca.getTiendasFisicas().get(0).getId());
    }

    /**
     * Prueba para desasociar un tienda fisica inexistente de una marca existente
     * 
     * @throws EntityNotFoundException
     *
     * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
     */
    @Test
    public void testRemoveInvalidPrenda() {
        assertThrows(EntityNotFoundException.class, () -> {
            marcaTiendaFisicaService.removeTiendaFisica(marcaList.get(0).getId(), 0L);
        });
    }

    /**
     * Prueba para desasociar una tienda fisica existente de una marca inexistente
     */
    @Test
    void testRemoveTiendaFisicaInvalidMarca() {
        assertThrows(EntityNotFoundException.class, () -> {
            marcaTiendaFisicaService.removeTiendaFisica(0L, tiendaFisicaList.get(0).getId());
        });
    }

    /**
     * Prueba para no existe tienda fisica dentro de la lista
     */
    @Test
    void testRemoveComentarioInvalidPrendaListMarca() {
        assertThrows(EntityNotFoundException.class, () -> {
            marcaTiendaFisicaService.removeTiendaFisica(marcaList.get(1).getId(), tiendaFisicaList.get(0).getId());
        });
    }

}