package br.com.phcf.financeiro.service;

import br.com.phcf.financeiro.domain.ContaCorrente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing ContaCorrente.
 */
public interface ContaCorrenteService {

    /**
     * Save a contaCorrente.
     * 
     * @param contaCorrente the entity to save
     * @return the persisted entity
     */
    ContaCorrente save(ContaCorrente contaCorrente);

    /**
     *  Get all the contaCorrentes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ContaCorrente> findAll(Pageable pageable);

    /**
     *  Get the "id" contaCorrente.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    ContaCorrente findOne(String id);

    /**
     *  Delete the "id" contaCorrente.
     *  
     *  @param id the id of the entity
     */
    void delete(String id);
}
