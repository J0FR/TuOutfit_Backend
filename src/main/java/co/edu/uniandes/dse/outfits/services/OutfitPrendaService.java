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
public class OutfitPrendaService {
	@Autowired
	private OutfitRepository outfitRepository;

	@Autowired
	private PrendaRepository prendaRepository;
	
	/**
	 * Asocia un Prenda existente a un Outfit
	 *
	 * @param outfitId 
	 * @param prendaId 
	 * @return Instancia de PrendaEntity que fue asociada a Outfit
	 */
	@Transactional
	public PrendaEntity addPrenda(Long outfitId, Long prendaId) throws EntityNotFoundException {
		log.info("Inicia proceso de asociarle un Prenda al outfit con id = {0}", outfitId);
		Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);
		if (prendaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);

		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
		if (outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

		outfitEntity.get().getPrendas().add(prendaEntity.get());
		log.info("Termina proceso de asociarle un Prenda al outfit con id = {0}", outfitId);
		return prendaEntity.get();
	}

	/**
	 * Obtiene una colección de instancias de PrendaEntity asociadas a una instancia de Outfit
	 *
	 * @param outfitId 
	 * @return Colección de instancias de PrendaEntity asociadas a la instancia de Outfit
	 */
	@Transactional
	public List<PrendaEntity> getPrendas(Long outfitId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar todos los Prendas del outfit con id = {0}", outfitId);
		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
		if (outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);
		log.info("Finaliza proceso de consultar todos los Prendas del outfit con id = {0}", outfitId);
		return outfitEntity.get().getPrendas();
	}

	/**
	 * Obtiene una instancia de PrendaEntity asociada a una instancia de Outfit
	 *
	 * @param outfitId 
	 * @param PrendaId 
	 * @return La entidad del Prenda asociada al Outfit
	 */
	@Transactional
	public PrendaEntity getPrenda(Long outfitId, Long prendaId)
			throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de consultar un Prenda del outfit con id = {0}", outfitId);
		Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);
		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);

		if (prendaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);

		if (outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);
		log.info("Termina proceso de consultar un Prenda del outfit con id = {0}", outfitId);
		if (!outfitEntity.get().getPrendas().contains(prendaEntity.get()))
			throw new IllegalOperationException("The Prenda is not associated to the outfit");
		
		return prendaEntity.get();
	}

	@Transactional
	/**
	 * Remplaza las instancias de Prenda asociadas a una instancia de Outfit
	 *
	 * @param outfitId 
	 * @param listPrendas    
	 * @return Nueva colección de PrendaEntity asociada a la instancia de Outfit
	 */
	public List<PrendaEntity> replacePrendas(Long outfitId, List<PrendaEntity> listPrendas) throws EntityNotFoundException {
		log.info("Inicia proceso de reemplazar los Prendas del outfit con id = {0}", outfitId);
		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
		if (outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

		for (PrendaEntity author : listPrendas) {
			Optional<PrendaEntity> prendaEntity = prendaRepository.findById(author.getId());
			if (prendaEntity.isEmpty())
				throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);

			if (!outfitEntity.get().getPrendas().contains(prendaEntity.get()))
				outfitEntity.get().getPrendas().add(prendaEntity.get());
		}
		log.info("Termina proceso de reemplazar los Prendas del outfit con id = {0}", outfitId);
		return getPrendas(outfitId);
	}

	@Transactional
	/**
	 * Desasocia un Prenda existente de un Outfit existente
	 *
	 * @param outfitId   
	 * @param prendaId 
	 */
	public void removePrenda(Long outfitId, Long prendaId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar un Prenda del outfit con id = {0}", outfitId);
		Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);
		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);

		if (prendaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);

		if (outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

		outfitEntity.get().getPrendas().remove(prendaEntity.get());

		log.info("Termina proceso de borrar un Prenda del outfit con id = {0}", outfitId);
	}
}