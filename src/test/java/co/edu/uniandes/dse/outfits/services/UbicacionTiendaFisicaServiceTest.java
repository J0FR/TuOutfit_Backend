package co.edu.uniandes.dse.outfits.services;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import antlr.collections.List;
import co.edu.uniandes.dse.outfits.entities.MarcaEntity;
import co.edu.uniandes.dse.outfits.entities.TiendaFisicaEntity;
import co.edu.uniandes.dse.outfits.entities.UbicacionEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import({ UbicacionTiendaFisicaService.class, TiendaFisicaService.class })
public class UbicacionTiendaFisicaServiceTest {
    
    @Autowired
    private UbicacionTiendaFisicaService ubicacionTiendaFisicaService;

    @Autowired
    private TiendaFisicaService tiendaFisicaService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private UbicacionEntity ubicacion = new UbicacionEntity();
    private TiendaFisicaEntity tiendaFisica = new TiendaFisicaEntity();
    private MarcaEntity marca = new MarcaEntity();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from UbicacionEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from TiendaFisicaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from MarcaEntity").executeUpdate();
    }

    private void insertData() {
        ubicacion = factory.manufacturePojo(UbicacionEntity.class);
        entityManager.persist(ubicacion);

        tiendaFisica = factory.manufacturePojo(TiendaFisicaEntity.class);
        entityManager.persist(tiendaFisica);

        marca = factory.manufacturePojo(MarcaEntity.class);
        entityManager.persist(marca);

        ubicacion.setTiendaFisica(tiendaFisica);
        tiendaFisica.setUbicacion(ubicacion);
        tiendaFisica.setMarca(marca);
    }

    @Test
    void testAddTiendaFisica() throws IllegalOperationException, EntityNotFoundException {
        TiendaFisicaEntity newTiendaFisica = factory.manufacturePojo(TiendaFisicaEntity.class);
        newTiendaFisica.setUbicacion(ubicacion);
        newTiendaFisica.setMarca(marca);
        tiendaFisicaService.createTiendaFisica(newTiendaFisica);

        TiendaFisicaEntity tiendaFisicaEntity = ubicacionTiendaFisicaService.addTiendaFisica(ubicacion.getId(), newTiendaFisica.getId());
        assertNotNull(tiendaFisicaEntity);

        assertEquals(tiendaFisicaEntity.getId(), newTiendaFisica.getId());
        assertEquals(tiendaFisicaEntity.getNombre(), newTiendaFisica.getNombre());
        assertEquals(tiendaFisicaEntity.getHorarios(), newTiendaFisica.getHorarios());
        
        TiendaFisicaEntity ultimaTiendaFisica = ubicacionTiendaFisicaService.getTiendaFisica(ubicacion.getId(), newTiendaFisica.getId());

        assertEquals(ultimaTiendaFisica.getId(), newTiendaFisica.getId());
        assertEquals(ultimaTiendaFisica.getNombre(), newTiendaFisica.getNombre());
        assertEquals(ultimaTiendaFisica.getHorarios(), newTiendaFisica.getHorarios());

    }

    @Test
    void testAddTiendaFisicaInvalidUbicacion() {
        assertThrows(EntityNotFoundException.class, () -> {
            TiendaFisicaEntity newTiendaFisica = factory.manufacturePojo(TiendaFisicaEntity.class);
            newTiendaFisica.setMarca(marca);
            newTiendaFisica.setUbicacion(ubicacion);
            tiendaFisicaService.createTiendaFisica(newTiendaFisica);
            newTiendaFisica.setUbicacion(null);
            ubicacionTiendaFisicaService.addTiendaFisica(0L, newTiendaFisica.getId());
        });
    }

    @Test
    void testAddInvalidTiendaFisica() {
        assertThrows(EntityNotFoundException.class, () -> {
			ubicacionTiendaFisicaService.addTiendaFisica(ubicacion.getId(), 0L);
		});
    }

    @Test
    void testGetTiendaFisica() throws EntityNotFoundException, IllegalOperationException {
        TiendaFisicaEntity tiendaFisicaEntity = tiendaFisica;
        TiendaFisicaEntity tiendaFisica = ubicacionTiendaFisicaService.getTiendaFisica(ubicacion.getId(), tiendaFisicaEntity.getId());
        assertNotNull(tiendaFisica);

        assertEquals(tiendaFisicaEntity.getId(), tiendaFisica.getId());
        assertEquals(tiendaFisicaEntity.getNombre(), tiendaFisica.getNombre());
        assertEquals(tiendaFisicaEntity.getHorarios(), tiendaFisica.getHorarios());
    }

    @Test
    void testGetTiendaFisicaInvalidUbicacion() {
        assertThrows(EntityNotFoundException.class, () -> {
            ubicacionTiendaFisicaService.getTiendaFisica(0L, tiendaFisica.getId());
        });
    }

    @Test
    void testGetInvalidTiendaFisica() {
        assertThrows(EntityNotFoundException.class, () -> {
            ubicacionTiendaFisicaService.getTiendaFisica(ubicacion.getId(), 0L);
        });
    }
    
    @Test
    void testGetTiendaFisicaNoAssociatedUbicacion() {
        assertThrows(IllegalOperationException.class, () -> {
            UbicacionEntity ubicacionEntity = factory.manufacturePojo(UbicacionEntity.class);
            entityManager.persist(ubicacionEntity);

            TiendaFisicaEntity tiendaFisicaEntity = factory.manufacturePojo(TiendaFisicaEntity.class);
            tiendaFisicaEntity.setMarca(marca);
            entityManager.persist(tiendaFisicaEntity);

            ubicacionTiendaFisicaService.getTiendaFisica(ubicacionEntity.getId(), tiendaFisicaEntity.getId());
        });
    }
}
