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
import co.edu.uniandes.dse.outfits.entities.ComentarioEntity;
import co.edu.uniandes.dse.outfits.entities.OutfitEntity;
import co.edu.uniandes.dse.outfits.entities.UsuarioEntity;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(UsuarioService.class)
class UsuarioServiceTest {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<UsuarioEntity> usuarioList = new ArrayList<>();
    private List<ComentarioEntity> comentariosList = new ArrayList<>();
    private List<OutfitEntity> outfitsList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from UsuarioEntity");
        entityManager.getEntityManager().createQuery("delete from ComentarioEntity");
        entityManager.getEntityManager().createQuery("delete from OutfitEntity");
    }

    private void insertData() {

        for (int i = 0; i < 3; i++) {
            UsuarioEntity usuarioEntity = factory.manufacturePojo(UsuarioEntity.class);
            entityManager.persist(usuarioEntity);
            usuarioList.add(usuarioEntity);
        }

        for (int i = 0; i < 3; i++) {
            ComentarioEntity comentarioEntity = factory.manufacturePojo(ComentarioEntity.class);
            entityManager.persist(comentarioEntity);
            comentariosList.add(comentarioEntity);
        }

        for (int i = 0; i < 3; i++) {
            OutfitEntity outfitEntity = factory.manufacturePojo(OutfitEntity.class);
            entityManager.persist(outfitEntity);
            outfitsList.add(outfitEntity);
        }

    }

    @Test
    void testCreateUsuario() throws EntityNotFoundException, IllegalOperationException {
        UsuarioEntity usuarioEntity = factory.manufacturePojo(UsuarioEntity.class);
        usuarioEntity.setEdad(19);
        UsuarioEntity result = usuarioService.createUsuario(usuarioEntity);
        assertNotNull(result);
        UsuarioEntity entity = entityManager.find(UsuarioEntity.class, result.getId());
        assertEquals(usuarioEntity.getId(), entity.getId());
        assertEquals(usuarioEntity.getEmail(), entity.getEmail());
        assertEquals(usuarioEntity.getGenero(), entity.getGenero());
        assertEquals(usuarioEntity.getEdad(), entity.getEdad());
    }

    @Test
    void testCreateUsuarioWithNoValidNombre() {
        assertThrows(IllegalOperationException.class, () -> {
            UsuarioEntity newEntity = factory.manufacturePojo(UsuarioEntity.class);
            newEntity.setNombre("");
            usuarioService.createUsuario(newEntity);
        });
    }

    @Test
    void testCreateUsuarioWithNoValidNombre2() {
        assertThrows(IllegalOperationException.class, () -> {
            UsuarioEntity newEntity = factory.manufacturePojo(UsuarioEntity.class);
            newEntity.setNombre(null);
            usuarioService.createUsuario(newEntity);
        });
    }

    @Test
    void testCreateUsuarioWithNoValidGenero() {
        assertThrows(IllegalOperationException.class, () -> {
            UsuarioEntity newEntity = factory.manufacturePojo(UsuarioEntity.class);
            newEntity.setGenero(null);
            usuarioService.createUsuario(newEntity);
        });
    }

    @Test
    void testCreateUsuarioWithNoValidEmail() {
        assertThrows(IllegalOperationException.class, () -> {
            UsuarioEntity newEntity = factory.manufacturePojo(UsuarioEntity.class);
            newEntity.setEmail("");
            usuarioService.createUsuario(newEntity);
        });
    }

    @Test
    void testCreateUsuarioWithNoValidEmail2() {
        assertThrows(IllegalOperationException.class, () -> {
            UsuarioEntity newEntity = factory.manufacturePojo(UsuarioEntity.class);
            newEntity.setEmail(null);
            usuarioService.createUsuario(newEntity);
        });
    }

    @Test
    void testCreateUsuarioWithNoValidEdad() {
        assertThrows(IllegalOperationException.class, () -> {
            UsuarioEntity newEntity = factory.manufacturePojo(UsuarioEntity.class);
            newEntity.setEdad(null);
            usuarioService.createUsuario(newEntity);
        });
    }

    @Test
    void testCreateUsuarioWithNoValidEdad2() {
        assertThrows(IllegalOperationException.class, () -> {
            UsuarioEntity newEntity = factory.manufacturePojo(UsuarioEntity.class);
            newEntity.setEdad(10000);
            usuarioService.createUsuario(newEntity);
        });
    }

    @Test
    void testCreateUsuarioWithNoValidEdad3() {
        assertThrows(IllegalOperationException.class, () -> {
            UsuarioEntity newEntity = factory.manufacturePojo(UsuarioEntity.class);
            newEntity.setEdad(-1);
            usuarioService.createUsuario(newEntity);
        });
    }

    @Test
    void testGetUsuarios() {
        List<UsuarioEntity> list = usuarioService.getUsuarios();
        assertEquals(usuarioList.size(), list.size());
        for (UsuarioEntity entity : list) {
            boolean found = false;
            for (UsuarioEntity storedEntity : usuarioList) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    void testGetUsuario() throws EntityNotFoundException {
        UsuarioEntity entity = usuarioList.get(0);
        UsuarioEntity resultEntity = usuarioService.getUsuario(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getNombre(), resultEntity.getNombre());
        assertEquals(entity.getGenero(), resultEntity.getGenero());
        assertEquals(entity.getEmail(), resultEntity.getEmail());
        assertEquals(entity.getEdad(), resultEntity.getEdad());
    }

    @Test
    void testGetInvalidUsuario() {
        assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.getUsuario(0L);
        });
    }

    @Test
    void testUpdateUsuario() throws EntityNotFoundException, IllegalOperationException {
        UsuarioEntity entity = usuarioList.get(0);
        UsuarioEntity pojoEntity = factory.manufacturePojo(UsuarioEntity.class);
        pojoEntity.setId(entity.getId());
        pojoEntity.setEdad(19);
        usuarioService.updateUsuario(entity.getId(), pojoEntity);

        UsuarioEntity resp = entityManager.find(UsuarioEntity.class, entity.getId());
        assertEquals(pojoEntity.getId(), resp.getId());
        assertEquals(pojoEntity.getNombre(), resp.getNombre());
        assertEquals(pojoEntity.getGenero(), resp.getGenero());
        assertEquals(pojoEntity.getEmail(), resp.getEmail());
        assertEquals(pojoEntity.getEdad(), resp.getEdad());
    }

    @Test
    void testUpdateUsuarioInvalid() {
        assertThrows(EntityNotFoundException.class, () -> {
            UsuarioEntity pojoEntity = factory.manufacturePojo(UsuarioEntity.class);
            pojoEntity.setId(0L);
            usuarioService.updateUsuario(0L, pojoEntity);
        });
    }

    @Test
    void testUpdateUsuarioWithNoValidNombre() {
        assertThrows(IllegalOperationException.class, () -> {
            UsuarioEntity entity = usuarioList.get(0);
            UsuarioEntity pojoEntity = factory.manufacturePojo(UsuarioEntity.class);
            pojoEntity.setNombre(null);
            pojoEntity.setId(entity.getId());
            usuarioService.updateUsuario(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testUpdateUsuarioWithNoValidNombre2() {
        assertThrows(IllegalOperationException.class, () -> {
            UsuarioEntity entity = usuarioList.get(0);
            UsuarioEntity pojoEntity = factory.manufacturePojo(UsuarioEntity.class);
            pojoEntity.setNombre("");
            pojoEntity.setId(entity.getId());
            usuarioService.updateUsuario(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testUpdateUsuarioWithNoValidGenero() {
        assertThrows(IllegalOperationException.class, () -> {
            UsuarioEntity entity = usuarioList.get(0);
            UsuarioEntity pojoEntity = factory.manufacturePojo(UsuarioEntity.class);
            pojoEntity.setGenero(null);
            pojoEntity.setId(entity.getId());
            usuarioService.updateUsuario(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testUpdateUsuarioWithNoValidEmail() {
        assertThrows(IllegalOperationException.class, () -> {
            UsuarioEntity entity = usuarioList.get(0);
            UsuarioEntity pojoEntity = factory.manufacturePojo(UsuarioEntity.class);
            pojoEntity.setEmail(null);
            pojoEntity.setId(entity.getId());
            usuarioService.updateUsuario(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testUpdateUsuarioWithNoValidEmail2() {
        assertThrows(IllegalOperationException.class, () -> {
            UsuarioEntity entity = usuarioList.get(0);
            UsuarioEntity pojoEntity = factory.manufacturePojo(UsuarioEntity.class);
            pojoEntity.setEmail("");
            pojoEntity.setId(entity.getId());
            usuarioService.updateUsuario(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testUpdateUsuarioWithNoValidEdad() {
        assertThrows(IllegalOperationException.class, () -> {
            UsuarioEntity entity = usuarioList.get(0);
            UsuarioEntity pojoEntity = factory.manufacturePojo(UsuarioEntity.class);
            pojoEntity.setEdad(null);
            pojoEntity.setId(entity.getId());
            usuarioService.updateUsuario(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testUpdateUsuarioWithNoValidEdad2() {
        assertThrows(IllegalOperationException.class, () -> {
            UsuarioEntity entity = usuarioList.get(0);
            UsuarioEntity pojoEntity = factory.manufacturePojo(UsuarioEntity.class);
            pojoEntity.setEdad(-1);
            pojoEntity.setId(entity.getId());
            usuarioService.updateUsuario(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testUpdateUsuarioWithNoValidEdad3() {
        assertThrows(IllegalOperationException.class, () -> {
            UsuarioEntity entity = usuarioList.get(0);
            UsuarioEntity pojoEntity = factory.manufacturePojo(UsuarioEntity.class);
            pojoEntity.setEdad(120);
            pojoEntity.setId(entity.getId());
            usuarioService.updateUsuario(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testDeleteUsuario() throws EntityNotFoundException, IllegalOperationException {
        UsuarioEntity entity = usuarioList.get(1);
        usuarioService.deleteUsuario(entity.getId());
        UsuarioEntity deleted = entityManager.find(UsuarioEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteUsuarioInvalid() {
        assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.deleteUsuario(0L);
        });
    }

    /*
     * @Test
     * void testDeleteUbicacionWithTiendaFisica() throws EntityNotFoundException,
     * IllegalOperationException{
     * assertThrows(IllegalOperationException.class, () -> {
     * UbicacionEntity entity = ubicacionList.get(0);
     * ubicacionService.deleteUbicacion(entity.getId());
     * });
     * }
     */
    @Test
    void testDeleteUsuarioWithComentarios() throws EntityNotFoundException, IllegalOperationException {
        assertThrows(IllegalOperationException.class, () -> {
            UsuarioEntity entity = usuarioList.get(0);
            entity.setComentarios(comentariosList);
            usuarioService.deleteUsuario(entity.getId());
        });
    }

}
