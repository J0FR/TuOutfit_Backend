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
import co.edu.uniandes.dse.outfits.entities.MarcaEntity;
import co.edu.uniandes.dse.outfits.entities.PrendaEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(MarcaPrendaService.class)
public class MarcaPrendaServiceTest {
	private PodamFactory factory = new PodamFactoryImpl();

	@Autowired
	private MarcaPrendaService marcaPrendaService;

	@Autowired
	private TestEntityManager entityManager;

	private List<MarcaEntity> marcaList = new ArrayList<>();
	private List<PrendaEntity> prendasList = new ArrayList<>();

	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from PrendaEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from OutfitEntity").executeUpdate();
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
			MarcaEntity marcaEntity = factory.manufacturePojo(MarcaEntity.class);
			entityManager.persist(marcaEntity);
			if (i == 0) {
				marcaEntity.setPrendas(prendasList);
			}
			marcaList.add(marcaEntity);
		}
	}

	/**
	 * Prueba para asociar una prenda existente a un comentario.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddPrenda() throws EntityNotFoundException {
		MarcaEntity entity = marcaList.get(0);
		PrendaEntity prendaEntity = prendasList.get(1);
		PrendaEntity response = marcaPrendaService.addPrenda(entity.getId(), prendaEntity.getId());

		assertNotNull(response);
		assertEquals(prendaEntity.getId(), response.getId());
	}

	/**
	 * Prueba para asociar un marca existente a un prenda que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddInvalidPrenda() {
		assertThrows(EntityNotFoundException.class, () -> {
			MarcaEntity marcaEntity = marcaList.get(1);
			marcaPrendaService.addPrenda(0L, marcaEntity.getId());
		});
	}

	/**
	 * Prueba para asociar un Marca que no existe a un prenda.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddPrendaInvalidMarca() {
		assertThrows(EntityNotFoundException.class, () -> {
			PrendaEntity entity = prendasList.get(0);
			marcaPrendaService.addPrenda(entity.getId(), 0L);
		});
	}

	/**
	 * Prueba para consultar prendas de una marca.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testGetPrendas() throws EntityNotFoundException {
		MarcaEntity entity = marcaList.get(0);
		List<PrendaEntity> resultEntity = marcaPrendaService.getPrendas(entity.getId());
		assertNotNull(resultEntity);
		assertEquals(entity.getPrendas(), resultEntity);
	}

	/**
	 * Prueba para consultar una prenda de una marca que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testGetPrendaInvalidMarca() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, () -> {
			marcaPrendaService.getPrendas(0L);
		});
	}

	@Test
	void testGetPrenda() throws EntityNotFoundException, IllegalOperationException {
		MarcaEntity entity = marcaList.get(0);
		PrendaEntity prendaEntity = prendasList.get(0);
		PrendaEntity response = marcaPrendaService.getPrenda(entity.getId(), prendaEntity.getId());

		assertEquals(prendaEntity.getId(), response.getId());
		assertEquals(prendaEntity.getColores(), response.getColores());
		assertEquals(prendaEntity.getComentarios(), response.getComentarios());
		assertEquals(prendaEntity.getGenero(), response.getGenero());
		assertEquals(prendaEntity.getId(), response.getId());
		assertEquals(prendaEntity.getImagen(), response.getImagen());
		assertEquals(prendaEntity.getMarca(), response.getMarca());
		assertEquals(prendaEntity.getNombre(), response.getNombre());
		assertEquals(prendaEntity.getOcasiones(), response.getOcasiones());
		assertEquals(prendaEntity.getOutfits(), response.getOutfits());
		assertEquals(prendaEntity.getPrecio(), response.getPrecio());
		assertEquals(prendaEntity.getRango_edad(), response.getRango_edad());
		assertEquals(prendaEntity.getTalla(), response.getTalla());
		assertEquals(prendaEntity.getUrl_sitio_web_compra(), response.getUrl_sitio_web_compra());

	}

	/**
	 * Prueba para obtener una instancia de prenda que no existe asociada a una
	 * marca
	 * 
	 * @throws EntityNotFoundException
	 * 
	 */
	@Test
	void testGetInvalidMarca() {
		assertThrows(EntityNotFoundException.class, () -> {
			PrendaEntity entity = prendasList.get(0);
			marcaPrendaService.getPrenda(entity.getId(), 0L);
		});
	}

	@Test
	public void getPrendaNoAsociadoTest() {
		assertThrows(IllegalOperationException.class, () -> {
			MarcaEntity entity = marcaList.get(1);
			PrendaEntity prendaEntity = prendasList.get(1);
			marcaPrendaService.getPrenda(entity.getId(), prendaEntity.getId());
		});
	}

	@Test
	void testCeroPrendaMarca() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, () -> {
			marcaPrendaService.getPrendas(marcaList.get(1).getId());
		});
	}

	/**
	 * Prueba para actualizar los Prendas de un Outfit.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testUpdatePrendas() throws EntityNotFoundException {
		List<PrendaEntity> nuevaLista = new ArrayList<>();
		MarcaEntity marca = marcaList.get(0);
		for (int i = 0; i < 3; i++) {
			PrendaEntity entity = factory.manufacturePojo(PrendaEntity.class);
			entityManager.persist(entity);
			marca.getPrendas().add(entity);
			nuevaLista.add(entity);
		}
		marcaPrendaService.updatePrendas(marca.getId(), nuevaLista);

		List<PrendaEntity> prendasEntities = marcaPrendaService.getPrendas(marca.getId());
		for (PrendaEntity aNuevaLista : nuevaLista) {
			assertTrue(prendasEntities.contains(aNuevaLista));
		}
	}

	/**
	 * Prueba para actualizar los Prendas de un Outfit.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testUpdatePrendas2() throws EntityNotFoundException {
		List<PrendaEntity> nuevaLista = new ArrayList<>();
		MarcaEntity marca = marcaList.get(0);
		for (int i = 0; i < 3; i++) {
			PrendaEntity entity = factory.manufacturePojo(PrendaEntity.class);
			entityManager.persist(entity);
			nuevaLista.add(entity);
		}
		marcaPrendaService.updatePrendas(marca.getId(), nuevaLista);

		List<PrendaEntity> prendasEntities = marcaPrendaService.getPrendas(marca.getId());
		for (PrendaEntity aNuevaLista : nuevaLista) {
			assertTrue(prendasEntities.contains(aNuevaLista));
		}
	}

	/**
	 * Prueba para actualizar los Prendas de un Outfit que no existe.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplacePrendasInvalidOutfit() {
		assertThrows(EntityNotFoundException.class, () -> {
			List<PrendaEntity> nuevaLista = new ArrayList<>();
			MarcaEntity marca = marcaList.get(0);
			for (int i = 0; i < 3; i++) {
				PrendaEntity entity = factory.manufacturePojo(PrendaEntity.class);
				entity.setMarca(marca);
				entityManager.persist(entity);
				nuevaLista.add(entity);
			}
			marcaPrendaService.updatePrendas(0L, nuevaLista);
		});
	}

	/**
	 * Prueba para actualizar los Prendaes que no existen de un Outfit.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceInvalidPrendas() {
		assertThrows(EntityNotFoundException.class, () -> {
			List<PrendaEntity> nuevaLista = new ArrayList<>();
			MarcaEntity marca = marcaList.get(0);
			PrendaEntity entity = factory.manufacturePojo(PrendaEntity.class);
			entity.setId(0L);
			nuevaLista.add(entity);
			marcaPrendaService.updatePrendas(marca.getId(), nuevaLista);
		});
	}

	/**
	 * Prueba para desasociar un marca existente de una prenda existente.
	 * 
	 * @throws EntityNotFoundException
	 *
	 * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
	 */
	@Test
	public void testRemovePrenda() throws EntityNotFoundException {
		marcaPrendaService.removePrenda(marcaList.get(0).getId(), prendasList.get(0).getId());
		MarcaEntity marca = entityManager.find(MarcaEntity.class, marcaList.get(0).getId());
		assertEquals(prendasList.get(0).getId(), marca.getPrendas().get(0).getId());
	}

	/**
	 * Prueba para desasociar un prenda inexistente de una marca existente
	 * 
	 * @throws EntityNotFoundException
	 *
	 * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
	 */
	@Test
	public void testRemoveInvalidPrenda() {
		assertThrows(EntityNotFoundException.class, () -> {
			marcaPrendaService.removePrenda(marcaList.get(0).getId(), 0L);
		});
	}

	/**
	 * Prueba para desasociar una prenda existente de una marca inexistente
	 */
	@Test
	void testRemovePrendaInvalidMarca() {
		assertThrows(EntityNotFoundException.class, () -> {
			marcaPrendaService.removePrenda(0L, prendasList.get(0).getId());
		});
	}

	/**
	 * Prueba para no existe prenda dentro de la lista
	 */
	@Test
	void testRemoveComentarioInvalidPrendaListMarca() {
		assertThrows(EntityNotFoundException.class, () -> {
			marcaPrendaService.removePrenda(marcaList.get(1).getId(), prendasList.get(0).getId());
		});
	}
}
