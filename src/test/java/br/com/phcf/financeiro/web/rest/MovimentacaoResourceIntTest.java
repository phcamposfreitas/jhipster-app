package br.com.phcf.financeiro.web.rest;

import br.com.phcf.financeiro.FinanceiroApp;
import br.com.phcf.financeiro.domain.Movimentacao;
import br.com.phcf.financeiro.repository.MovimentacaoRepository;
import br.com.phcf.financeiro.service.MovimentacaoService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.phcf.financeiro.domain.enumeration.TipoMovimentacao;

/**
 * Test class for the MovimentacaoResource REST controller.
 *
 * @see MovimentacaoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FinanceiroApp.class)
@WebAppConfiguration
@IntegrationTest
public class MovimentacaoResourceIntTest {


    private static final Double DEFAULT_VALOR = 1D;
    private static final Double UPDATED_VALOR = 2D;

    private static final TipoMovimentacao DEFAULT_TIPO = TipoMovimentacao.CREDITO;
    private static final TipoMovimentacao UPDATED_TIPO = TipoMovimentacao.DEBITO;

    private static final LocalDate DEFAULT_DATA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private MovimentacaoRepository movimentacaoRepository;

    @Inject
    private MovimentacaoService movimentacaoService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMovimentacaoMockMvc;

    private Movimentacao movimentacao;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MovimentacaoResource movimentacaoResource = new MovimentacaoResource();
        ReflectionTestUtils.setField(movimentacaoResource, "movimentacaoService", movimentacaoService);
        this.restMovimentacaoMockMvc = MockMvcBuilders.standaloneSetup(movimentacaoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        movimentacaoRepository.deleteAll();
        movimentacao = new Movimentacao();
        movimentacao.setValor(DEFAULT_VALOR);
        movimentacao.setTipo(DEFAULT_TIPO);
        movimentacao.setData(DEFAULT_DATA);
    }

    @Test
    public void createMovimentacao() throws Exception {
        int databaseSizeBeforeCreate = movimentacaoRepository.findAll().size();

        // Create the Movimentacao

        restMovimentacaoMockMvc.perform(post("/api/movimentacaos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(movimentacao)))
                .andExpect(status().isCreated());

        // Validate the Movimentacao in the database
        List<Movimentacao> movimentacaos = movimentacaoRepository.findAll();
        assertThat(movimentacaos).hasSize(databaseSizeBeforeCreate + 1);
        Movimentacao testMovimentacao = movimentacaos.get(movimentacaos.size() - 1);
        assertThat(testMovimentacao.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testMovimentacao.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testMovimentacao.getData()).isEqualTo(DEFAULT_DATA);
    }

    @Test
    public void getAllMovimentacaos() throws Exception {
        // Initialize the database
        movimentacaoRepository.save(movimentacao);

        // Get all the movimentacaos
        restMovimentacaoMockMvc.perform(get("/api/movimentacaos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(movimentacao.getId())))
                .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.doubleValue())))
                .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
                .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())));
    }

    @Test
    public void getMovimentacao() throws Exception {
        // Initialize the database
        movimentacaoRepository.save(movimentacao);

        // Get the movimentacao
        restMovimentacaoMockMvc.perform(get("/api/movimentacaos/{id}", movimentacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(movimentacao.getId()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.doubleValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()));
    }

    @Test
    public void getNonExistingMovimentacao() throws Exception {
        // Get the movimentacao
        restMovimentacaoMockMvc.perform(get("/api/movimentacaos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateMovimentacao() throws Exception {
        // Initialize the database
        movimentacaoService.save(movimentacao);

        int databaseSizeBeforeUpdate = movimentacaoRepository.findAll().size();

        // Update the movimentacao
        Movimentacao updatedMovimentacao = new Movimentacao();
        updatedMovimentacao.setId(movimentacao.getId());
        updatedMovimentacao.setValor(UPDATED_VALOR);
        updatedMovimentacao.setTipo(UPDATED_TIPO);
        updatedMovimentacao.setData(UPDATED_DATA);

        restMovimentacaoMockMvc.perform(put("/api/movimentacaos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMovimentacao)))
                .andExpect(status().isOk());

        // Validate the Movimentacao in the database
        List<Movimentacao> movimentacaos = movimentacaoRepository.findAll();
        assertThat(movimentacaos).hasSize(databaseSizeBeforeUpdate);
        Movimentacao testMovimentacao = movimentacaos.get(movimentacaos.size() - 1);
        assertThat(testMovimentacao.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testMovimentacao.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testMovimentacao.getData()).isEqualTo(UPDATED_DATA);
    }

    @Test
    public void deleteMovimentacao() throws Exception {
        // Initialize the database
        movimentacaoService.save(movimentacao);

        int databaseSizeBeforeDelete = movimentacaoRepository.findAll().size();

        // Get the movimentacao
        restMovimentacaoMockMvc.perform(delete("/api/movimentacaos/{id}", movimentacao.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Movimentacao> movimentacaos = movimentacaoRepository.findAll();
        assertThat(movimentacaos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
