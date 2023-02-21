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
}
