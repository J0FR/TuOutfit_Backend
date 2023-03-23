package co.edu.uniandes.dse.outfits.services;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import co.edu.uniandes.dse.outfits.entities.ComentarioEntity;
import co.edu.uniandes.dse.outfits.entities.OutfitEntity;
import co.edu.uniandes.dse.outfits.entities.PrendaEntity;
import co.edu.uniandes.dse.outfits.entities.UsuarioEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Tests de lógica para el ComentarioService
 * @author: c4ts0up
 * Link diagrama modelo: https://camo.githubusercontent.com/e57789895faa08f909e15068c479183461fbfddd3668bb43d58bca79a6f5e89e/68747470733a2f2f63646e2e646973636f72646170702e636f6d2f6174746163686d656e74732f313037313934313331343830393137323038392f313037343534353637373837363539323731302f636f6e6365707475616c2e6a706567
 * 
 */

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(ComentarioService.class)
public class ComentarioServiceTest {

    // número de entidades de prueba instanciadas
    static final int USUARIO_ENTITIES = 3;
    static final int PRENDA_ENTITIES = 3;
    static final int OUTFIT_ENTITIES = 3;
    // tamaño máximo de cada lista de comentarios
    static final int COMENTARIO_ENTITIES = 3;

    
    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    
    private List <ComentarioEntity> comentarioList = new ArrayList <ComentarioEntity> ();
    private List <UsuarioEntity> usuarioList = new ArrayList <UsuarioEntity> ();
    private List <OutfitEntity> outfitList = new ArrayList <OutfitEntity> ();
    private List <PrendaEntity> prendaList = new ArrayList <PrendaEntity> ();

    
    /**
     * Configuración inicial de la prueba.
     */
    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    /**
     * Limpia las tablas implicadas en la prueba.
     */
    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from ComentarioEntity");
        entityManager.getEntityManager().createQuery("delete from UsuarioEntity");
        entityManager.getEntityManager().createQuery("delete from OutfitEntity");
        entityManager.getEntityManager().createQuery("delete from PrendaEntity");
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
     */
    private void insertData() {
        // Usuario
        for (int i=0; i<USUARIO_ENTITIES; i++) {
            UsuarioEntity usuarioEntity =  factory.manufacturePojo(UsuarioEntity.class);
            usuarioList.add(usuarioEntity);
            entityManager.persist(usuarioEntity);
        }

        // Comentarios añadidos a prendas
        for (int i=0; i<PRENDA_ENTITIES; i++) {
            ArrayList <ComentarioEntity> comentariosToAdd = new ArrayList <ComentarioEntity> ();

            PrendaEntity prendaEntity =  factory.manufacturePojo(PrendaEntity.class);
            prendaList.add(prendaEntity);

            // llena la lista con un número aleatorio de comentarios
            for (int j=0; j<COMENTARIO_ENTITIES; j++) {
                ComentarioEntity comentarioEntity =  factory.manufacturePojo(ComentarioEntity.class);

                // configura el comentario
                comentarioEntity.setAutor(usuarioList.get(0));
                comentarioEntity.setPrenda(prendaEntity);

                comentarioList.add(comentarioEntity);
                comentariosToAdd.add(comentarioEntity);
                entityManager.persist(comentarioEntity);
            }

            prendaEntity.setComentarios(comentariosToAdd);
            entityManager.persist(prendaEntity);
        }

        // Comentarios añadidos a outfits
        for (int i=0; i<OUTFIT_ENTITIES; i++) {
            ArrayList <ComentarioEntity> comentariosToAdd = new ArrayList <ComentarioEntity> ();

            OutfitEntity outfitEntity =  factory.manufacturePojo(OutfitEntity.class);
            outfitList.add(outfitEntity);

            // llena la lista con un número aleatorio de comentarios
            for (int j=0; j<COMENTARIO_ENTITIES; j++) {
                ComentarioEntity comentarioEntity =  factory.manufacturePojo(ComentarioEntity.class);

                // configura el comentario
                comentarioEntity.setAutor(usuarioList.get(0));
                comentarioEntity.setOutfit(outfitEntity);

                comentarioList.add(comentarioEntity);
                comentariosToAdd.add(comentarioEntity);
                entityManager.persist(comentarioEntity);
            }

            outfitEntity.setComentarios(comentariosToAdd);
            entityManager.persist(outfitEntity);
        }
        comentarioList.get(1).setOutfit(outfitList.get(0));
        comentarioList.get(1).setPrenda(null);
    }

