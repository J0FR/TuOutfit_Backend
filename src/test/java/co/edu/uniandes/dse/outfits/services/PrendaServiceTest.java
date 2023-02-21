package co.edu.uniandes.dse.outfits.services;


import static org.junit.jupiter.api.Assertions.*;

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
import co.edu.uniandes.dse.outfits.entities.MarcaEntity;
import co.edu.uniandes.dse.outfits.entities.OutfitEntity;
import co.edu.uniandes.dse.outfits.entities.PrendaEntity;
import co.edu.uniandes.dse.outfits.entities.ProductoEntity.Color;
import co.edu.uniandes.dse.outfits.entities.ProductoEntity.Genero;
import co.edu.uniandes.dse.outfits.entities.ProductoEntity.Ocacion;
import co.edu.uniandes.dse.outfits.entities.ProductoEntity.RangoEdad;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(PrendaService.class)


class PrendaServiceTest {
    
    @Autowired
    private PrendaService prendaService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    
    private List<PrendaEntity> prendaList = new ArrayList<>();
    private List<MarcaEntity> marcaList = new ArrayList<>();
    private List<ComentarioEntity> comentarioList = new ArrayList<>();
    private List<OutfitEntity> outfitList = new ArrayList<>();


    @BeforeEach
    void setUp(){
        clearData();
        insertData();
    }

    
    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from PrendaEntity");
        entityManager.getEntityManager().createQuery("delete from MarcaEntity");
        entityManager.getEntityManager().createQuery("delete from OutfitEntity");
        entityManager.getEntityManager().createQuery("delete from ComentarioEntity");
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            PrendaEntity PrendaEntity = factory.manufacturePojo(PrendaEntity.class);
            entityManager.persist(PrendaEntity);
            prendaList.add(PrendaEntity);
        }
        for (int i = 0; i < 3; i++) {
            MarcaEntity marcaEntity = factory.manufacturePojo(MarcaEntity.class);
            entityManager.persist(marcaEntity);
            marcaList.add(marcaEntity);
        }
        for (int i = 0; i < 3; i++) {
            ComentarioEntity comentarioEntity = factory.manufacturePojo(ComentarioEntity.class);
            entityManager.persist(comentarioEntity);
            comentarioList.add(comentarioEntity);
        }
        for (int i = 0; i < 3; i++) {
            OutfitEntity outfitEntity = factory.manufacturePojo(OutfitEntity.class);
            entityManager.persist(outfitEntity);
            outfitList.add(outfitEntity);
        }
        prendaList.get(0).setMarca(marcaList.get(0));
        prendaList.get(0).setCommentario(comentarioList);
        prendaList.get(0).setOutfits(outfitList);
        marcaList.get(0).setPrendas(prendaList);
        comentarioList.get(0).setPrenda(prendaList.get(0));
        outfitList.get(0).setPrendas(prendaList);
    }
    @Test
    void testCreatePrenda() throws EntityNotFoundException, IllegalOperationException {
        PrendaEntity prendaEntity = factory.manufacturePojo(PrendaEntity.class);
        prendaEntity.setMarca(marcaList.get(0));
        PrendaEntity result = prendaService.createPrenda(prendaEntity);
        assertNotNull(result);
        PrendaEntity entity = entityManager.find(PrendaEntity.class, result.getId());
        assertEquals(prendaEntity.getId(), entity.getId());//
        assertEquals(prendaEntity.getNombre(),entity.getNombre());//
        assertEquals(prendaEntity.getCommentario(),entity.getCommentario());//
        assertEquals(prendaEntity.getFoto(),entity.getFoto());
        assertEquals(prendaEntity.getGenero(),entity.getGenero());//
        assertEquals(prendaEntity.getOcaciones(), entity.getOcaciones());//
        assertEquals(prendaEntity.getMarca().getId(), entity.getMarca().getId());//
        assertEquals(prendaEntity.getOutfits(), entity.getOutfits());//
        assertEquals(prendaEntity.getUrl_sitio_web_compra(), entity.getUrl_sitio_web_compra());//
        assertEquals(prendaEntity.getColores(), entity.getColores());//
        assertEquals(prendaEntity.getPrecio(), entity.getPrecio());//
        assertEquals(prendaEntity.getRango_edad(), entity.getRango_edad());//
    }
    //Test de marca    
    @Test
    void testCreatePrendaWithNoValidMarca() {
        assertThrows(IllegalOperationException.class, () -> {
            PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
            newEntity.setNombre("Nombre Prueba");
            newEntity.setGenero(Genero.HOMBRE);
            newEntity.setId(1L);
            newEntity.setOcaciones(Ocacion.BODA);
            newEntity.setPrecio(20000);
            newEntity.setColores(Color.ROJO);
            newEntity.setUrl_sitio_web_compra("link_prueba");
            newEntity.setMarca(null);
            prendaService.createPrenda(newEntity);
    });
    }
   //Test de Nombre
   @Test
   void testCreatePrendaWithNoValidNombre(){
    assertThrows(IllegalOperationException.class, () -> {
        PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
        newEntity.setGenero(Genero.HOMBRE);
        newEntity.setId(1L);
        newEntity.setOcaciones(Ocacion.BODA);
        newEntity.setPrecio(20000);
        newEntity.setColores(Color.ROJO);
        newEntity.setRango_edad(RangoEdad.ADOLECENTE);
        newEntity.setUrl_sitio_web_compra("link_prueba");
        newEntity.setNombre(null);
        prendaService.createPrenda(newEntity);
});
   }
   @Test
   void testCreatePrendaWithNoValidNombre2(){
    assertThrows(IllegalOperationException.class, () -> {
        PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
        newEntity.setGenero(Genero.HOMBRE);
        newEntity.setId(1L);
        newEntity.setOcaciones(Ocacion.BODA);
        newEntity.setPrecio(20000);
        newEntity.setColores(Color.ROJO);
        newEntity.setRango_edad(RangoEdad.ADOLECENTE);
        newEntity.setUrl_sitio_web_compra("link_prueba");
        newEntity.setNombre("");
        prendaService.createPrenda(newEntity);
});
   }
   //Test de Colores
   @Test
   void testCreatePrendaWithNoValidColores(){
    assertThrows(IllegalOperationException.class, () -> {
        PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
        newEntity.setNombre("Nombre Prueba");
        newEntity.setGenero(Genero.HOMBRE);
        newEntity.setId(1L);
        newEntity.setOcaciones(Ocacion.BODA);
        newEntity.setUrl_sitio_web_compra("link_prueba");
        newEntity.setPrecio(20000);
        newEntity.setRango_edad(RangoEdad.ADOLECENTE);
        newEntity.setColores(null);
        prendaService.createPrenda(newEntity);
});
   }
   //Test de precio
   @Test
   void testCreatePrendaWithNoValidPrecio(){
    assertThrows(IllegalOperationException.class, () -> {
        PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
        newEntity.setNombre("Nombre Prueba");
        newEntity.setGenero(Genero.HOMBRE);
        newEntity.setId(1L);
        newEntity.setOcaciones(Ocacion.BODA);
        newEntity.setColores(Color.ROJO);
        newEntity.setRango_edad(RangoEdad.ADOLECENTE);
        newEntity.setUrl_sitio_web_compra("link_prueba");
        newEntity.setPrecio(null);
        prendaService.createPrenda(newEntity);
});
   }
