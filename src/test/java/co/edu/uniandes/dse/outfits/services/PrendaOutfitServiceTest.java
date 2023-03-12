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
import co.edu.uniandes.dse.outfits.services.PrendaOutfitService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(PrendaOutfitService.class)
public class PrendaOutfitServiceTest {
	@Autowired
	private PrendaOutfitService prendaOutfitService;
	
	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private PrendaEntity prenda = new PrendaEntity();
	private List<OutfitEntity> outfitList = new ArrayList<>();

	
	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}
	
	/**
	 * Limpia las tablas que están implicadas en la prueba.
	 */
	private void clearData() {
        entityManager.getEntityManager().createQuery("delete from PrendaEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from OutfitEntity").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		prenda = factory.manufacturePojo(PrendaEntity.class);
		entityManager.persist(prenda);

		for (int i = 0; i < 3; i++) {
			OutfitEntity entity = factory.manufacturePojo(OutfitEntity.class);
			entityManager.persist(entity);
			entity.getPrendas().add(prenda);
			outfitList.add(entity);
			prenda.getOutfits().add(entity);	
		}
	}

	/**
	 * Prueba para asociar un Outfit a una Prenda.
	 *
	 */
	@Test
	void testAddOutfit() throws EntityNotFoundException, IllegalOperationException {
		PrendaEntity newPrenda = factory.manufacturePojo(PrendaEntity.class);
		entityManager.persist(newPrenda);
		
		OutfitEntity outfit = factory.manufacturePojo(OutfitEntity.class);
		entityManager.persist(outfit);
		
		prendaOutfitService.addOutfit(outfit.getId(),newPrenda.getId());
		
		OutfitEntity lastOutfit = prendaOutfitService.getOutfit(newPrenda.getId(),outfit.getId());
		assertEquals(outfit.getId(), lastOutfit.getId());
		assertEquals(outfit.getNombre(), lastOutfit.getNombre());
		assertEquals(outfit.getImagen(), lastOutfit.getImagen());
		assertEquals(outfit.getGenero(), lastOutfit.getGenero());
	}
	
	/**
	 * Prueba para asociar un Outfit que no existe a una prenda.
	 *
	 */
	@Test
	void testAddInvalidOutfit() {
		assertThrows(EntityNotFoundException.class, ()->{
			PrendaEntity newPrenda = factory.manufacturePojo(PrendaEntity.class);
			entityManager.persist(newPrenda);
			prendaOutfitService.addOutfit(0L, newPrenda.getId());
		});
	}
	
	/**
	 * Prueba para asociar un Outfit a un Prenda que no existe.
	 *
	 */
	@Test
	void testAddOutfitInvalidPrenda() throws EntityNotFoundException, IllegalOperationException {
		assertThrows(EntityNotFoundException.class, ()->{
			OutfitEntity outfit = factory.manufacturePojo(OutfitEntity.class);
			entityManager.persist(outfit);
			prendaOutfitService.addOutfit(outfit.getId(), 0L);
		});
	}

	/**
	 * Prueba para consultar la lista de Prendaes de un Outfit.
	 */
	@Test
	void testGetOutfits() throws EntityNotFoundException {
		List<OutfitEntity> outfitEntities = prendaOutfitService.getOutfits(prenda.getId());

		assertEquals(outfitList.size(), outfitEntities.size());

		for (int i = 0; i < outfitList.size(); i++) {
			assertTrue(outfitEntities.contains(outfitList.get(0)));
		}
	}
	
	/**
	 * Prueba para consultar la lista de Outfits de una Prenda que no existe.
	 */
	@Test
	void testGetOutfitsInvalidPrenda(){
		assertThrows(EntityNotFoundException.class, ()->{
			prendaOutfitService.getOutfits(0L);
		});
	}

	/**
	 * Prueba para consultar un Outfit de una Prenda.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetOutfit() throws EntityNotFoundException, IllegalOperationException {
		OutfitEntity outfitEntity = outfitList.get(0);
        OutfitEntity outfit = prendaOutfitService.getOutfit(prenda.getId(), outfitEntity.getId());
		assertNotNull(outfit);

		assertEquals(outfitEntity.getId(), outfit.getId());
		assertEquals(outfitEntity.getNombre(), outfit.getNombre());
		assertEquals(outfitEntity.getImagen(), outfit.getImagen());
		assertEquals(outfitEntity.getGenero(), outfit.getGenero());
	}
	
	/**
	 * Prueba para consultar un Outfit que no existe de un Prenda.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetInvalidOutfit()  {
		assertThrows(EntityNotFoundException.class, ()->{
            prendaOutfitService.getOutfit(prenda.getId(), 0L);
		});
	}
	
	/**
	 * Prueba para consultar un Outfit de un Prenda que no existe.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetOutfitInvalidPrenda() {
		assertThrows(EntityNotFoundException.class, ()->{
			OutfitEntity outfitEntity = outfitList.get(0);
            prendaOutfitService.getOutfit(0L, outfitEntity.getId());
		});
	}
	
	/**
	 * Prueba para obtener un Outfit no asociado a un Prenda.
	 *
	 */
	@Test
	void testGetNotAssociatedOutfit() {
		assertThrows(IllegalOperationException.class, ()->{
			PrendaEntity newPrenda = factory.manufacturePojo(PrendaEntity.class);
			entityManager.persist(newPrenda);
			OutfitEntity outfit = factory.manufacturePojo(OutfitEntity.class);
			entityManager.persist(outfit);
            prendaOutfitService.getOutfit(newPrenda.getId(), outfit.getId());
		});
	}

	/**
	 * Prueba para actualizar los Outfits de una Prenda.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceOutfits() throws EntityNotFoundException {
		List<OutfitEntity> nuevaLista = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			OutfitEntity entity = factory.manufacturePojo(OutfitEntity.class);
			entityManager.persist(entity);
			prenda.getOutfits().add(entity);
			nuevaLista.add(entity);
		}
        prendaOutfitService.replaceOutfits(prenda.getId(), nuevaLista);
		
		List<OutfitEntity> outfitEntities = prendaOutfitService.getOutfits(prenda.getId());
		for (OutfitEntity aNuevaLista : nuevaLista) {
			assertTrue(outfitEntities.contains(aNuevaLista));
		}
	}
	
	/**
	 * Prueba para actualizar los Outfits de un Prenda.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceOutfits2() throws EntityNotFoundException {
		List<OutfitEntity> nuevaLista = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			OutfitEntity entity = factory.manufacturePojo(OutfitEntity.class);
			entityManager.persist(entity);
			nuevaLista.add(entity);
		}
        prendaOutfitService.replaceOutfits(prenda.getId(), nuevaLista);
		
		List<OutfitEntity> authorEntities = prendaOutfitService.getOutfits(prenda.getId());
		for (OutfitEntity aNuevaLista : nuevaLista) {
			assertTrue(authorEntities.contains(aNuevaLista));
		}
	}
	
	
	/**
	 * Prueba para actualizar los Outfits de un Prenda que no existe.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceOutfitsInvalidPrenda(){
		assertThrows(EntityNotFoundException.class, ()->{
			List<OutfitEntity> nuevaLista = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				OutfitEntity entity = factory.manufacturePojo(OutfitEntity.class);
                entity.getPrendas().add(prenda);		
				entityManager.persist(entity);
				nuevaLista.add(entity);
			}
            prendaOutfitService.replaceOutfits(0L, nuevaLista);
		});
	}
	
	/**
	 * Prueba para actualizar los Outfits que no existen de una Prenda.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceInvalidOutfits() {
		assertThrows(EntityNotFoundException.class, ()->{
			List<OutfitEntity> nuevaLista = new ArrayList<>();
			OutfitEntity entity = factory.manufacturePojo(OutfitEntity.class);
			entity.setId(0L);
			nuevaLista.add(entity);
            prendaOutfitService.replaceOutfits(prenda.getId(), nuevaLista);
		});
	}
	
	/**
	 * Prueba para actualizar un Outfit de una Prenda que no existe.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceOutfitsInvalidOutfit(){
		assertThrows(EntityNotFoundException.class, ()->{
			List<OutfitEntity> nuevaLista = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				OutfitEntity entity = factory.manufacturePojo(OutfitEntity.class);
                entity.getPrendas().add(prenda);		
				entityManager.persist(entity);
				nuevaLista.add(entity);
			}
            prendaOutfitService.replaceOutfits(0L, nuevaLista);
		});
	}

	/**
	 * Prueba desasociar un Outfit con un Prenda.
	 *
	 */
	@Test
	void testRemoveOutfit() throws EntityNotFoundException {
		for (OutfitEntity author : outfitList) {
            prendaOutfitService.removeOutfit(prenda.getId(), author.getId());
		}
		assertTrue(prendaOutfitService.getOutfits(prenda.getId()).isEmpty());
	}
	
	/**
	 * Prueba desasociar un Outfit que no existe con un Prenda.
	 *
	 */
	@Test
	void testRemoveInvalidOutfit(){
		assertThrows(EntityNotFoundException.class, ()->{
            prendaOutfitService.removeOutfit(prenda.getId(),0L);
		});
	}
	
	/**
	 * Prueba desasociar un Outfit con una Prenda que no existe.
	 *
	 */
	@Test
	void testRemoveAuthorInvalidPrenda(){
		assertThrows(EntityNotFoundException.class, ()->{
            prendaOutfitService.removeOutfit(0L, outfitList.get(0).getId());
		});
	}
}