    /**
     * Compara los atributos propios de dos entidades
     */
    private void compareEntidades(ComentarioEntity a, ComentarioEntity b) throws EntityNotFoundException, IllegalOperationException {
        assertEquals(a.getId(), b.getId());
        assertEquals(a.getTitulo(), b.getTitulo());
        assertEquals(a.getCalificacion(), b.getCalificacion());
        assertEquals(a.getMensaje(), b.getMensaje());
    }

    /**
     * Test para crear un Comentario
     */
    @Test
    public void testCreateComentario() throws EntityNotFoundException, IllegalOperationException {
        // crea la entidad
        ComentarioEntity newEntity = factory.manufacturePojo(ComentarioEntity.class);
        newEntity.setCalificacion(3);
        newEntity.setAutor(usuarioList.get(0));
        newEntity.setPrenda(prendaList.get(0));

        ComentarioEntity result = comentarioService.createComentario(newEntity);
        // creado exitosamente
        assertNotNull(result);

        ComentarioEntity entity = entityManager.find(ComentarioEntity.class, result.getId());
        // recuperado exitosamente
        assertNotNull(entity);

        // verifica que los atributos coinciden
        compareEntidades(newEntity, entity);
    }

    /* TODO */
    // /**
    //  * Test para crear un Comentario con un autor nulo
    //  */
    // @Test
    // public void testCreateComentarioAutorNull() throws IllegalOperationException {
    //     assertThrows(IllegalOperationException.class, () -> {
    //         // sin autor
    //         ComentarioEntity newEntity = factory.manufacturePojo(ComentarioEntity.class);
    //         newEntity.setCalificacion(3);
    //         newEntity.setAutor(null);
    //         newEntity.setPrenda(prendaList.get(0));
    //         comentarioService.createComentario(newEntity);
    //     });
    // }

    /**
     * Test para crear un Comentario con una calificación nula
     */
    @Test
    public void testCreateComentarioCalificacionNull() throws IllegalOperationException {
        assertThrows(IllegalOperationException.class, () -> {
            ComentarioEntity newEntity = factory.manufacturePojo(ComentarioEntity.class);
            newEntity.setCalificacion(null);
            newEntity.setAutor(usuarioList.get(0));
            newEntity.setPrenda(prendaList.get(0));
            comentarioService.createComentario(newEntity);
        });
    }

    /**
     * Test para crear un Comentario con un mensaje nulo
     */
    @Test
    public void testCreateComentarioMensajeNull() throws IllegalOperationException {
        assertThrows(IllegalOperationException.class, () -> {
            ComentarioEntity newEntity = factory.manufacturePojo(ComentarioEntity.class);
            newEntity.setCalificacion(3);
            newEntity.setMensaje(null);
            newEntity.setAutor(usuarioList.get(0));
            newEntity.setPrenda(prendaList.get(0));
            comentarioService.createComentario(newEntity);
        });
    }

    /**
     * Test para crear un Comentario con un título nulo
     */
    @Test
    public void testCreateComentarioTituloNull() throws IllegalOperationException {
        assertThrows(IllegalOperationException.class, () -> {
            ComentarioEntity newEntity = factory.manufacturePojo(ComentarioEntity.class);
            newEntity.setCalificacion(3);
            newEntity.setTitulo(null);
            newEntity.setAutor(usuarioList.get(0));
            newEntity.setPrenda(prendaList.get(0));
            comentarioService.createComentario(newEntity);
        });
    }

