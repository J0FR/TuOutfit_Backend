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
import co.edu.uniandes.dse.outfits.entities.PrendaEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.services.OutfitPrendaService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(OutfitPrendaService.class)
public class OutfitPrendaServiceTest {
	@Autowired
	private OutfitPrendaService outfitPrendaService;
	
	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private OutfitEntity outfit = new OutfitEntity();
	private List<PrendaEntity> prendaList = new ArrayList<>();

	
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
		entityManager.getEntityManager().createQuery("delete from PrendaEntity").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		outfit = factory.manufacturePojo(OutfitEntity.class);
		entityManager.persist(outfit);

		for (int i = 0; i < 3; i++) {
			PrendaEntity entity = factory.manufacturePojo(PrendaEntity.class);
			entityManager.persist(entity);
			entity.getOutfits().add(outfit);
			prendaList.add(entity);
			outfit.getPrendas().add(entity);	
		}
	}

	/**
	 * Prueba para asociar un Prenda a un Outfit.
	 *
	 */
	@Test
	void testAddPrenda() throws EntityNotFoundException, IllegalOperationException {
		OutfitEntity newOutfit = factory.manufacturePojo(OutfitEntity.class);
		entityManager.persist(newOutfit);
		
		PrendaEntity prenda = factory.manufacturePojo(PrendaEntity.class);
		entityManager.persist(prenda);
		
		outfitPrendaService.addPrenda(newOutfit.getId(), prenda.getId());
		
		PrendaEntity lastPrenda = outfitPrendaService.getPrenda(newOutfit.getId(), prenda.getId());
		assertEquals(prenda.getId(), lastPrenda.getId());
		assertEquals(prenda.getImagen(), lastPrenda.getImagen());
		assertEquals(prenda.getNombre(), lastPrenda.getNombre());
		assertEquals(prenda.getGenero(), lastPrenda.getGenero());
	}
	
	/**
	 * Prueba para asociar un Prenda que no existe a un Outfit.
	 *
	 */
	@Test
	void testAddInvalidPrenda() {
		assertThrows(EntityNotFoundException.class, ()->{
			OutfitEntity newOutfit = factory.manufacturePojo(OutfitEntity.class);
			entityManager.persist(newOutfit);
			outfitPrendaService.addPrenda(newOutfit.getId(), 0L);
		});
	}
	
	/**
	 * Prueba para asociar un Prenda a un Outfit que no existe.
	 *
	 */
	@Test
	void testAddPrendaInvalidOutfit() throws EntityNotFoundException, IllegalOperationException {
		assertThrows(EntityNotFoundException.class, ()->{
			PrendaEntity prenda = factory.manufacturePojo(PrendaEntity.class);
			entityManager.persist(prenda);
			outfitPrendaService.addPrenda(0L, prenda.getId());
		});
	}

	/**
	 * Prueba para consultar la lista de Prendaes de un Outfit.
	 */
	@Test
	void testGetPrendas() throws EntityNotFoundException {
		List<PrendaEntity> prendaEntities = outfitPrendaService.getPrendas(outfit.getId());

		assertEquals(prendaList.size(), prendaEntities.size());

		for (int i = 0; i < prendaList.size(); i++) {
			assertTrue(prendaEntities.contains(prendaList.get(0)));
		}
	}
	
	/**
	 * Prueba para consultar la lista de Prendaes de un Outfit que no existe.
	 */
	@Test
	void testGetPrendasInvalidOutfit(){
		assertThrows(EntityNotFoundException.class, ()->{
			outfitPrendaService.getPrendas(0L);
		});
	}

	/**
	 * Prueba para consultar un Prenda de un Outfit.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetPrenda() throws EntityNotFoundException, IllegalOperationException {
		PrendaEntity prendaEntity = prendaList.get(0);
		PrendaEntity prenda = outfitPrendaService.getPrenda(outfit.getId(), prendaEntity.getId());
		assertNotNull(prenda);

		assertEquals(prendaEntity.getId(), prenda.getId());
		assertEquals(prendaEntity.getImagen(), prenda.getImagen());
		assertEquals(prendaEntity.getNombre(), prenda.getNombre());
		assertEquals(prendaEntity.getGenero(), prenda.getGenero());
	}
	
	/**
	 * Prueba para consultar un Prenda que no existe de un Outfit.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetInvalidPrenda()  {
		assertThrows(EntityNotFoundException.class, ()->{
			outfitPrendaService.getPrenda(outfit.getId(), 0L);
		});
	}
	
	/**
	 * Prueba para consultar un Prenda de un Outfit que no existe.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetPrendaInvalidOutfit() {
		assertThrows(EntityNotFoundException.class, ()->{
			PrendaEntity prendaEntity = prendaList.get(0);
			outfitPrendaService.getPrenda(0L, prendaEntity.getId());
		});
	}
	
	/**
	 * Prueba para obtener un Prenda no asociado a un Outfit.
	 *
	 */
	@Test
	void testGetNotAssociatedPrenda() {
		assertThrows(IllegalOperationException.class, ()->{
			OutfitEntity newOutfit = factory.manufacturePojo(OutfitEntity.class);
			entityManager.persist(newOutfit);
			PrendaEntity prenda = factory.manufacturePojo(PrendaEntity.class);
			entityManager.persist(prenda);
			outfitPrendaService.getPrenda(newOutfit.getId(), prenda.getId());
		});
	}

	/**
	 * Prueba para actualizar los Prendas de un Outfit.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplacePrendas() throws EntityNotFoundException {
		List<PrendaEntity> nuevaLista = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			PrendaEntity entity = factory.manufacturePojo(PrendaEntity.class);
			entityManager.persist(entity);
			outfit.getPrendas().add(entity);
			nuevaLista.add(entity);
		}
		outfitPrendaService.replacePrendas(outfit.getId(), nuevaLista);
		
		List<PrendaEntity> authorEntities = outfitPrendaService.getPrendas(outfit.getId());
		for (PrendaEntity aNuevaLista : nuevaLista) {
			assertTrue(authorEntities.contains(aNuevaLista));
		}
	}
	
	/**
	 * Prueba para actualizar los Prendas de un Outfit.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplacePrendas2() throws EntityNotFoundException {
		List<PrendaEntity> nuevaLista = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			PrendaEntity entity = factory.manufacturePojo(PrendaEntity.class);
			entityManager.persist(entity);
			nuevaLista.add(entity);
		}
		outfitPrendaService.replacePrendas(outfit.getId(), nuevaLista);
		
		List<PrendaEntity> authorEntities = outfitPrendaService.getPrendas(outfit.getId());
		for (PrendaEntity aNuevaLista : nuevaLista) {
			assertTrue(authorEntities.contains(aNuevaLista));
		}
	}
	
	
	/**
	 * Prueba para actualizar los Prendas de un Outfit que no existe.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplacePrendasInvalidOutfit(){
		assertThrows(EntityNotFoundException.class, ()->{
			List<PrendaEntity> nuevaLista = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				PrendaEntity entity = factory.manufacturePojo(PrendaEntity.class);
				entity.getOutfits().add(outfit);		
				entityManager.persist(entity);
				nuevaLista.add(entity);
			}
			outfitPrendaService.replacePrendas(0L, nuevaLista);
		});
	}
	
	/**
	 * Prueba para actualizar los Prendaes que no existen de un Outfit.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceInvalidPrendas() {
		assertThrows(EntityNotFoundException.class, ()->{
			List<PrendaEntity> nuevaLista = new ArrayList<>();
			PrendaEntity entity = factory.manufacturePojo(PrendaEntity.class);
			entity.setId(0L);
			nuevaLista.add(entity);
			outfitPrendaService.replacePrendas(outfit.getId(), nuevaLista);
		});
	}
	
	/**
	 * Prueba para actualizar un Prenda de un Outfit que no existe.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplacePrendasInvalidPrenda(){
		assertThrows(EntityNotFoundException.class, ()->{
			List<PrendaEntity> nuevaLista = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				PrendaEntity entity = factory.manufacturePojo(PrendaEntity.class);
				entity.getOutfits().add(outfit);		
				entityManager.persist(entity);
				nuevaLista.add(entity);
			}
			outfitPrendaService.replacePrendas(0L, nuevaLista);
		});
	}

	/**
	 * Prueba desasociar un Prenda con un Outfit.
	 *
	 */
	@Test
	void testRemovePrenda() throws EntityNotFoundException {
		for (PrendaEntity author : prendaList) {
			outfitPrendaService.removePrenda(outfit.getId(), author.getId());
		}
		assertTrue(outfitPrendaService.getPrendas(outfit.getId()).isEmpty());
	}
	
	/**
	 * Prueba desasociar un Prenda que no existe con un Outfit.
	 *
	 */
	@Test
	void testRemoveInvalidPrenda(){
		assertThrows(EntityNotFoundException.class, ()->{
			outfitPrendaService.removePrenda(outfit.getId(), 0L);
		});
	}
	
	/**
	 * Prueba desasociar un Prenda con un Outfit que no existe.
	 *
	 */
	@Test
	void testRemoveAuthorInvalidOutfit(){
		assertThrows(EntityNotFoundException.class, ()->{
			outfitPrendaService.removePrenda(0L, prendaList.get(0).getId());
		});
	}
}











