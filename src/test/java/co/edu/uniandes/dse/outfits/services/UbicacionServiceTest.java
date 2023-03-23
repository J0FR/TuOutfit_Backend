package co.edu.uniandes.dse.outfits.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import co.edu.uniandes.dse.outfits.entities.TiendaFisicaEntity;
import co.edu.uniandes.dse.outfits.entities.UbicacionEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(UbicacionService.class)
class UbicacionServiceTest {
    
    @Autowired
    UbicacionService ubicacionService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<UbicacionEntity> ubicacionList = new ArrayList<>();
    private List<TiendaFisicaEntity> tiendaFisicaList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from UbicacionEntity");
        entityManager.getEntityManager().createQuery("delete from TiendaFisicaEntity");
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            UbicacionEntity ubicacionEntity = factory.manufacturePojo(UbicacionEntity.class);
            entityManager.persist(ubicacionEntity);
            ubicacionList.add(ubicacionEntity);
        }
        for (int i = 0; i < 3; i++) {
            TiendaFisicaEntity tiendaFisicaEntity = factory.manufacturePojo(TiendaFisicaEntity.class);
            entityManager.persist(tiendaFisicaEntity);
            tiendaFisicaList.add(tiendaFisicaEntity);
        }
        ubicacionList.get(0).setTiendaFisica(tiendaFisicaList.get(0));
        tiendaFisicaList.get(0).setUbicacion(ubicacionList.get(0));
    }

    @Test
    void testCreateUbicacion() throws EntityNotFoundException, IllegalOperationException {
        UbicacionEntity ubicacionEntity = factory.manufacturePojo(UbicacionEntity.class);
        ubicacionEntity.setTiendaFisica(tiendaFisicaList.get(0));
        UbicacionEntity result = ubicacionService.createUbicacion(ubicacionEntity);
        assertNotNull(result);
        UbicacionEntity entity = entityManager.find(UbicacionEntity.class, result.getId());
        assertEquals(ubicacionEntity.getId(), entity.getId());
        assertEquals(ubicacionEntity.getLatitud(), entity.getLatitud());
        assertEquals(ubicacionEntity.getLongitud(), entity.getLongitud());
        assertEquals(ubicacionEntity.getTiendaFisica().getId(), entity.getTiendaFisica().getId());
    }

    @Test
    void testCreateUbicacionWithNoValidTiendaFisica() {
        assertThrows(IllegalOperationException.class, () -> {
                UbicacionEntity newEntity = factory.manufacturePojo(UbicacionEntity.class);
                newEntity.setTiendaFisica(null);
                ubicacionService.createUbicacion(newEntity);
        });
    }

    @Test
    void testGetUbicaciones() {
        List<UbicacionEntity> list = ubicacionService.getUbicaciones();
        assertEquals(ubicacionList.size(), list.size());
        for (UbicacionEntity entity : list) {
            boolean found = false;
            for (UbicacionEntity storedEntity : ubicacionList) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    void testGetUbicacion() throws EntityNotFoundException {
        UbicacionEntity entity = ubicacionList.get(0);
        UbicacionEntity resultEntity = ubicacionService.getUbicacion(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getLatitud(), resultEntity.getLatitud());
        assertEquals(entity.getLongitud(), resultEntity.getLongitud());
        assertEquals(entity.getTiendaFisica().getId(), resultEntity.getTiendaFisica().getId());
    }

    @Test
    void testGetInvalidUbicacion() {
        assertThrows(EntityNotFoundException.class, () -> {
            ubicacionService.getUbicacion(0L);
        });
    }

    @Test
    void testUpdateUbicacion() throws EntityNotFoundException, IllegalOperationException {
        UbicacionEntity entity = ubicacionList.get(0);
        UbicacionEntity pojoEntity = factory.manufacturePojo(UbicacionEntity.class);
        pojoEntity.setId(entity.getId());
        pojoEntity.setTiendaFisica(entity.getTiendaFisica());
        ubicacionService.updateUbicacion(entity.getId(), pojoEntity);

        UbicacionEntity resp = entityManager.find(UbicacionEntity.class, entity.getId());
        assertEquals(pojoEntity.getId(), resp.getId());
        assertEquals(pojoEntity.getLatitud(), resp.getLatitud());
        assertEquals(pojoEntity.getLongitud(), resp.getLongitud());
    }

    @Test
    void testUpdateUbicacionInvalid() {
        assertThrows(EntityNotFoundException.class, () -> {
            UbicacionEntity pojEntity = factory.manufacturePojo(UbicacionEntity.class);
            pojEntity.setId(0L);
            ubicacionService.updateUbicacion(0L, pojEntity);
        });
    }

    @Test
    void testUpdateUbicacionWithNoValidTiendaFisica() {
        assertThrows(IllegalOperationException.class, () -> {
            UbicacionEntity entity = ubicacionList.get(0);
            UbicacionEntity pojoEntity = factory.manufacturePojo(UbicacionEntity.class);
            pojoEntity.setId(entity.getId());
            pojoEntity.setTiendaFisica(null);
            ubicacionService.updateUbicacion(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testDeleteUbicacion() throws EntityNotFoundException, IllegalOperationException {
        UbicacionEntity entity = ubicacionList.get(1);
        ubicacionService.deleteUbicacion(entity.getId());
        UbicacionEntity deleted = entityManager.find(UbicacionEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteUbicacionInvalid() {
        assertThrows(EntityNotFoundException.class, () -> {
            ubicacionService.deleteUbicacion(0L);
        });
    }

    @Test
    void testDeleteUbicacionWithTiendaFisica() throws EntityNotFoundException, IllegalOperationException{
        assertThrows(IllegalOperationException.class, () -> {
            UbicacionEntity entity = ubicacionList.get(0);
            ubicacionService.deleteUbicacion(entity.getId());
        });
    }
}