    /**
     * Test para crear un Comentario con prenda y outfit asignados
     */
    @Test
    public void testCreateComentarioPrendaYOutfit() throws IllegalOperationException {
        assertThrows(IllegalOperationException.class, () -> {
            ComentarioEntity newEntity = factory.manufacturePojo(ComentarioEntity.class);
            newEntity.setCalificacion(3);
            newEntity.setAutor(usuarioList.get(0));
            newEntity.setPrenda(prendaList.get(0));
            newEntity.setOutfit(outfitList.get(0));
            comentarioService.createComentario(newEntity);
        });
    }

    /**
     * Test para crear un Comentario con calificacion por debajo del rango permitido
     */
    @Test
    public void testCreateComentarioCalificacionDebajo() throws EntityNotFoundException, IllegalOperationException {
        assertThrows(IllegalOperationException.class, () -> {
            ComentarioEntity newEntityDebajo = factory.manufacturePojo(ComentarioEntity.class);
            newEntityDebajo.setCalificacion(0);
            newEntityDebajo.setAutor(usuarioList.get(0));
            newEntityDebajo.setPrenda(prendaList.get(0));
            comentarioService.createComentario(newEntityDebajo);
        });
    }

    /**
     * Test para crear un Comentario con calificacion por encima del rango permitido
     */
    @Test
    public void testCreateComentarioCalificacionEncima() throws EntityNotFoundException, IllegalOperationException {
        assertThrows(IllegalOperationException.class, () -> {
            ComentarioEntity newEntityDebajo = factory.manufacturePojo(ComentarioEntity.class);
            newEntityDebajo.setCalificacion(6);
            newEntityDebajo.setAutor(usuarioList.get(0));
            newEntityDebajo.setPrenda(prendaList.get(0));
            comentarioService.createComentario(newEntityDebajo);
        });
    }

    /**
     * Test para obtener todos los comentarios
     */
    @Test
    public void testGetComentarios() {
        List <ComentarioEntity> list = comentarioService.getComentarios();
        assertEquals(comentarioList.size(), list.size());

        for (ComentarioEntity entity : list) {
            boolean found = false;
            for (ComentarioEntity storedEntity : comentarioList) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            assertTrue(found);
        }
    }

