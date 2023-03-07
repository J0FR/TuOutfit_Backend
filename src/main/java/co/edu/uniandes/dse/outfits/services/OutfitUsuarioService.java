package co.edu.uniandes.dse.outfits.services;


import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.outfits.entities.OutfitEntity;
import co.edu.uniandes.dse.outfits.entities.UsuarioEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.ErrorMessage;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.repositories.OutfitRepository;
import co.edu.uniandes.dse.outfits.repositories.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OutfitUsuarioService {
	@Autowired
	private OutfitRepository outfitRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	/**
	 * Asocia un Usuario existente a un Outfit
	 *
	 * @param outfitId 
	 * @param usuarioId 
	 * @return Instancia de usuarioEntity que fue asociada a Outfit
	 */
	@Transactional
	public UsuarioEntity addUsuario(Long outfitId, Long usuarioId) throws EntityNotFoundException {
		log.info("Inicia proceso de asociarle un usuario al outfit con id = {0}", outfitId);
		Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findById(usuarioId);
		if (usuarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);

		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
		if (outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

		outfitEntity.get().getUsuarios().add(usuarioEntity.get());
		log.info("Termina proceso de asociarle un usuario al outfit con id = {0}", outfitId);
		return usuarioEntity.get();
	}

	/**
	 * Obtiene una colección de instancias de UsuarioEntity asociadas a una instancia de Outfit
	 *
	 * @param outfitId 
	 * @return Colección de instancias de UsuarioEntity asociadas a la instancia de Outfit
	 */
	@Transactional
	public List<UsuarioEntity> getUsuarios(Long outfitId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar todos los usuarios del outfit con id = {0}", outfitId);
		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
		if (outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);
		log.info("Finaliza proceso de consultar todos los usuarios del outfit con id = {0}", outfitId);
		return outfitEntity.get().getUsuarios();
	}

	/**
	 * Obtiene una instancia de UsuarioEntity asociada a una instancia de Outfit
	 *
	 * @param outfitId 
	 * @param usuarioId 
	 * @return La entidad del Usuario asociada al Outfit
	 */
	@Transactional
	public UsuarioEntity getUsuario(Long outfitId, Long usuarioId)
			throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de consultar un usuario del outfit con id = {0}", outfitId);
		Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findById(usuarioId);
		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);

		if (usuarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);

		if (outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);
		log.info("Termina proceso de consultar un usuario del outfit con id = {0}", outfitId);
		if (!outfitEntity.get().getUsuarios().contains(usuarioEntity.get()))
			throw new IllegalOperationException("The usuario is not associated to the outfit");
		
		return usuarioEntity.get();
	}

	@Transactional
	/**
	 * Remplaza las instancias de usuario asociadas a una instancia de Outfit
	 *
	 * @param outfitId 
	 * @param listUsuarios    
	 * @return Nueva colección de UsuarioEntity asociada a la instancia de Outfit
	 */
	public List<UsuarioEntity> replaceUsuarios(Long outfitId, List<UsuarioEntity> listUsuarios) throws EntityNotFoundException {
		log.info("Inicia proceso de reemplazar los usuarios del outfit con id = {0}", outfitId);
		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
		if (outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

		for (UsuarioEntity author : listUsuarios) {
			Optional<UsuarioEntity> usuariosEntity = usuarioRepository.findById(author.getId());
			if (usuariosEntity.isEmpty())
				throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);

			if (!outfitEntity.get().getUsuarios().contains(usuariosEntity.get()))
				outfitEntity.get().getUsuarios().add(usuariosEntity.get());
		}
		log.info("Termina proceso de reemplazar los usuarios del outfit con id = {0}", outfitId);
		return getUsuarios(outfitId);
	}

	@Transactional
	/**
	 * Desasocia un usuario existente de un Outfit existente
	 *
	 * @param outfitId   
	 * @param comentarioId 
	 */
	public void removeUsuario(Long outfitId, Long usuarioId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar un usuario del outfit con id = {0}", outfitId);
		Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findById(usuarioId);
		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);

		if (usuarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);

		if (outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

		outfitEntity.get().getUsuarios().remove(usuarioEntity.get());

		log.info("Termina proceso de borrar un usuario del outfit con id = {0}", outfitId);
	}
}




