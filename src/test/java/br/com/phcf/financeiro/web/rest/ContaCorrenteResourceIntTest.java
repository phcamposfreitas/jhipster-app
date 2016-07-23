package br.com.phcf.financeiro.web.rest;

import br.com.phcf.financeiro.FinanceiroApp;
import br.com.phcf.financeiro.domain.ContaCorrente;
import br.com.phcf.financeiro.repository.ContaCorrenteRepository;
import br.com.phcf.financeiro.service.ContaCorrenteService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ContaCorrenteResource REST controller.
 *
 * @see ContaCorrenteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FinanceiroApp.class)
@WebAppConfiguration
@IntegrationTest
public class ContaCorrenteResourceIntTest {

    private static final String DEFAULT_CODIGO = "AAAAA";
    private static final String UPDATED_CODIGO = "BBBBB";

    private static final Double DEFAULT_SALDO = 1D;
    private static final Double UPDATED_SALDO = 2D;

    @Inject
    private ContaCorrenteRepository contaCorrenteRepository;

    @Inject
    private ContaCorrenteService contaCorrenteService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restContaCorrenteMockMvc;

    private ContaCorrente contaCorrente;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ContaCorrenteResource contaCorrenteResource = new ContaCorrenteResource();
        ReflectionTestUtils.setField(contaCorrenteResource, "contaCorrenteService", contaCorrenteService);
        this.restContaCorrenteMockMvc = MockMvcBuilders.standaloneSetup(contaCorrenteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        contaCorrenteRepository.deleteAll();
        contaCorrente = new ContaCorrente();
        contaCorrente.setCodigo(DEFAULT_CODIGO);
        contaCorrente.setSaldo(DEFAULT_SALDO);
    }

    @Test
    public void createContaCorrente() throws Exception {
        int databaseSizeBeforeCreate = contaCorrenteRepository.findAll().size();

        // Create the ContaCorrente

        restContaCorrenteMockMvc.perform(post("/api/conta-correntes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contaCorrente)))
                .andExpect(status().isCreated());

        // Validate the ContaCorrente in the database
        List<ContaCorrente> contaCorrentes = contaCorrenteRepository.findAll();
        assertThat(contaCorrentes).hasSize(databaseSizeBeforeCreate + 1);
        ContaCorrente testContaCorrente = contaCorrentes.get(contaCorrentes.size() - 1);
        assertThat(testContaCorrente.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testContaCorrente.getSaldo()).isEqualTo(DEFAULT_SALDO);
    }

    @Test
    public void checkSaldoIsRequired() throws Exception {
        int databaseSizeBeforeTest = contaCorrenteRepository.findAll().size();
        // set the field null
        contaCorrente.setSaldo(null);

        // Create the ContaCorrente, which fails.

        restContaCorrenteMockMvc.perform(post("/api/conta-correntes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contaCorrente)))
                .andExpect(status().isBadRequest());

        List<ContaCorrente> contaCorrentes = contaCorrenteRepository.findAll();
        assertThat(contaCorrentes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllContaCorrentes() throws Exception {
        // Initialize the database
        contaCorrenteRepository.save(contaCorrente);

        // Get all the contaCorrentes
        restContaCorrenteMockMvc.perform(get("/api/conta-correntes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(contaCorrente.getId())))
                .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO.toString())))
                .andExpect(jsonPath("$.[*].saldo").value(hasItem(DEFAULT_SALDO.doubleValue())));
    }

    @Test
    public void getContaCorrente() throws Exception {
        // Initialize the database
        contaCorrenteRepository.save(contaCorrente);

        // Get the contaCorrente
        restContaCorrenteMockMvc.perform(get("/api/conta-correntes/{id}", contaCorrente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(contaCorrente.getId()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO.toString()))
            .andExpect(jsonPath("$.saldo").value(DEFAULT_SALDO.doubleValue()));
    }

    @Test
    public void getNonExistingContaCorrente() throws Exception {
        // Get the contaCorrente
        restContaCorrenteMockMvc.perform(get("/api/conta-correntes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateContaCorrente() throws Exception {
        // Initialize the database
        contaCorrenteService.save(contaCorrente);

        int databaseSizeBeforeUpdate = contaCorrenteRepository.findAll().size();

        // Update the contaCorrente
        ContaCorrente updatedContaCorrente = new ContaCorrente();
        updatedContaCorrente.setId(contaCorrente.getId());
        updatedContaCorrente.setCodigo(UPDATED_CODIGO);
        updatedContaCorrente.setSaldo(UPDATED_SALDO);

        restContaCorrenteMockMvc.perform(put("/api/conta-correntes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedContaCorrente)))
                .andExpect(status().isOk());

        // Validate the ContaCorrente in the database
        List<ContaCorrente> contaCorrentes = contaCorrenteRepository.findAll();
        assertThat(contaCorrentes).hasSize(databaseSizeBeforeUpdate);
        ContaCorrente testContaCorrente = contaCorrentes.get(contaCorrentes.size() - 1);
        assertThat(testContaCorrente.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testContaCorrente.getSaldo()).isEqualTo(UPDATED_SALDO);
    }

    @Test
    public void deleteContaCorrente() throws Exception {
        // Initialize the database
        contaCorrenteService.save(contaCorrente);

        int databaseSizeBeforeDelete = contaCorrenteRepository.findAll().size();

        // Get the contaCorrente
        restContaCorrenteMockMvc.perform(delete("/api/conta-correntes/{id}", contaCorrente.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ContaCorrente> contaCorrentes = contaCorrenteRepository.findAll();
        assertThat(contaCorrentes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
