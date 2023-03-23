package co.edu.uniandes.dse.outfits.services;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import co.edu.uniandes.dse.outfits.entities.TiendaFisicaEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.ErrorMessage;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.repositories.MarcaRepository;
import co.edu.uniandes.dse.outfits.repositories.TiendaFisicaRepository;
import co.edu.uniandes.dse.outfits.repositories.UbicacionRepository;

@Slf4j
@Service
public class TiendaFisicaService {

    @Autowired
    TiendaFisicaRepository tiendaFisicaRepository;

    @Autowired
    UbicacionRepository ubicacionRepository;

    @Autowired
    MarcaRepository marcaRepository;

    @Transactional
    public TiendaFisicaEntity createTiendaFisica(TiendaFisicaEntity tiendaFisica) throws IllegalOperationException  {
        log.info("Inicia proceso de creación de la tienda física");
        if (tiendaFisica.getUbicacion() == null) {
            throw new IllegalOperationException("La tienda física no tiene una ubicación asociada");
        }
        if (tiendaFisica.getMarca() == null) {
            throw new IllegalOperationException("La tienda física no tiene una marca asociada");
        }
        log.info("Termina proceso de creación de la tienda física");
        return tiendaFisicaRepository.save(tiendaFisica);
    }

    @Transactional
    public List<TiendaFisicaEntity> getTiendasFisicas() {
        log.info("Inicia proceso de consultar todas las tiendas físicas");
        return tiendaFisicaRepository.findAll();
    }

    @Transactional
    public TiendaFisicaEntity getTiendaFisica(Long tiendaFisicaId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar la tienda física con id = {0}", tiendaFisicaId);
        Optional<TiendaFisicaEntity> tiendaFisicaEntity = tiendaFisicaRepository.findById(tiendaFisicaId);
        if (tiendaFisicaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.TIENDA_FISICA_NOT_FOUND);
        log.info("Termina proceso de consultar la tienda física con id = {0}", tiendaFisicaId);
        return tiendaFisicaEntity.get();
    }

    @Transactional
    public TiendaFisicaEntity updateTiendaFisica(Long tiendaFisicaId, TiendaFisicaEntity tiendaFisica) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar la tienda física con id = {0}", tiendaFisicaId);
        Optional<TiendaFisicaEntity> tiendaFisicaEntity = tiendaFisicaRepository.findById(tiendaFisicaId);
        if (tiendaFisicaEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.TIENDA_FISICA_NOT_FOUND);
        }
        if (tiendaFisica.getUbicacion() == null) {
            throw new IllegalOperationException("La tienda física no tiene una ubicación asociada");
        }
        if (tiendaFisica.getMarca() == null) {
            throw new IllegalOperationException("La tienda física no tiene una marca asociada");
        }
        tiendaFisica.setId(tiendaFisicaId);
        log.info("Termina proceso de actualizar la tienda física con id = {0}", tiendaFisicaId);
        return tiendaFisicaRepository.save(tiendaFisica);
    }

    @Transactional
    public void deleteTiendaFisica(Long tiendaFisicaId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de borrar la tienda física con id = {0}", tiendaFisicaId);
        Optional<TiendaFisicaEntity> tiendaFisicaEntity = tiendaFisicaRepository.findById(tiendaFisicaId);
        if (tiendaFisicaEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.TIENDA_FISICA_NOT_FOUND);
        }
        if (tiendaFisicaEntity.get().getUbicacion() != null) {
            throw new IllegalOperationException("La tienda física tiene una ubicación asociada");
        }
        if (tiendaFisicaEntity.get().getMarca() != null) {
            throw new IllegalOperationException("La tienda física tiene una marca asociada");
        }
        tiendaFisicaRepository.deleteById(tiendaFisicaId);
        log.info("Termina proceso de borrar la tienda física con id = {0}", tiendaFisicaId);
    }
}
