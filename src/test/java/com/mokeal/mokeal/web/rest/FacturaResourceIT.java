package com.mokeal.mokeal.web.rest;

import static com.mokeal.mokeal.domain.FacturaAsserts.*;
import static com.mokeal.mokeal.web.rest.TestUtil.createUpdateProxyForBean;
import static com.mokeal.mokeal.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mokeal.mokeal.IntegrationTest;
import com.mokeal.mokeal.domain.Cliente;
import com.mokeal.mokeal.domain.Factura;
import com.mokeal.mokeal.domain.Servicio;
import com.mokeal.mokeal.domain.enumeration.EstadoFactura;
import com.mokeal.mokeal.repository.FacturaRepository;
import com.mokeal.mokeal.service.dto.FacturaDTO;
import com.mokeal.mokeal.service.mapper.FacturaMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FacturaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FacturaResourceIT {

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_EMISION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_EMISION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_EMISION = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_VENCIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_VENCIMIENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_VENCIMIENTO = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_BASE_IMPONIBLE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BASE_IMPONIBLE = new BigDecimal(2);
    private static final BigDecimal SMALLER_BASE_IMPONIBLE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_IVA = new BigDecimal(1);
    private static final BigDecimal UPDATED_IVA = new BigDecimal(2);
    private static final BigDecimal SMALLER_IVA = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TOTAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL = new BigDecimal(1 - 1);

    private static final EstadoFactura DEFAULT_ESTADO = EstadoFactura.BORRADOR;
    private static final EstadoFactura UPDATED_ESTADO = EstadoFactura.EMITIDA;

    private static final String DEFAULT_NOTAS = "AAAAAAAAAA";
    private static final String UPDATED_NOTAS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/facturas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private FacturaMapper facturaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacturaMockMvc;

    private Factura factura;

    private Factura insertedFactura;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Factura createEntity(EntityManager em) {
        Factura factura = new Factura()
            .numero(DEFAULT_NUMERO)
            .fechaEmision(DEFAULT_FECHA_EMISION)
            .fechaVencimiento(DEFAULT_FECHA_VENCIMIENTO)
            .baseImponible(DEFAULT_BASE_IMPONIBLE)
            .iva(DEFAULT_IVA)
            .total(DEFAULT_TOTAL)
            .estado(DEFAULT_ESTADO)
            .notas(DEFAULT_NOTAS);
        // Add required entity
        Servicio servicio;
        if (TestUtil.findAll(em, Servicio.class).isEmpty()) {
            servicio = ServicioResourceIT.createEntity(em);
            em.persist(servicio);
            em.flush();
        } else {
            servicio = TestUtil.findAll(em, Servicio.class).get(0);
        }
        factura.setServicio(servicio);
        // Add required entity
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            cliente = ClienteResourceIT.createEntity();
            em.persist(cliente);
            em.flush();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        factura.setCliente(cliente);
        return factura;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Factura createUpdatedEntity(EntityManager em) {
        Factura updatedFactura = new Factura()
            .numero(UPDATED_NUMERO)
            .fechaEmision(UPDATED_FECHA_EMISION)
            .fechaVencimiento(UPDATED_FECHA_VENCIMIENTO)
            .baseImponible(UPDATED_BASE_IMPONIBLE)
            .iva(UPDATED_IVA)
            .total(UPDATED_TOTAL)
            .estado(UPDATED_ESTADO)
            .notas(UPDATED_NOTAS);
        // Add required entity
        Servicio servicio;
        if (TestUtil.findAll(em, Servicio.class).isEmpty()) {
            servicio = ServicioResourceIT.createUpdatedEntity(em);
            em.persist(servicio);
            em.flush();
        } else {
            servicio = TestUtil.findAll(em, Servicio.class).get(0);
        }
        updatedFactura.setServicio(servicio);
        // Add required entity
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            cliente = ClienteResourceIT.createUpdatedEntity();
            em.persist(cliente);
            em.flush();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        updatedFactura.setCliente(cliente);
        return updatedFactura;
    }

    @BeforeEach
    void initTest() {
        factura = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedFactura != null) {
            facturaRepository.delete(insertedFactura);
            insertedFactura = null;
        }
    }

    @Test
    @Transactional
    void createFactura() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);
        var returnedFacturaDTO = om.readValue(
            restFacturaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facturaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FacturaDTO.class
        );

        // Validate the Factura in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFactura = facturaMapper.toEntity(returnedFacturaDTO);
        assertFacturaUpdatableFieldsEquals(returnedFactura, getPersistedFactura(returnedFactura));

        insertedFactura = returnedFactura;
    }

    @Test
    @Transactional
    void createFacturaWithExistingId() throws Exception {
        // Create the Factura with an existing ID
        factura.setId(1L);
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeroIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        factura.setNumero(null);

        // Create the Factura, which fails.
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaEmisionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        factura.setFechaEmision(null);

        // Create the Factura, which fails.
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBaseImponibleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        factura.setBaseImponible(null);

        // Create the Factura, which fails.
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIvaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        factura.setIva(null);

        // Create the Factura, which fails.
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        factura.setTotal(null);

        // Create the Factura, which fails.
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        factura.setEstado(null);

        // Create the Factura, which fails.
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFacturas() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList
        restFacturaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factura.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].fechaEmision").value(hasItem(DEFAULT_FECHA_EMISION.toString())))
            .andExpect(jsonPath("$.[*].fechaVencimiento").value(hasItem(DEFAULT_FECHA_VENCIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].baseImponible").value(hasItem(sameNumber(DEFAULT_BASE_IMPONIBLE))))
            .andExpect(jsonPath("$.[*].iva").value(hasItem(sameNumber(DEFAULT_IVA))))
            .andExpect(jsonPath("$.[*].total").value(hasItem(sameNumber(DEFAULT_TOTAL))))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].notas").value(hasItem(DEFAULT_NOTAS)));
    }

    @Test
    @Transactional
    void getFactura() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get the factura
        restFacturaMockMvc
            .perform(get(ENTITY_API_URL_ID, factura.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(factura.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.fechaEmision").value(DEFAULT_FECHA_EMISION.toString()))
            .andExpect(jsonPath("$.fechaVencimiento").value(DEFAULT_FECHA_VENCIMIENTO.toString()))
            .andExpect(jsonPath("$.baseImponible").value(sameNumber(DEFAULT_BASE_IMPONIBLE)))
            .andExpect(jsonPath("$.iva").value(sameNumber(DEFAULT_IVA)))
            .andExpect(jsonPath("$.total").value(sameNumber(DEFAULT_TOTAL)))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.notas").value(DEFAULT_NOTAS));
    }

    @Test
    @Transactional
    void getFacturasByIdFiltering() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        Long id = factura.getId();

        defaultFacturaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFacturaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFacturaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFacturasByNumeroIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where numero equals to
        defaultFacturaFiltering("numero.equals=" + DEFAULT_NUMERO, "numero.equals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllFacturasByNumeroIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where numero in
        defaultFacturaFiltering("numero.in=" + DEFAULT_NUMERO + "," + UPDATED_NUMERO, "numero.in=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllFacturasByNumeroIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where numero is not null
        defaultFacturaFiltering("numero.specified=true", "numero.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByNumeroContainsSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where numero contains
        defaultFacturaFiltering("numero.contains=" + DEFAULT_NUMERO, "numero.contains=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllFacturasByNumeroNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where numero does not contain
        defaultFacturaFiltering("numero.doesNotContain=" + UPDATED_NUMERO, "numero.doesNotContain=" + DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    void getAllFacturasByFechaEmisionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fechaEmision equals to
        defaultFacturaFiltering("fechaEmision.equals=" + DEFAULT_FECHA_EMISION, "fechaEmision.equals=" + UPDATED_FECHA_EMISION);
    }

    @Test
    @Transactional
    void getAllFacturasByFechaEmisionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fechaEmision in
        defaultFacturaFiltering(
            "fechaEmision.in=" + DEFAULT_FECHA_EMISION + "," + UPDATED_FECHA_EMISION,
            "fechaEmision.in=" + UPDATED_FECHA_EMISION
        );
    }

    @Test
    @Transactional
    void getAllFacturasByFechaEmisionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fechaEmision is not null
        defaultFacturaFiltering("fechaEmision.specified=true", "fechaEmision.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByFechaEmisionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fechaEmision is greater than or equal to
        defaultFacturaFiltering(
            "fechaEmision.greaterThanOrEqual=" + DEFAULT_FECHA_EMISION,
            "fechaEmision.greaterThanOrEqual=" + UPDATED_FECHA_EMISION
        );
    }

    @Test
    @Transactional
    void getAllFacturasByFechaEmisionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fechaEmision is less than or equal to
        defaultFacturaFiltering(
            "fechaEmision.lessThanOrEqual=" + DEFAULT_FECHA_EMISION,
            "fechaEmision.lessThanOrEqual=" + SMALLER_FECHA_EMISION
        );
    }

    @Test
    @Transactional
    void getAllFacturasByFechaEmisionIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fechaEmision is less than
        defaultFacturaFiltering("fechaEmision.lessThan=" + UPDATED_FECHA_EMISION, "fechaEmision.lessThan=" + DEFAULT_FECHA_EMISION);
    }

    @Test
    @Transactional
    void getAllFacturasByFechaEmisionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fechaEmision is greater than
        defaultFacturaFiltering("fechaEmision.greaterThan=" + SMALLER_FECHA_EMISION, "fechaEmision.greaterThan=" + DEFAULT_FECHA_EMISION);
    }

    @Test
    @Transactional
    void getAllFacturasByFechaVencimientoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fechaVencimiento equals to
        defaultFacturaFiltering(
            "fechaVencimiento.equals=" + DEFAULT_FECHA_VENCIMIENTO,
            "fechaVencimiento.equals=" + UPDATED_FECHA_VENCIMIENTO
        );
    }

    @Test
    @Transactional
    void getAllFacturasByFechaVencimientoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fechaVencimiento in
        defaultFacturaFiltering(
            "fechaVencimiento.in=" + DEFAULT_FECHA_VENCIMIENTO + "," + UPDATED_FECHA_VENCIMIENTO,
            "fechaVencimiento.in=" + UPDATED_FECHA_VENCIMIENTO
        );
    }

    @Test
    @Transactional
    void getAllFacturasByFechaVencimientoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fechaVencimiento is not null
        defaultFacturaFiltering("fechaVencimiento.specified=true", "fechaVencimiento.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByFechaVencimientoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fechaVencimiento is greater than or equal to
        defaultFacturaFiltering(
            "fechaVencimiento.greaterThanOrEqual=" + DEFAULT_FECHA_VENCIMIENTO,
            "fechaVencimiento.greaterThanOrEqual=" + UPDATED_FECHA_VENCIMIENTO
        );
    }

    @Test
    @Transactional
    void getAllFacturasByFechaVencimientoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fechaVencimiento is less than or equal to
        defaultFacturaFiltering(
            "fechaVencimiento.lessThanOrEqual=" + DEFAULT_FECHA_VENCIMIENTO,
            "fechaVencimiento.lessThanOrEqual=" + SMALLER_FECHA_VENCIMIENTO
        );
    }

    @Test
    @Transactional
    void getAllFacturasByFechaVencimientoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fechaVencimiento is less than
        defaultFacturaFiltering(
            "fechaVencimiento.lessThan=" + UPDATED_FECHA_VENCIMIENTO,
            "fechaVencimiento.lessThan=" + DEFAULT_FECHA_VENCIMIENTO
        );
    }

    @Test
    @Transactional
    void getAllFacturasByFechaVencimientoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where fechaVencimiento is greater than
        defaultFacturaFiltering(
            "fechaVencimiento.greaterThan=" + SMALLER_FECHA_VENCIMIENTO,
            "fechaVencimiento.greaterThan=" + DEFAULT_FECHA_VENCIMIENTO
        );
    }

    @Test
    @Transactional
    void getAllFacturasByBaseImponibleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where baseImponible equals to
        defaultFacturaFiltering("baseImponible.equals=" + DEFAULT_BASE_IMPONIBLE, "baseImponible.equals=" + UPDATED_BASE_IMPONIBLE);
    }

    @Test
    @Transactional
    void getAllFacturasByBaseImponibleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where baseImponible in
        defaultFacturaFiltering(
            "baseImponible.in=" + DEFAULT_BASE_IMPONIBLE + "," + UPDATED_BASE_IMPONIBLE,
            "baseImponible.in=" + UPDATED_BASE_IMPONIBLE
        );
    }

    @Test
    @Transactional
    void getAllFacturasByBaseImponibleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where baseImponible is not null
        defaultFacturaFiltering("baseImponible.specified=true", "baseImponible.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByBaseImponibleIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where baseImponible is greater than or equal to
        defaultFacturaFiltering(
            "baseImponible.greaterThanOrEqual=" + DEFAULT_BASE_IMPONIBLE,
            "baseImponible.greaterThanOrEqual=" + UPDATED_BASE_IMPONIBLE
        );
    }

    @Test
    @Transactional
    void getAllFacturasByBaseImponibleIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where baseImponible is less than or equal to
        defaultFacturaFiltering(
            "baseImponible.lessThanOrEqual=" + DEFAULT_BASE_IMPONIBLE,
            "baseImponible.lessThanOrEqual=" + SMALLER_BASE_IMPONIBLE
        );
    }

    @Test
    @Transactional
    void getAllFacturasByBaseImponibleIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where baseImponible is less than
        defaultFacturaFiltering("baseImponible.lessThan=" + UPDATED_BASE_IMPONIBLE, "baseImponible.lessThan=" + DEFAULT_BASE_IMPONIBLE);
    }

    @Test
    @Transactional
    void getAllFacturasByBaseImponibleIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where baseImponible is greater than
        defaultFacturaFiltering(
            "baseImponible.greaterThan=" + SMALLER_BASE_IMPONIBLE,
            "baseImponible.greaterThan=" + DEFAULT_BASE_IMPONIBLE
        );
    }

    @Test
    @Transactional
    void getAllFacturasByIvaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where iva equals to
        defaultFacturaFiltering("iva.equals=" + DEFAULT_IVA, "iva.equals=" + UPDATED_IVA);
    }

    @Test
    @Transactional
    void getAllFacturasByIvaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where iva in
        defaultFacturaFiltering("iva.in=" + DEFAULT_IVA + "," + UPDATED_IVA, "iva.in=" + UPDATED_IVA);
    }

    @Test
    @Transactional
    void getAllFacturasByIvaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where iva is not null
        defaultFacturaFiltering("iva.specified=true", "iva.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByIvaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where iva is greater than or equal to
        defaultFacturaFiltering("iva.greaterThanOrEqual=" + DEFAULT_IVA, "iva.greaterThanOrEqual=" + UPDATED_IVA);
    }

    @Test
    @Transactional
    void getAllFacturasByIvaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where iva is less than or equal to
        defaultFacturaFiltering("iva.lessThanOrEqual=" + DEFAULT_IVA, "iva.lessThanOrEqual=" + SMALLER_IVA);
    }

    @Test
    @Transactional
    void getAllFacturasByIvaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where iva is less than
        defaultFacturaFiltering("iva.lessThan=" + UPDATED_IVA, "iva.lessThan=" + DEFAULT_IVA);
    }

    @Test
    @Transactional
    void getAllFacturasByIvaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where iva is greater than
        defaultFacturaFiltering("iva.greaterThan=" + SMALLER_IVA, "iva.greaterThan=" + DEFAULT_IVA);
    }

    @Test
    @Transactional
    void getAllFacturasByTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where total equals to
        defaultFacturaFiltering("total.equals=" + DEFAULT_TOTAL, "total.equals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllFacturasByTotalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where total in
        defaultFacturaFiltering("total.in=" + DEFAULT_TOTAL + "," + UPDATED_TOTAL, "total.in=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllFacturasByTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where total is not null
        defaultFacturaFiltering("total.specified=true", "total.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where total is greater than or equal to
        defaultFacturaFiltering("total.greaterThanOrEqual=" + DEFAULT_TOTAL, "total.greaterThanOrEqual=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllFacturasByTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where total is less than or equal to
        defaultFacturaFiltering("total.lessThanOrEqual=" + DEFAULT_TOTAL, "total.lessThanOrEqual=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllFacturasByTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where total is less than
        defaultFacturaFiltering("total.lessThan=" + UPDATED_TOTAL, "total.lessThan=" + DEFAULT_TOTAL);
    }

    @Test
    @Transactional
    void getAllFacturasByTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where total is greater than
        defaultFacturaFiltering("total.greaterThan=" + SMALLER_TOTAL, "total.greaterThan=" + DEFAULT_TOTAL);
    }

    @Test
    @Transactional
    void getAllFacturasByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where estado equals to
        defaultFacturaFiltering("estado.equals=" + DEFAULT_ESTADO, "estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllFacturasByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where estado in
        defaultFacturaFiltering("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO, "estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllFacturasByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where estado is not null
        defaultFacturaFiltering("estado.specified=true", "estado.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByNotasIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where notas equals to
        defaultFacturaFiltering("notas.equals=" + DEFAULT_NOTAS, "notas.equals=" + UPDATED_NOTAS);
    }

    @Test
    @Transactional
    void getAllFacturasByNotasIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where notas in
        defaultFacturaFiltering("notas.in=" + DEFAULT_NOTAS + "," + UPDATED_NOTAS, "notas.in=" + UPDATED_NOTAS);
    }

    @Test
    @Transactional
    void getAllFacturasByNotasIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where notas is not null
        defaultFacturaFiltering("notas.specified=true", "notas.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturasByNotasContainsSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where notas contains
        defaultFacturaFiltering("notas.contains=" + DEFAULT_NOTAS, "notas.contains=" + UPDATED_NOTAS);
    }

    @Test
    @Transactional
    void getAllFacturasByNotasNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        // Get all the facturaList where notas does not contain
        defaultFacturaFiltering("notas.doesNotContain=" + UPDATED_NOTAS, "notas.doesNotContain=" + DEFAULT_NOTAS);
    }

    @Test
    @Transactional
    void getAllFacturasByServicioIsEqualToSomething() throws Exception {
        Servicio servicio;
        if (TestUtil.findAll(em, Servicio.class).isEmpty()) {
            facturaRepository.saveAndFlush(factura);
            servicio = ServicioResourceIT.createEntity(em);
        } else {
            servicio = TestUtil.findAll(em, Servicio.class).get(0);
        }
        em.persist(servicio);
        em.flush();
        factura.setServicio(servicio);
        facturaRepository.saveAndFlush(factura);
        Long servicioId = servicio.getId();
        // Get all the facturaList where servicio equals to servicioId
        defaultFacturaShouldBeFound("servicioId.equals=" + servicioId);

        // Get all the facturaList where servicio equals to (servicioId + 1)
        defaultFacturaShouldNotBeFound("servicioId.equals=" + (servicioId + 1));
    }

    @Test
    @Transactional
    void getAllFacturasByClienteIsEqualToSomething() throws Exception {
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            facturaRepository.saveAndFlush(factura);
            cliente = ClienteResourceIT.createEntity();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        em.persist(cliente);
        em.flush();
        factura.setCliente(cliente);
        facturaRepository.saveAndFlush(factura);
        Long clienteId = cliente.getId();
        // Get all the facturaList where cliente equals to clienteId
        defaultFacturaShouldBeFound("clienteId.equals=" + clienteId);

        // Get all the facturaList where cliente equals to (clienteId + 1)
        defaultFacturaShouldNotBeFound("clienteId.equals=" + (clienteId + 1));
    }

    private void defaultFacturaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFacturaShouldBeFound(shouldBeFound);
        defaultFacturaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFacturaShouldBeFound(String filter) throws Exception {
        restFacturaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factura.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].fechaEmision").value(hasItem(DEFAULT_FECHA_EMISION.toString())))
            .andExpect(jsonPath("$.[*].fechaVencimiento").value(hasItem(DEFAULT_FECHA_VENCIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].baseImponible").value(hasItem(sameNumber(DEFAULT_BASE_IMPONIBLE))))
            .andExpect(jsonPath("$.[*].iva").value(hasItem(sameNumber(DEFAULT_IVA))))
            .andExpect(jsonPath("$.[*].total").value(hasItem(sameNumber(DEFAULT_TOTAL))))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].notas").value(hasItem(DEFAULT_NOTAS)));

        // Check, that the count call also returns 1
        restFacturaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFacturaShouldNotBeFound(String filter) throws Exception {
        restFacturaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFacturaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFactura() throws Exception {
        // Get the factura
        restFacturaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFactura() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the factura
        Factura updatedFactura = facturaRepository.findById(factura.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFactura are not directly saved in db
        em.detach(updatedFactura);
        updatedFactura
            .numero(UPDATED_NUMERO)
            .fechaEmision(UPDATED_FECHA_EMISION)
            .fechaVencimiento(UPDATED_FECHA_VENCIMIENTO)
            .baseImponible(UPDATED_BASE_IMPONIBLE)
            .iva(UPDATED_IVA)
            .total(UPDATED_TOTAL)
            .estado(UPDATED_ESTADO)
            .notas(UPDATED_NOTAS);
        FacturaDTO facturaDTO = facturaMapper.toDto(updatedFactura);

        restFacturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facturaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facturaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Factura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFacturaToMatchAllProperties(updatedFactura);
    }

    @Test
    @Transactional
    void putNonExistingFactura() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        factura.setId(longCount.incrementAndGet());

        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facturaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facturaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFactura() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        factura.setId(longCount.incrementAndGet());

        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(facturaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFactura() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        factura.setId(longCount.incrementAndGet());

        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(facturaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Factura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFacturaWithPatch() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the factura using partial update
        Factura partialUpdatedFactura = new Factura();
        partialUpdatedFactura.setId(factura.getId());

        partialUpdatedFactura.numero(UPDATED_NUMERO).fechaEmision(UPDATED_FECHA_EMISION).iva(UPDATED_IVA).estado(UPDATED_ESTADO);

        restFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactura.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFactura))
            )
            .andExpect(status().isOk());

        // Validate the Factura in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFacturaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFactura, factura), getPersistedFactura(factura));
    }

    @Test
    @Transactional
    void fullUpdateFacturaWithPatch() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the factura using partial update
        Factura partialUpdatedFactura = new Factura();
        partialUpdatedFactura.setId(factura.getId());

        partialUpdatedFactura
            .numero(UPDATED_NUMERO)
            .fechaEmision(UPDATED_FECHA_EMISION)
            .fechaVencimiento(UPDATED_FECHA_VENCIMIENTO)
            .baseImponible(UPDATED_BASE_IMPONIBLE)
            .iva(UPDATED_IVA)
            .total(UPDATED_TOTAL)
            .estado(UPDATED_ESTADO)
            .notas(UPDATED_NOTAS);

        restFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactura.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFactura))
            )
            .andExpect(status().isOk());

        // Validate the Factura in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFacturaUpdatableFieldsEquals(partialUpdatedFactura, getPersistedFactura(partialUpdatedFactura));
    }

    @Test
    @Transactional
    void patchNonExistingFactura() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        factura.setId(longCount.incrementAndGet());

        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, facturaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(facturaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFactura() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        factura.setId(longCount.incrementAndGet());

        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(facturaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFactura() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        factura.setId(longCount.incrementAndGet());

        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(facturaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Factura in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFactura() throws Exception {
        // Initialize the database
        insertedFactura = facturaRepository.saveAndFlush(factura);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the factura
        restFacturaMockMvc
            .perform(delete(ENTITY_API_URL_ID, factura.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return facturaRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Factura getPersistedFactura(Factura factura) {
        return facturaRepository.findById(factura.getId()).orElseThrow();
    }

    protected void assertPersistedFacturaToMatchAllProperties(Factura expectedFactura) {
        assertFacturaAllPropertiesEquals(expectedFactura, getPersistedFactura(expectedFactura));
    }

    protected void assertPersistedFacturaToMatchUpdatableProperties(Factura expectedFactura) {
        assertFacturaAllUpdatablePropertiesEquals(expectedFactura, getPersistedFactura(expectedFactura));
    }
}
