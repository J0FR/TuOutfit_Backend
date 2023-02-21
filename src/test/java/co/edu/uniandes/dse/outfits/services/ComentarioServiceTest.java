package co.edu.uniandes.dse.outfits.services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
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
    }

    /**
     * Compara los atributos propios de dos entidades
     */
    private void compareEntidades(ComentarioEntity a, ComentarioEntity b) throws EntityNotFoundException, IllegalOperationException {
        Assertions.assertEquals(a.getId(), b.getId());
        Assertions.assertEquals(a.getTitulo(), b.getTitulo());
        Assertions.assertEquals(a.getCalificacion(), b.getCalificacion());
        Assertions.assertEquals(a.getMensaje(), b.getMensaje());
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
        Assertions.assertNotNull(result);

        ComentarioEntity entity = entityManager.find(ComentarioEntity.class, result.getId());
        // recuperado exitosamente
        Assertions.assertNotNull(entity);

        // verifica que los atributos coinciden
        compareEntidades(newEntity, entity);
    }

    /**
     * Test para crear un Comentario con algún atributo nulo
     */
    @Test
    public void testCreateComentarioAtributosNull() throws EntityNotFoundException, IllegalOperationException {
        Assertions.assertThrows(IllegalOperationException.class, () -> {
            ComentarioEntity newEntity;

            // sin autor
            newEntity = factory.manufacturePojo(ComentarioEntity.class);
            newEntity.setCalificacion(3);
            newEntity.setAutor(null);
            newEntity.setPrenda(prendaList.get(0));
            comentarioService.createComentario(newEntity);

            // sin calificacion
            newEntity = factory.manufacturePojo(ComentarioEntity.class);
            newEntity.setCalificacion(null);
            newEntity.setAutor(usuarioList.get(0));
            newEntity.setPrenda(prendaList.get(0));
            comentarioService.createComentario(newEntity);

            // sin mensaje
            newEntity = factory.manufacturePojo(ComentarioEntity.class);
            newEntity.setCalificacion(3);
            newEntity.setMensaje(null);
            newEntity.setAutor(usuarioList.get(0));
            newEntity.setPrenda(prendaList.get(0));
            comentarioService.createComentario(newEntity);

            // sin prenda y sin outfit
            newEntity = factory.manufacturePojo(ComentarioEntity.class);
            newEntity.setCalificacion(3);
            newEntity.setAutor(usuarioList.get(0));
            newEntity.setPrenda(null);
            newEntity.setOutfit(null);
            comentarioService.createComentario(newEntity);
        });
    }

    /**
     * Test para crear un Comentario con prenda y outfit asignados
     */
    @Test
    public void testCreateComentarioPrendaYOutfit() throws EntityNotFoundException, IllegalOperationException {
        Assertions.assertThrows(IllegalOperationException.class, () -> {
            ComentarioEntity newEntity = factory.manufacturePojo(ComentarioEntity.class);
            newEntity.setCalificacion(3);
            newEntity.setAutor(usuarioList.get(0));
            newEntity.setPrenda(prendaList.get(0));
            newEntity.setOutfit(outfitList.get(0));
            comentarioService.createComentario(newEntity);
        });
    }

    /**
     * Test para crear un Comentario con calificacion fuera del rango
     */
    @Test
    public void testCreateComentarioCalificacionRango() throws EntityNotFoundException, IllegalOperationException {
        Assertions.assertThrows(IllegalOperationException.class, () -> {
            // calificacion por debajo
            ComentarioEntity newEntityDebajo = factory.manufacturePojo(ComentarioEntity.class);
            newEntityDebajo.setCalificacion(0);
            newEntityDebajo.setAutor(usuarioList.get(0));
            newEntityDebajo.setPrenda(prendaList.get(0));
            comentarioService.createComentario(newEntityDebajo);

            // calificacion por encima
            ComentarioEntity newEntityArriba = factory.manufacturePojo(ComentarioEntity.class);
            newEntityArriba.setCalificacion(6);
            newEntityArriba.setPrenda(prendaList.get(0));
            newEntityArriba.setAutor(usuarioList.get(0));
            comentarioService.createComentario(newEntityArriba);
        });
    }

    /**
     * Test para obtener todos los comentarios
     */
    @Test
    public void testGetComentarios() {
        List <ComentarioEntity> list = comentarioService.getComentarios();
        Assertions.assertEquals(comentarioList.size(), list.size());

        for (ComentarioEntity entity : list) {
            boolean found = false;
            for (ComentarioEntity storedEntity : comentarioList) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            Assertions.assertTrue(found);
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
        Assertions.assertNotNull(resultEntity);

        // verifica que los atributos coinciden
        Assertions.assertEquals(entity.getId(), resultEntity.getId());
        Assertions.assertEquals(entity.getTitulo(), resultEntity.getTitulo());
        Assertions.assertEquals(entity.getCalificacion(), resultEntity.getCalificacion());
        Assertions.assertEquals(entity.getMensaje(), resultEntity.getMensaje());
    }

    /**
     * Test para obtener un comentario con id inválido
     */
    @Test
    public void testGetComentarioIdInvalid() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
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
        Assertions.assertThrows(IllegalOperationException.class, () -> {
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
     * Test para actualizar un comentario con algún atributo nulo
     */
    @Test
    public void testUpdateComentarioAtributosNull() throws EntityNotFoundException, IllegalOperationException {
        Assertions.assertThrows(IllegalOperationException.class, () -> {

            // entidad inicial
            ComentarioEntity entity = comentarioList.get(0);
            // entidad para remplazar
            ComentarioEntity pojoEntity, resp;

            // sin autor
            pojoEntity = factory.manufacturePojo(ComentarioEntity.class);
            pojoEntity.setCalificacion(3);
            pojoEntity.setAutor(null);
            pojoEntity.setPrenda(prendaList.get(0));
            comentarioService.updateComentario(entity.getId(), pojoEntity);

            // pide la entidad actualizada
            resp = entityManager.find(ComentarioEntity.class, entity.getId());
            // valida que los atributos se hayan actualizado
            compareEntidades(pojoEntity, resp);

            // sin calificacion
            pojoEntity = factory.manufacturePojo(ComentarioEntity.class);
            pojoEntity.setCalificacion(null);
            pojoEntity.setAutor(usuarioList.get(0));
            pojoEntity.setPrenda(prendaList.get(0));
            comentarioService.updateComentario(entity.getId(), pojoEntity);

            // pide la entidad actualizada
            resp = entityManager.find(ComentarioEntity.class, entity.getId());
            // valida que los atributos se hayan actualizado
            compareEntidades(pojoEntity, resp);

            // sin mensaje
            pojoEntity = factory.manufacturePojo(ComentarioEntity.class);
            pojoEntity.setCalificacion(3);
            pojoEntity.setMensaje(null);
            pojoEntity.setAutor(usuarioList.get(0));
            pojoEntity.setPrenda(prendaList.get(0));
            comentarioService.updateComentario(entity.getId(), pojoEntity);

            // pide la entidad actualizada
            resp = entityManager.find(ComentarioEntity.class, entity.getId());
            // valida que los atributos se hayan actualizado
            compareEntidades(pojoEntity, resp);

            // sin prenda y outfit
            pojoEntity = factory.manufacturePojo(ComentarioEntity.class);
            pojoEntity.setCalificacion(3);
            pojoEntity.setAutor(usuarioList.get(0));
            pojoEntity.setPrenda(null);
            pojoEntity.setOutfit(null);
            comentarioService.updateComentario(entity.getId(), pojoEntity);

            // pide la entidad actualizada
            resp = entityManager.find(ComentarioEntity.class, entity.getId());
            // valida que los atributos se hayan actualizado
            compareEntidades(pojoEntity, resp);

            // sin titulo
            pojoEntity = factory.manufacturePojo(ComentarioEntity.class);
            pojoEntity.setCalificacion(3);
            pojoEntity.setTitulo(null);
            pojoEntity.setAutor(usuarioList.get(0));
            pojoEntity.setPrenda(prendaList.get(0));
            comentarioService.updateComentario(entity.getId(), pojoEntity);

            // pide la entidad actualizada
            resp = entityManager.find(ComentarioEntity.class, entity.getId());
            // valida que los atributos se hayan actualizado
            compareEntidades(pojoEntity, resp);
        });
    }

    /**
     * Test para actualizar un comentario con id inválido
     */
    @Test
    public void testUpdateComentarioIdInvalido() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            ComentarioEntity pojoEntity = factory.manufacturePojo(ComentarioEntity.class);
            comentarioService.updateComentario(0L, pojoEntity); 
        });
    }

    /**
     * Test para actualizar un comentario con calificación fuera de rango
     */
    @Test
    public void testUpdateComentarioCalificacionRango() {
        Assertions.assertThrows(IllegalOperationException.class, () -> {
            ComentarioEntity entityt = comentarioList.get(0);
            ComentarioEntity pojoEntity = factory.manufacturePojo(ComentarioEntity.class);

            pojoEntity.setCalificacion(0);
            pojoEntity.setAutor(usuarioList.get(0));
            pojoEntity.setPrenda(prendaList.get(0));

            comentarioService.updateComentario(entityt.getId(), pojoEntity);

            pojoEntity = factory.manufacturePojo(ComentarioEntity.class);
            pojoEntity.setCalificacion(6);
            pojoEntity.setAutor(usuarioList.get(0));
            pojoEntity.setPrenda(prendaList.get(0));

            comentarioService.updateComentario(entityt.getId(), pojoEntity);
        });
    }

    /**
     * Test para borrar un comentario
     */
    @Test
    public void testDeleteComentario() throws EntityNotFoundException, IllegalOperationException {
        ComentarioEntity entity = comentarioList.get(0);
        comentarioService.deleteComentario(entity.getId());
        ComentarioEntity deleted = entityManager.find(ComentarioEntity.class, entity.getId());
        Assertions.assertNull(deleted);
    }

    /**
     * Test para borrar un comentario con id inválido
     */
    @Test
    public void testDeleteComentarioInvalid() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            comentarioService.deleteComentario(0L);
        });
    }
}
