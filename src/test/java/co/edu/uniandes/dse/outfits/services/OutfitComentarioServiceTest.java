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
import co.edu.uniandes.dse.outfits.entities.ComentarioEntity;
import co.edu.uniandes.dse.outfits.entities.OutfitEntity;
import co.edu.uniandes.dse.outfits.entities.PrendaEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(OutfitComentarioService.class)
public class OutfitComentarioServiceTest {
	@Autowired
	private OutfitComentarioService outfitComentarioService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<OutfitEntity> outfitList = new ArrayList<>();
	private List<ComentarioEntity> comentariosList = new ArrayList<>();

	/**
	 * Configuración inicial de la prueba.
	 */
	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

	/**
	 * Limpia las tablas que están implicadas en la prueba.
	 */
	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from OutfitEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from ComentarioEntity").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		for (int i = 0; i < 3; i++) {
			ComentarioEntity comentario = factory.manufacturePojo(ComentarioEntity.class);
			entityManager.persist(comentario);
			comentariosList.add(comentario);
		}

		for (int i = 0; i < 3; i++) {
			OutfitEntity entity = factory.manufacturePojo(OutfitEntity.class);
			entityManager.persist(entity);
			outfitList.add(entity);
			if (i == 0) {
				entity.getComentarios().add(comentariosList.get(i));
			}
		}
	}

	/**
	 * Prueba para asociar un Comentario existente a un Outfit.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddComentario() throws EntityNotFoundException {
		OutfitEntity entity = outfitList.get(0);
		ComentarioEntity comentarioEntity = comentariosList.get(1);
		ComentarioEntity response = outfitComentarioService.addComentario(entity.getId(), comentarioEntity.getId());

		assertNotNull(response);
		assertEquals(comentarioEntity.getId(), response.getId());
	}
	
	/**
	 * Prueba para asociar un Comentario que no existe a un Outfit.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddInvalidComentario() {
		assertThrows(EntityNotFoundException.class, ()->{
			OutfitEntity entity = outfitList.get(0);
			outfitComentarioService.addComentario(0L, entity.getId());
		});
	}
	
	/**
	 * Prueba para asociar un Comentario a una Outfit que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddComentarioInvalidOutfit() {
		assertThrows(EntityNotFoundException.class, ()->{
			ComentarioEntity comentarioEntity = comentariosList.get(1);
			outfitComentarioService.addComentario(comentarioEntity.getId(), 0L);
		});
	}

	/**
	 * Prueba para obtener una colección de instancias de Comentarios asociadas a una
	 * instancia Outfit.
	 * 
	 * @throws EntityNotFoundException
	 */

	@Test
	void testGetComentarios() throws EntityNotFoundException {
		List<ComentarioEntity> list = outfitComentarioService.getComentarios(outfitList.get(0).getId());
		assertEquals(1, list.size());
	}
	
	/**
	 * Prueba para obtener una colección de instancias de Comentarios asociadas a una
	 * instancia Outfit que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */

	@Test
	void testGetComentariosInvalidOutfit() {
		assertThrows(EntityNotFoundException.class,()->{
			outfitComentarioService.getComentarios(0L);
		});
	}

	/**
	 * Prueba para obtener una instancia de Comentario asociada a una instancia Outfit.
	 * 
	 * @throws IllegalOperationException
	 * @throws EntityNotFoundException
	 *
	 * @throws co.edu.uniandes.csw.Comentario.exceptions.BusinessLogicException
	 */
	@Test
	void testGetComentario() throws EntityNotFoundException, IllegalOperationException {
		OutfitEntity entity = outfitList.get(0);
		ComentarioEntity comentarioEntity = comentariosList.get(0);
		ComentarioEntity response = outfitComentarioService.getComentario(entity.getId(), comentarioEntity.getId());

		assertEquals(comentarioEntity.getId(), response.getId());
		assertEquals(comentarioEntity.getCalificacion(), response.getCalificacion());
		assertEquals(comentarioEntity.getMensaje(), response.getMensaje());
		assertEquals(comentarioEntity.getTitulo(), response.getTitulo());
		assertEquals(comentarioEntity.getAutor(), response.getAutor());
	}
	
	/**
	 * Prueba para obtener una instancia de Comentario asociada a una instancia Outfit que no existe.
	 * 
	 * @throws EntityNotFoundException
	 *
	 */
	@Test
	void testGetComentarioInvalidOutfit()  {
		assertThrows(EntityNotFoundException.class, ()->{
			ComentarioEntity comentarioEntity = comentariosList.get(0);
			outfitComentarioService.getComentario(0L, comentarioEntity.getId());
		});
	}
	
	/**
	 * Prueba para obtener una instancia de Comentario que no existe asociada a una instancia Outfit.
	 * 
	 * @throws EntityNotFoundException
	 * 
	 */
	@Test
	void testGetInvalidComentario()  {
		assertThrows(EntityNotFoundException.class, ()->{
			OutfitEntity entity = outfitList.get(0);
			outfitComentarioService.getComentario(entity.getId(), 0L);
		});
	}

	/**
	 * Prueba para obtener una instancia de Comentarios asociada a una instancia Outfit
	 * que no le pertenece.
	 *
	 * @throws co.edu.uniandes.csw.Comentario.exceptions.BusinessLogicException
	 */
	@Test
	public void getComentarioNoAsociadoTest() {
		assertThrows(IllegalOperationException.class, () -> {
			OutfitEntity entity = outfitList.get(0);
			ComentarioEntity comentarioEntity = comentariosList.get(1);
			outfitComentarioService.getComentario(entity.getId(), comentarioEntity.getId());
		});
	}

	/**
	 * Prueba para remplazar las instancias de Comentarios asociadas a una instancia de
	 * Outfit.
	 */
	@Test
	void testReplaceComentarios() throws EntityNotFoundException {
		OutfitEntity entity = outfitList.get(0);
		List<ComentarioEntity> list = comentariosList.subList(1, 3);
		outfitComentarioService.replaceComentarios(entity.getId(), list);

		for (ComentarioEntity comentario : list) {
			ComentarioEntity b = entityManager.find(ComentarioEntity.class, comentario.getId());
			assertTrue(b.getOutfit().equals(entity));
		}
	}
	
	/**
	 * Prueba para remplazar las instancias de Comentarios que no existen asociadas a una instancia de
	 * Outfit.
	 */
	@Test
	void testReplaceInvalidComentarios() {
		assertThrows(EntityNotFoundException.class, ()->{
			OutfitEntity entity = outfitList.get(0);
			
			List<ComentarioEntity> comentarios = new ArrayList<>();
			ComentarioEntity newComentario = factory.manufacturePojo(ComentarioEntity.class);
			newComentario.setId(0L);
			comentarios.add(newComentario);
			
			outfitComentarioService.replaceComentarios(entity.getId(), comentarios);
		});
	}
	
	/**
	 * Prueba para remplazar las instancias de Comentarios asociadas a una instancia de
	 * Outfit que no existe.
	 */
	@Test
	void testReplaceComentariosInvalidOutfit() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, ()->{
			List<ComentarioEntity> list = comentariosList.subList(1, 3);
			outfitComentarioService.replaceComentarios(0L, list);
		});
	}
}
