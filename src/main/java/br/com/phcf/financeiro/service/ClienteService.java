package br.com.phcf.financeiro.service;

import br.com.phcf.financeiro.domain.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Cliente.
 */
public interface ClienteService {

    /**
     * Save a cliente.
     * 
     * @param cliente the entity to save
     * @return the persisted entity
     */
    Cliente save(Cliente cliente);

    /**
     *  Get all the clientes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Cliente> findAll(Pageable pageable);

    /**
     *  Get the "id" cliente.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Cliente findOne(String id);

    /**
     *  Delete the "id" cliente.
     *  
     *  @param id the id of the entity
     */
    void delete(String id);
}
