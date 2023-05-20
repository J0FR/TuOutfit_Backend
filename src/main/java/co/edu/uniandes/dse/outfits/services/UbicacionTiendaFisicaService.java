package co.edu.uniandes.dse.outfits.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.outfits.entities.TiendaFisicaEntity;
import co.edu.uniandes.dse.outfits.entities.UbicacionEntity;
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
public class UbicacionTiendaFisicaService {
    
    @Autowired
    private TiendaFisicaRepository tiendaFisicaRepository;

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @Transactional
    public TiendaFisicaEntity addTiendaFisica(Long ubicacionId, Long tiendaFisicaId) throws EntityNotFoundException {
        log.info("Inicia proceso de asociarle una tienda física a la ubicación con id = {0}", ubicacionId);
        Optional<TiendaFisicaEntity> tiendaFisicaEntity = tiendaFisicaRepository.findById(tiendaFisicaId);
        Optional<UbicacionEntity> ubicacionEntity = ubicacionRepository.findById(ubicacionId);

        if (tiendaFisicaEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.TIENDA_FISICA_NOT_FOUND);
        }
        if (ubicacionEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.UBICACION_NOT_FOUND);
        }
        ubicacionEntity.get().setTiendaFisica(tiendaFisicaEntity.get());
        log.info("Termina proceso de agregarle una tienda física a la ubicación con id = {0}", ubicacionId);
        return tiendaFisicaEntity.get();
    }

    @Transactional
    public TiendaFisicaEntity getTiendaFisica(Long ubicacionId, Long tiendaFisicaId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de consultar la tienda física con id = {0} de la ubicación con id = " + ubicacionId, tiendaFisicaId);
        Optional<TiendaFisicaEntity> tiendaFisicaEntity = tiendaFisicaRepository.findById(tiendaFisicaId);
        Optional<UbicacionEntity> ubicacionEntity = ubicacionRepository.findById(ubicacionId);
        if (tiendaFisicaEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.TIENDA_FISICA_NOT_FOUND);
        }
        if (ubicacionEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.UBICACION_NOT_FOUND);
        }
        log.info("Termina proceso de consultar la tienda física con id = {0} de la ubicación con id = " + ubicacionId, tiendaFisicaId);
        if (ubicacionEntity.get().getTiendaFisica() != tiendaFisicaEntity.get()) {
            throw new IllegalOperationException("La tienda no esta asociada a la ubicación");
        }
        return ubicacionEntity.get().getTiendaFisica();
    }

}
