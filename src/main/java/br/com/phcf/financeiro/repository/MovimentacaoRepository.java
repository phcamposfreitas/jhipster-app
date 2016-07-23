package br.com.phcf.financeiro.repository;

import br.com.phcf.financeiro.domain.Movimentacao;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Movimentacao entity.
 */
@SuppressWarnings("unused")
public interface MovimentacaoRepository extends MongoRepository<Movimentacao,String> {

}
