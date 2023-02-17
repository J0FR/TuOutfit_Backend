package co.edu.uniandes.dse.outfits.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.spi.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.outfits.entities.OutfitEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.repositories.OutfitRepository;
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
        if (!outfitEntity.getNombre().isEmpty()) {
            throw new IllegalOperationException("Se esta registrando un outfit sin nombre lo cual no es valido.");
        }

        log.info("Termina proceso de creación del libro");
        return outfitRepository.save(outfitEntity);
    }

    @Transactional
    public List<OutfitEntity> getOutfits() {
            log.info("Inicia proceso de consultar todos los libros");
            return outfitRepository.findAll();
    }

    // paso 6
    @Transactional
    public OutfitEntity getOutfit(Long outfitID) throws EntityNotFoundException {
            log.info("Inicia proceso de consultar el outfit con id = {0}", outfitID);
            Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitID);
            if (outfitEntity.isEmpty())
                    throw new EntityNotFoundException("Outfit not found");
            log.info("Termina proceso de consultar el outfit con id = {0}", outfitID);
            return outfitEntity.get();
    }


}