@Test
   void testCreatePrendaWithNoValidPrecio2(){
    assertThrows(IllegalOperationException.class, () -> {
        PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
        newEntity.setNombre("Nombre Prueba");
        newEntity.setGenero(Genero.HOMBRE);
        newEntity.setId(1L);
        newEntity.setOcaciones(Ocacion.BODA);
        newEntity.setColores(Color.ROJO);
        newEntity.setRango_edad(RangoEdad.ADOLECENTE);
        newEntity.setUrl_sitio_web_compra("link_prueba");
        newEntity.setPrecio(0);
        prendaService.createPrenda(newEntity);
});

   }
    //Test de Rango de edades
    @Test
   void testCreatePrendaWithNoValidRangoEdad(){
    assertThrows(IllegalOperationException.class, () -> {
        PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
        newEntity.setId(1L);
        newEntity.setGenero(Genero.HOMBRE);
        newEntity.setOcaciones(Ocacion.BODA);
        newEntity.setPrecio(20000);
        newEntity.setColores(Color.ROJO);
        newEntity.setNombre("Nombre Prueba");
        newEntity.setUrl_sitio_web_compra("link_prueba");
        newEntity.setRango_edad(null);
        prendaService.createPrenda(newEntity);
});
   }
   //Test URL
   @Test
   void testCreatePrendaWithNoValidURL(){
    assertThrows(IllegalOperationException.class, () -> {
        PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
        newEntity.setId(1L);
        newEntity.setGenero(Genero.HOMBRE);
        newEntity.setOcaciones(Ocacion.BODA);
        newEntity.setPrecio(20000);
        newEntity.setColores(Color.ROJO);
        newEntity.setRango_edad(RangoEdad.ADOLECENTE);
        newEntity.setNombre("Nombre Prueba");
        newEntity.setUrl_sitio_web_compra(null);
        prendaService.createPrenda(newEntity);
});
   }
   @Test
   void testCreatePrendaWithNoValidURL2(){
    assertThrows(IllegalOperationException.class, () -> {
        PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
        newEntity.setId(1L);
        newEntity.setGenero(Genero.HOMBRE);
        newEntity.setOcaciones(Ocacion.BODA);
        newEntity.setPrecio(20000);
        newEntity.setColores(Color.ROJO);
        newEntity.setRango_edad(RangoEdad.ADOLECENTE);
        newEntity.setNombre("Nombre Prueba");
        newEntity.setUrl_sitio_web_compra("");
        prendaService.createPrenda(newEntity);
});
   }
   //Test ID
   @Test
   void testCreatePrendaWithNoValidId(){
    assertThrows(IllegalOperationException.class, () -> {
        PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
        newEntity.setGenero(Genero.HOMBRE);
        newEntity.setOcaciones(Ocacion.BODA);
        newEntity.setPrecio(20000);
        newEntity.setColores(Color.ROJO);
        newEntity.setRango_edad(RangoEdad.ADOLECENTE);
        newEntity.setNombre("Nombre Prueba");
	  newEntity.setUrl_sitio_web_compra("link_prueba");
        newEntity.setId(null);
        prendaService.createPrenda(newEntity);
}); 

   }
   //Test Genero
   @Test
   void testCreatePrendaWithNoValidGenero(){
    assertThrows(IllegalOperationException.class, () -> {
        PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
        newEntity.setId(1L);
        newEntity.setOcaciones(Ocacion.BODA);
        newEntity.setPrecio(20000);
        newEntity.setColores(Color.ROJO);
        newEntity.setRango_edad(RangoEdad.ADOLECENTE);
        newEntity.setNombre("Nombre Prueba");
	  newEntity.setUrl_sitio_web_compra("link_prueba");
        newEntity.setGenero(null);
        prendaService.createPrenda(newEntity);
});
   }
   //Test ocasiones
   @Test
   void testCreatePrendaWithNoValidOcasiones(){
    assertThrows(IllegalOperationException.class, () -> {
        PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
        newEntity.setId(1L);
        newEntity.setPrecio(20000);
        newEntity.setColores(Color.ROJO);
        newEntity.setRango_edad(RangoEdad.ADOLECENTE);
        newEntity.setNombre("Nombre Prueba");
	  newEntity.setUrl_sitio_web_compra("link_prueba");
        newEntity.setGenero(Genero.HOMBRE);
        newEntity.setOcaciones(null);
        prendaService.createPrenda(newEntity);
});
   }
   //Test imagen
   @Test
   void testCreatePrendaWithNoValidImagen(){
    assertThrows(IllegalOperationException.class, () -> {
        PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
        newEntity.setImagen(null);
        prendaService.createPrenda(newEntity);
});
   }

   @Test
   void testGetPrendas() {
       List<PrendaEntity> list = prendaService.getPrendas();
       assertEquals(prendaList.size(), list.size());
       for (PrendaEntity entity : list) {
           boolean found = false;
           for (PrendaEntity storedEntity : prendaList) {
               if (entity.getId().equals(storedEntity.getId())) {
                   found = true;
               }
           }
           assertTrue(found);
       }
   }
   @Test
   void testGetPrenda() throws EntityNotFoundException{
    PrendaEntity entity = prendaList.get(0);
    PrendaEntity resultEntity = prendaService.getPrenda(entity.getId());
    assertNotNull(resultEntity);
    assertEquals(entity.getId(), resultEntity.getId());
    assertEquals(entity.getNombre(),resultEntity.getNombre());//
    assertEquals(entity.getCommentario(),resultEntity.getCommentario());//
    assertEquals(entity.getFoto(),resultEntity.getFoto());
    assertEquals(entity.getGenero(),resultEntity.getGenero());//
    assertEquals(entity.getOcaciones(), resultEntity.getOcaciones());//
    assertEquals(entity.getMarca().getId(), resultEntity.getMarca().getId());//
    assertEquals(entity.getOutfits(), resultEntity.getOutfits());//
    assertEquals(entity.getUrl_sitio_web_compra(), resultEntity.getUrl_sitio_web_compra());//
    assertEquals(entity.getColores(), resultEntity.getColores());//
    assertEquals(entity.getPrecio(), resultEntity.getPrecio());//
    assertEquals(entity.getRango_edad(), resultEntity.getRango_edad());//

   }
   @Test
    void testGetInvalidPrenda() {
        assertThrows(EntityNotFoundException.class, () -> {
            prendaService.getPrenda(0L);
        });
    }

    @Test
    void testUpdatePrenda() throws EntityNotFoundException, IllegalOperationException {
        PrendaEntity entity = prendaList.get(0);
        PrendaEntity pojoEntity = factory.manufacturePojo(PrendaEntity.class);
        pojoEntity.setId(entity.getId());
        pojoEntity.setMarca(entity.getMarca());
        prendaService.updatePrenda(entity.getId(), pojoEntity);

        PrendaEntity resp = entityManager.find(PrendaEntity.class, entity.getId());
        assertEquals(pojoEntity.getId(), resp.getId());
        assertEquals(pojoEntity.getNombre(),resp.getNombre());//
        assertEquals(pojoEntity.getCommentario(),resp.getCommentario());//
        assertEquals(pojoEntity.getFoto(),resp.getFoto());
        assertEquals(pojoEntity.getGenero(),resp.getGenero());//
        assertEquals(pojoEntity.getOcaciones(), resp.getOcaciones());//
        assertEquals(pojoEntity.getMarca(), resp.getMarca());//
        assertEquals(pojoEntity.getOutfits(), resp.getOutfits());//
        assertEquals(pojoEntity.getUrl_sitio_web_compra(), resp.getUrl_sitio_web_compra());//
        assertEquals(pojoEntity.getColores(), resp.getColores());//
        assertEquals(pojoEntity.getPrecio(), resp.getPrecio());//
        assertEquals(pojoEntity.getRango_edad(), resp.getRango_edad());//
}
    
    @Test
    void testUpdatePrendaInvalid() {
        assertThrows(EntityNotFoundException.class, () -> {
            PrendaEntity pojEntity = factory.manufacturePojo(PrendaEntity.class);
            pojEntity.setId(0L);
            prendaService.updatePrenda(0L, pojEntity);
        });
    }
    @Test
    void testUpdatePrendaWithNoValidMarca(){
        assertThrows(IllegalOperationException.class, () -> {
            PrendaEntity entity = prendaList.get(0);
            PrendaEntity pojoEntity = factory.manufacturePojo(PrendaEntity.class);
            pojoEntity.setId(entity.getId());
            pojoEntity.setId(1L);
            pojoEntity.setOcaciones(Ocacion.BODA);
            pojoEntity.setPrecio(20000);
            pojoEntity.setColores(Color.ROJO);
            pojoEntity.setRango_edad(RangoEdad.ADOLECENTE);
            pojoEntity.setNombre("Nombre Prueba");
	        pojoEntity.setUrl_sitio_web_compra("link_prueba");
            pojoEntity.setGenero(Genero.HOMBRE);
            pojoEntity.setMarca(null);
            prendaService.updatePrenda(entity.getId(), pojoEntity);
        });
    }
    @Test
    void testDeletePrenda() throws EntityNotFoundException, IllegalOperationException {
        PrendaEntity entity = prendaList.get(1);
        prendaService.deletePrenda(entity.getId());
        PrendaEntity deleted = entityManager.find(PrendaEntity.class, entity.getId());
        assertNull(deleted);
    }
    @Test
    void testDeleteUbicacionInvalid() {
        assertThrows(EntityNotFoundException.class, () -> {
            prendaService.deletePrenda(0L);
        });
    }
    @Test
    void testDeleteUbicacionWithOutfit() throws EntityNotFoundException, IllegalOperationException{
        assertThrows(IllegalOperationException.class, () -> {
            PrendaEntity entity = prendaList.get(0);
            prendaService.deletePrenda(entity.getId());
        });
    }


}