package co.edu.uniandes.dse.outfits.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.outfits.entities.TiendaFisicaEntity;
import co.edu.uniandes.dse.outfits.entities.UbicacionEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.ErrorMessage;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.repositories.TiendaFisicaRepository;
import co.edu.uniandes.dse.outfits.repositories.UbicacionRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TiendaFisicaUbicacionService {
    
    @Autowired
    private TiendaFisicaRepository tiendaFisicaRepository;

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @Transactional
    public UbicacionEntity addUbicacion(Long tiendaFisicaId, Long ubicacionId) throws EntityNotFoundException {
        log.info("Inicia proceso de asociarle una ubicación a la tienda física con id = {0}", tiendaFisicaId);
        Optional<TiendaFisicaEntity> tiendaFisicaEntity = tiendaFisicaRepository.findById(tiendaFisicaId);
        Optional<UbicacionEntity> ubicacionEntity = ubicacionRepository.findById(ubicacionId);

        if (tiendaFisicaEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.TIENDA_FISICA_NOT_FOUND);
        }
        if (ubicacionEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.UBICACION_NOT_FOUND);
        }
        tiendaFisicaEntity.get().setUbicacion(ubicacionEntity.get());
        log.info("Termina proceso de agregarle una ubicación a la tienda física con id = {0}", tiendaFisicaId);
        return ubicacionEntity.get();
    }

    @Transactional
    public UbicacionEntity getUbicacion(Long tiendaFisicaId, Long ubicacionId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de consultar la ubicación con id = {0} de la tienda física con id = " + ubicacionId, tiendaFisicaId);
        Optional<TiendaFisicaEntity> tiendaFisicaEntity = tiendaFisicaRepository.findById(tiendaFisicaId);
        Optional<UbicacionEntity> ubicacionEntity = ubicacionRepository.findById(ubicacionId);
        if (tiendaFisicaEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.TIENDA_FISICA_NOT_FOUND);
        }
        if (ubicacionEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.UBICACION_NOT_FOUND);
        }
        log.info("Termina proceso de consultar la ubicación con id = {0} de la tienda física con id = " + ubicacionId, tiendaFisicaId);
        if (tiendaFisicaEntity.get().getUbicacion() != ubicacionEntity.get()) {
            throw new IllegalOperationException("La ubicación no esta asociada a la tienda");
        }
        return tiendaFisicaEntity.get().getUbicacion();
    }
}
