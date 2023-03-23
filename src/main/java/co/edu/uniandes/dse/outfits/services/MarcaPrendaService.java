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
        log.info("Inicia proceso de asociar el marca con id = {0} a la prenda con id = " + marcaId, prendaId);
        Optional<MarcaEntity> prendaEntity = marcaRepository.findById(marcaId);
        if (prendaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

        Optional<PrendaEntity> comentarioEntity = prendaRepository.findById(prendaId);
        if (comentarioEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);

        prendaEntity.get().addPrenda(comentarioEntity.get());
        log.info("Termina proceso de asociar el marca con id = {0} a la prenda con id = {1}", marcaId, prendaId);
        return comentarioEntity.get();
    }

    /**
     *
     * Obtener los comentarios pertenecientes al id del marca dado.
     *
     * @param marcaId id del outfit a ser buscado.
     * @return los comentarios asociados.
     * @throws EntityNotFoundException
     */
    @Transactional
    public List<PrendaEntity> getPrendas(Long marcaId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar las prendas del marca con id = {0}", marcaId);
        Optional<MarcaEntity> outfitEntity = marcaRepository.findById(marcaId);
        if (outfitEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

        List<PrendaEntity> comentarioEntity = outfitEntity.get().getPrendas();

        if (comentarioEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);

        log.info("Termina proceso de consultar las prendas del marca con id = {0}", marcaId);
        return comentarioEntity;
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
