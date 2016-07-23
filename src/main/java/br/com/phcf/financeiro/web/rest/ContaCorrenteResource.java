package br.com.phcf.financeiro.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.phcf.financeiro.domain.ContaCorrente;
import br.com.phcf.financeiro.service.ContaCorrenteService;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ContaCorrente.
 */
@RestController
@RequestMapping("/api")
public class ContaCorrenteResource {

    private final Logger log = LoggerFactory.getLogger(ContaCorrenteResource.class);
        
    @Inject
    private ContaCorrenteService contaCorrenteService;
    
    /**
     * POST  /conta-correntes : Create a new contaCorrente.
     *
     * @param contaCorrente the contaCorrente to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contaCorrente, or with status 400 (Bad Request) if the contaCorrente has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/conta-correntes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContaCorrente> createContaCorrente(@Valid @RequestBody ContaCorrente contaCorrente) throws URISyntaxException {
        log.debug("REST request to save ContaCorrente : {}", contaCorrente);
        if (contaCorrente.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("contaCorrente", "idexists", "A new contaCorrente cannot already have an ID")).body(null);
        }
        ContaCorrente result = contaCorrenteService.save(contaCorrente);
        return ResponseEntity.created(new URI("/api/conta-correntes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("contaCorrente", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /conta-correntes : Updates an existing contaCorrente.
     *
     * @param contaCorrente the contaCorrente to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contaCorrente,
     * or with status 400 (Bad Request) if the contaCorrente is not valid,
     * or with status 500 (Internal Server Error) if the contaCorrente couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/conta-correntes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContaCorrente> updateContaCorrente(@Valid @RequestBody ContaCorrente contaCorrente) throws URISyntaxException {
        log.debug("REST request to update ContaCorrente : {}", contaCorrente);
        if (contaCorrente.getId() == null) {
            return createContaCorrente(contaCorrente);
        }
        ContaCorrente result = contaCorrenteService.save(contaCorrente);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("contaCorrente", contaCorrente.getId().toString()))
            .body(result);
    }

    /**
     * GET  /conta-correntes : get all the contaCorrentes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of contaCorrentes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/conta-correntes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ContaCorrente>> getAllContaCorrentes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ContaCorrentes");
        Page<ContaCorrente> page = contaCorrenteService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/conta-correntes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /conta-correntes/:id : get the "id" contaCorrente.
     *
     * @param id the id of the contaCorrente to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contaCorrente, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/conta-correntes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContaCorrente> getContaCorrente(@PathVariable String id) {
        log.debug("REST request to get ContaCorrente : {}", id);
        ContaCorrente contaCorrente = contaCorrenteService.findOne(id);
        return Optional.ofNullable(contaCorrente)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /conta-correntes/:id : delete the "id" contaCorrente.
     *
     * @param id the id of the contaCorrente to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/conta-correntes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteContaCorrente(@PathVariable String id) {
        log.debug("REST request to delete ContaCorrente : {}", id);
        contaCorrenteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("contaCorrente", id.toString())).build();
    }

}
