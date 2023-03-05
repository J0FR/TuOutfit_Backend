package co.edu.uniandes.dse.outfits.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.outfits.entities.MarcaEntity;
import co.edu.uniandes.dse.outfits.entities.PrendaEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.ErrorMessage;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.repositories.MarcaRepository;
import co.edu.uniandes.dse.outfits.repositories.PrendaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MarcaPrendaService {
    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private PrendaRepository prendaRepository;

    /**
     * Asocia un Usuario existente a un outfit
     *
     * @param marcaId  Identificador de la instancia de marca
     * @param prendaId Identificador de la instancia de Prenda
     * @return Instancia de marcaEntity que fue asociada a Tienda Fisica
     */
    @Transactional
    public PrendaEntity addTiendaFisica(Long marcaId, Long prendaId) throws EntityNotFoundException {
        log.info("Inicia proceso de asociarle una prenda a la marca con id = {0}", marcaId);
        Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);
        if (prendaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);

        Optional<MarcaEntity> marcaEntity = marcaRepository.findById(marcaId);
        if (marcaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.MARCA_NOT_FOUND);

        marcaEntity.get().getPrendas().add(prendaEntity.get());
        log.info("Termina proceso de asociarle una prenda a la marca con id = {0}", marcaId);
        return prendaEntity.get();
    }

    @Transactional
    public List<PrendaEntity> getPrendas(Long marcaId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar todas las prendas de la marca con id = {0}", marcaId);
        Optional<MarcaEntity> marcaEntity = marcaRepository.findById(marcaId);
        if (marcaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.MARCA_NOT_FOUND);
        log.info("Finaliza proceso de consultar todas las prendas de la marca con id = {0}", marcaId);
        return marcaEntity.get().getPrendas();
    }

    @Transactional
    public PrendaEntity getPrenda(Long marcaId, Long prendaId)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de consultar una prenda de la marca con id = {0}", marcaId);
        Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);
        Optional<MarcaEntity> marcaEntity = marcaRepository.findById(marcaId);

        if (prendaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);

        if (marcaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.MARCA_NOT_FOUND);
        log.info("Termina proceso de consultar una tienda fisica de la marca con id = {0}", marcaId);
        if (!marcaEntity.get().getPrendas().contains(prendaEntity.get()))
            throw new IllegalOperationException("La prenda no esta asociada con la marca");

        return prendaEntity.get();
    }

    @Transactional
    public void removeOutfit(Long marcaId, Long prendaId) throws EntityNotFoundException {
        log.info("Inicia proceso de borrar un comentario del usuario con id = {0}", marcaId);
        Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);
        Optional<MarcaEntity> marcaEntity = marcaRepository.findById(marcaId);

        if (prendaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);

        if (marcaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.MARCA_NOT_FOUND);

        marcaEntity.get().getPrendas().remove(prendaEntity.get());

        log.info("Termina proceso de borrar una tienda fisica de la marca con id = {0}", marcaId);
    }

}
