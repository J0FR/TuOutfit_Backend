package co.edu.uniandes.dse.outfits.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(OutfitService.class)

public class OutfitServiceTest {
        @Autowired
        private OutfitService outfitService;

        @Autowired
        private TestEntityManager entityManager;

        private PodamFactory factory = new PodamFactoryImpl();

        private List<OutfitEntity> outfitList = new ArrayList<>();
        private List<PrendaEntity> prendasList = new ArrayList<>();

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
                entityManager.getEntityManager().createQuery("delete from OutfitEntity");
                entityManager.getEntityManager().createQuery("delete from PrendaEntity");
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
                        OutfitEntity prendaEntity = factory.manufacturePojo(OutfitEntity.class);
                        entityManager.persist(prendaEntity);
                        outfitList.add(prendaEntity);
                }
        }

        /**
         * Prueba creacion oufit.
         */
        @Test
        void testCreateOutfit() throws EntityNotFoundException, IllegalOperationException {
                OutfitEntity newEntity = factory.manufacturePojo(OutfitEntity.class);
                newEntity.setNombre("Outfit - #1");
                newEntity.setPrendas(prendasList);
                newEntity.setImagen("../imagenes/imagenOutfit1.jpg");
                newEntity.setDescripcion("Descripcion");
                OutfitEntity result = outfitService.createOutfit(newEntity);
                assertNotNull(result);
                OutfitEntity entity = entityManager.find(OutfitEntity.class, result.getId());
                assertEquals(newEntity.getId(), entity.getId());
                assertEquals(newEntity.getDescripcion(), entity.getDescripcion());
                assertEquals(newEntity.getNombre(), entity.getNombre());
                assertEquals(newEntity.getPrecio(), entity.getPrecio());
                assertEquals(newEntity.getColores(), entity.getColores());
                assertEquals(newEntity.getGenero(), entity.getGenero());
                assertEquals(newEntity.getOcaciones(), entity.getOcaciones());
                assertEquals(newEntity.getRango_edad(), entity.getRango_edad());
                assertEquals(newEntity.getImagen(), entity.getImagen());
                assertEquals(newEntity.getTalla(), entity.getTalla());
        }

        /**
         * Prueba creacion oufit nombre cadena vacia.
         */
        @Test
        void testCreateOutfitWithNoNameString() {
                assertThrows(IllegalOperationException.class, () -> {
                        OutfitEntity newEntity = factory.manufacturePojo(OutfitEntity.class);
                        newEntity.setNombre("");
                        newEntity.setPrendas(prendasList);
                        newEntity.setImagen("../imagenes/imagenOutfit1.jpg");
                        newEntity.setDescripcion("Descripcion");
                        outfitService.createOutfit(newEntity);
                });
        }

        /**
         * Prueba creacion oufit nombre null.
         */
        @Test
        void testCreateOutfitWithNameNull() {
                assertThrows(IllegalOperationException.class, () -> {
                        OutfitEntity newEntity = factory.manufacturePojo(OutfitEntity.class);
                        newEntity.setNombre(null);
                        newEntity.setPrendas(prendasList);
                        newEntity.setImagen("../imagenes/imagenOutfit1.jpg");
                        newEntity.setDescripcion("Descripcion");
                        outfitService.createOutfit(newEntity);
                });
        }

        // /**
        //  * Prueba creacion oufit sin prendas.
        //  */
        // @Test
        // void testCreateBookWithNoPrendas() {
        //         assertThrows(IllegalOperationException.class, () -> {
        //                 OutfitEntity newEntity = factory.manufacturePojo(OutfitEntity.class);
        //                 List<PrendaEntity> prendasListVacio = new ArrayList<>();
        //                 newEntity.setNombre("Outfit - #1");
        //                 newEntity.setPrendas(prendasListVacio);
        //                 newEntity.setImagen("../imagenes/imagenOutfit1.jpg");
        //                 newEntity.setDescripcion("Descripcion");
        //                 outfitService.createOutfit(newEntity);
        //         });
        // }

        // /**
        //  * Prueba creacion oufit con prenda null.
        //  */
        // @Test
        // void testCreateOutfitWithPrendaNull() {
        //         assertThrows(IllegalOperationException.class, () -> {
        //                 OutfitEntity newEntity = factory.manufacturePojo(OutfitEntity.class);
        //                 newEntity.setNombre("Outfit - #1");
        //                 newEntity.setPrendas(null);
        //                 newEntity.setImagen("../imagenes/imagenOutfit1.jpg");
        //                 newEntity.setDescripcion("Descripcion");
        //                 outfitService.createOutfit(newEntity);
        //         });
        // }

        /**
         * Prueba creacion oufit con descripcion string vacio.
         */
        @Test
        void testCreateOutfitWithNoDescripcion() {
                assertThrows(IllegalOperationException.class, () -> {
                        OutfitEntity newEntity = factory.manufacturePojo(OutfitEntity.class);
                        newEntity.setNombre("Outfit - #1");
                        newEntity.setPrendas(prendasList);
                        newEntity.setImagen("../imagenes/imagenOutfit1.jpg");
                        newEntity.setDescripcion("");
                        outfitService.createOutfit(newEntity);
                });
        }

        /**
         * Prueba creacion oufit con descripcion null.
         */
        @Test
        void testCreateOutfitWithDescripcionNull() {
                assertThrows(IllegalOperationException.class, () -> {
                        OutfitEntity newEntity = factory.manufacturePojo(OutfitEntity.class);
                        newEntity.setNombre("Outfit - #1");
                        newEntity.setPrendas(prendasList);
                        newEntity.setImagen("../imagenes/imagenOutfit1.jpg");
                        newEntity.setDescripcion(null);
                        outfitService.createOutfit(newEntity);
                });
        }

        /**
         * Prueba creacion oufit con foto string vacio.
         */
        @Test
        void testCreateOutfitWithFotoStringVacio() {
                assertThrows(IllegalOperationException.class, () -> {
                        OutfitEntity newEntity = factory.manufacturePojo(OutfitEntity.class);
                        newEntity.setNombre("Outfit - #1");
                        newEntity.setPrendas(prendasList);
                        newEntity.setImagen("");
                        newEntity.setDescripcion("Descripcion");
                        outfitService.createOutfit(newEntity);
                });
        }

        /**
         * Prueba creacion oufit con foto string null.
         */
        @Test
        void testCreateOutfitWithFotoNull() {
                assertThrows(IllegalOperationException.class, () -> {
                        OutfitEntity newEntity = factory.manufacturePojo(OutfitEntity.class);
                        newEntity.setNombre("Outfit - #1");
                        newEntity.setPrendas(prendasList);
                        newEntity.setImagen(null);
                        newEntity.setDescripcion("Descripcion");
                        outfitService.createOutfit(newEntity);
                });
        }

        /**
         * Prueba get all oufits.
         */
        @Test
        void testGetOutfits() {
                List<OutfitEntity> list = outfitService.getOutfits();
                assertEquals(outfitList.size(), list.size());
                for (OutfitEntity entity : list) {
                        boolean found = false;
                        for (OutfitEntity storedEntity : outfitList) {
                                if (entity.getId().equals(storedEntity.getId())) {
                                        found = true;
                                }
                        }
                        assertTrue(found);
                }
        }

        /**
         * Prueba get specific oufit by id.
         */
        @Test
        void testGetOutfit() throws EntityNotFoundException {
                OutfitEntity entity = outfitList.get(0);
                OutfitEntity resultEntity = outfitService.getOutfit(entity.getId());
                assertNotNull(resultEntity);
                assertEquals(entity.getId(), resultEntity.getId());
                assertEquals(entity.getDescripcion(), resultEntity.getDescripcion());
                assertEquals(entity.getNombre(), resultEntity.getNombre());
                assertEquals(entity.getPrecio(), resultEntity.getPrecio());
                assertEquals(entity.getColores(), resultEntity.getColores());
                assertEquals(entity.getGenero(), resultEntity.getGenero());
                assertEquals(entity.getOcaciones(), resultEntity.getOcaciones());
                assertEquals(entity.getRango_edad(), resultEntity.getRango_edad());
                assertEquals(entity.getImagen(), resultEntity.getImagen());
                assertEquals(entity.getTalla(), resultEntity.getTalla());
        }

        /**
         * Prueba get specific oufit by id no existente.
         */
        @Test
        void testGetInvalidOutfit() {
                assertThrows(EntityNotFoundException.class,()->{
                        outfitService.getOutfit(0L);
                });
        }

        /**
         * Prueba update outfit.
         */
        @Test
        void testUpdateOutfit() throws EntityNotFoundException, IllegalOperationException {
                OutfitEntity entity = outfitList.get(0);
                OutfitEntity pojoEntity = factory.manufacturePojo(OutfitEntity.class);
                pojoEntity.setId(entity.getId());
                outfitService.updateOutfit(entity.getId(), pojoEntity);

                OutfitEntity resp = entityManager.find(OutfitEntity.class, entity.getId());
                assertEquals(pojoEntity.getId(), resp.getId());
                assertEquals(pojoEntity.getDescripcion(), resp.getDescripcion());
                assertEquals(pojoEntity.getNombre(), resp.getNombre());
                assertEquals(pojoEntity.getPrecio(), resp.getPrecio());
                assertEquals(pojoEntity.getColores(), resp.getColores());
                assertEquals(pojoEntity.getGenero(), resp.getGenero());
                assertEquals(pojoEntity.getOcaciones(), resp.getOcaciones());
                assertEquals(pojoEntity.getRango_edad(), resp.getRango_edad());
                assertEquals(pojoEntity.getImagen(), resp.getImagen());
                assertEquals(pojoEntity.getTalla(), resp.getTalla());
        }

        /**
         * Prueba update outfit id no existente.
         */
        @Test
        void testUpdateOutfitInvalid() {
                assertThrows(EntityNotFoundException.class, () -> {
                        OutfitEntity pojoEntity = factory.manufacturePojo(OutfitEntity.class);
                        pojoEntity.setId(0L);
                        outfitService.updateOutfit(0L, pojoEntity);
                });
        }

        /**
         * Prueba delete outfit.
         */
        @Test
        void testDeleteOutfit() throws EntityNotFoundException, IllegalOperationException {
                OutfitEntity entity = outfitList.get(1);
                outfitService.deleteOutfit(entity.getId());
                OutfitEntity deleted = entityManager.find(OutfitEntity.class, entity.getId());
                assertNull(deleted);
        }

        /**
         * Prueba delete invalid outfit.
         */
        @Test
        void testDeleteInvalidOutfit() {
                assertThrows(EntityNotFoundException.class, ()->{
                        outfitService.deleteOutfit(0L);
                });
        }
}
