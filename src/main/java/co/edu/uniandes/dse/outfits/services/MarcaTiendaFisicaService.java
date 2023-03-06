package co.edu.uniandes.dse.outfits.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.outfits.entities.MarcaEntity;
import co.edu.uniandes.dse.outfits.entities.TiendaFisicaEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.ErrorMessage;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.repositories.TiendaFisicaRepository;
import co.edu.uniandes.dse.outfits.repositories.MarcaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MarcaTiendaFisicaService {
    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private TiendaFisicaRepository tiendaFisicaRepository;

    /**
     * Asocia un Usuario existente a un outfit
     *
     * @param marcaId        Identificador de la instancia de Usuario
     * @param tiendaFisicaId Identificador de la instancia de tienda fisica
     * @return Instancia de marcaEntity que fue asociada a Tienda Fisica
     */
    @Transactional
    public TiendaFisicaEntity addTiendaFisica(Long marcaId, Long tiendaFisicaId) throws EntityNotFoundException {
        log.info("Inicia proceso de asociarle un comentario a la marca con id = {0}", marcaId);
        Optional<TiendaFisicaEntity> tiendaFisicaEntity = tiendaFisicaRepository.findById(tiendaFisicaId);
        if (tiendaFisicaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.TIENDA_FISICA_NOT_FOUND);

        Optional<MarcaEntity> marcaEntity = marcaRepository.findById(marcaId);
        if (marcaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.MARCA_NOT_FOUND);

        marcaEntity.get().getTiendas_fisicas().add(tiendaFisicaEntity.get());
        log.info("Termina proceso de asociarle una tienda fisica a la marca con id = {0}", marcaId);
        return tiendaFisicaEntity.get();
    }

    @Transactional
    public List<TiendaFisicaEntity> getTiendasFisicas(Long marcaId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar todas las tiendas fisicas de la marca con id = {0}", marcaId);
        Optional<MarcaEntity> marcaEntity = marcaRepository.findById(marcaId);
        if (marcaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.MARCA_NOT_FOUND);

        List<TiendaFisicaEntity> tiendaFisicaEntities = marcaEntity.get().getTiendas_fisicas();

        if (tiendaFisicaEntities.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.TIENDA_FISICA_NOT_FOUND);

        log.info("Finaliza proceso de consultar todas las tiendas fisicas de la marca con id = {0}", marcaId);
        return marcaEntity.get().getTiendas_fisicas();
    }

    @Transactional
    public TiendaFisicaEntity getTiendaFisica(Long marcaId, Long tiendaFisicaId)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de consultar una tienda fisica de la marca con id = {0}", marcaId);
        Optional<TiendaFisicaEntity> tiendaFisicaEntity = tiendaFisicaRepository.findById(tiendaFisicaId);
        Optional<MarcaEntity> marcaEntity = marcaRepository.findById(marcaId);

        if (tiendaFisicaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.TIENDA_FISICA_NOT_FOUND);

        if (marcaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.MARCA_NOT_FOUND);
        log.info("Termina proceso de consultar una tienda fisica de la marca con id = {0}", marcaId);
        if (!marcaEntity.get().getTiendas_fisicas().contains(tiendaFisicaEntity.get()))
            throw new IllegalOperationException("La tienda no esta asociada con la marca");

        return tiendaFisicaEntity.get();
    }

    @Transactional
    public void removeTiendaFisica(Long marcaId, Long tiendaFisicaId) throws EntityNotFoundException {
        log.info("Inicia proceso de borrar un comentario del usuario con id = {0}", marcaId);
        Optional<MarcaEntity> marcaEntity = marcaRepository.findById(marcaId);
        if (marcaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.MARCA_NOT_FOUND);

        List<TiendaFisicaEntity> tiendaFisicaEntity = marcaEntity.get().getTiendas_fisicas();
        Optional<TiendaFisicaEntity> tiendaFisicaToFind = tiendaFisicaRepository.findById(tiendaFisicaId);
        if (tiendaFisicaToFind.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.TIENDA_FISICA_NOT_FOUND);
        if (tiendaFisicaEntity.contains(tiendaFisicaToFind.get())) {
            int index = tiendaFisicaEntity.indexOf(tiendaFisicaToFind.get());
            tiendaFisicaEntity.remove(index);
        } else {
            throw new EntityNotFoundException(ErrorMessage.TIENDA_FISICA_NOT_FOUND);
        }

        log.info("Termina proceso de borrar una tienda fisica de la marca con id = {0}", marcaId);
    }

}
