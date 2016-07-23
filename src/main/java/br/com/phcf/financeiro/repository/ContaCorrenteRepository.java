package br.com.phcf.financeiro.repository;

import br.com.phcf.financeiro.domain.ContaCorrente;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the ContaCorrente entity.
 */
@SuppressWarnings("unused")
public interface ContaCorrenteRepository extends MongoRepository<ContaCorrente,String> {

}
