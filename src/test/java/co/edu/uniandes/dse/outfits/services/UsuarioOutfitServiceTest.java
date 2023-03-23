package co.edu.uniandes.dse.outfits.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import co.edu.uniandes.dse.outfits.entities.OutfitEntity;
import co.edu.uniandes.dse.outfits.entities.UsuarioEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * pruebas de logica de la relacion Usuario - Outfits
 * 
 * @author Daniel Pedroza
 * 
 */

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(UsuarioOutfitService.class)
class UsuarioOutfitServiceTest {

    @Autowired
    private UsuarioOutfitService usuarioOutfitService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private UsuarioEntity usuario = new UsuarioEntity();
    private List<OutfitEntity> outfitList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from UsuarioEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from OutfitEntity").executeUpdate();
    }

    private void insertData() {

        usuario = factory.manufacturePojo(UsuarioEntity.class);
        entityManager.persist(usuario);

        for (int i = 0; i < 3; i++) {

            OutfitEntity entity = factory.manufacturePojo(OutfitEntity.class);
            entityManager.persist(entity);
            entity.getUsuarios().add(usuario);
            outfitList.add(entity);
            usuario.getFavoritos().add(entity);

        }

    }

    /*
     * Pruebas para asociar un outfit a un usuario
     * 
     */

    @Test
    void testAddOutfit() throws EntityNotFoundException, IllegalOperationException {
        UsuarioEntity newUsuario = factory.manufacturePojo(UsuarioEntity.class);
        entityManager.persist(newUsuario);

        OutfitEntity outfit = factory.manufacturePojo(OutfitEntity.class);
        entityManager.persist(outfit);

        usuarioOutfitService.addOutfit(newUsuario.getId(), outfit.getId());

        OutfitEntity lastOutfit = usuarioOutfitService.getOutfit(newUsuario.getId(), outfit.getId());
        assertEquals(outfit.getId(), lastOutfit.getId());
        assertEquals(outfit.getNombre(), outfit.getNombre());
        assertEquals(outfit.getPrendas(), outfit.getPrendas());
        assertEquals(outfit.getImagen(), outfit.getImagen());
        assertEquals(outfit.getDescripcion(), outfit.getDescripcion());
    }

    /**
     * Prueba para asociar un outfit que no existe a un usuario.
     *
     */
    @Test
    void testAddInvalidOutfit() {
        assertThrows(EntityNotFoundException.class, () -> {
            UsuarioEntity newUsuario = factory.manufacturePojo(UsuarioEntity.class);
            entityManager.persist(newUsuario);
            usuarioOutfitService.addOutfit(newUsuario.getId(), 0L);
        });
    }

    /**
     * Prueba para asociar un outfit a un usuario que no existe.
     *
     */

    @Test
    void testAddOutfitInvalidUsuario() throws EntityNotFoundException, IllegalOperationException {
        assertThrows(EntityNotFoundException.class, () -> {
            OutfitEntity outfit = factory.manufacturePojo(OutfitEntity.class);
            entityManager.persist(outfit);
            usuarioOutfitService.addOutfit(0L, outfit.getId());
        });
    }

    /**
     * Prueba para consultar la lista de outfits de un usuario.
     */

    @Test
    void testGetOutfits() throws EntityNotFoundException {
        List<OutfitEntity> outfitEntities = usuarioOutfitService.getOutfits(usuario.getId());

        assertEquals(outfitList.size(), outfitEntities.size());

        for (int i = 0; i < outfitList.size(); i++) {
            assertTrue(outfitEntities.contains(outfitList.get(0)));
        }
    }

    /**
     * Prueba para consultar la lista de comentarios de un usuario que no existe.
     */
    @Test
    void testGetOutfitsInvalidUsuario() {
        assertThrows(EntityNotFoundException.class, () -> {
            usuarioOutfitService.getOutfits(0L);
        });
    }

    /**
     * Prueba para consultar un outfit de un usuario.
     *
     * @throws throws EntityNotFoundException, IllegalOperationException
     */
    @Test
    void testGetOutfit() throws EntityNotFoundException, IllegalOperationException {
        OutfitEntity outfitEntity = outfitList.get(0);
        OutfitEntity outfit = usuarioOutfitService.getOutfit(usuario.getId(), outfitEntity.getId());
        assertNotNull(usuario);

        assertEquals(outfitEntity.getId(), outfit.getId());
        assertEquals(outfitEntity.getNombre(), outfit.getNombre());
        assertEquals(outfitEntity.getPrendas(), outfit.getPrendas());
        assertEquals(outfitEntity.getImagen(), outfit.getImagen());
        assertEquals(outfitEntity.getDescripcion(), outfit.getDescripcion());
    }

    /**
     * Prueba para consultar un outfit que no existe de un usuario.
     *
     * @throws throws EntityNotFoundException, IllegalOperationException
     */
    @Test
    void testGetInvalidOutfit() {
        assertThrows(EntityNotFoundException.class, () -> {
            usuarioOutfitService.getOutfit(usuario.getId(), 0L);
        });
    }

    /**
     * Prueba para consultar un outfit de un usuario que no existe.
     *
     * @throws throws EntityNotFoundException, IllegalOperationException
     */
    @Test
    void testGetOutfitInvalidUsuario() {
        assertThrows(EntityNotFoundException.class, () -> {
            OutfitEntity outfitEntity = outfitList.get(0);
            usuarioOutfitService.getOutfit(0L, outfitEntity.getId());
        });
    }

    /**
     * Prueba para obtener un outfit no asociado a un usuario.
     *
     */
    @Test
    void testGetNotAssociatedOutfit() {
        assertThrows(IllegalOperationException.class, () -> {
            UsuarioEntity newUsuario = factory.manufacturePojo(UsuarioEntity.class);
            entityManager.persist(newUsuario);
            OutfitEntity outfit = factory.manufacturePojo(OutfitEntity.class);
            entityManager.persist(outfit);
            usuarioOutfitService.getOutfit(newUsuario.getId(), outfit.getId());
        });
    }

    /**
     * Prueba desasociar un outfit con un usuario.
     *
     */
    @Test
    void testRemoveOutfit() throws EntityNotFoundException {
        for (OutfitEntity outfit : outfitList) {
            usuarioOutfitService.removeOutfit(usuario.getId(), outfit.getId());
        }
        assertTrue(usuarioOutfitService.getOutfits(usuario.getId()).isEmpty());
    }

    /**
     * Prueba desasociar un outfit que no existe con un usuario.
     *
     */
    @Test
    void testRemoveInvalidOutfit() {
        assertThrows(EntityNotFoundException.class, () -> {
            usuarioOutfitService.removeOutfit(usuario.getId(), 0L);
        });
    }

    /**
     * Prueba desasociar un outfit con un usuario que no existe.
     *
     */
    @Test
    void testRemoveOutfitInvalidUsuario() {
        assertThrows(EntityNotFoundException.class, () -> {
            usuarioOutfitService.removeOutfit(0L, outfitList.get(0).getId());
        });
    }

}
