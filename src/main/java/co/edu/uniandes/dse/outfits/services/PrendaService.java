package co.edu.uniandes.dse.outfits.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import co.edu.uniandes.dse.outfits.entities.OutfitEntity;
import co.edu.uniandes.dse.outfits.entities.PrendaEntity;
import co.edu.uniandes.dse.outfits.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.outfits.exceptions.ErrorMessage;
import co.edu.uniandes.dse.outfits.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.outfits.repositories.ComentarioRepository;
import co.edu.uniandes.dse.outfits.repositories.MarcaRepository;
import co.edu.uniandes.dse.outfits.repositories.OutfitRepository;
import co.edu.uniandes.dse.outfits.repositories.PrendaRepository;


@Slf4j
@Service
public class PrendaService {
    @Autowired
    PrendaRepository prendaRepository;

    @Autowired
    MarcaRepository marcaRepository;

    @Autowired
    ComentarioRepository comentarioRepository;

    @Autowired
    OutfitRepository outfitRepository;

    @Transactional
    public PrendaEntity createPrenda(PrendaEntity prendaEntity) throws IllegalOperationException {
        log.info("Inicia proceso de creación de la prenda");
        if (prendaEntity.getMarca()== null){
            throw new IllegalOperationException("La prenda no tiene una marca asociada empty");
        }
        if (prendaEntity.getColores()== null){
            throw new IllegalOperationException("La prenda no tiene colores asociados null");
        }
        if (prendaEntity.getNombre()== null){
            throw new IllegalOperationException("La prenda no tiene un nombre asociado null");
        }
        if (prendaEntity.getNombre().isEmpty()){
            throw new IllegalOperationException("La prenda no tiene un nombre asociado empty");
        }
        if (prendaEntity.getOcaciones() == null){
            throw new IllegalOperationException("La prenda no tiene una ocasion asociada null");
        }
        if (prendaEntity.getRango_edad()== null){
            throw new IllegalOperationException("La prenda no tiene un rando de edad asociado null");
        }
        if (prendaEntity.getTalla() == null){
            throw new IllegalOperationException("La prenda no tiene una talla asociada null");
        }
        if (prendaEntity.getTalla().isEmpty()){
            throw new IllegalOperationException("La prenda no tiene una talla asociada empty");
        }
        if (prendaEntity.getUrl_sitio_web_compra()== null){
            throw new IllegalOperationException("La prenda no tiene un URL asociado null");
        }
        if (prendaEntity.getUrl_sitio_web_compra().equals("")){
            throw new IllegalOperationException("La prenda no tiene un URL asociado empty");
        }
        if (prendaEntity.getFoto()== null){
            throw new IllegalOperationException("La prenda no tiene una foto asociada null");
        }   
        if (prendaEntity.getFoto().isEmpty()){
            throw new IllegalOperationException("La prenda no tiene una foto asociada empty");
        }    
        if (prendaEntity.getCommentario()== null){
            throw new IllegalOperationException("La prenda no tiene un comentario asociado null");
        }

        log.info("Termina proceso de creación del prenda");
        return prendaRepository.save(prendaEntity);
    }
    @Transactional
    public List<PrendaEntity> getPrendas(){
        log.info("Inicia proceso de consultar todas las prendas");
        return prendaRepository.findAll();
    }

    @Transactional
    public PrendaEntity getPrenda(Long prendaId) throws EntityNotFoundException{
        log.info("Inicia proceso de actualizar la prenda con id = {0}", prendaId);
        Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);
        if(prendaEntity.isEmpty()){
            throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);
        }
        log.info("Termina proceso de consultar la prenda con id = {0}",prendaEntity);
        return prendaEntity.get();
    }
    

    @Transactional
    public PrendaEntity updatePrenda(Long prendaId,PrendaEntity prenda) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar la prenda con id = {0}", prendaId);
        Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);
        if (prendaEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);
        }
        if (prenda.getMarca()== null){
            throw new IllegalOperationException("La prenda no tiene una marca asociada empty");
        }
        if (prenda.getColores()== null){
            throw new IllegalOperationException("La prenda no tiene colores asociados null");
        }
        if (prenda.getNombre()== null){
            throw new IllegalOperationException("La prenda no tiene un nombre asociado null");
        }
        if (prenda.getNombre().isEmpty()){
            throw new IllegalOperationException("La prenda no tiene un nombre asociado empty");
        }
        if (prenda.getOcaciones() == null){
            throw new IllegalOperationException("La prenda no tiene una ocasion asociada null");
        }
        if (prenda.getRango_edad()== null){
            throw new IllegalOperationException("La prenda no tiene un rando de edad asociado null");
        }
        if (prenda.getTalla() == null){
            throw new IllegalOperationException("La prenda no tiene una talla asociada null");
        }
        if (prenda.getTalla().isEmpty()){
            throw new IllegalOperationException("La prenda no tiene una talla asociada empty");
        }
        if (prenda.getUrl_sitio_web_compra()== null){
            throw new IllegalOperationException("La prenda no tiene un URL asociado null");
        }
        if (prenda.getUrl_sitio_web_compra().equals("")){
            throw new IllegalOperationException("La prenda no tiene un URL asociado empty");
        }
        if (prenda.getFoto()== null){
            throw new IllegalOperationException("La prenda no tiene una foto asociada null");
        }   
        if (prenda.getFoto().isEmpty()){
            throw new IllegalOperationException("La prenda no tiene una foto asociada empty");
        }    
        if (prenda.getCommentario()== null){
            throw new IllegalOperationException("La prenda no tiene un comentario asociado null");
        }
        
        prenda.setId(prendaId);
        log.info("Termina proceso de actualizar la prenda con id = {0}", prendaId);
        return prendaRepository.save(prenda);
    }



    @Transactional
    public void deletePrenda(Long prendaId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de borrar la prenda con id = {0}", prendaId);
        Optional<PrendaEntity> prendaEntity = prendaRepository.findById(prendaId);
        if (prendaEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.PRENDA_NOT_FOUND);
        
        List<OutfitEntity> outfits = prendaEntity.get().getOutfits();
        
        if (!outfits.isEmpty()){
            throw new IllegalOperationException("Unable to delete prenda because it has associated outfits");
        }
        prendaRepository.deleteById(prendaId);
        log.info("Termina proceso de borrar la prenda con id = {0}", prendaId);
    

    }
}

