package br.com.phcf.financeiro.service.impl;

import br.com.phcf.financeiro.service.ClienteService;
import br.com.phcf.financeiro.domain.Cliente;
import br.com.phcf.financeiro.repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Cliente.
 */
@Service
public class ClienteServiceImpl implements ClienteService{

    private final Logger log = LoggerFactory.getLogger(ClienteServiceImpl.class);
    
    @Inject
    private ClienteRepository clienteRepository;
    
    /**
     * Save a cliente.
     * 
     * @param cliente the entity to save
     * @return the persisted entity
     */
    public Cliente save(Cliente cliente) {
        log.debug("Request to save Cliente : {}", cliente);
        Cliente result = clienteRepository.save(cliente);
        return result;
    }

    /**
     *  Get all the clientes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    public Page<Cliente> findAll(Pageable pageable) {
        log.debug("Request to get all Clientes");
        Page<Cliente> result = clienteRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one cliente by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    public Cliente findOne(String id) {
        log.debug("Request to get Cliente : {}", id);
        Cliente cliente = clienteRepository.findOne(id);
        return cliente;
    }

    /**
     *  Delete the  cliente by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Cliente : {}", id);
        clienteRepository.delete(id);
    }
}
