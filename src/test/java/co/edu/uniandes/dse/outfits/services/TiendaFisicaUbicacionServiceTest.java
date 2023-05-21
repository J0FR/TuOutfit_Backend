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
@Import({ UbicacionService.class, TiendaFisicaUbicacionService.class })
public class TiendaFisicaUbicacionServiceTest {
    
    @Autowired
    private TiendaFisicaUbicacionService tiendaFisicaUbicacionService;

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

        tiendaFisica.setUbicacion(ubicacion);
        tiendaFisica.setMarca(marca);
    }

    @Test
    void testAddUbicacion() throws EntityNotFoundException, IllegalOperationException{
        TiendaFisicaEntity newTiendaFisica = factory.manufacturePojo(TiendaFisicaEntity.class);
        newTiendaFisica.setMarca(marca);
        entityManager.persist(newTiendaFisica);

        UbicacionEntity newUbicacion = factory.manufacturePojo(UbicacionEntity.class);
        entityManager.persist(newUbicacion);

        tiendaFisicaUbicacionService.addUbicacion(newTiendaFisica.getId(), newUbicacion.getId());

        UbicacionEntity lastUbicacion = tiendaFisicaUbicacionService.getUbicacion(newTiendaFisica.getId(), newUbicacion.getId());
        assertEquals(newUbicacion.getId(), lastUbicacion.getId());
        assertEquals(newUbicacion.getLatitud(), lastUbicacion.getLatitud());
        assertEquals(newUbicacion.getLongitud(), lastUbicacion.getLongitud());
    }

    @Test
    void testAddInvalidUbicacion() {
        assertThrows(EntityNotFoundException.class, () -> {
            TiendaFisicaEntity newTiendaFisica = factory.manufacturePojo(TiendaFisicaEntity.class);
            newTiendaFisica.setMarca(marca);
            entityManager.persist(newTiendaFisica);
            tiendaFisicaUbicacionService.addUbicacion(newTiendaFisica.getId(), 0L);
        });
    }

    @Test
    void testAddUbicacionInvalidTiendaFisica() throws EntityNotFoundException, IllegalOperationException {
        assertThrows(EntityNotFoundException.class, ()->{
            UbicacionEntity newUbicacion = factory.manufacturePojo(UbicacionEntity.class);
            entityManager.persist(newUbicacion);
            tiendaFisicaUbicacionService.addUbicacion(0L, newUbicacion.getId());
        });
    }

    @Test
    void testGetUbicacion() throws EntityNotFoundException, IllegalOperationException {
        UbicacionEntity newUbicacion = tiendaFisicaUbicacionService.getUbicacion(tiendaFisica.getId(), ubicacion.getId());
        assertNotNull(newUbicacion);

        assertEquals(ubicacion.getId(), newUbicacion.getId());
        assertEquals(ubicacion.getLatitud(), newUbicacion.getLatitud());
        assertEquals(ubicacion.getLongitud(), newUbicacion.getLongitud());
    }

    @Test
    void testGetInvalidUbicacion() {
        assertThrows(EntityNotFoundException.class, () -> {
            tiendaFisicaUbicacionService.getUbicacion(tiendaFisica.getId(), 0L);
        });
    }

    @Test
    void testGetUbicacionInvalidTiendaFisica() {
        assertThrows(EntityNotFoundException.class, () -> {
            tiendaFisicaUbicacionService.getUbicacion(0L, ubicacion.getId());
        });
    }

    @Test
    void testGetNotAssociatedUbicacion() {
        assertThrows(IllegalOperationException.class, () -> {
            TiendaFisicaEntity newTiendaFisica = factory.manufacturePojo(TiendaFisicaEntity.class);
            newTiendaFisica.setMarca(marca);
            entityManager.persist(newTiendaFisica);
            UbicacionEntity newUbicacion = factory.manufacturePojo(UbicacionEntity.class);
            entityManager.persist(newUbicacion);
            tiendaFisicaUbicacionService.getUbicacion(newTiendaFisica.getId(), newUbicacion.getId());
        });
    }

    


}
