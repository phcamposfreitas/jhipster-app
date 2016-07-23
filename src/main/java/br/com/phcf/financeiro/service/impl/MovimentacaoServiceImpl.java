package br.com.phcf.financeiro.service.impl;

import br.com.phcf.financeiro.service.MovimentacaoService;
import br.com.phcf.financeiro.domain.Movimentacao;
import br.com.phcf.financeiro.repository.MovimentacaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Movimentacao.
 */
@Service
public class MovimentacaoServiceImpl implements MovimentacaoService{

    private final Logger log = LoggerFactory.getLogger(MovimentacaoServiceImpl.class);
    
    @Inject
    private MovimentacaoRepository movimentacaoRepository;
    
    /**
     * Save a movimentacao.
     * 
     * @param movimentacao the entity to save
     * @return the persisted entity
     */
    public Movimentacao save(Movimentacao movimentacao) {
        log.debug("Request to save Movimentacao : {}", movimentacao);
        Movimentacao result = movimentacaoRepository.save(movimentacao);
        return result;
    }

    /**
     *  Get all the movimentacaos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<Movimentacao> findAll(Pageable pageable) {
        log.debug("Request to get all Movimentacaos");
        Page<Movimentacao> result = movimentacaoRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one movimentacao by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public Movimentacao findOne(String id) {
        log.debug("Request to get Movimentacao : {}", id);
        Movimentacao movimentacao = movimentacaoRepository.findOne(id);
        return movimentacao;
    }

    /**
     *  Delete the  movimentacao by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Movimentacao : {}", id);
        movimentacaoRepository.delete(id);
    }
}
