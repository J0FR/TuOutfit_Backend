package co.edu.uniandes.dse.outfits.services;

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
@Import({ UbicacionTiendaFisicaService.class, TiendaFisicaService.class })
public class UbicacionTiendaFisicaServiceTest {
    
    @Autowired
    private UbicacionTiendaFisicaService ubicacionTiendaFisicaService;

    @Autowired
    private TiendaFisicaService tiendaFisicaService;

    /* @Autowired
    private MarcaEntityService marcaEntityService; */

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private UbicacionEntity ubicacion = new UbicacionEntity();
    private TiendaFisicaEntity tiendaFisica = new TiendaFisicaEntity();
    /* private MarcaEntity marca = new MarcaEntity(); */

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from UbicacionEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from TiendaFisicaEntity").executeUpdate();
        /* entityManager.getEntityManager().createQuery("delete from MarcaEntity").executeUpdate(); */
    }

    private void insertData() {
        ubicacion = factory.manufacturePojo(UbicacionEntity.class);
        entityManager.persist(ubicacion);

        tiendaFisica = factory.manufacturePojo(TiendaFisicaEntity.class);
        entityManager.persist(tiendaFisica);

        /* marca = factory.manufacturePojo(MarcaEntity.class);
        entityManager.persist(marca); */

        ubicacion.setTiendaFisica(tiendaFisica);
        tiendaFisica.setUbicacion(ubicacion);
        /* tiendaFisica.setMarca(marca); */
    }

    @Test
    void testAddTiendaFisica() throws IllegalOperationException, EntityNotFoundException {
        TiendaFisicaEntity tiendaFisicaEntity = factory.manufacturePojo(TiendaFisicaEntity.class);
        tiendaFisicaEntity.setUbicacion(ubicacion);
        /* tiendaFisicaEntity.setMarca(marca) */
        tiendaFisicaService.createTiendaFisica(tiendaFisicaEntity);

        ubicacionTiendaFisicaService.addTiendaFisica(ubicacion.getId(), tiendaFisicaEntity.getId());
    }

    
}
