package br.com.phcf.financeiro.service.impl;

import br.com.phcf.financeiro.service.ContaCorrenteService;
import br.com.phcf.financeiro.domain.ContaCorrente;
import br.com.phcf.financeiro.repository.ContaCorrenteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing ContaCorrente.
 */
@Service
public class ContaCorrenteServiceImpl implements ContaCorrenteService{

    private final Logger log = LoggerFactory.getLogger(ContaCorrenteServiceImpl.class);
    
    @Inject
    private ContaCorrenteRepository contaCorrenteRepository;
    
    /**
     * Save a contaCorrente.
     * 
     * @param contaCorrente the entity to save
     * @return the persisted entity
     */
    public ContaCorrente save(ContaCorrente contaCorrente) {
        log.debug("Request to save ContaCorrente : {}", contaCorrente);
        ContaCorrente result = contaCorrenteRepository.save(contaCorrente);
        return result;
    }

    /**
     *  Get all the contaCorrentes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<ContaCorrente> findAll(Pageable pageable) {
        log.debug("Request to get all ContaCorrentes");
        Page<ContaCorrente> result = contaCorrenteRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one contaCorrente by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public ContaCorrente findOne(String id) {
        log.debug("Request to get ContaCorrente : {}", id);
        ContaCorrente contaCorrente = contaCorrenteRepository.findOne(id);
        return contaCorrente;
    }

    /**
     *  Delete the  contaCorrente by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete ContaCorrente : {}", id);
        contaCorrenteRepository.delete(id);
    }
}