    /**
     * Test para obtener un comentario
     */
    @Test
    public void testGetComentario() throws EntityNotFoundException {
        ComentarioEntity entity = comentarioList.get(0);
        ComentarioEntity resultEntity = comentarioService.getComentario(entity.getId());

        // se obtuvo exitosamente
        assertNotNull(resultEntity);

        // verifica que los atributos coinciden
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getTitulo(), resultEntity.getTitulo());
        assertEquals(entity.getCalificacion(), resultEntity.getCalificacion());
        assertEquals(entity.getMensaje(), resultEntity.getMensaje());
    }

    /**
     * Test para obtener un comentario con id inválido
     */
    @Test
    public void testGetComentarioIdInvalid() {
        assertThrows(EntityNotFoundException.class, () -> {
            comentarioService.getComentario(0L);
        });
    }

    /**
     * Test para actualizar un comentario
     */
    @Test
    public void testUpdateComentario() throws EntityNotFoundException, IllegalOperationException {
        // entidad inicial
        ComentarioEntity entity = comentarioList.get(0);
        // entidad para remplazar
        ComentarioEntity pojoEntity = factory.manufacturePojo(ComentarioEntity.class);

        // configura al pojo
        pojoEntity.setCalificacion(3);
        pojoEntity.setAutor(usuarioList.get(0));
        pojoEntity.setPrenda(prendaList.get(0));
        
        comentarioService.updateComentario(entity.getId(), pojoEntity);

        // pide la entidad actualizada
        ComentarioEntity resp = entityManager.find(ComentarioEntity.class, entity.getId());
        // valida que los atributos se hayan actualizado
        compareEntidades(pojoEntity, resp);
    }

    /**
     * Test para actualizar un comentario con prenda y outfit asignados
     */
    @Test
    public void testUpdateComentarioPrendaYOutfit() throws EntityNotFoundException, IllegalOperationException {
        assertThrows(IllegalOperationException.class, () -> {
            // entidad inicial
            ComentarioEntity entity = comentarioList.get(0);
            // entidad para remplazar
            ComentarioEntity pojoEntity = factory.manufacturePojo(ComentarioEntity.class);

            // configura al pojo
            pojoEntity.setCalificacion(3);
            pojoEntity.setAutor(usuarioList.get(0));
            pojoEntity.setPrenda(prendaList.get(0));
            pojoEntity.setOutfit(outfitList.get(0));
            
            comentarioService.updateComentario(entity.getId(), pojoEntity);
        });
    }

    /**
     * Test para actualizar un comentario con un autor nulo
     */
    @Test
    public void testUpdateComentarioAutorNull() throws IllegalOperationException {
        assertThrows(IllegalOperationException.class, () -> {
            // entidad inicial
            ComentarioEntity entity = comentarioList.get(0);

            ComentarioEntity pojoEntity = factory.manufacturePojo(ComentarioEntity.class);
            pojoEntity.setCalificacion(3);
            pojoEntity.setAutor(null);
            pojoEntity.setPrenda(prendaList.get(0));
            comentarioService.updateComentario(entity.getId(), pojoEntity);

            // pide la entidad actualizada
            ComentarioEntity resp = entityManager.find(ComentarioEntity.class, entity.getId());
            // valida que los atributos se hayan actualizado
            compareEntidades(pojoEntity, resp);
        });
    }

    /**
     * Test para actualizar un comentario con una calificacion nula
     */
    @Test
    public void testUpdateComentarioCalificacionNull() throws IllegalOperationException {
        assertThrows(IllegalOperationException.class, () -> {
            // entidad inicial
            ComentarioEntity entity = comentarioList.get(0);

            ComentarioEntity pojoEntity = factory.manufacturePojo(ComentarioEntity.class);
            pojoEntity.setCalificacion(null);
            pojoEntity.setAutor(usuarioList.get(0));
            pojoEntity.setPrenda(prendaList.get(0));
            comentarioService.updateComentario(entity.getId(), pojoEntity);

            // pide la entidad actualizada
            ComentarioEntity resp = entityManager.find(ComentarioEntity.class, entity.getId());
            // valida que los atributos se hayan actualizado
            compareEntidades(pojoEntity, resp);
        });
    }

    /**
     * Test para actualizar un comentario con un mensaje nulo
     */
    @Test
    public void testUpdateComentarioMensajeNull() {
        assertThrows(IllegalOperationException.class, () -> {
            // entidad inicial
            ComentarioEntity entity = comentarioList.get(0);

            ComentarioEntity pojoEntity = factory.manufacturePojo(ComentarioEntity.class);
            pojoEntity.setCalificacion(3);
            pojoEntity.setMensaje(null);
            pojoEntity.setAutor(usuarioList.get(0));
            pojoEntity.setPrenda(prendaList.get(0));
            comentarioService.updateComentario(entity.getId(), pojoEntity);

            // pide la entidad actualizada
            ComentarioEntity resp = entityManager.find(ComentarioEntity.class, entity.getId());
            // valida que los atributos se hayan actualizado
            compareEntidades(pojoEntity, resp);
        });
    }

    /**
     * Test para actualizar un comentario con un titulo nulo
     */
    @Test
    public void testUpdateComentarioTituloNull() {
        assertThrows(IllegalOperationException.class, () -> {
            // entidad inicial
            ComentarioEntity entity = comentarioList.get(0);

            ComentarioEntity pojoEntity = factory.manufacturePojo(ComentarioEntity.class);
            pojoEntity.setCalificacion(3);
            pojoEntity.setTitulo(null);
            pojoEntity.setAutor(usuarioList.get(0));
            pojoEntity.setPrenda(prendaList.get(0));
            comentarioService.updateComentario(entity.getId(), pojoEntity);

            // pide la entidad actualizada
            ComentarioEntity resp = entityManager.find(ComentarioEntity.class, entity.getId());
            // valida que los atributos se hayan actualizado
            compareEntidades(pojoEntity, resp);
        });
    }

    /**
     * Test para actualizar un comentario con prenda y outfit nulos
     */
    @Test
    public void testUpdateComentarioPrendaYOutfitNull() {
        assertThrows(IllegalOperationException.class, () -> {
            // entidad inicial
            ComentarioEntity entity = comentarioList.get(0);

            ComentarioEntity pojoEntity = factory.manufacturePojo(ComentarioEntity.class);
            pojoEntity.setCalificacion(3);
            pojoEntity.setAutor(usuarioList.get(0));
            pojoEntity.setPrenda(null);
            pojoEntity.setOutfit(null);
            comentarioService.updateComentario(entity.getId(), pojoEntity);

            // pide la entidad actualizada
            ComentarioEntity resp = entityManager.find(ComentarioEntity.class, entity.getId());
            // valida que los atributos se hayan actualizado
            compareEntidades(pojoEntity, resp);
        });
    }

    /**
     * Test para actualizar un comentario con id inválido
     */
    @Test
    public void testUpdateComentarioIdInvalido() {
        assertThrows(EntityNotFoundException.class, () -> {
            ComentarioEntity pojoEntity = factory.manufacturePojo(ComentarioEntity.class);
            comentarioService.updateComentario(0L, pojoEntity); 
        });
    }

    /**
     * Test para actualizar un comentario con calificación encima del rango
     */
    @Test
    public void testUpdateComentarioCalificacionEncima() {
        assertThrows(IllegalOperationException.class, () -> {
            ComentarioEntity entity = comentarioList.get(0);
            ComentarioEntity pojoEntity = factory.manufacturePojo(ComentarioEntity.class);

            pojoEntity.setCalificacion(6);
            pojoEntity.setAutor(usuarioList.get(0));
            pojoEntity.setPrenda(prendaList.get(0));

            comentarioService.updateComentario(entity.getId(), pojoEntity);
        });
    }

    /**
     * Test para actualizar un comentario con calificación debajo del rango
     */
    @Test
    public void testUpdateComentarioCalificacionDebajo() {
        assertThrows(IllegalOperationException.class, () -> {
            ComentarioEntity entity = comentarioList.get(0);
            ComentarioEntity pojoEntity = factory.manufacturePojo(ComentarioEntity.class);

            pojoEntity.setCalificacion(0);
            pojoEntity.setAutor(usuarioList.get(0));
            pojoEntity.setPrenda(prendaList.get(0));

            comentarioService.updateComentario(entity.getId(), pojoEntity);
        });
    }

    /**
     * Test para borrar un comentario
     */
    @Test
    public void testDeleteComentarioWithPrenda() throws EntityNotFoundException, IllegalOperationException {
        ComentarioEntity entity = comentarioList.get(0);
        comentarioService.deleteComentario(entity.getId());
        ComentarioEntity deleted = entityManager.find(ComentarioEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    public void testDeleteComentarioWithOutfit() throws EntityNotFoundException, IllegalOperationException {
        ComentarioEntity entity = comentarioList.get(1);
        comentarioService.deleteComentario(entity.getId());
        ComentarioEntity deleted = entityManager.find(ComentarioEntity.class, entity.getId());
        assertNull(deleted);
    }

    /**
     * Test para borrar un comentario con id inválido
     */
    @Test
    public void testDeleteComentarioInvalid() {
        assertThrows(EntityNotFoundException.class, () -> {
            comentarioService.deleteComentario(0L);
        });
    }
}
