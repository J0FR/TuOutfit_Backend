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
public class PrendaMarcaService {
    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private PrendaRepository prendaRepository;

    /**
     * Agregar un comentario a un outfit
     *
     * @param prendaId El id prenda a guardar
     * @param marcaId El id del comentario al cual se le va a guardar el premio.
     * @return El comentario agregado al outfit.
     * @throws EntityNotFoundException
     */
    @Transactional
    public MarcaEntity addMarca(Long prendaId, Long marcaId) throws EntityNotFoundException {
        log.info("Inicia proceso de asociar la prenda con id = {0} a la marca con id = " + prendaId, marcaId);
        Optional<MarcaEntity> marcaEntity = marcaRepository.findById(marcaId);
        if (marcaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.MARCA_NOT_FOUND);

        Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);
        if (prendaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);

        marcaEntity.get().addPrenda(prendaEntity.get());
        log.info("Termina proceso de asociar la prenda con id = {0} a la marca con id = {1}", prendaId, marcaId);
        return marcaEntity.get();
    }

    /**
     *
     * Obtener los comentarios pertenecientes al id del marca dado.
     *
     * @param prendaId id de la prenda a ser buscado.
     * @return los comentarios asociados.
     * @throws EntityNotFoundException
     */
    @Transactional
    public MarcaEntity getMarca(Long prendaId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar la marca de la prenda con id = {0}", prendaId);
        Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);

        if (prendaEntity.isEmpty()){
            throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);
        }
        log.info("Termina proceso de consultar la marca de la prenda con id = {0}", prendaId);
        return prendaEntity.get().getMarca();
    }

    /**
     * Borrar el comentario de una marca
     *
     * @param prendaId  El id de la prenda.
     * @throws EntityNotFoundException si el outfit no tiene autor
     */
    @Transactional
    public void removeMarca(Long prendaId) throws EntityNotFoundException {
        log.info("Inicia proceso de borrar la marca de la prenda con id = {0}", prendaId);
        Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);
        if (prendaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);


        MarcaEntity marcaEntity = prendaEntity.get().getMarca();

        if (marcaEntity == null) {
            throw new EntityNotFoundException(ErrorMessage.MARCA_NOT_FOUND);
        }

        // desasocia el prenda del comentario
        prendaEntity.get().setMarca(null);
        log.info("Finaliza borrado de marca de prenda");
        return;
    }
}
