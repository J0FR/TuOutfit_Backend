package co.edu.uniandes.dse.outfits.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.outfits.entities.UbicacionEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.ErrorMessage;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.repositories.TiendaFisicaRepository;
import co.edu.uniandes.dse.outfits.repositories.UbicacionRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UbicacionService {
    
    @Autowired
    UbicacionRepository ubicacionRepository;

    @Autowired
    TiendaFisicaRepository tiendaFisicaRepository;

    @Transactional
    public UbicacionEntity createUbicacion(UbicacionEntity ubicacion) throws IllegalOperationException {
        log.info("Inicia proceso de creación de la ubicación");
        if (ubicacion.getTiendaFisica() == null) {
            throw new IllegalOperationException("La ubicación no tiene una tienda asociada");
        }
        log.info("Termina proceso de creación de la ubicación");
        return ubicacionRepository.save(ubicacion);
    }

    @Transactional
    public List<UbicacionEntity> getUbicaciones() {
        log.info("Inicia proceso de consultar todas las ubicaciones");
        return ubicacionRepository.findAll();
    }

    @Transactional
    public UbicacionEntity getUbicacion(Long ubicacionId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar la ubicación con id = {0}", ubicacionId);
        Optional<UbicacionEntity> ubicacionEntity = ubicacionRepository.findById(ubicacionId);
        if (ubicacionEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.UBICACION_NOT_FOUND);
        log.info("Termina proceso de consultar la ubicación con id = {0}", ubicacionId);
        return ubicacionEntity.get();
    }

    @Transactional
    public UbicacionEntity updateUbicacion(Long ubicacionId, UbicacionEntity ubicacion) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar la ubicación con id = {0}", ubicacionId);
        Optional<UbicacionEntity> ubicacionEntity = ubicacionRepository.findById(ubicacionId);
        if (ubicacionEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.UBICACION_NOT_FOUND);
        }
        if (ubicacion.getTiendaFisica() == null) {
            throw new IllegalOperationException("La ubicación no tiene una tienda asociada");
        }
        ubicacion.setId(ubicacionId);
        log.info("Termina proceso de actualizar la ubicación con id = {0}", ubicacionId);
        return ubicacionRepository.save(ubicacion);
    }

    @Transactional
    public void deleteUbicacion(Long ubicacionId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de borrar la ubicación con id = {0}", ubicacionId);
        Optional<UbicacionEntity> ubicacionEntity = ubicacionRepository.findById(ubicacionId);
        if (ubicacionEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.UBICACION_NOT_FOUND);
        if (ubicacionEntity.get().getTiendaFisica() != null) {
            throw new IllegalOperationException("La ubicación tiene una tienda asociada");
        }
        ubicacionRepository.deleteById(ubicacionId);
        log.info("Termina proceso de borrar la ubicación con id = {0}", ubicacionId);
    }
}
