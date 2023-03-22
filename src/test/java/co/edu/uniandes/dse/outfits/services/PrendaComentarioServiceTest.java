package co.edu.uniandes.dse.outfits.services;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import co.edu.uniandes.dse.outfits.entities.PrendaEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.services.PrendaComentarioService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import (PrendaComentarioService.class)
public class PrendaComentarioServiceTest {
    private PodamFactory factory = new PodamFactoryImpl();

    @Autowired
	private PrendaComentarioService prendaComentarioService;

	@Autowired
	private TestEntityManager entityManager;

    private List<PrendaEntity> prendaList = new ArrayList<>();
    private List<ComentarioEntity> comentarioList = new ArrayList<>();

    @BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

    private void clearData() {
		entityManager.getEntityManager().createQuery("delete from PrendaEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from ComentarioEntity").executeUpdate();
	}

	private void insertData() {
		for (int i = 0; i < 3; i++) {
			ComentarioEntity comentarioEntity = factory.manufacturePojo(ComentarioEntity.class);
			entityManager.persist(comentarioEntity);
			comentarioList.add(comentarioEntity);
		}
		for (int i = 0; i < 3; i++) {
			PrendaEntity prendaEntity = factory.manufacturePojo(PrendaEntity.class);
			entityManager.persist(prendaEntity);
			prendaList.add(prendaEntity);
			if (i == 0) {
				prendaEntity.getComentarios().add(comentarioList.get(i));
			}
			
		}
	}

	@Test
	void testAddComentario() throws EntityNotFoundException {
		PrendaEntity entity = prendaList.get(0);
		ComentarioEntity comentarioEntity = comentarioList.get(1);
		ComentarioEntity response = prendaComentarioService.addComentario(comentarioEntity.getId(), entity.getId());

		assertNotNull(response);
		assertEquals(comentarioEntity.getId(), response.getId());
	}

	@Test
	void testAddInvalidComentario() {
		assertThrows(EntityNotFoundException.class, () -> {
			PrendaEntity prendaEntity = prendaList.get(0);
			prendaComentarioService.addComentario(0L, prendaEntity.getId());
		});
	}

	@Test
	void testAddComentarioInvalidPrenda() {
		assertThrows(EntityNotFoundException.class, () -> {
			ComentarioEntity entity = comentarioList.get(1);
			prendaComentarioService.addComentario(entity.getId(), 0L);
		});
	}

	@Test
	void testGetComentarios() throws EntityNotFoundException {
		List<ComentarioEntity> list = prendaComentarioService.getComentarios(prendaList.get(0).getId());
		assertEquals(1, list.size());
		
	}

	@Test
	void testGetComentariosInvalidPrenda() {
		assertThrows(EntityNotFoundException.class, () -> {
			prendaComentarioService.getComentarios(0L);
		});
	}


	/**
	 * Prueba para obtener una instancia de Comentario asociada a una instancia Prenda.
	 * 
	 * @throws IllegalOperationException
	 * @throws EntityNotFoundException
	 *
	 * @throws co.edu.uniandes.csw.Comentario.exceptions.BusinessLogicException
	 */
	@Test
	void testGetComentario() throws EntityNotFoundException , IllegalOperationException{
		PrendaEntity entity = prendaList.get(0);
		ComentarioEntity comentarioEntity = comentarioList.get(0);
		ComentarioEntity response = prendaComentarioService.getComentario(entity.getId(), comentarioEntity.getId());

		assertEquals(comentarioEntity.getId(), response.getId());
		assertEquals(comentarioEntity.getCalificacion(), response.getCalificacion());
		assertEquals(comentarioEntity.getMensaje(), response.getMensaje());
		assertEquals(comentarioEntity.getTitulo(), response.getTitulo());
		assertEquals(comentarioEntity.getAutor(), response.getAutor());
	}
	

	@Test
	void testGetComentarioInvalidPrenda() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, () -> {
			ComentarioEntity comentarioEntity = comentarioList.get(0);
			prendaComentarioService.getComentario(0L, comentarioEntity.getId());
		});
	}

	@Test 
	void testGetComentarioInvalidComentario() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, () -> {
			PrendaEntity prendaEntity = prendaList.get(0);
			prendaComentarioService.getComentario(prendaEntity.getId(), 0L);
		});
	}


	@Test
	public void getComentarioNoAsociadoTest(){
		assertThrows(IllegalOperationException.class, ()->{
			PrendaEntity entity = prendaList.get(0);
			ComentarioEntity comentarioEntity = comentarioList.get(1);
			prendaComentarioService.getComentario(entity.getId(), comentarioEntity.getId());
		});
	}


	@Test
	public void testRemoveComentario() throws EntityNotFoundException {
		PrendaEntity entity =prendaList.get(0);
		prendaComentarioService.removeComentario(entity.getId(), comentarioList.get(0).getId());
		ComentarioEntity comentario = entityManager.find(ComentarioEntity.class, comentarioList.get(0).getId());
		assertNull(comentario.getPrenda());

	}
	
	@Test
	public void testRemoveInvalidComentario(){
		assertThrows(EntityNotFoundException.class, ()->{
			prendaComentarioService.removeComentario(prendaList.get(0).getId(), 0L);
		});
	}

	@Test
	void testRemoveComentarioInvalidPrenda() {
		assertThrows(EntityNotFoundException.class, () -> {
			prendaComentarioService.removeComentario(0L,comentarioList.get(0).getId());
		});
	}

}
