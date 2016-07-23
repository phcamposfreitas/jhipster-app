package br.com.phcf.financeiro.service;

import br.com.phcf.financeiro.domain.Movimentacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Movimentacao.
 */
public interface MovimentacaoService {

    /**
     * Save a movimentacao.
     * 
     * @param movimentacao the entity to save
     * @return the persisted entity
     */
    Movimentacao save(Movimentacao movimentacao);

    /**
     *  Get all the movimentacaos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Movimentacao> findAll(Pageable pageable);

    /**
     *  Get the "id" movimentacao.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Movimentacao findOne(String id);

    /**
     *  Delete the "id" movimentacao.
     *  
     *  @param id the id of the entity
     */
    void delete(String id);
}
