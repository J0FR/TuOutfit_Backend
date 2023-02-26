package co.edu.uniandes.dse.outfits.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(OutfitComentarioService.class)
public class OutfitComentarioServiceTest {
    private PodamFactory factory = new PodamFactoryImpl();

	@Autowired
	private OutfitComentarioService outfitComentarioService;

	@Autowired
	private TestEntityManager entityManager;

    private List<OutfitEntity> outfitsList = new ArrayList<>();
    private List<PrendaEntity> prendasList = new ArrayList<>();
	private List<ComentarioEntity> comentariosList = new ArrayList<>();

    @BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

    private void clearData() {
		entityManager.getEntityManager().createQuery("delete from OutfitEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from ComentarioEntity ").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		for (int i = 0; i < 3; i++) {
			PrendaEntity prendaEntity = factory.manufacturePojo(PrendaEntity.class);
			entityManager.persist(prendaEntity);
			prendasList.add(prendaEntity);
		}
		for (int i = 0; i < 3; i++) {
			OutfitEntity outfitEntity = factory.manufacturePojo(OutfitEntity.class);
			entityManager.persist(outfitEntity);
			outfitsList.add(outfitEntity);
		}
		for (int i = 0; i < 3; i++) {
			ComentarioEntity comentarioEntity = factory.manufacturePojo(ComentarioEntity.class);
			entityManager.persist(comentarioEntity);
			comentariosList.add(comentarioEntity);
		}

		outfitsList.get(0).setComentarios(comentariosList);
	}

	/**
	 * Prueba para asociar un comentario existente a un outfit.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddComentario() throws EntityNotFoundException {
		OutfitEntity entity = outfitsList.get(0);
		ComentarioEntity comentarioEntity = comentariosList.get(1);
		ComentarioEntity response = outfitComentarioService.addComment(entity.getId(), comentarioEntity.getId());

		assertNotNull(response);
		assertEquals(comentarioEntity.getId(), response.getId());
	}

	/**
	 * Prueba para asociar un outfit existente a un comentario que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddInvalidComentario() {
		assertThrows(EntityNotFoundException.class, () -> {
			OutfitEntity outfitEntity = outfitsList.get(1);
			outfitComentarioService.addComment(0L, outfitEntity.getId());
		});
	}

	/**
	 * Prueba para asociar un outfit que no existe a un comentario.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddComentarioInvalidOutfit() {
		assertThrows(EntityNotFoundException.class, () -> {
			ComentarioEntity entity = comentariosList.get(0);
			outfitComentarioService.addComment(entity.getId(), 0L);
		});
	}

	/**
	 * Prueba para consultar comentarios de un outfit.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testGetComentario() throws EntityNotFoundException {
		OutfitEntity entity = outfitsList.get(0);
		List<ComentarioEntity> resultEntity = outfitComentarioService.getComentarios(entity.getId());
		assertNotNull(resultEntity);
		assertEquals(entity.getComentarios(), resultEntity);
	}

	/**
	 * Prueba para consultar un comentario de un outfit que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testGetComentarioInvalidOutfit() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, () -> {
			outfitComentarioService.getComentarios(0L);
		});
	}

	/**
	 * Prueba para desasociar un outfit existente de un comentario existente.
	 * 
	 * @throws EntityNotFoundException
	 *
	 * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
	 */
	@Test
	public void testRemoveComentario() throws EntityNotFoundException {
		outfitComentarioService.removeComentario(outfitsList.get(0).getId(), comentariosList.get(0).getId());
		OutfitEntity outfit = entityManager.find(OutfitEntity.class, outfitsList.get(0).getId());
		assertEquals(comentariosList.get(0).getId(), outfit.getComentarios().get(0).getId());
	}
	
	/**
	 * Prueba para desasociar un comentario inexistente de un outfit existente
	 * 
	 * @throws EntityNotFoundException
	 *
	 * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
	 */
	@Test
	public void testRemoveInvalidComentario(){
		assertThrows(NoSuchElementException.class, ()->{
			outfitComentarioService.removeComentario(outfitsList.get(0).getId(), 0L);
		});
	}

	/**
	 * Prueba para desasociar un comentario existente de un outfit inexistente
	 */
	@Test
	void testRemoveComentarioInvalidOutfit() {
		assertThrows(EntityNotFoundException.class, () -> {
			outfitComentarioService.removeComentario(0L, comentariosList.get(0).getId());
		});
	}
}
