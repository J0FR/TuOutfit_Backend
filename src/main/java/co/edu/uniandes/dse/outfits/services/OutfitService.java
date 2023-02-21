package co.edu.uniandes.dse.outfits.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.outfits.entities.OutfitEntity;
import co.edu.uniandes.dse.outfits.entities.PrendaEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.ErrorMessage;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.repositories.OutfitRepository;
import co.edu.uniandes.dse.outfits.repositories.PrendaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OutfitService {
        @Autowired
        OutfitRepository outfitRepository;

        @Transactional
        public OutfitEntity createOutfit(OutfitEntity outfitEntity) throws IllegalOperationException {
                log.info("Inicia proceso de creación del Outfit");

                // Aca va la logica
                if (outfitEntity.getNombre() == null) {
                        throw new IllegalOperationException("Se esta registrando un outfit sin nulllll.");
                } else if (outfitEntity.getNombre().isEmpty()) {
                        throw new IllegalOperationException(
                                        "Se esta registrando un outfit sin nombre lo cual no es valido.");
                } else if (outfitEntity.getPrendas() == null) {
                        throw new IllegalOperationException("Se esta registrando un outfit sin prendas.");
                } else if (outfitEntity.getPrendas().size() == 0) {
                        throw new IllegalOperationException("Se esta registrando un outfit sin prendas.");
                } else if (outfitEntity.getFoto() == null) {
                        throw new IllegalOperationException("Se esta registrando un outfit sin foto.");
                } else if (outfitEntity.getFoto().isEmpty()) {
                        throw new IllegalOperationException("Se esta registrando un outfit sin foto.");
                } else if (outfitEntity.getDescripcion() == null) {
                        throw new IllegalOperationException("Se esta registrando un outfit sin descripcion.");
                } else if (outfitEntity.getDescripcion().isEmpty()) {
                        throw new IllegalOperationException("Se esta registrando un outfit sin descripcion.");
                }

                log.info("Termina proceso de creación del outfit");
                return outfitRepository.save(outfitEntity);
        }

        @Transactional
        public List<OutfitEntity> getOutfits() {
                log.info("Inicia proceso de consultar todos los outfits");
                return outfitRepository.findAll();
        }

        @Transactional
        public OutfitEntity getOutfit(Long outfitID) throws EntityNotFoundException {
                log.info("Inicia proceso de consultar el outfit con id = {0}", outfitID);
                Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitID);
                if (outfitEntity.isEmpty())
                        throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);
                log.info("Termina proceso de consultar el outfit con id = {0}", outfitID);
                return outfitEntity.get();
        }

        @Transactional
        public OutfitEntity updateOutfit(Long outfitId, OutfitEntity outfit)
                        throws EntityNotFoundException, IllegalOperationException {
                log.info("Inicia proceso de actualizar el outfit con id = {0}", outfitId);
                Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
                if (outfitEntity.isEmpty())
                        throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

                outfit.setId(outfitId);
                log.info("Termina proceso de actualizar el outfit con id = {0}", outfitId);
                return outfitRepository.save(outfit);
        }

        @Transactional
        public void deleteOutfit(Long outfitId) throws EntityNotFoundException, IllegalOperationException {
                log.info("Inicia proceso de borrar el outfit con id = {0}", outfitId);
                Optional<OutfitEntity> bookEntity = outfitRepository.findById(outfitId);
                if (bookEntity.isEmpty())
                        throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

                outfitRepository.deleteById(outfitId);
                log.info("Termina proceso de borrar el outfit con id = {0}", outfitId);
        }

}
