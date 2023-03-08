package co.edu.uniandes.dse.outfits.services;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import co.edu.uniandes.dse.outfits.entities.OutfitEntity;
import co.edu.uniandes.dse.outfits.entities.PrendaEntity;
import co.edu.uniandes.dse.outfits.entities.UsuarioEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
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
			if (i == 0) {
				prendaEntity.setComentarios(comentarioList);
			}
			prendaList.add(prendaEntity);
		}
	}

	@Test
	void testAddComentario() throws EntityNotFoundException {
		PrendaEntity entity = prendaList.get(0);
		ComentarioEntity comentarioEntity = comentarioList.get(0);
		ComentarioEntity response = prendaComentarioService.addComentario(entity.getId(), comentarioEntity.getId());

		assertNotNull(response);
		assertEquals(comentarioEntity.getId(), response.getId());
	}

	@Test
	void testAddInvalidComentario() {
		assertThrows(EntityNotFoundException.class, () -> {
			PrendaEntity prendaEntity = prendaList.get(1);
			prendaComentarioService.addComentario(0L, prendaEntity.getId());
		});
	}

	@Test
	void testAddComentarioInvalidComentario() {
		assertThrows(EntityNotFoundException.class, () -> {
			ComentarioEntity entity = comentarioList.get(0);
			prendaComentarioService.addComentario(entity.getId(), 0L);
		});
	}

	@Test
	void testGetComentario() throws EntityNotFoundException {
		PrendaEntity entity = prendaList.get(0);
		List<ComentarioEntity> resultEntity = prendaComentarioService.getComentarios(entity.getId());
		assertNotNull(resultEntity);
		assertEquals(entity.getComentarios(), resultEntity);
	}

	@Test
	void testGetComentarioInvalidOutfit() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, () -> {
			prendaComentarioService.getComentarios(0L);
		});
	}

	@Test
	void testCeroComentarioOutfit() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, () -> {
			prendaComentarioService.getComentarios(prendaList.get(2).getId());
		});
	}

	@Test
	public void testRemoveComentario() throws EntityNotFoundException {
		prendaComentarioService.removeComentario(prendaList.get(0).getId(), comentarioList.get(0).getId());
		PrendaEntity prenda = entityManager.find(PrendaEntity.class, prendaList.get(0).getId());
		assertEquals(comentarioList.get(0).getId(), prenda.getComentarios().get(0).getId());
	}
	
	@Test
	public void testRemoveInvalidComentario(){
		assertThrows(EntityNotFoundException.class, ()->{
			prendaComentarioService.removeComentario(prendaList.get(0).getId(), 0L);
		});
	}

	@Test
	void testRemoveComentarioInvalidOutfit() {
		assertThrows(EntityNotFoundException.class, () -> {
			prendaComentarioService.removeComentario(0L,comentarioList.get(0).getId());
		});
	}

	@Test
	void testRemoveComentarioInvalidCommentListOutfit() {
		assertThrows(EntityNotFoundException.class, () -> {
			prendaComentarioService.removeComentario(prendaList.get(1).getId(), comentarioList.get(0).getId());
		});
	}
}
