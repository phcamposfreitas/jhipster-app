package br.com.phcf.financeiro.repository;

import br.com.phcf.financeiro.domain.Cliente;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Cliente entity.
 */
@SuppressWarnings("unused")
public interface ClienteRepository extends MongoRepository<Cliente,String> {

}
