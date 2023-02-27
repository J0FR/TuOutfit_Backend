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
	 * Agregar un usuario a un outfit
	 *
	 * @param ourfitId  El id premio a guardar
	 * @param usuarioId El id del autor al cual se le va a guardar el premio.
	 * @return El usuario agregado al outfit.
	 * @throws EntityNotFoundException
	 */
	@Transactional
	public UsuarioEntity addUsuario(Long outfitId, Long usuarioId) throws EntityNotFoundException {
		log.info("Inicia proceso de asociar el outfit con id = {0} al usuario con id = " + outfitId, usuarioId);
		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
		if (outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

		Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findById(usuarioId);
		if (usuarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);

		outfitEntity.get().addUsuario(usuarioEntity.get());
		log.info("Termina proceso de asociar el outfit con id = {0} al usuario con id = {1}", outfitId, usuarioId);
		return usuarioEntity.get();
	}

    /**
	 *
	 * Obtener los usuario pertenecientes al id del outfit dado.
	 *
	 * @param outfitId id del outfit a ser buscado.
	 * @return los usuario asociados.
	 * @throws EntityNotFoundException
	 */
	@Transactional
	public List<UsuarioEntity> getUsuarios(Long outfitId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar los usuario del outfit con id = {0}", outfitId);
		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
		if (outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

        List<UsuarioEntity> usuarioEntity = outfitEntity.get().getUsuarios();

		if (usuarioEntity.size() == 0)
			throw new EntityNotFoundException(ErrorMessage.USUARIO_NOT_FOUND);

		log.info("Termina proceso de consultar los usuario del outfit con id = {0}", outfitId);
		return usuarioEntity;
	}

    /**
	 * Borrar el usuario de un outfit
	 *
	 * @param outfitId El id del outfit.
	 * @param usuarioId El usuario que se desea borrar del outfit.
	 * @throws EntityNotFoundException si el outfit no tiene autor
	 */
	@Transactional
	public void removeUser(Long outfitId, Long usuarioId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar el usuario del outfit con id = {0}", outfitId);
		Optional<OutfitEntity> outfitEntity = outfitRepository.findById(outfitId);
		if (outfitEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OUTFIT_NOT_FOUND);

        List<UsuarioEntity> usuarioEntity = outfitEntity.get().getUsuarios();
        Optional<UsuarioEntity> usuarioToFind = usuarioRepository.findById(usuarioId);

        if (usuarioToFind.isEmpty())
        throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);

        if (usuarioEntity.contains(usuarioToFind.get())){
            int index = usuarioEntity.indexOf(usuarioToFind.get());
            usuarioEntity.remove(index);
        } else {
            throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);
        }

		log.info("Termina proceso de borrar el usuario del outfit con id = " + usuarioId);
	}
}




