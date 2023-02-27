package co.edu.uniandes.dse.outfits.services;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.uniandes.dse.outfits.entities.OutfitEntity;
import co.edu.uniandes.dse.outfits.entities.PrendaEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.ErrorMessage;
import co.edu.uniandes.dse.outfits.repositories.OutfitRepository;
import co.edu.uniandes.dse.outfits.repositories.PrendaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OutfitPrendaService {
    @Autowired
	private OutfitRepository outfitRepository;

	@Autowired
	private PrendaRepository prendaRepository;


	@Transactional
	public PrendaEntity addPrenda(Long outfitId, Long prendaId) throws EntityNotFoundException {
		log.info("Inicia proceso de asociar el outfit con id = {0} al usuario con id = " + outfitId, prendaId);
		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
		if (outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

		Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);
		if (prendaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);

		outfitEntity.get().addPrenda(prendaEntity.get());
		log.info("Termina proceso de asociar el outfit con id = {0} al usuario con id = {1}", outfitId, prendaId);
		return prendaEntity.get();
	}

	@Transactional
	public List<PrendaEntity> getPrendas(Long outfitId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar los usuario del outfit con id = {0}", outfitId);
		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
		if (outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

        List<PrendaEntity> prendaEntity = outfitEntity.get().getPrendas();

		if (prendaEntity.size() == 0)
			throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);

		log.info("Termina proceso de consultar los usuario del outfit con id = {0}", outfitId);
		return prendaEntity;
	}

	@Transactional
	public void removePrenda(Long outfitId, Long prendaId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar el usuario del outfit con id = {0}", outfitId);
		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
		if (outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

        List<PrendaEntity> prendaEntity = outfitEntity.get().getPrendas();
        Optional<PrendaEntity> prendaToFind = prendaRepository.findById(prendaId);

        if (prendaToFind.isEmpty())
        throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);

        if (prendaEntity.contains(prendaToFind.get())){
            int index = prendaEntity.indexOf(prendaToFind.get());
            prendaEntity.remove(index);
        } else {
            throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);
        }

		log.info("Termina proceso de borrar el usuario del outfit con id = " + prendaId);
	}
}