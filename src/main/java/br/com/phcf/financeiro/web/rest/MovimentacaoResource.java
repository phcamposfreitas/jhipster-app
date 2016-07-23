package br.com.phcf.financeiro.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.phcf.financeiro.domain.Movimentacao;
import br.com.phcf.financeiro.service.MovimentacaoService;
import br.com.phcf.financeiro.web.rest.util.HeaderUtil;
import br.com.phcf.financeiro.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Movimentacao.
 */
@RestController
@RequestMapping("/api")
public class MovimentacaoResource {

    private final Logger log = LoggerFactory.getLogger(MovimentacaoResource.class);
        
    @Inject
    private MovimentacaoService movimentacaoService;
    
    /**
     * POST  /movimentacaos : Create a new movimentacao.
     *
     * @param movimentacao the movimentacao to create
     * @return the ResponseEntity with status 201 (Created) and with body the new movimentacao, or with status 400 (Bad Request) if the movimentacao has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/movimentacaos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Movimentacao> createMovimentacao(@RequestBody Movimentacao movimentacao) throws URISyntaxException {
        log.debug("REST request to save Movimentacao : {}", movimentacao);
        if (movimentacao.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("movimentacao", "idexists", "A new movimentacao cannot already have an ID")).body(null);
        }
        Movimentacao result = movimentacaoService.save(movimentacao);
        return ResponseEntity.created(new URI("/api/movimentacaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("movimentacao", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /movimentacaos : Updates an existing movimentacao.
     *
     * @param movimentacao the movimentacao to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated movimentacao,
     * or with status 400 (Bad Request) if the movimentacao is not valid,
     * or with status 500 (Internal Server Error) if the movimentacao couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/movimentacaos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Movimentacao> updateMovimentacao(@RequestBody Movimentacao movimentacao) throws URISyntaxException {
        log.debug("REST request to update Movimentacao : {}", movimentacao);
        if (movimentacao.getId() == null) {
            return createMovimentacao(movimentacao);
        }
        Movimentacao result = movimentacaoService.save(movimentacao);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("movimentacao", movimentacao.getId().toString()))
            .body(result);
    }

    /**
     * GET  /movimentacaos : get all the movimentacaos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of movimentacaos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/movimentacaos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Movimentacao>> getAllMovimentacaos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Movimentacaos");
        Page<Movimentacao> page = movimentacaoService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/movimentacaos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /movimentacaos/:id : get the "id" movimentacao.
     *
     * @param id the id of the movimentacao to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the movimentacao, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/movimentacaos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Movimentacao> getMovimentacao(@PathVariable String id) {
        log.debug("REST request to get Movimentacao : {}", id);
        Movimentacao movimentacao = movimentacaoService.findOne(id);
        return Optional.ofNullable(movimentacao)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /movimentacaos/:id : delete the "id" movimentacao.
     *
     * @param id the id of the movimentacao to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/movimentacaos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMovimentacao(@PathVariable String id) {
        log.debug("REST request to delete Movimentacao : {}", id);
        movimentacaoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("movimentacao", id.toString())).build();
    }

}
