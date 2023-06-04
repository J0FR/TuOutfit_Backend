package co.edu.uniandes.dse.outfits.services;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class MarcaPrendaService {
    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private PrendaRepository prendaRepository;

    /**
     * Agregar un comentario a un outfit
     *
     * @param ourfitId El id marca a guardar
     * @param prendaId El id del comentario al cual se le va a guardar el premio.
     * @return El comentario agregado al outfit.
     * @throws EntityNotFoundException
     */
    @Transactional
    public PrendaEntity addPrenda(Long marcaId, Long prendaId) throws EntityNotFoundException {
        log.info("Inicia proceso de asociar el marca con id = {0} a la prenda con id = {0}" + marcaId, prendaId);
        Optional<MarcaEntity> marcaEntity = marcaRepository.findById(marcaId);
        if (marcaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.MARCA_NOT_FOUND);

        Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);
        if (prendaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);

        marcaEntity.get().addPrenda(prendaEntity.get());
        prendaEntity.get().setMarca(marcaEntity.get());

        log.info("Termina proceso de asociar el marca con id = {0} a la prenda con id = {0}", marcaId, prendaId);
        return prendaEntity.get();
    }

    /**
     *
     * Obtener los comentarios pertenecientes al id del marca dado.
     *
     * @param marcaId  id de la marca a ser buscado.
     * @param prendaId id de la prenda a ser buscada.
     * @return los comentarios asociados.
     * @throws EntityNotFoundException
     * @throws IllegalOperationException
     */
    @Transactional
    public PrendaEntity getPrenda(Long marcaId, Long prendaId)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de consultar la prenda con el id = {0} de la marca con id = {0}", prendaId, marcaId);

        Optional<MarcaEntity> marcaEntity = marcaRepository.findById(marcaId);
        if (marcaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.MARCA_NOT_FOUND);

        Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);
        if (prendaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);

        log.info("Termina proceso de consultar la prenda con el id = {0} de la marca con id = {0}", prendaId,
                marcaId);

        if (!marcaEntity.get().getPrendas().contains(prendaEntity.get()))
            throw new IllegalOperationException("La prenda no esta asociada a la marca");

        return prendaEntity.get();
    }

    /**
     *
     * Obtener los comentarios pertenecientes al id del marca dado.
     *
     * @param marcaId id de la marca a ser buscado.
     * @return los comentarios asociados.
     * @throws EntityNotFoundException
     */
    @Transactional
    public List<PrendaEntity> getPrendas(Long marcaId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar las prendas del marca con id = {0}", marcaId);
        Optional<MarcaEntity> marcaEntity = marcaRepository.findById(marcaId);
        if (marcaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.MARCA_NOT_FOUND);

        List<PrendaEntity> comentarioEntity = marcaEntity.get().getPrendas();

        if (comentarioEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);

        log.info("Termina proceso de consultar las prendas del marca con id = {0}", marcaId);
        return comentarioEntity;
    }

    /**
     * Remplazar comentarios de una Outfit
     *
     * @param prendas  Lista de prendas que serán las de la marca.
     * @param outfitId El id de la marca que se quiere actualizar.
     * @return La lista de Comentarios actualizada.
     * @throws EntityNotFoundException Si la marca o una prenda de la lista no
     *                                 se encuentran
     */
    @Transactional
    public List<PrendaEntity> updatePrendas(Long marcaId, List<PrendaEntity> prendas)
            throws EntityNotFoundException {
        log.info("Inicia proceso de actualizar la marca con id = {0}", marcaId);
        Optional<MarcaEntity> marcaEntity = marcaRepository.findById(marcaId);
        if (marcaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.MARCA_NOT_FOUND);

        for (PrendaEntity prenda : prendas) {
            Optional<PrendaEntity> cambio = prendaRepository.findById(prenda.getId());
            if (cambio.isEmpty())
                throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);

            if (!marcaEntity.get().getPrendas().contains(cambio.get())) {
                marcaEntity.get().getPrendas().add(cambio.get());
                cambio.get().setMarca(marcaEntity.get());
            }
        }
        log.info("Termina proceso de aztualizar la marca con id = {0}", marcaId);
        return getPrendas(marcaId);
    }

    /**
     * Borrar el comentario de una marca
     *
     * @param marcaId  El id del outfit.
     * @param prendaId El comentario que se desea borrar del outfit.
     * @throws EntityNotFoundException si el outfit no tiene autor
     */
    @Transactional
    public void removePrenda(Long marcaId, Long prendaId) throws EntityNotFoundException {
        log.info("Inicia proceso de borrar el prenda del marca con id = {0}", marcaId);
        Optional<MarcaEntity> marcaEntity = marcaRepository.findById(marcaId);
        if (marcaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.MARCA_NOT_FOUND);

        List<PrendaEntity> prendaEntity = marcaEntity.get().getPrendas();
        Optional<PrendaEntity> prendaToFind = prendaRepository.findById(prendaId);

        if (prendaToFind.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);

        if (prendaEntity.contains(prendaToFind.get())) {
            int index = prendaEntity.indexOf(prendaToFind.get());
            prendaEntity.remove(index);
        } else {
            throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);
        }

        log.info("Termina proceso de borrar el prenda del marca con id = " + prendaId);
    }
}
