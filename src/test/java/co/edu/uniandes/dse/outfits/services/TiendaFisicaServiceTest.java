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
import co.edu.uniandes.dse.outfits.entities.MarcaEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(TiendaFisicaService.class)
public class TiendaFisicaServiceTest {

    @Autowired
    TiendaFisicaService tiendaFisicaService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<TiendaFisicaEntity> tiendaFisicaList = new ArrayList<>();
    private List<UbicacionEntity> ubicacionList = new ArrayList<>();
    private List<MarcaEntity> marcaList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from TiendaFisicaEntity");
        entityManager.getEntityManager().createQuery("delete from UbicacionEntity");
        entityManager.getEntityManager().createQuery("delete from MarcaEntity");
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            TiendaFisicaEntity tiendaFisicaEntity = factory.manufacturePojo(TiendaFisicaEntity.class);
            entityManager.persist(tiendaFisicaEntity);
            tiendaFisicaList.add(tiendaFisicaEntity);
        }
        for (int i = 0; i < 3; i++) {
            UbicacionEntity ubicacionEntity = factory.manufacturePojo(UbicacionEntity.class);
            entityManager.persist(ubicacionEntity);
            ubicacionList.add(ubicacionEntity);
        }
        for (int i = 0; i < 3; i++) {
            MarcaEntity marcaEntity = factory.manufacturePojo(MarcaEntity.class);
            entityManager.persist(marcaEntity);
            marcaList.add(marcaEntity);
        }
        tiendaFisicaList.get(0).setUbicacion(ubicacionList.get(0));
        tiendaFisicaList.get(0).setMarca(marcaList.get(0));
    }

    @Test
    void testCreateTiendaFisica() throws IllegalOperationException {
        TiendaFisicaEntity tiendaFisicaEntity = factory.manufacturePojo(TiendaFisicaEntity.class);
        tiendaFisicaEntity.setUbicacion(ubicacionList.get(0));
        tiendaFisicaEntity.setMarca(marcaList.get(0));
        TiendaFisicaEntity result = tiendaFisicaService.createTiendaFisica(tiendaFisicaEntity);
        assertNotNull(result);
        TiendaFisicaEntity entity = entityManager.find(TiendaFisicaEntity.class, result.getId());
        assertEquals(tiendaFisicaEntity.getId(), entity.getId());
        assertEquals(tiendaFisicaEntity.getNombre(), entity.getNombre());
        assertEquals(tiendaFisicaEntity.getUbicacion(), entity.getUbicacion());
        assertEquals(tiendaFisicaEntity.getMarca(), entity.getMarca());
    }

    @Test
    void testCreateTiendaFisicaWithNoValidUbicacion() {
        assertThrows(IllegalOperationException.class, () -> {
            TiendaFisicaEntity newEntity = factory.manufacturePojo(TiendaFisicaEntity.class);
            newEntity.setUbicacion(null);
            newEntity.setMarca(marcaList.get(0));
            tiendaFisicaService.createTiendaFisica(newEntity);
        });
    }

    @Test
    void testCreateTiendaFisicaWithNoValidMarca() {
        assertThrows(IllegalOperationException.class, () -> {
            TiendaFisicaEntity newEntity = factory.manufacturePojo(TiendaFisicaEntity.class);
            newEntity.setUbicacion(ubicacionList.get(0));
            newEntity.setMarca(null);
            tiendaFisicaService.createTiendaFisica(newEntity);
        });
    }

    @Test
    void testgetTiendasFisicas() {
        List<TiendaFisicaEntity> list = tiendaFisicaService.getTiendasFisicas();
        assertEquals(tiendaFisicaList.size(), list.size());
        for (TiendaFisicaEntity entity : list) {
            boolean found = false;
            for (TiendaFisicaEntity storedEntity : tiendaFisicaList) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    void testGetTiendaFisica() throws EntityNotFoundException {
        TiendaFisicaEntity entity = tiendaFisicaList.get(0);
        TiendaFisicaEntity resultEntity = tiendaFisicaService.getTiendaFisica(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getNombre(), resultEntity.getNombre());
        assertEquals(entity.getHorarios(), resultEntity.getHorarios());
        assertEquals(entity.getUbicacion(), resultEntity.getUbicacion());
        assertEquals(entity.getMarca(), resultEntity.getMarca());
    }

    @Test
    void testGetInvalidTiendaFisica() {
        assertThrows(EntityNotFoundException.class, () -> {
            tiendaFisicaService.getTiendaFisica(0L);
        });
    }

    @Test
    void testUpdateTiendaFisica() throws EntityNotFoundException, IllegalOperationException {
        TiendaFisicaEntity entity = tiendaFisicaList.get(0);
        TiendaFisicaEntity pojoEntity = factory.manufacturePojo(TiendaFisicaEntity.class);
        pojoEntity.setId(entity.getId());
        pojoEntity.setUbicacion(entity.getUbicacion());
        pojoEntity.setMarca(entity.getMarca());
        tiendaFisicaService.updateTiendaFisica(pojoEntity.getId(), pojoEntity);

        TiendaFisicaEntity resp = entityManager.find(TiendaFisicaEntity.class, entity.getId());
        assertEquals(pojoEntity.getId(), resp.getId());
        assertEquals(pojoEntity.getNombre(), resp.getNombre());
        assertEquals(pojoEntity.getHorarios(), resp.getHorarios());
        assertEquals(pojoEntity.getUbicacion(), resp.getUbicacion());
        assertEquals(pojoEntity.getMarca(), resp.getMarca());
    }

    @Test
    void testUpdateTiendaFisicaInvalid() {
        assertThrows(EntityNotFoundException.class, () -> {
            TiendaFisicaEntity pojoEntity = factory.manufacturePojo(TiendaFisicaEntity.class);
            pojoEntity.setId(0L);
            tiendaFisicaService.updateTiendaFisica(0L, pojoEntity);
        });
    }

    @Test
    void testUpdateTiendaFisicaWithNoValidUbicacion() {
        assertThrows(IllegalOperationException.class, () -> {
            TiendaFisicaEntity entity = tiendaFisicaList.get(0);
            TiendaFisicaEntity pojoEntity = factory.manufacturePojo(TiendaFisicaEntity.class);
            pojoEntity.setId(entity.getId());
            pojoEntity.setUbicacion(null);
            pojoEntity.setMarca(entity.getMarca());
            tiendaFisicaService.updateTiendaFisica(entity.getId(), pojoEntity);
        });
    }
    
    @Test
    void testUpdateTiendaFisicaWithNoValidMarca() {
        assertThrows(IllegalOperationException.class, () -> {
            TiendaFisicaEntity entity = tiendaFisicaList.get(0);
            TiendaFisicaEntity pojoEntity = factory.manufacturePojo(TiendaFisicaEntity.class);
            pojoEntity.setId(entity.getId());
            pojoEntity.setUbicacion(entity.getUbicacion());
            pojoEntity.setMarca(null);
            tiendaFisicaService.updateTiendaFisica(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testDeleteTiendaFisica() throws EntityNotFoundException, IllegalOperationException {
        TiendaFisicaEntity entity = tiendaFisicaList.get(1);
        tiendaFisicaService.deleteTiendaFisica(entity.getId());
        TiendaFisicaEntity deleted = entityManager.find(TiendaFisicaEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteTiendaFisicaInvalid() {
        assertThrows(EntityNotFoundException.class, () -> {
            tiendaFisicaService.deleteTiendaFisica(0L);
        });
    }
}
