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
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(OutfitUsuarioService.class)
public class OutfitUsuarioServiceTest {
    private PodamFactory factory = new PodamFactoryImpl();

	@Autowired
	private OutfitUsuarioService outfitUsuarioService;

	@Autowired
	private TestEntityManager entityManager;

    private List<OutfitEntity> outfitsList = new ArrayList<>();
    private List<UsuarioEntity> usuariosList = new ArrayList<>();

    @BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

    private void clearData() {
		entityManager.getEntityManager().createQuery("delete from UsuarioEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from OutfitEntity").executeUpdate();
	}

	private void insertData() {
		for (int i = 0; i < 3; i++) {
			UsuarioEntity usuarioEntity = factory.manufacturePojo(UsuarioEntity.class);
			entityManager.persist(usuarioEntity);
			usuariosList.add(usuarioEntity);
		}
		for (int i = 0; i < 3; i++) {
			OutfitEntity outfitEntity = factory.manufacturePojo(OutfitEntity.class);
			entityManager.persist(outfitEntity);
			if (i == 0) {
				outfitEntity.setUsuarios(usuariosList);
			}
			outfitsList.add(outfitEntity);
		}
	}

	@Test
	void testAddComentario() throws EntityNotFoundException {
		OutfitEntity entity = outfitsList.get(0);
		UsuarioEntity comentarioEntity = usuariosList.get(1);
		UsuarioEntity response = outfitUsuarioService.addUsuario(entity.getId(), comentarioEntity.getId());

		assertNotNull(response);
		assertEquals(comentarioEntity.getId(), response.getId());
	}

	@Test
	void testAddInvalidComentario() {
		assertThrows(EntityNotFoundException.class, () -> {
			OutfitEntity outfitEntity = outfitsList.get(1);
			outfitUsuarioService.addUsuario(0L, outfitEntity.getId());
		});
	}

	@Test
	void testAddComentarioInvalidOutfit() {
		assertThrows(EntityNotFoundException.class, () -> {
			UsuarioEntity entity = usuariosList.get(0);
			outfitUsuarioService.addUsuario(entity.getId(), 0L);
		});
	}

	@Test
	void testGetComentario() throws EntityNotFoundException {
		OutfitEntity entity = outfitsList.get(0);
		List<UsuarioEntity> resultEntity = outfitUsuarioService.getUsuarios(entity.getId());
		assertNotNull(resultEntity);
		assertEquals(entity.getUsuarios(), resultEntity);
	}

	@Test
	void testGetComentarioInvalidOutfit() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, () -> {
			outfitUsuarioService.getUsuarios(0L);
		});
	}

	@Test
	void testCeroComentarioOutfit() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, () -> {
			outfitUsuarioService.getUsuarios(outfitsList.get(2).getId());
		});
	}

	@Test
	public void testRemoveComentario() throws EntityNotFoundException {
		outfitUsuarioService.removeUser(outfitsList.get(0).getId(), usuariosList.get(0).getId());
		OutfitEntity outfit = entityManager.find(OutfitEntity.class, outfitsList.get(0).getId());
		assertEquals(usuariosList.get(0).getId(), outfit.getUsuarios().get(0).getId());
	}
	
	@Test
	public void testRemoveInvalidComentario(){
		assertThrows(EntityNotFoundException.class, ()->{
			outfitUsuarioService.removeUser(outfitsList.get(0).getId(), 0L);
		});
	}

	@Test
	void testRemoveComentarioInvalidOutfit() {
		assertThrows(EntityNotFoundException.class, () -> {
			outfitUsuarioService.removeUser(0L, usuariosList.get(0).getId());
		});
	}

	@Test
	void testRemoveComentarioInvalidCommentListOutfit() {
		assertThrows(EntityNotFoundException.class, () -> {
			outfitUsuarioService.removeUser(outfitsList.get(1).getId(), usuariosList.get(0).getId());
		});
	}
}







