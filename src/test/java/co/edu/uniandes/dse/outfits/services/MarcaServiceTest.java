package co.edu.uniandes.dse.outfits.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.entities.MarcaEntity;
import co.edu.uniandes.dse.outfits.entities.PrendaEntity;
import co.edu.uniandes.dse.outfits.entities.TiendaFisicaEntity;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(MarcaService.class)
class MarcaServiceTest {

    @Autowired
    MarcaService marcaService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<MarcaEntity> marcaList = new ArrayList<>();
    private List<TiendaFisicaEntity> tiendaFisicaList = new ArrayList<>();
    private List<PrendaEntity> prendasList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from MarcaEntity");
        entityManager.getEntityManager().createQuery("delete from TiendaFisicaEntity");
        entityManager.getEntityManager().createQuery("delete from PrendaEntity");
    }

    private void insertData() {

        for (int i = 0; i < 3; i++) {
            MarcaEntity marcaEntity = factory.manufacturePojo(MarcaEntity.class);
            entityManager.persist(marcaEntity);
            marcaList.add(marcaEntity);
        }

        for (int i = 0; i < 3; i++) {
            TiendaFisicaEntity tiendaFisicaEntity = factory.manufacturePojo(TiendaFisicaEntity.class);
            entityManager.persist(tiendaFisicaEntity);
            tiendaFisicaList.add(tiendaFisicaEntity);
        }

        for (int i = 0; i < 3; i++) {
            PrendaEntity prendaEntity = factory.manufacturePojo(PrendaEntity.class);
            entityManager.persist(prendaEntity);
            prendasList.add(prendaEntity);
        }

        marcaList.get(0).setTiendas_fisicas(tiendaFisicaList);
        marcaList.get(0).setPrendas(prendasList);
    }

    @Test
    void testCreateOutfit() throws EntityNotFoundException, IllegalOperationException {
        MarcaEntity newEntity = factory.manufacturePojo(MarcaEntity.class);
        newEntity.setTiendas_fisicas(tiendaFisicaList);
        newEntity.setPrendas(prendasList);
        MarcaEntity result = marcaService.createMarca(newEntity);
        assertNotNull(result);
        MarcaEntity entity = entityManager.find(MarcaEntity.class, result.getId());
        assertEquals(newEntity.getId(), entity.getId());
        assertEquals(newEntity.getNombre(), entity.getNombre());
        assertEquals(newEntity.getLogo(), entity.getLogo());
        assertEquals(newEntity.getUrl_sitio_web(), entity.getUrl_sitio_web());
        assertEquals(newEntity.getDetalle_de_marca(), entity.getDetalle_de_marca());
    }

    @Test
    void testCreateMarcaWithNoValidNombre() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity newEntity = factory.manufacturePojo(MarcaEntity.class);
            newEntity.setNombre("");
            marcaService.createMarca(newEntity);
        });
    }

    @Test
    void testCreateMarcaWithNoValidNombre2() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity newEntity = factory.manufacturePojo(MarcaEntity.class);
            newEntity.setNombre(null);
            marcaService.createMarca(newEntity);
        });
    }

    @Test
    void testCreateMarcaWithNoValidURL_sitio_web() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity newEntity = factory.manufacturePojo(MarcaEntity.class);
            newEntity.setUrl_sitio_web(null);
            marcaService.createMarca(newEntity);
        });
    }

    @Test
    void testCreateMarcaWithNoValidURL_sitio_web2() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity newEntity = factory.manufacturePojo(MarcaEntity.class);
            newEntity.setUrl_sitio_web("");
            marcaService.createMarca(newEntity);
        });
    }

    @Test
    void testCreateMarcaWithNoValidLogo() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity newEntity = factory.manufacturePojo(MarcaEntity.class);
            newEntity.setLogo(null);
            marcaService.createMarca(newEntity);
        });
    }

    @Test
    void testCreateMarcaWithNoValidLogo2() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity newEntity = factory.manufacturePojo(MarcaEntity.class);
            newEntity.setLogo("");
            marcaService.createMarca(newEntity);
        });
    }

    @Test
    void testCreateMarcaWithNoValidDetalleDeMarca() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity newEntity = factory.manufacturePojo(MarcaEntity.class);
            newEntity.setDetalle_de_marca(null);
            marcaService.createMarca(newEntity);
        });
    }

    @Test
    void testCreateMarcaWithNoValidDetalleDeMarca2() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity newEntity = factory.manufacturePojo(MarcaEntity.class);
            newEntity.setDetalle_de_marca("");
            marcaService.createMarca(newEntity);
        });
    }

    @Test
    void testCreateMarcaWithNoValidTiendaFisicas() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity newEntity = factory.manufacturePojo(MarcaEntity.class);
            newEntity.setTiendas_fisicas(null);
            marcaService.createMarca(newEntity);
        });
    }

    @Test
    void testCreateMarcaWithNoValidTiendaFisicas2() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity newEntity = factory.manufacturePojo(MarcaEntity.class);
            newEntity.setTiendas_fisicas(new ArrayList<TiendaFisicaEntity>());
            marcaService.createMarca(newEntity);
        });
    }

    @Test
    void testCreateMarcaWithNoValidPrendas() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity newEntity = factory.manufacturePojo(MarcaEntity.class);
            newEntity.setPrendas(null);
            marcaService.createMarca(newEntity);
        });
    }

    @Test
    void testCreateMarcaWithNoValidPrendas2() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity newEntity = factory.manufacturePojo(MarcaEntity.class);
            newEntity.setPrendas(new ArrayList<PrendaEntity>());
            marcaService.createMarca(newEntity);
        });
    }

    @Test
    void testGetMarcas() {
        List<MarcaEntity> list = marcaService.getMarcas();
        assertEquals(marcaList.size(), list.size());
        for (MarcaEntity entity : list) {
            boolean found = false;
            for (MarcaEntity storedEntity : marcaList) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    void testGetMarca() throws EntityNotFoundException {
        MarcaEntity entity = marcaList.get(0);
        MarcaEntity resultEntity = marcaService.getMarca(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getNombre(), resultEntity.getNombre());
        assertEquals(entity.getUrl_sitio_web(), resultEntity.getUrl_sitio_web());
        assertEquals(entity.getLogo(), resultEntity.getLogo());
        assertEquals(entity.getDetalle_de_marca(), resultEntity.getDetalle_de_marca());
        assertEquals(entity.getTiendas_fisicas(), resultEntity.getTiendas_fisicas());
        assertEquals(entity.getPrendas(), resultEntity.getPrendas());

    }

    @Test
    void testGetInvalidMarca() {
        assertThrows(EntityNotFoundException.class, () -> {
            marcaService.getMarca(0L);
        });
    }

    @Test
    void testUpdateMarca() throws EntityNotFoundException, IllegalOperationException {
        MarcaEntity entity = marcaList.get(0);
        MarcaEntity pojoEntity = factory.manufacturePojo(MarcaEntity.class);
        pojoEntity.setId(entity.getId());
        pojoEntity.setTiendas_fisicas(tiendaFisicaList);
        pojoEntity.setPrendas(prendasList);
        marcaService.updateMarca(entity.getId(), pojoEntity);

        MarcaEntity resp = entityManager.find(MarcaEntity.class, entity.getId());
        assertEquals(pojoEntity.getId(), resp.getId());
        assertEquals(pojoEntity.getNombre(), resp.getNombre());
        assertEquals(pojoEntity.getUrl_sitio_web(), resp.getUrl_sitio_web());
        assertEquals(pojoEntity.getLogo(), resp.getLogo());
        assertEquals(pojoEntity.getDetalle_de_marca(), resp.getDetalle_de_marca());
        assertEquals(pojoEntity.getTiendas_fisicas(), resp.getTiendas_fisicas());
        assertEquals(pojoEntity.getPrendas(), resp.getPrendas());
    }

    @Test
    void testUpdateMarcaInvalId() {
        assertThrows(EntityNotFoundException.class, () -> {
            MarcaEntity pojoEntity = factory.manufacturePojo(MarcaEntity.class);
            pojoEntity.setId(0L);
            marcaService.updateMarca(0L, pojoEntity);
        });
    }

    @Test
    void testUpdateMarcaWithNoValidNombre() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity entity = marcaList.get(0);
            MarcaEntity pojoEntity = factory.manufacturePojo(MarcaEntity.class);
            pojoEntity.setNombre(null);
            pojoEntity.setId(entity.getId());
            marcaService.updateMarca(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testUpdateMarcaWithNoValidNombre2() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity entity = marcaList.get(0);
            MarcaEntity pojoEntity = factory.manufacturePojo(MarcaEntity.class);
            pojoEntity.setNombre("");
            pojoEntity.setId(entity.getId());
            marcaService.updateMarca(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testUpdateMarcaWithNoValidURL_Sitio_Web() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity entity = marcaList.get(0);
            MarcaEntity pojoEntity = factory.manufacturePojo(MarcaEntity.class);
            pojoEntity.setUrl_sitio_web(null);
            pojoEntity.setId(entity.getId());
            marcaService.updateMarca(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testUpdateMarcaWithNoValidURL_Sitio_Web2() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity entity = marcaList.get(0);
            MarcaEntity pojoEntity = factory.manufacturePojo(MarcaEntity.class);
            pojoEntity.setUrl_sitio_web("");
            pojoEntity.setId(entity.getId());
            marcaService.updateMarca(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testUpdateMarcaWithNoValidLogo() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity entity = marcaList.get(0);
            MarcaEntity pojoEntity = factory.manufacturePojo(MarcaEntity.class);
            pojoEntity.setLogo(null);
            pojoEntity.setId(entity.getId());
            marcaService.updateMarca(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testUpdateMarcaWithNoValidLogo2() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity entity = marcaList.get(0);
            MarcaEntity pojoEntity = factory.manufacturePojo(MarcaEntity.class);
            pojoEntity.setLogo("");
            pojoEntity.setId(entity.getId());
            marcaService.updateMarca(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testUpdateMarcaWithNoValidDetalleDeMarca() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity entity = marcaList.get(0);
            MarcaEntity pojoEntity = factory.manufacturePojo(MarcaEntity.class);
            pojoEntity.setDetalle_de_marca(null);
            pojoEntity.setId(entity.getId());
            marcaService.updateMarca(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testUpdateMarcaWithNoValidDetalleDeMarca2() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity entity = marcaList.get(0);
            MarcaEntity pojoEntity = factory.manufacturePojo(MarcaEntity.class);
            pojoEntity.setDetalle_de_marca("");
            pojoEntity.setId(entity.getId());
            marcaService.updateMarca(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testUpdateMarcaWithNoValidTiendaFisica() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity entity = marcaList.get(0);
            MarcaEntity pojoEntity = factory.manufacturePojo(MarcaEntity.class);
            pojoEntity.setTiendas_fisicas(null);
            pojoEntity.setId(entity.getId());
            marcaService.updateMarca(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testUpdateMarcaWithNoValidTiendaFisica2() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity entity = marcaList.get(0);
            MarcaEntity pojoEntity = factory.manufacturePojo(MarcaEntity.class);
            pojoEntity.setTiendas_fisicas(new ArrayList<TiendaFisicaEntity>());
            pojoEntity.setId(entity.getId());
            marcaService.updateMarca(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testUpdateMarcaWithNoValidPrendas() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity entity = marcaList.get(0);
            MarcaEntity pojoEntity = factory.manufacturePojo(MarcaEntity.class);
            pojoEntity.setPrendas(null);
            pojoEntity.setId(entity.getId());
            marcaService.updateMarca(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testUpdateMarcaWithNoValidPrendas2() {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity entity = marcaList.get(0);
            MarcaEntity pojoEntity = factory.manufacturePojo(MarcaEntity.class);
            pojoEntity.setPrendas(new ArrayList<PrendaEntity>());
            pojoEntity.setId(entity.getId());
            marcaService.updateMarca(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testDeleteMarca() throws EntityNotFoundException, IllegalOperationException {
        MarcaEntity entity = marcaList.get(1);
        marcaService.deleteMarca(entity.getId());
        MarcaEntity deleted = entityManager.find(MarcaEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteMarcaInvalId() {
        assertThrows(EntityNotFoundException.class, () -> {
            marcaService.deleteMarca(0L);
        });
    }

    @Test
    void testDeleteMarcaWithPrendas() throws EntityNotFoundException, IllegalOperationException {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity entity = marcaList.get(0);
            entity.setPrendas(prendasList);
            marcaService.deleteMarca(entity.getId());
        });
    }

    @Test
    void testDeleteMarcaWithTiendasFisicas() throws EntityNotFoundException, IllegalOperationException {
        assertThrows(IllegalOperationException.class, () -> {
            MarcaEntity entity = marcaList.get(0);
            entity.setTiendas_fisicas(tiendaFisicaList);
            marcaService.deleteMarca(entity.getId());
        });
    }

}