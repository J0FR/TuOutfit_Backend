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
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.repositories.OutfitRepository;
import co.edu.uniandes.dse.outfits.repositories.PrendaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PrendaOutfitService {
	@Autowired
	private OutfitRepository outfitRepository;

	@Autowired
	private PrendaRepository prendaRepository;
	
	/**
	 * Asocia un outfit existente a una prenda
	 *
	 * @param outfitId 
	 * @param prendaId 
	 * @return Instancia de PrendaEntity que fue asociada a Outfit
	 */
	@Transactional
	public OutfitEntity addOutfit(Long outfitId, Long prendaId) throws EntityNotFoundException {
		log.info("Inicia proceso de asociarle un Prenda al outfit con id = {0}", outfitId);
		Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);
		if (prendaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);

		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
		if (outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

		prendaEntity.get().getOutfits().add(outfitEntity.get());
		log.info("Termina proceso de asociarle un outfit a la prenda con id = {0}", prendaId);
		return outfitEntity.get();
	}

	/**
	 * Obtiene una colección de instancias de PrendaEntity asociadas a una instancia de Outfit
	 *
	 * @param prendaId 
	 * @return Colección de instancias de PrendaEntity asociadas a la instancia de Outfit
	 */
	@Transactional
	public List<OutfitEntity> getOutfits(Long prendaId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar todos los outfits de la prenda con id = {0}", prendaId);
		Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);
		if (prendaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);
		log.info("Finaliza proceso de consultar todos los Prendas del outfit con id = {0}", prendaId);
		return prendaEntity.get().getOutfits();
	}

	/**
	 * Obtiene una instancia de OutfitEntity asociada a una instancia de Prenda
	 *
	 * @param prendaId 
	 * @param outfitId 
	 * @return La entidad del Outfit asociada a la Prenda
	 */
	@Transactional
	public OutfitEntity getOutfit(Long prendaId, Long outfitId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de consultar un outfit de la prenda con id = {0}", prendaId);
		Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);
		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);

		if (prendaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);

		if (outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);
			
		if (!prendaEntity.get().getOutfits().contains(outfitEntity.get()))
			throw new IllegalOperationException("The Prenda is not associated to the outfit");
		
		log.info("Termina proceso de consultar un outfit de la prenda con id = {0}", prendaId);
		return outfitEntity.get();
	}

	@Transactional
	/**
	 * Remplaza las instancias de Prenda asociadas a una instancia de Outfit
	 *
	 * @param prendaId 
	 * @param listOutfits    
	 * @return Nueva colección de PrendaEntity asociada a la instancia de Outfit
	 */
	public List<OutfitEntity> replaceOutfits(Long prendaId, List<OutfitEntity> listOutfits) throws EntityNotFoundException {
		log.info("Inicia proceso de reemplazar los outfits de la prenda con id = {0}", prendaId);
		Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);
		if (prendaEntity.isEmpty()) {
			throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);
		}
			

		for (OutfitEntity author : listOutfits) {
			Optional<OutfitEntity> outfitEntity = outfitRepository.findById(author.getId());
			if (outfitEntity.isEmpty())
				throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

            if (!prendaEntity.get().getOutfits().contains(outfitEntity.get()))
                    prendaEntity.get().getOutfits().add(outfitEntity.get());

		}
		log.info("Termina proceso de reemplazar los Prendas del outfit con id = {0}", prendaId);
		return getOutfits(prendaId);
	}

	@Transactional
	/**
	 * Desasocia un Prenda existente de un Outfit existente
	 *
	 * @param prendaId   
	 * @param outfitId 
	 */
	public void removeOutfit(Long prendaId, Long outfitId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar un outfit de la prenda con id = {0}", prendaId);
		Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);
		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);

		if (prendaEntity.isEmpty()) {
			throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);
		}
			
		if (outfitEntity.isEmpty()) {
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);
		}
			
        prendaEntity.get().getOutfits().remove(outfitEntity.get());
		
		log.info("Termina proceso de borrar un Prenda del outfit con id = {0}", outfitId);
	}
}