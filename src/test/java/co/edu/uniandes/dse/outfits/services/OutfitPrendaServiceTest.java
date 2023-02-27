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
import co.edu.uniandes.dse.outfits.entities.OutfitEntity;
import co.edu.uniandes.dse.outfits.entities.PrendaEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.services.OutfitPrendaService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(OutfitPrendaService.class)
public class OutfitPrendaServiceTest {
    private PodamFactory factory = new PodamFactoryImpl();

	@Autowired
	private OutfitPrendaService outfitPrendaService;

	@Autowired
	private TestEntityManager entityManager;

    private List<OutfitEntity> outfitsList = new ArrayList<>();
    private List<PrendaEntity> prendaList = new ArrayList<>();

    @BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

    private void clearData() {
		entityManager.getEntityManager().createQuery("delete from PrendaEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from OutfitEntity").executeUpdate();
	}

	private void insertData() {
		for (int i = 0; i < 3; i++) {
			PrendaEntity prendaEntity = factory.manufacturePojo(PrendaEntity.class);
			entityManager.persist(prendaEntity);
			prendaList.add(prendaEntity);
		}
		for (int i = 0; i < 3; i++) {
			OutfitEntity outfitEntity = factory.manufacturePojo(OutfitEntity.class);
			entityManager.persist(outfitEntity);
			if (i == 0) {
				outfitEntity.setPrendas(prendaList);
			}
			outfitsList.add(outfitEntity);
		}
	}

	@Test
	void testAddComentario() throws EntityNotFoundException {
		OutfitEntity entity = outfitsList.get(0);
		PrendaEntity comentarioEntity = prendaList.get(1);
		PrendaEntity response = outfitPrendaService.addPrenda(entity.getId(), comentarioEntity.getId());

		assertNotNull(response);
		assertEquals(comentarioEntity.getId(), response.getId());
	}

	@Test
	void testAddInvalidComentario() {
		assertThrows(EntityNotFoundException.class, () -> {
			OutfitEntity outfitEntity = outfitsList.get(1);
			outfitPrendaService.addPrenda(0L, outfitEntity.getId());
		});
	}

	@Test
	void testAddComentarioInvalidOutfit() {
		assertThrows(EntityNotFoundException.class, () -> {
			PrendaEntity entity = prendaList.get(0);
			outfitPrendaService.addPrenda(entity.getId(), 0L);
		});
	}

	@Test
	void testGetComentario() throws EntityNotFoundException {
		OutfitEntity entity = outfitsList.get(0);
		List<PrendaEntity> resultEntity = outfitPrendaService.getPrendas(entity.getId());
		assertNotNull(resultEntity);
		assertEquals(entity.getPrendas(), resultEntity);
	}

	@Test
	void testGetComentarioInvalidOutfit() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, () -> {
			outfitPrendaService.getPrendas(0L);
		});
	}

	@Test
	void testCeroComentarioOutfit() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, () -> {
			outfitPrendaService.getPrendas(outfitsList.get(2).getId());
		});
	}

	@Test
	public void testRemoveComentario() throws EntityNotFoundException {
		outfitPrendaService.removePrenda(outfitsList.get(0).getId(), prendaList.get(0).getId());
		OutfitEntity outfit = entityManager.find(OutfitEntity.class, outfitsList.get(0).getId());
		assertEquals(prendaList.get(0).getId(), outfit.getPrendas().get(0).getId());
	}
	
	@Test
	public void testRemoveInvalidComentario(){
		assertThrows(EntityNotFoundException.class, ()->{
			outfitPrendaService.removePrenda(outfitsList.get(0).getId(), 0L);
		});
	}

	@Test
	void testRemoveComentarioInvalidOutfit() {
		assertThrows(EntityNotFoundException.class, () -> {
			outfitPrendaService.removePrenda(0L, prendaList.get(0).getId());
		});
	}

	@Test
	void testRemoveComentarioInvalidCommentListOutfit() {
		assertThrows(EntityNotFoundException.class, () -> {
			outfitPrendaService.removePrenda(outfitsList.get(1).getId(), prendaList.get(0).getId());
		});
	}
}











