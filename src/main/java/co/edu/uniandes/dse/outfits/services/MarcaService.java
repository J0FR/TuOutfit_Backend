package co.edu.uniandes.dse.outfits.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.outfits.entities.MarcaEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.ErrorMessage;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.repositories.MarcaRepository;
import co.edu.uniandes.dse.outfits.repositories.PrendaRepository;
import co.edu.uniandes.dse.outfits.repositories.TiendaFisicaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MarcaService {

    @Autowired
    MarcaRepository marcaRepository;

    @Autowired
    TiendaFisicaRepository tiendaFisicaRepository;

    @Autowired
    PrendaRepository prendaRepository;

    @Transactional
    public MarcaEntity createMarca(MarcaEntity marca) throws IllegalOperationException {
        log.info("Inicia proceso de creación de la marca");
        if (marca.getNombre() == null || marca.getNombre() == "")
            throw new IllegalOperationException("La marca no tiene un nombre valido");
        if (marca.getUrl_sitio_web() == null || marca.getUrl_sitio_web() == "")
            throw new IllegalOperationException("La marca no tiene un URL_Sitio_Web valido");
        if (marca.getLogo() == null || marca.getLogo() == "")
            throw new IllegalOperationException("La marca no tiene un URL de logo valido");
        if (marca.getDetalle_de_marca() == null || marca.getDetalle_de_marca() == "")
            throw new IllegalOperationException("La marca no tiene detalles de la marca valido");
        if (marca.getTiendas_fisicas() == null || marca.getTiendas_fisicas().size() == 0)
            throw new IllegalOperationException("La marca no tiene tiendas fisicas");
        if (marca.getPrendas() == null || marca.getPrendas().size() == 0)
            throw new IllegalOperationException("La marca no tiene prendas");
        log.info("Termina proceso de creación de la marca");
        return marcaRepository.save(marca);
    }

    @Transactional
    public List<MarcaEntity> getMarcas() {
        log.info("Inicia proceso de consultar todos las marcas");
        return marcaRepository.findAll();
    }

    @Transactional
    public MarcaEntity getMarca(Long marcaId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar la marca  con id = {0}", marcaId);
        Optional<MarcaEntity> usuarioEntity = marcaRepository.findById(marcaId);
        if (usuarioEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.MARCA_NOT_FOUND);
        }
        log.info("Termina proceso de consultar la marca con id = {0}", marcaId);
        return usuarioEntity.get();
    }

    @Transactional
    public MarcaEntity updateMarca(Long marcaId, MarcaEntity marca)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar la marca con id = {0}", marcaId);
        Optional<MarcaEntity> outfitEntity = marcaRepository.findById(marcaId);
        if (outfitEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);
        if (marca.getNombre() == null || marca.getNombre() == "")
            throw new IllegalOperationException("nombre no valido");
        if (marca.getUrl_sitio_web() == null || marca.getUrl_sitio_web() == "")
            throw new IllegalOperationException("URL_Sitio_Web no valido");
        if (marca.getLogo() == null || marca.getLogo() == "")
            throw new IllegalOperationException("URL de logo no valido");
        if (marca.getDetalle_de_marca() == null || marca.getDetalle_de_marca() == "")
            throw new IllegalOperationException("detalle de la marca no valido");
        if (marca.getTiendas_fisicas() == null || marca.getTiendas_fisicas().size() == 0)
            throw new IllegalOperationException("La marca no tiene tiendas fisicas");
        if (marca.getPrendas() == null || marca.getPrendas().size() == 0)
            throw new IllegalOperationException("La marca no tiene prendas");

        marca.setId(marcaId);
        log.info("Termina proceso de actualizar la marca con id = {0}", marcaId);
        return marcaRepository.save(marca);
    }

    @Transactional
    public void deleteMarca(Long marcaId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de borrarla marca con id = {0}", marcaId);
        Optional<MarcaEntity> usuarioEntity = marcaRepository.findById(marcaId);
        if (usuarioEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);

        marcaRepository.deleteById(marcaId);
        log.info("Terminas proceso de borrar el usuario con id = {0}", marcaId);
    }

}
