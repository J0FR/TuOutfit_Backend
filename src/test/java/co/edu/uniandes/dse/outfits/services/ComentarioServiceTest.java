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
    static final int USUARIO_ENTITIES = 5;
    static final int PRENDA_ENTITIES = 10;
    static final int OUTFIT_ENTITIES = 5;
    // numero de listas de comentarios a crear
    static final int COMENTARIO_LIST_SIZE = 5;
    // tamaño máximo de cada lista de comentarios
    static final int COMENTARIO_ENTITIES = 5;

    
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
            entityManager.persist(usuarioEntity);
        }

        // Comentarios añadidos a prendas
        for (int i=0; i<PRENDA_ENTITIES; i++) {
            ArrayList <ComentarioEntity> comentariosToAdd = new ArrayList <ComentarioEntity> ();

            PrendaEntity prendaEntity =  factory.manufacturePojo(PrendaEntity.class);
            prendaList.add(prendaEntity);

            // llena la lista con un número aleatorio de comentarios
            int numero_comentarios_to_add = (int)(Math.random() * COMENTARIO_ENTITIES);
            for (int j=0; j<numero_comentarios_to_add; j++) {
                ComentarioEntity comentarioEntity =  factory.manufacturePojo(ComentarioEntity.class);
                comentarioEntity.setTitulo("Título de prueba");
                comentarioEntity.setCalificacion(3);
                comentarioEntity.setMensaje("Comentario de prueba");
                comentarioEntity.setPrenda(prendaEntity);
                // elige el autor para el comentario
                comentarioEntity.setAutor(usuarioList.get(0));
                comentarioList.add(comentarioEntity);
                comentariosToAdd.add(comentarioEntity);
                entityManager.persist(comentarioEntity);
            }

            prendaEntity.setComentarios(comentariosToAdd);
            entityManager.persist(prendaEntity);
        }

        // Comentarios añadidos a outfits
        for (int i=0; i<COMENTARIO_ENTITIES; i++) {
            ArrayList <ComentarioEntity> comentariosToAdd = new ArrayList <ComentarioEntity> ();

            OutfitEntity outfitEntity =  factory.manufacturePojo(OutfitEntity.class);
            outfitList.add(outfitEntity);

            // llena la lista con un número aleatorio de comentarios
            int numero_comentarios_to_add = (int)(Math.random() * COMENTARIO_ENTITIES);
            for (int j=0; j<numero_comentarios_to_add; j++) {
                ComentarioEntity comentarioEntity =  factory.manufacturePojo(ComentarioEntity.class);
                comentarioEntity.setTitulo("Título de prueba");
                comentarioEntity.setCalificacion(3);
                comentarioEntity.setMensaje("Comentario de prueba");
                comentarioEntity.setOutfit(outfitEntity);
                // elige el autor para el comentario
                comentarioEntity.setAutor(usuarioList.get(0));
                comentarioList.add(comentarioEntity);
                comentariosToAdd.add(comentarioEntity);
                entityManager.persist(comentarioEntity);
            }

            outfitEntity.setComentarios(comentariosToAdd);
            entityManager.persist(outfitEntity);
        }
    }

    @Test
    public void testCreateComentario() throws EntityNotFoundException, IllegalOperationException {
        // crea la entidad
        ComentarioEntity newEntity = factory.manufacturePojo(ComentarioEntity.class);
        newEntity.setTitulo("Título de prueba");
        newEntity.setCalificacion(3);
        newEntity.setMensaje("Mensaje de prueba");
        newEntity.setAutor(usuarioList.get(0));

        ComentarioEntity result = comentarioService.createComentario(newEntity);
        // creado exitosamente
        Assertions.assertNotNull(result);

        ComentarioEntity entity = entityManager.find(ComentarioEntity.class, result.getId());
        // recuperado exitosamente
        Assertions.assertNotNull(entity);

        // verifica que los atributos coinciden
        Assertions.assertEquals(newEntity.getId(), entity.getId());
        Assertions.assertEquals(newEntity.getTitulo(), entity.getTitulo());
        Assertions.assertEquals(newEntity.getCalificacion(), entity.getCalificacion());
        Assertions.assertEquals(newEntity.getMensaje(), entity.getMensaje());
    }

    /**
     * Los siguientes tests validan los errores cuándo se 
     * crean comentarios con atributos inválidos.
     * Esto incluye:
     * 1. Calificación fuera de rango (1-5)
     * 2. Sin calificación
     * 3. Sin autor
     * 4. Sin prenda especificada (TODO: generalizar de Prenda a Elemento)
     */
    @Test
    public void testCreateComentarioCalificacionRango() throws EntityNotFoundException, IllegalOperationException {
        Assertions.assertThrows(IllegalOperationException.class, () -> {
            // calificacion por debajo
            ComentarioEntity newEntityDebajo = factory.manufacturePojo(ComentarioEntity.class);
            newEntityDebajo.setTitulo("Título de prueba");
            newEntityDebajo.setCalificacion(0);
            newEntityDebajo.setMensaje("Mensaje de prueba");
            newEntityDebajo.setAutor(usuarioList.get(0));
            newEntityDebajo.setPrenda(prendaList.get(0));
            comentarioService.createComentario(newEntityDebajo);

            // calificacion por encima
            ComentarioEntity newEntityArriba = factory.manufacturePojo(ComentarioEntity.class);
            newEntityArriba.setTitulo("Título de prueba");
            newEntityArriba.setCalificacion(6);
            newEntityArriba.setMensaje("Mensaje de prueba");
            newEntityArriba.setPrenda(prendaList.get(0));
            newEntityArriba.setAutor(usuarioList.get(0));
            comentarioService.createComentario(newEntityArriba);
        });
    }

    @Test
    public void testCreateComentarioSinCalificacion() throws IllegalOperationException {
        Assertions.assertThrows(IllegalOperationException.class, () -> {
            ComentarioEntity newEntity = factory.manufacturePojo(ComentarioEntity.class);
            newEntity.setTitulo("Título de prueba");
            newEntity.setMensaje("Mensaje de prueba");
            newEntity.setAutor(usuarioList.get(0));
            newEntity.setPrenda(prendaList.get(0));
            comentarioService.createComentario(newEntity);
        });
    }

    @Test
    public void testCreateComentarioSinAutor() throws IllegalOperationException {
        Assertions.assertThrows(IllegalOperationException.class, () -> {
            ComentarioEntity newEntity = factory.manufacturePojo(ComentarioEntity.class);
            newEntity.setTitulo("Título de prueba");
            newEntity.setCalificacion(3);
            newEntity.setMensaje("Mensaje de prueba");
            newEntity.setPrenda(prendaList.get(0));
            comentarioService.createComentario(newEntity);
        });
    }

    @Test
    public void testCreateComentarioSinPrenda() throws IllegalOperationException {
        Assertions.assertThrows(IllegalOperationException.class, () -> {
            ComentarioEntity newEntity = factory.manufacturePojo(ComentarioEntity.class);
            newEntity.setTitulo("Título de prueba");
            newEntity.setCalificacion(3);
            newEntity.setMensaje("Mensaje de prueba");
            newEntity.setAutor(usuarioList.get(0));
            comentarioService.createComentario(newEntity);
        });
    }

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

    @Test
    public void testGetComentarioInvalid() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            comentarioService.getComentario(0L);
        });
    }

    @Test
    public void testUpdateComentario() throws EntityNotFoundException, IllegalOperationException {
        // entidad inicial
        ComentarioEntity entity = comentarioList.get(0);
        // entidad para remplazar
        ComentarioEntity pojoEntity = factory.manufacturePojo(ComentarioEntity.class);
        pojoEntity.setId(entity.getId());
        comentarioService.updateComentario(entity.getId(), pojoEntity);

        // pide la entidad actualizada
        ComentarioEntity resp = entityManager.find(ComentarioEntity.class, entity.getId());
        // valida que los atributos se hayan actualizado
        Assertions.assertEquals(pojoEntity.getId(), resp.getId());
        Assertions.assertEquals(pojoEntity.getTitulo(), resp.getTitulo());
        Assertions.assertEquals(pojoEntity.getCalificacion(), resp.getCalificacion());
        Assertions.assertEquals(pojoEntity.getMensaje(), resp.getMensaje());
    }

    @Test
    public void testUpdateComentarioInvalid() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            ComentarioEntity pojoEntity = factory.manufacturePojo(ComentarioEntity.class);
            pojoEntity.setId(0L);
            comentarioService.updateComentario(0L, pojoEntity); 
        });
    }

    /**
     * Los siguientes tests validan los errores cuándo se 
     * actualizan comentarios con atributos inválidos.
     * Esto incluye:
     * 1. Calificación fuera de rango (1-5)
     * 2. Sin calificación
     * 3. Sin autor
     * 4. Sin prenda especificada (TODO: generalizar de Prenda a Elemento)
     */
    @Test
    public void testUpdateComentarioCalificacionRango() {
        Assertions.assertThrows(IllegalOperationException.class, () -> {
            ComentarioEntity entityt = comentarioList.get(0);
            ComentarioEntity pojoEntity = factory.manufacturePojo(ComentarioEntity.class);

            pojoEntity.setId(entityt.getId());
            pojoEntity.setTitulo("Título de prueba");
            pojoEntity.setCalificacion(0);
            pojoEntity.setMensaje("Mensaje de prueba");
            pojoEntity.setAutor(usuarioList.get(0));
            pojoEntity.setPrenda(prendaList.get(0));

            comentarioService.updateComentario(entityt.getId(), pojoEntity);

            pojoEntity = factory.manufacturePojo(ComentarioEntity.class);
            pojoEntity.setId(entityt.getId());
            pojoEntity.setTitulo("Título de prueba");
            pojoEntity.setCalificacion(6);
            pojoEntity.setMensaje("Mensaje de prueba");
            pojoEntity.setAutor(usuarioList.get(0));
            pojoEntity.setPrenda(prendaList.get(0));

            comentarioService.updateComentario(entityt.getId(), pojoEntity);
        });
    }

    @Test
    public void testUpdateComentarioSinCalificacion() {
        Assertions.assertThrows(IllegalOperationException.class, () -> {
            ComentarioEntity entityt = comentarioList.get(0);
            ComentarioEntity pojoEntity = factory.manufacturePojo(ComentarioEntity.class);

            pojoEntity.setId(entityt.getId());
            pojoEntity.setTitulo("Título de prueba");
            pojoEntity.setMensaje("Mensaje de prueba");
            pojoEntity.setAutor(usuarioList.get(0));
            pojoEntity.setPrenda(prendaList.get(0));

            comentarioService.updateComentario(entityt.getId(), pojoEntity);
        });
    }

    @Test
    public void testUpdateComentarioSinAutor() {
        Assertions.assertThrows(IllegalOperationException.class, () -> {
            ComentarioEntity entityt = comentarioList.get(0);
            ComentarioEntity pojoEntity = factory.manufacturePojo(ComentarioEntity.class);

            pojoEntity.setId(entityt.getId());
            pojoEntity.setTitulo("Título de prueba");
            pojoEntity.setCalificacion(3);
            pojoEntity.setMensaje("Mensaje de prueba");
            pojoEntity.setPrenda(prendaList.get(0));

            comentarioService.updateComentario(entityt.getId(), pojoEntity);
        });
    }

    @Test
    public void testUpdateComentarioSinPrenda() {
        Assertions.assertThrows(IllegalOperationException.class, () -> {
            ComentarioEntity entityt = comentarioList.get(0);
            ComentarioEntity pojoEntity = factory.manufacturePojo(ComentarioEntity.class);

            pojoEntity.setId(entityt.getId());
            pojoEntity.setTitulo("Título de prueba");
            pojoEntity.setCalificacion(3);
            pojoEntity.setMensaje("Mensaje de prueba");
            pojoEntity.setAutor(usuarioList.get(0));

            comentarioService.updateComentario(entityt.getId(), pojoEntity);
        });
    }

    @Test
    public void testDeleteComentario() throws EntityNotFoundException, IllegalOperationException {
        ComentarioEntity entity = comentarioList.get(0);
        comentarioService.deleteComentario(entity.getId());
        ComentarioEntity deleted = entityManager.find(ComentarioEntity.class, entity.getId());
        Assertions.assertNull(deleted);
    }

    @Test
    public void testDeleteComentarioInvalid() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            comentarioService.deleteComentario(0L);
        });
    }

    // No hay tests de relaciones porque no existe una entidad 
    // que componga al comentario.
}
