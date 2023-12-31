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
import co.edu.uniandes.dse.outfits.entities.ProductoEntity.Ocasion;
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
        prendaList.get(0).setComentarios(comentarioList);
        prendaList.get(0).setOutfits(outfitList);
        marcaList.get(0).setPrendas(prendaList);
        comentarioList.get(0).setPrenda(prendaList.get(0));
        outfitList.get(0).setPrendas(prendaList);
    }
    @Test
    void testCreatePrenda() throws EntityNotFoundException, IllegalOperationException {
        PrendaEntity prendaEntity = factory.manufacturePojo(PrendaEntity.class);
        prendaEntity.setMarca(marcaList.get(0));
        prendaEntity.setComentarios(comentarioList);
        PrendaEntity result = prendaService.createPrenda(prendaEntity);
        assertNotNull(result);
        PrendaEntity entity = entityManager.find(PrendaEntity.class, result.getId());
        assertEquals(prendaEntity.getId(), entity.getId());//
        assertEquals(prendaEntity.getNombre(),entity.getNombre());//
        assertEquals(prendaEntity.getComentarios(),entity.getComentarios());//
        assertEquals(prendaEntity.getImagen(),entity.getImagen());
        assertEquals(prendaEntity.getGenero(),entity.getGenero());//
        assertEquals(prendaEntity.getOcasiones(), entity.getOcasiones());//
        assertEquals(prendaEntity.getMarca().getId(), entity.getMarca().getId());//
        assertEquals(prendaEntity.getOutfits(), entity.getOutfits());//
        assertEquals(prendaEntity.getUrlSitioWebCompra(), entity.getUrlSitioWebCompra());//
        assertEquals(prendaEntity.getColores(), entity.getColores());//
        assertEquals(prendaEntity.getPrecio(), entity.getPrecio());//
        assertEquals(prendaEntity.getRangoEdad(), entity.getRangoEdad());//
        assertEquals(prendaEntity.getTalla(), entity.getTalla());//
    }

    @Test
    void testCreatePrendaWithNullColores() {
        assertThrows(IllegalOperationException.class, () -> {
            PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
            newEntity.setId(1L);
            newEntity.setNombre("Nombre Prueba");
            newEntity.setComentarios(comentarioList);
            newEntity.setImagen("foto");
            newEntity.setGenero(Genero.HOMBRE);
            newEntity.setOcasiones(Ocasion.BODA);
            newEntity.setMarca(marcaList.get(0));
            newEntity.setOutfits(outfitList);
            newEntity.setUrlSitioWebCompra("link_prueba");
            newEntity.setColores(null);
            newEntity.setPrecio(20000);
            newEntity.setRangoEdad(RangoEdad.ADOLECENTE);
            newEntity.setTalla("A1");
            prendaService.createPrenda(newEntity);
    });
    }

    @Test
    void testCreatePrendaWithNullNombre() {
        assertThrows(IllegalOperationException.class, () -> {
            PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
            newEntity.setId(1L);
            newEntity.setNombre(null);
            newEntity.setComentarios(comentarioList);
            newEntity.setImagen("foto");
            newEntity.setGenero(Genero.HOMBRE);
            newEntity.setOcasiones(Ocasion.BODA);
            newEntity.setMarca(marcaList.get(0));
            newEntity.setOutfits(outfitList);
            newEntity.setUrlSitioWebCompra("link_prueba");
            newEntity.setColores(Color.ROJO);
            newEntity.setPrecio(20000);
            newEntity.setRangoEdad(RangoEdad.ADOLECENTE);
            newEntity.setTalla("A1");
            prendaService.createPrenda(newEntity);
    });
    }

    @Test
    void testCreatePrendaWithVacioNombre() {
        assertThrows(IllegalOperationException.class, () -> {
            PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
            newEntity.setId(1L);
            newEntity.setNombre("");
            newEntity.setComentarios(comentarioList);
            newEntity.setImagen("foto");
            newEntity.setGenero(Genero.HOMBRE);
            newEntity.setOcasiones(Ocasion.BODA);
            newEntity.setMarca(marcaList.get(0));
            newEntity.setOutfits(outfitList);
            newEntity.setUrlSitioWebCompra("link_prueba");
            newEntity.setColores(Color.ROJO);
            newEntity.setPrecio(20000);
            newEntity.setRangoEdad(RangoEdad.ADOLECENTE);
            newEntity.setTalla("A1");
            prendaService.createPrenda(newEntity);
    });
    }

    @Test
    void testCreatePrendaWithNullOcasiones() {
        assertThrows(IllegalOperationException.class, () -> {
            PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
            newEntity.setId(1L);
            newEntity.setNombre("Nombre Prueba");
            newEntity.setComentarios(comentarioList);
            newEntity.setImagen("foto");
            newEntity.setGenero(Genero.HOMBRE);
            newEntity.setOcasiones(null);
            newEntity.setMarca(marcaList.get(0));
            newEntity.setOutfits(outfitList);
            newEntity.setUrlSitioWebCompra("link_prueba");
            newEntity.setColores(Color.ROJO);
            newEntity.setPrecio(20000);
            newEntity.setRangoEdad(RangoEdad.ADOLECENTE);
            newEntity.setTalla("A1");
            prendaService.createPrenda(newEntity);
    });
    }

    @Test
    void testCreatePrendaWithNoValidRangoEdad() {
        assertThrows(IllegalOperationException.class, () -> {
            PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
            newEntity.setId(1L);
            newEntity.setNombre("Nombre Prueba");
            newEntity.setComentarios(comentarioList);
            newEntity.setImagen("foto");
            newEntity.setGenero(Genero.HOMBRE);
            newEntity.setOcasiones(Ocasion.BODA);
            newEntity.setMarca(marcaList.get(0));
            newEntity.setOutfits(outfitList);
            newEntity.setUrlSitioWebCompra("link_prueba");
            newEntity.setColores(Color.ROJO);
            newEntity.setPrecio(20000);
            newEntity.setRangoEdad(null);
            newEntity.setTalla("A1");
            prendaService.createPrenda(newEntity);
    });
    }

    @Test
    void testCreatePrendaWithNullTalla() {
        assertThrows(IllegalOperationException.class, () -> {
            PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
            newEntity.setId(1L);
            newEntity.setNombre("Nombre Prueba");
            newEntity.setComentarios(comentarioList);
            newEntity.setImagen("foto");
            newEntity.setGenero(Genero.HOMBRE);
            newEntity.setOcasiones(Ocasion.BODA);
            newEntity.setMarca(marcaList.get(0));
            newEntity.setOutfits(outfitList);
            newEntity.setUrlSitioWebCompra("link_prueba");
            newEntity.setColores(Color.ROJO);
            newEntity.setPrecio(20000);
            newEntity.setRangoEdad(RangoEdad.ADOLECENTE);
            newEntity.setTalla(null);
            prendaService.createPrenda(newEntity);
    });
    }

    @Test
    void testCreatePrendaWithVacioTalla() {
        assertThrows(IllegalOperationException.class, () -> {
            PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
            newEntity.setId(1L);
            newEntity.setNombre("Nombre Prueba");
            newEntity.setComentarios(comentarioList);
            newEntity.setImagen("foto");
            newEntity.setGenero(Genero.HOMBRE);
            newEntity.setOcasiones(Ocasion.BODA);
            newEntity.setMarca(marcaList.get(0));
            newEntity.setOutfits(outfitList);
            newEntity.setUrlSitioWebCompra("link_prueba");
            newEntity.setColores(Color.ROJO);
            newEntity.setPrecio(20000);
            newEntity.setRangoEdad(RangoEdad.ADOLECENTE);
            newEntity.setTalla("");
            prendaService.createPrenda(newEntity);
    });
    }

    @Test
    void testCreatePrendaWithNullUrl() {
        assertThrows(IllegalOperationException.class, () -> {
            PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
            newEntity.setId(1L);
            newEntity.setNombre("Nombre Prueba");
            newEntity.setComentarios(comentarioList);
            newEntity.setImagen("foto");
            newEntity.setGenero(Genero.HOMBRE);
            newEntity.setOcasiones(Ocasion.BODA);
            newEntity.setMarca(marcaList.get(0));
            newEntity.setOutfits(outfitList);
            newEntity.setUrlSitioWebCompra(null);
            newEntity.setColores(Color.ROJO);
            newEntity.setPrecio(20000);
            newEntity.setRangoEdad(RangoEdad.ADOLECENTE);
            prendaService.createPrenda(newEntity);
    });
    }

    @Test
    void testCreatePrendaWithVacioUrl() {
        assertThrows(IllegalOperationException.class, () -> {
            PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
            newEntity.setId(1L);
            newEntity.setNombre("Nombre Prueba");
            newEntity.setComentarios(comentarioList);
            newEntity.setImagen("foto");
            newEntity.setGenero(Genero.HOMBRE);
            newEntity.setOcasiones(Ocasion.BODA);
            newEntity.setMarca(marcaList.get(0));
            newEntity.setOutfits(outfitList);
            newEntity.setUrlSitioWebCompra("");
            newEntity.setColores(Color.ROJO);
            newEntity.setPrecio(20000);
            newEntity.setRangoEdad(RangoEdad.ADOLECENTE);
            prendaService.createPrenda(newEntity);
    });
    }

    @Test
    void testCreatePrendaWithNullFoto() {
        assertThrows(IllegalOperationException.class, () -> {
            PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
            newEntity.setId(1L);
            newEntity.setNombre("Nombre Prueba");
            newEntity.setComentarios(comentarioList);
            newEntity.setImagen(null);
            newEntity.setGenero(Genero.HOMBRE);
            newEntity.setOcasiones(Ocasion.BODA);
            newEntity.setMarca(marcaList.get(0));
            newEntity.setOutfits(outfitList);
            newEntity.setUrlSitioWebCompra("link_prueba");
            newEntity.setColores(Color.ROJO);
            newEntity.setPrecio(20000);
            newEntity.setRangoEdad(RangoEdad.ADOLECENTE);
            prendaService.createPrenda(newEntity);
    });
    }

    @Test
    void testCreatePrendaWithVacioFoto() {
        assertThrows(IllegalOperationException.class, () -> {
            PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
            newEntity.setId(1L);
            newEntity.setNombre("Nombre Prueba");
            newEntity.setComentarios(comentarioList);
            newEntity.setImagen("");
            newEntity.setGenero(Genero.HOMBRE);
            newEntity.setOcasiones(Ocasion.BODA);
            newEntity.setMarca(marcaList.get(0));
            newEntity.setOutfits(outfitList);
            newEntity.setUrlSitioWebCompra("link_prueba");
            newEntity.setColores(Color.ROJO);
            newEntity.setPrecio(20000);
            newEntity.setRangoEdad(RangoEdad.ADOLECENTE);
            prendaService.createPrenda(newEntity);
    });
    }

    @Test
    void testCreatePrendaWithNullComentario() {
        assertThrows(IllegalOperationException.class, () -> {
            PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
            newEntity.setId(1L);
            newEntity.setNombre("Nombre Prueba");
            newEntity.setComentarios(null);
            newEntity.setImagen("foto");
            newEntity.setGenero(Genero.HOMBRE);
            newEntity.setOcasiones(Ocasion.BODA);
            newEntity.setMarca(marcaList.get(0));
            newEntity.setOutfits(outfitList);
            newEntity.setUrlSitioWebCompra("link_prueba");
            newEntity.setColores(Color.ROJO);
            newEntity.setPrecio(20000);
            newEntity.setRangoEdad(RangoEdad.ADOLECENTE);
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
    assertEquals(entity.getComentarios(),resultEntity.getComentarios());//
    assertEquals(entity.getImagen(),resultEntity.getImagen());
    assertEquals(entity.getGenero(),resultEntity.getGenero());//
    assertEquals(entity.getOcasiones(), resultEntity.getOcasiones());//
    assertEquals(entity.getMarca().getId(), resultEntity.getMarca().getId());//
    assertEquals(entity.getOutfits(), resultEntity.getOutfits());//
    assertEquals(entity.getUrlSitioWebCompra(), resultEntity.getUrlSitioWebCompra());//
    assertEquals(entity.getColores(), resultEntity.getColores());//
    assertEquals(entity.getPrecio(), resultEntity.getPrecio());//
    assertEquals(entity.getRangoEdad(), resultEntity.getRangoEdad());//

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
        assertEquals(pojoEntity.getComentarios(),resp.getComentarios());//
        assertEquals(pojoEntity.getImagen(),resp.getImagen());
        assertEquals(pojoEntity.getGenero(),resp.getGenero());//
        assertEquals(pojoEntity.getOcasiones(), resp.getOcasiones());//
        assertEquals(pojoEntity.getMarca(), resp.getMarca());//
        assertEquals(pojoEntity.getOutfits(), resp.getOutfits());//
        assertEquals(pojoEntity.getUrlSitioWebCompra(), resp.getUrlSitioWebCompra());//
        assertEquals(pojoEntity.getColores(), resp.getColores());//
        assertEquals(pojoEntity.getPrecio(), resp.getPrecio());//
        assertEquals(pojoEntity.getRangoEdad(), resp.getRangoEdad());//
}
    
    @Test
    void testUpdatePrendaInvalid() {
        assertThrows(EntityNotFoundException.class, () -> {
            PrendaEntity pojEntity = factory.manufacturePojo(PrendaEntity.class);
            pojEntity.setId(0L);
            prendaService.updatePrenda(0L, pojEntity);
        });
    }

    // Inicia


    @Test
    void testUpdatePrendaWithNullColores() {
        assertThrows(IllegalOperationException.class, () -> {
            PrendaEntity entity = prendaList.get(0);
            PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
            newEntity.setId(entity.getId());
            newEntity.setId(1L);
            newEntity.setNombre("Nombre Prueba");
            newEntity.setComentarios(comentarioList);
            newEntity.setImagen("foto");
            newEntity.setGenero(Genero.HOMBRE);
            newEntity.setOcasiones(Ocasion.BODA);
            newEntity.setMarca(marcaList.get(0));
            newEntity.setOutfits(outfitList);
            newEntity.setUrlSitioWebCompra("link_prueba");
            newEntity.setColores(null);
            newEntity.setPrecio(20000);
            newEntity.setRangoEdad(RangoEdad.ADOLECENTE);
            newEntity.setTalla("A1");
            prendaService.updatePrenda(entity.getId(), newEntity);
    });
    }

    @Test
    void testUpdatePrendaWithNullNombre() {
        assertThrows(IllegalOperationException.class, () -> {
            PrendaEntity entity = prendaList.get(0);
            PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
            newEntity.setId(entity.getId());
            newEntity.setId(1L);
            newEntity.setNombre(null);
            newEntity.setComentarios(comentarioList);
            newEntity.setImagen("foto");
            newEntity.setGenero(Genero.HOMBRE);
            newEntity.setOcasiones(Ocasion.BODA);
            newEntity.setMarca(marcaList.get(0));
            newEntity.setOutfits(outfitList);
            newEntity.setUrlSitioWebCompra("link_prueba");
            newEntity.setColores(Color.ROJO);
            newEntity.setPrecio(20000);
            newEntity.setRangoEdad(RangoEdad.ADOLECENTE);
            newEntity.setTalla("A1");
            prendaService.updatePrenda(entity.getId(), newEntity);
    });
    }

    @Test
    void testUpdatePrendaWithVacioNombre() {
        assertThrows(IllegalOperationException.class, () -> {
            PrendaEntity entity = prendaList.get(0);
            PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
            newEntity.setId(entity.getId());
            newEntity.setId(1L);
            newEntity.setNombre("");
            newEntity.setComentarios(comentarioList);
            newEntity.setImagen("foto");
            newEntity.setGenero(Genero.HOMBRE);
            newEntity.setOcasiones(Ocasion.BODA);
            newEntity.setMarca(marcaList.get(0));
            newEntity.setOutfits(outfitList);
            newEntity.setUrlSitioWebCompra("link_prueba");
            newEntity.setColores(Color.ROJO);
            newEntity.setPrecio(20000);
            newEntity.setRangoEdad(RangoEdad.ADOLECENTE);
            newEntity.setTalla("A1");
            prendaService.updatePrenda(entity.getId(), newEntity);
    });
    }

    @Test
    void testUpdatePrendaWithNullOcasiones() {
        assertThrows(IllegalOperationException.class, () -> {
            PrendaEntity entity = prendaList.get(0);
            PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
            newEntity.setId(entity.getId());
            newEntity.setId(1L);
            newEntity.setNombre("Nombre Prueba");
            newEntity.setComentarios(comentarioList);
            newEntity.setImagen("foto");
            newEntity.setGenero(Genero.HOMBRE);
            newEntity.setOcasiones(null);
            newEntity.setMarca(marcaList.get(0));
            newEntity.setOutfits(outfitList);
            newEntity.setUrlSitioWebCompra("link_prueba");
            newEntity.setColores(Color.ROJO);
            newEntity.setPrecio(20000);
            newEntity.setRangoEdad(RangoEdad.ADOLECENTE);
            newEntity.setTalla("A1");
            prendaService.updatePrenda(entity.getId(), newEntity);
    });
    }

    @Test
    void testUpdatePrendaWithNoValidRangoEdad() {
        assertThrows(IllegalOperationException.class, () -> {
            PrendaEntity entity = prendaList.get(0);
            PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
            newEntity.setId(entity.getId());
            newEntity.setId(1L);
            newEntity.setNombre("Nombre Prueba");
            newEntity.setComentarios(comentarioList);
            newEntity.setImagen("foto");
            newEntity.setGenero(Genero.HOMBRE);
            newEntity.setOcasiones(Ocasion.BODA);
            newEntity.setMarca(marcaList.get(0));
            newEntity.setOutfits(outfitList);
            newEntity.setUrlSitioWebCompra("link_prueba");
            newEntity.setColores(Color.ROJO);
            newEntity.setPrecio(20000);
            newEntity.setRangoEdad(null);
            newEntity.setTalla("A1");
            prendaService.updatePrenda(entity.getId(), newEntity);
    });
    }

    @Test
    void testUpdatePrendaWithNullTalla() {
        assertThrows(IllegalOperationException.class, () -> {
            PrendaEntity entity = prendaList.get(0);
            PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
            newEntity.setId(entity.getId());
            newEntity.setId(1L);
            newEntity.setNombre("Nombre Prueba");
            newEntity.setComentarios(comentarioList);
            newEntity.setImagen("foto");
            newEntity.setGenero(Genero.HOMBRE);
            newEntity.setOcasiones(Ocasion.BODA);
            newEntity.setMarca(marcaList.get(0));
            newEntity.setOutfits(outfitList);
            newEntity.setUrlSitioWebCompra("link_prueba");
            newEntity.setColores(Color.ROJO);
            newEntity.setPrecio(20000);
            newEntity.setRangoEdad(RangoEdad.ADOLECENTE);
            newEntity.setTalla(null);
            prendaService.updatePrenda(entity.getId(), newEntity);
    });
    }

    @Test
    void testUpdatePrendaWithVacioTalla() {
        assertThrows(IllegalOperationException.class, () -> {
            PrendaEntity entity = prendaList.get(0);
            PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
            newEntity.setId(entity.getId());
            newEntity.setId(1L);
            newEntity.setNombre("Nombre Prueba");
            newEntity.setComentarios(comentarioList);
            newEntity.setImagen("foto");
            newEntity.setGenero(Genero.HOMBRE);
            newEntity.setOcasiones(Ocasion.BODA);
            newEntity.setMarca(marcaList.get(0));
            newEntity.setOutfits(outfitList);
            newEntity.setUrlSitioWebCompra("link_prueba");
            newEntity.setColores(Color.ROJO);
            newEntity.setPrecio(20000);
            newEntity.setRangoEdad(RangoEdad.ADOLECENTE);
            newEntity.setTalla("");
            prendaService.updatePrenda(entity.getId(), newEntity);
    });
    }

    @Test
    void testUpdatePrendaWithNullUrl() {
        assertThrows(IllegalOperationException.class, () -> {
            PrendaEntity entity = prendaList.get(0);
            PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
            newEntity.setId(entity.getId());
            newEntity.setId(1L);
            newEntity.setNombre("Nombre Prueba");
            newEntity.setComentarios(comentarioList);
            newEntity.setImagen("foto");
            newEntity.setGenero(Genero.HOMBRE);
            newEntity.setOcasiones(Ocasion.BODA);
            newEntity.setMarca(marcaList.get(0));
            newEntity.setOutfits(outfitList);
            newEntity.setUrlSitioWebCompra(null);
            newEntity.setColores(Color.ROJO);
            newEntity.setPrecio(20000);
            newEntity.setRangoEdad(RangoEdad.ADOLECENTE);
            prendaService.updatePrenda(entity.getId(), newEntity);
    });
    }

    @Test
    void testUpdatePrendaWithVacioUrl() {
        assertThrows(IllegalOperationException.class, () -> {
            PrendaEntity entity = prendaList.get(0);
            PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
            newEntity.setId(entity.getId());
            newEntity.setId(1L);
            newEntity.setNombre("Nombre Prueba");
            newEntity.setComentarios(comentarioList);
            newEntity.setImagen("foto");
            newEntity.setGenero(Genero.HOMBRE);
            newEntity.setOcasiones(Ocasion.BODA);
            newEntity.setMarca(marcaList.get(0));
            newEntity.setOutfits(outfitList);
            newEntity.setUrlSitioWebCompra("");
            newEntity.setColores(Color.ROJO);
            newEntity.setPrecio(20000);
            newEntity.setRangoEdad(RangoEdad.ADOLECENTE);
            prendaService.updatePrenda(entity.getId(), newEntity);
    });
    }

    @Test
    void testUpdatePrendaWithNullFoto() {
        assertThrows(IllegalOperationException.class, () -> {
            PrendaEntity entity = prendaList.get(0);
            PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
            newEntity.setId(entity.getId());
            newEntity.setId(1L);
            newEntity.setNombre("Nombre Prueba");
            newEntity.setComentarios(comentarioList);
            newEntity.setImagen(null);
            newEntity.setGenero(Genero.HOMBRE);
            newEntity.setOcasiones(Ocasion.BODA);
            newEntity.setMarca(marcaList.get(0));
            newEntity.setOutfits(outfitList);
            newEntity.setUrlSitioWebCompra("link_prueba");
            newEntity.setColores(Color.ROJO);
            newEntity.setPrecio(20000);
            newEntity.setRangoEdad(RangoEdad.ADOLECENTE);
            prendaService.updatePrenda(entity.getId(), newEntity);
    });
    }

    @Test
    void testUpdatePrendaWithVacioFoto() {
        assertThrows(IllegalOperationException.class, () -> {
            PrendaEntity entity = prendaList.get(0);
            PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
            newEntity.setId(entity.getId());
            newEntity.setId(1L);
            newEntity.setNombre("Nombre Prueba");
            newEntity.setComentarios(comentarioList);
            newEntity.setImagen("");
            newEntity.setGenero(Genero.HOMBRE);
            newEntity.setOcasiones(Ocasion.BODA);
            newEntity.setMarca(marcaList.get(0));
            newEntity.setOutfits(outfitList);
            newEntity.setUrlSitioWebCompra("link_prueba");
            newEntity.setColores(Color.ROJO);
            newEntity.setPrecio(20000);
            newEntity.setRangoEdad(RangoEdad.ADOLECENTE);
            prendaService.updatePrenda(entity.getId(), newEntity);
    });
    }

    @Test
    void testUpdatePrendaWithNullComentario() {
        assertThrows(IllegalOperationException.class, () -> {
            PrendaEntity entity = prendaList.get(0);
            PrendaEntity newEntity = factory.manufacturePojo(PrendaEntity.class);
            newEntity.setId(entity.getId());
            newEntity.setId(1L);
            newEntity.setNombre("Nombre Prueba");
            newEntity.setComentarios(null);
            newEntity.setImagen("foto");
            newEntity.setGenero(Genero.HOMBRE);
            newEntity.setOcasiones(Ocasion.BODA);
            newEntity.setMarca(marcaList.get(0));
            newEntity.setOutfits(outfitList);
            newEntity.setUrlSitioWebCompra("link_prueba");
            newEntity.setColores(Color.ROJO);
            newEntity.setPrecio(20000);
            newEntity.setRangoEdad(RangoEdad.ADOLECENTE);
            prendaService.updatePrenda(entity.getId(), newEntity);
    });
    }

    // Finaliza


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
