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
import co.edu.uniandes.dse.outfits.entities.UsuarioEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(OutfitUsuarioService.class)
public class OutfitUsuarioServiceTest {
	@Autowired
	private OutfitUsuarioService outfitUsuarioService;
	
	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private OutfitEntity outfit = new OutfitEntity();
	private List<UsuarioEntity> usuarioList = new ArrayList<>();

	
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
		entityManager.getEntityManager().createQuery("delete from UsuarioEntity").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		outfit = factory.manufacturePojo(OutfitEntity.class);
		entityManager.persist(outfit);

		for (int i = 0; i < 3; i++) {
			UsuarioEntity entity = factory.manufacturePojo(UsuarioEntity.class);
			entityManager.persist(entity);
			entity.getFavoritos().add(outfit);
			usuarioList.add(entity);
			outfit.getUsuarios().add(entity);	
		}
	}

	/**
	 * Prueba para asociar un Comentario a un Outfit.
	 *
	 */
	@Test
	void testAddUsuario() throws EntityNotFoundException, IllegalOperationException {
		OutfitEntity newOutfit = factory.manufacturePojo(OutfitEntity.class);
		entityManager.persist(newOutfit);
		
		UsuarioEntity usuario = factory.manufacturePojo(UsuarioEntity.class);
		entityManager.persist(usuario);
		
		outfitUsuarioService.addUsuario(newOutfit.getId(), usuario.getId());
		
		UsuarioEntity lastUsuario = outfitUsuarioService.getUsuario(newOutfit.getId(), usuario.getId());
		assertEquals(usuario.getId(), lastUsuario.getId());
		assertEquals(usuario.getNombre(), lastUsuario.getNombre());
		assertEquals(usuario.getEdad(), lastUsuario.getEdad());
		assertEquals(usuario.getEmail(), lastUsuario.getEmail());
		assertEquals(usuario.getGenero(), lastUsuario.getGenero());
	}
	
	/**
	 * Prueba para asociar un Comentario que no existe a un Outfit.
	 *
	 */
	@Test
	void testAddInvalidUsuario() {
		assertThrows(EntityNotFoundException.class, ()->{
			OutfitEntity newOutfit = factory.manufacturePojo(OutfitEntity.class);
			entityManager.persist(newOutfit);
			outfitUsuarioService.addUsuario(newOutfit.getId(), 0L);
		});
	}
	
	/**
	 * Prueba para asociar un Comentario a un Outfit que no existe.
	 *
	 */
	@Test
	void testAddUsuarioInvalidOutfit() throws EntityNotFoundException, IllegalOperationException {
		assertThrows(EntityNotFoundException.class, ()->{
			UsuarioEntity usuario = factory.manufacturePojo(UsuarioEntity.class);
			entityManager.persist(usuario);
			outfitUsuarioService.addUsuario(0L, usuario.getId());
		});
	}

	/**
	 * Prueba para consultar la lista de Comentarioes de un Outfit.
	 */
	@Test
	void testGetUsuarios() throws EntityNotFoundException {
		List<UsuarioEntity> usuarioEntities = outfitUsuarioService.getUsuarios(outfit.getId());

		assertEquals(usuarioList.size(), usuarioEntities.size());

		for (int i = 0; i < usuarioList.size(); i++) {
			assertTrue(usuarioEntities.contains(usuarioList.get(0)));
		}
	}
	
	/**
	 * Prueba para consultar la lista de Comentarioes de un Outfit que no existe.
	 */
	@Test
	void testGetUsuariosInvalidOutfit(){
		assertThrows(EntityNotFoundException.class, ()->{
			outfitUsuarioService.getUsuarios(0L);
		});
	}

	/**
	 * Prueba para consultar un Comentario de un Outfit.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetUsuario() throws EntityNotFoundException, IllegalOperationException {
		UsuarioEntity usuarioEntity = usuarioList.get(0);
		UsuarioEntity usuario = outfitUsuarioService.getUsuario(outfit.getId(), usuarioEntity.getId());
		assertNotNull(usuario);

		assertEquals(usuarioEntity.getId(), usuario.getId());
		assertEquals(usuarioEntity.getNombre(), usuario.getNombre());
		assertEquals(usuarioEntity.getEdad(), usuario.getEdad());
		assertEquals(usuarioEntity.getEmail(), usuario.getEmail());
		assertEquals(usuarioEntity.getGenero(), usuario.getGenero());
	}
	
	/**
	 * Prueba para consultar un Comentario que no existe de un Outfit.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetInvalidUsuario()  {
		assertThrows(EntityNotFoundException.class, ()->{
			outfitUsuarioService.getUsuario(outfit.getId(), 0L);
		});
	}
	
	/**
	 * Prueba para consultar un Comentario de un Outfit que no existe.
	 *
	 * @throws throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testGetUsuarioInvalidOutfit() {
		assertThrows(EntityNotFoundException.class, ()->{
			UsuarioEntity usuarioEntity = usuarioList.get(0);
			outfitUsuarioService.getUsuario(0L, usuarioEntity.getId());
		});
	}
	
	/**
	 * Prueba para obtener un Comentario no asociado a un Outfit.
	 *
	 */
	@Test
	void testGetNotAssociatedUsuario() {
		assertThrows(IllegalOperationException.class, ()->{
			OutfitEntity newOutfit = factory.manufacturePojo(OutfitEntity.class);
			entityManager.persist(newOutfit);
			UsuarioEntity usuario = factory.manufacturePojo(UsuarioEntity.class);
			entityManager.persist(usuario);
			outfitUsuarioService.getUsuario(newOutfit.getId(), usuario.getId());
		});
	}

	/**
	 * Prueba para actualizar los Comentarios de un Outfit.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceUsuarios() throws EntityNotFoundException {
		List<UsuarioEntity> nuevaLista = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			UsuarioEntity entity = factory.manufacturePojo(UsuarioEntity.class);
			entityManager.persist(entity);
			outfit.getUsuarios().add(entity);
			nuevaLista.add(entity);
		}
		outfitUsuarioService.replaceUsuarios(outfit.getId(), nuevaLista);
		
		List<UsuarioEntity> UsuarioEntities = outfitUsuarioService.getUsuarios(outfit.getId());
		for (UsuarioEntity aNuevaLista : nuevaLista) {
			assertTrue(UsuarioEntities.contains(aNuevaLista));
		}
	}
	
	/**
	 * Prueba para actualizar los Comentarios de un Outfit.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceUsuarios2() throws EntityNotFoundException {
		List<UsuarioEntity> nuevaLista = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			UsuarioEntity entity = factory.manufacturePojo(UsuarioEntity.class);
			entityManager.persist(entity);
			nuevaLista.add(entity);
		}
		outfitUsuarioService.replaceUsuarios(outfit.getId(), nuevaLista);
		
		List<UsuarioEntity> usuarioEntities = outfitUsuarioService.getUsuarios(outfit.getId());
		for (UsuarioEntity aNuevaLista : nuevaLista) {
			assertTrue(usuarioEntities.contains(aNuevaLista));
		}
	}
	
	
	/**
	 * Prueba para actualizar los Comentarios de un Outfit que no existe.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceUsuariosInvalidOutfit(){
		assertThrows(EntityNotFoundException.class, ()->{
			List<UsuarioEntity> nuevaLista = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				UsuarioEntity entity = factory.manufacturePojo(UsuarioEntity.class);
				entity.getFavoritos().add(outfit);		
				entityManager.persist(entity);
				nuevaLista.add(entity);
			}
			outfitUsuarioService.replaceUsuarios(0L, nuevaLista);
		});
	}
	
	/**
	 * Prueba para actualizar los Comentarioes que no existen de un Outfit.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceInvalidUsuarios() {
		assertThrows(EntityNotFoundException.class, ()->{
			List<UsuarioEntity> nuevaLista = new ArrayList<>();
			UsuarioEntity entity = factory.manufacturePojo(UsuarioEntity.class);
			entity.setId(0L);
			nuevaLista.add(entity);
			outfitUsuarioService.replaceUsuarios(outfit.getId(), nuevaLista);
		});
	}
	
	/**
	 * Prueba para actualizar un Comentario de un Outfit que no existe.
	 *
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceUsuariosInvalidUsuario(){
		assertThrows(EntityNotFoundException.class, ()->{
			List<UsuarioEntity> nuevaLista = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				UsuarioEntity entity = factory.manufacturePojo(UsuarioEntity.class);
				entity.getFavoritos().add(outfit);		
				entityManager.persist(entity);
				nuevaLista.add(entity);
			}
			outfitUsuarioService.replaceUsuarios(0L, nuevaLista);
		});
	}

	/**
	 * Prueba desasociar un Comentario con un Outfit.
	 *
	 */
	@Test
	void testRemoveUsuario() throws EntityNotFoundException {
		for (UsuarioEntity usuario : usuarioList) {
			outfitUsuarioService.removeUsuario(outfit.getId(), usuario.getId());
		}
		assertTrue(outfitUsuarioService.getUsuarios(outfit.getId()).isEmpty());
	}
	
	/**
	 * Prueba desasociar un Comentario que no existe con un Outfit.
	 *
	 */
	@Test
	void testRemoveInvalidUsuario(){
		assertThrows(EntityNotFoundException.class, ()->{
			outfitUsuarioService.removeUsuario(outfit.getId(), 0L);
		});
	}
	
	/**
	 * Prueba desasociar un Comentario con un Outfit que no existe.
	 *
	 */
	@Test
	void testRemoveUsuarioInvalidOutfit(){
		assertThrows(EntityNotFoundException.class, ()->{
			outfitUsuarioService.removeUsuario(0L, usuarioList.get(0).getId());
		});
	}
}







