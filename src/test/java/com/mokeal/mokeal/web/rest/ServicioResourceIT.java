package com.mokeal.mokeal.web.rest;

import static com.mokeal.mokeal.domain.ServicioAsserts.*;
import static com.mokeal.mokeal.web.rest.TestUtil.createUpdateProxyForBean;
import static com.mokeal.mokeal.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mokeal.mokeal.IntegrationTest;
import com.mokeal.mokeal.domain.Cliente;
import com.mokeal.mokeal.domain.Servicio;
import com.mokeal.mokeal.domain.Tarifa;
import com.mokeal.mokeal.domain.Trabajador;
import com.mokeal.mokeal.domain.enumeration.EstadoServicio;
import com.mokeal.mokeal.domain.enumeration.Frecuencia;
import com.mokeal.mokeal.domain.enumeration.TipoServicio;
import com.mokeal.mokeal.domain.enumeration.ZonaTarifa;
import com.mokeal.mokeal.repository.ServicioRepository;
import com.mokeal.mokeal.service.ServicioService;
import com.mokeal.mokeal.service.dto.ServicioDTO;
import com.mokeal.mokeal.service.mapper.ServicioMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ServicioResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ServicioResourceIT {

    private static final TipoServicio DEFAULT_TIPO_SERVICIO = TipoServicio.HOGAR;
    private static final TipoServicio UPDATED_TIPO_SERVICIO = TipoServicio.OFICINA;

    private static final ZonaTarifa DEFAULT_ZONA = ZonaTarifa.MADRID_CAPITAL;
    private static final ZonaTarifa UPDATED_ZONA = ZonaTarifa.COMUNIDAD_MADRID;

    private static final Frecuencia DEFAULT_FRECUENCIA = Frecuencia.PUNTUAL;
    private static final Frecuencia UPDATED_FRECUENCIA = Frecuencia.SEMANAL;

    private static final LocalDate DEFAULT_FECHA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_HORA_INICIO = "AAAAA";
    private static final String UPDATED_HORA_INICIO = "BBBBB";

    private static final BigDecimal DEFAULT_DURACION_HORAS = new BigDecimal(1);
    private static final BigDecimal UPDATED_DURACION_HORAS = new BigDecimal(2);
    private static final BigDecimal SMALLER_DURACION_HORAS = new BigDecimal(1 - 1);

    private static final Integer DEFAULT_NUM_TRABAJADORES = 1;
    private static final Integer UPDATED_NUM_TRABAJADORES = 2;
    private static final Integer SMALLER_NUM_TRABAJADORES = 1 - 1;

    private static final EstadoServicio DEFAULT_ESTADO = EstadoServicio.PENDIENTE;
    private static final EstadoServicio UPDATED_ESTADO = EstadoServicio.CONFIRMADO;

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_MUNICIPIO = "AAAAAAAAAA";
    private static final String UPDATED_MUNICIPIO = "BBBBBBBBBB";

    private static final String DEFAULT_NOTAS = "AAAAAAAAAA";
    private static final String UPDATED_NOTAS = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRECIO_TOTAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRECIO_TOTAL = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRECIO_TOTAL = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_DESCUENTO = new BigDecimal(1);
    private static final BigDecimal UPDATED_DESCUENTO = new BigDecimal(2);
    private static final BigDecimal SMALLER_DESCUENTO = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/servicios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ServicioRepository servicioRepository;

    @Mock
    private ServicioRepository servicioRepositoryMock;

    @Autowired
    private ServicioMapper servicioMapper;

    @Mock
    private ServicioService servicioServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServicioMockMvc;

    private Servicio servicio;

    private Servicio insertedServicio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Servicio createEntity(EntityManager em) {
        Servicio servicio = new Servicio()
            .tipoServicio(DEFAULT_TIPO_SERVICIO)
            .zona(DEFAULT_ZONA)
            .frecuencia(DEFAULT_FRECUENCIA)
            .fecha(DEFAULT_FECHA)
            .horaInicio(DEFAULT_HORA_INICIO)
            .duracionHoras(DEFAULT_DURACION_HORAS)
            .numTrabajadores(DEFAULT_NUM_TRABAJADORES)
            .estado(DEFAULT_ESTADO)
            .direccion(DEFAULT_DIRECCION)
            .municipio(DEFAULT_MUNICIPIO)
            .notas(DEFAULT_NOTAS)
            .precioTotal(DEFAULT_PRECIO_TOTAL)
            .descuento(DEFAULT_DESCUENTO);
        // Add required entity
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            cliente = ClienteResourceIT.createEntity();
            em.persist(cliente);
            em.flush();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        servicio.setCliente(cliente);
        return servicio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Servicio createUpdatedEntity(EntityManager em) {
        Servicio updatedServicio = new Servicio()
            .tipoServicio(UPDATED_TIPO_SERVICIO)
            .zona(UPDATED_ZONA)
            .frecuencia(UPDATED_FRECUENCIA)
            .fecha(UPDATED_FECHA)
            .horaInicio(UPDATED_HORA_INICIO)
            .duracionHoras(UPDATED_DURACION_HORAS)
            .numTrabajadores(UPDATED_NUM_TRABAJADORES)
            .estado(UPDATED_ESTADO)
            .direccion(UPDATED_DIRECCION)
            .municipio(UPDATED_MUNICIPIO)
            .notas(UPDATED_NOTAS)
            .precioTotal(UPDATED_PRECIO_TOTAL)
            .descuento(UPDATED_DESCUENTO);
        // Add required entity
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            cliente = ClienteResourceIT.createUpdatedEntity();
            em.persist(cliente);
            em.flush();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        updatedServicio.setCliente(cliente);
        return updatedServicio;
    }

    @BeforeEach
    void initTest() {
        servicio = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedServicio != null) {
            servicioRepository.delete(insertedServicio);
            insertedServicio = null;
        }
    }

    @Test
    @Transactional
    void createServicio() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Servicio
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);
        var returnedServicioDTO = om.readValue(
            restServicioMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ServicioDTO.class
        );

        // Validate the Servicio in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedServicio = servicioMapper.toEntity(returnedServicioDTO);
        assertServicioUpdatableFieldsEquals(returnedServicio, getPersistedServicio(returnedServicio));

        insertedServicio = returnedServicio;
    }

    @Test
    @Transactional
    void createServicioWithExistingId() throws Exception {
        // Create the Servicio with an existing ID
        servicio.setId(1L);
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Servicio in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTipoServicioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        servicio.setTipoServicio(null);

        // Create the Servicio, which fails.
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        restServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkZonaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        servicio.setZona(null);

        // Create the Servicio, which fails.
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        restServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFrecuenciaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        servicio.setFrecuencia(null);

        // Create the Servicio, which fails.
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        restServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        servicio.setFecha(null);

        // Create the Servicio, which fails.
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        restServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHoraInicioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        servicio.setHoraInicio(null);

        // Create the Servicio, which fails.
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        restServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDuracionHorasIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        servicio.setDuracionHoras(null);

        // Create the Servicio, which fails.
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        restServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumTrabajadoresIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        servicio.setNumTrabajadores(null);

        // Create the Servicio, which fails.
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        restServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        servicio.setEstado(null);

        // Create the Servicio, which fails.
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        restServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServicios() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList
        restServicioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(servicio.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipoServicio").value(hasItem(DEFAULT_TIPO_SERVICIO.toString())))
            .andExpect(jsonPath("$.[*].zona").value(hasItem(DEFAULT_ZONA.toString())))
            .andExpect(jsonPath("$.[*].frecuencia").value(hasItem(DEFAULT_FRECUENCIA.toString())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].horaInicio").value(hasItem(DEFAULT_HORA_INICIO)))
            .andExpect(jsonPath("$.[*].duracionHoras").value(hasItem(sameNumber(DEFAULT_DURACION_HORAS))))
            .andExpect(jsonPath("$.[*].numTrabajadores").value(hasItem(DEFAULT_NUM_TRABAJADORES)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].municipio").value(hasItem(DEFAULT_MUNICIPIO)))
            .andExpect(jsonPath("$.[*].notas").value(hasItem(DEFAULT_NOTAS)))
            .andExpect(jsonPath("$.[*].precioTotal").value(hasItem(sameNumber(DEFAULT_PRECIO_TOTAL))))
            .andExpect(jsonPath("$.[*].descuento").value(hasItem(sameNumber(DEFAULT_DESCUENTO))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServiciosWithEagerRelationshipsIsEnabled() throws Exception {
        when(servicioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restServicioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(servicioServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServiciosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(servicioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restServicioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(servicioRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getServicio() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get the servicio
        restServicioMockMvc
            .perform(get(ENTITY_API_URL_ID, servicio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(servicio.getId().intValue()))
            .andExpect(jsonPath("$.tipoServicio").value(DEFAULT_TIPO_SERVICIO.toString()))
            .andExpect(jsonPath("$.zona").value(DEFAULT_ZONA.toString()))
            .andExpect(jsonPath("$.frecuencia").value(DEFAULT_FRECUENCIA.toString()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.horaInicio").value(DEFAULT_HORA_INICIO))
            .andExpect(jsonPath("$.duracionHoras").value(sameNumber(DEFAULT_DURACION_HORAS)))
            .andExpect(jsonPath("$.numTrabajadores").value(DEFAULT_NUM_TRABAJADORES))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.municipio").value(DEFAULT_MUNICIPIO))
            .andExpect(jsonPath("$.notas").value(DEFAULT_NOTAS))
            .andExpect(jsonPath("$.precioTotal").value(sameNumber(DEFAULT_PRECIO_TOTAL)))
            .andExpect(jsonPath("$.descuento").value(sameNumber(DEFAULT_DESCUENTO)));
    }

    @Test
    @Transactional
    void getServiciosByIdFiltering() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        Long id = servicio.getId();

        defaultServicioFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultServicioFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultServicioFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllServiciosByTipoServicioIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where tipoServicio equals to
        defaultServicioFiltering("tipoServicio.equals=" + DEFAULT_TIPO_SERVICIO, "tipoServicio.equals=" + UPDATED_TIPO_SERVICIO);
    }

    @Test
    @Transactional
    void getAllServiciosByTipoServicioIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where tipoServicio in
        defaultServicioFiltering(
            "tipoServicio.in=" + DEFAULT_TIPO_SERVICIO + "," + UPDATED_TIPO_SERVICIO,
            "tipoServicio.in=" + UPDATED_TIPO_SERVICIO
        );
    }

    @Test
    @Transactional
    void getAllServiciosByTipoServicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where tipoServicio is not null
        defaultServicioFiltering("tipoServicio.specified=true", "tipoServicio.specified=false");
    }

    @Test
    @Transactional
    void getAllServiciosByZonaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where zona equals to
        defaultServicioFiltering("zona.equals=" + DEFAULT_ZONA, "zona.equals=" + UPDATED_ZONA);
    }

    @Test
    @Transactional
    void getAllServiciosByZonaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where zona in
        defaultServicioFiltering("zona.in=" + DEFAULT_ZONA + "," + UPDATED_ZONA, "zona.in=" + UPDATED_ZONA);
    }

    @Test
    @Transactional
    void getAllServiciosByZonaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where zona is not null
        defaultServicioFiltering("zona.specified=true", "zona.specified=false");
    }

    @Test
    @Transactional
    void getAllServiciosByFrecuenciaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where frecuencia equals to
        defaultServicioFiltering("frecuencia.equals=" + DEFAULT_FRECUENCIA, "frecuencia.equals=" + UPDATED_FRECUENCIA);
    }

    @Test
    @Transactional
    void getAllServiciosByFrecuenciaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where frecuencia in
        defaultServicioFiltering("frecuencia.in=" + DEFAULT_FRECUENCIA + "," + UPDATED_FRECUENCIA, "frecuencia.in=" + UPDATED_FRECUENCIA);
    }

    @Test
    @Transactional
    void getAllServiciosByFrecuenciaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where frecuencia is not null
        defaultServicioFiltering("frecuencia.specified=true", "frecuencia.specified=false");
    }

    @Test
    @Transactional
    void getAllServiciosByFechaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where fecha equals to
        defaultServicioFiltering("fecha.equals=" + DEFAULT_FECHA, "fecha.equals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllServiciosByFechaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where fecha in
        defaultServicioFiltering("fecha.in=" + DEFAULT_FECHA + "," + UPDATED_FECHA, "fecha.in=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllServiciosByFechaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where fecha is not null
        defaultServicioFiltering("fecha.specified=true", "fecha.specified=false");
    }

    @Test
    @Transactional
    void getAllServiciosByFechaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where fecha is greater than or equal to
        defaultServicioFiltering("fecha.greaterThanOrEqual=" + DEFAULT_FECHA, "fecha.greaterThanOrEqual=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllServiciosByFechaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where fecha is less than or equal to
        defaultServicioFiltering("fecha.lessThanOrEqual=" + DEFAULT_FECHA, "fecha.lessThanOrEqual=" + SMALLER_FECHA);
    }

    @Test
    @Transactional
    void getAllServiciosByFechaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where fecha is less than
        defaultServicioFiltering("fecha.lessThan=" + UPDATED_FECHA, "fecha.lessThan=" + DEFAULT_FECHA);
    }

    @Test
    @Transactional
    void getAllServiciosByFechaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where fecha is greater than
        defaultServicioFiltering("fecha.greaterThan=" + SMALLER_FECHA, "fecha.greaterThan=" + DEFAULT_FECHA);
    }

    @Test
    @Transactional
    void getAllServiciosByHoraInicioIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where horaInicio equals to
        defaultServicioFiltering("horaInicio.equals=" + DEFAULT_HORA_INICIO, "horaInicio.equals=" + UPDATED_HORA_INICIO);
    }

    @Test
    @Transactional
    void getAllServiciosByHoraInicioIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where horaInicio in
        defaultServicioFiltering(
            "horaInicio.in=" + DEFAULT_HORA_INICIO + "," + UPDATED_HORA_INICIO,
            "horaInicio.in=" + UPDATED_HORA_INICIO
        );
    }

    @Test
    @Transactional
    void getAllServiciosByHoraInicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where horaInicio is not null
        defaultServicioFiltering("horaInicio.specified=true", "horaInicio.specified=false");
    }

    @Test
    @Transactional
    void getAllServiciosByHoraInicioContainsSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where horaInicio contains
        defaultServicioFiltering("horaInicio.contains=" + DEFAULT_HORA_INICIO, "horaInicio.contains=" + UPDATED_HORA_INICIO);
    }

    @Test
    @Transactional
    void getAllServiciosByHoraInicioNotContainsSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where horaInicio does not contain
        defaultServicioFiltering("horaInicio.doesNotContain=" + UPDATED_HORA_INICIO, "horaInicio.doesNotContain=" + DEFAULT_HORA_INICIO);
    }

    @Test
    @Transactional
    void getAllServiciosByDuracionHorasIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where duracionHoras equals to
        defaultServicioFiltering("duracionHoras.equals=" + DEFAULT_DURACION_HORAS, "duracionHoras.equals=" + UPDATED_DURACION_HORAS);
    }

    @Test
    @Transactional
    void getAllServiciosByDuracionHorasIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where duracionHoras in
        defaultServicioFiltering(
            "duracionHoras.in=" + DEFAULT_DURACION_HORAS + "," + UPDATED_DURACION_HORAS,
            "duracionHoras.in=" + UPDATED_DURACION_HORAS
        );
    }

    @Test
    @Transactional
    void getAllServiciosByDuracionHorasIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where duracionHoras is not null
        defaultServicioFiltering("duracionHoras.specified=true", "duracionHoras.specified=false");
    }

    @Test
    @Transactional
    void getAllServiciosByDuracionHorasIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where duracionHoras is greater than or equal to
        defaultServicioFiltering(
            "duracionHoras.greaterThanOrEqual=" + DEFAULT_DURACION_HORAS,
            "duracionHoras.greaterThanOrEqual=" + UPDATED_DURACION_HORAS
        );
    }

    @Test
    @Transactional
    void getAllServiciosByDuracionHorasIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where duracionHoras is less than or equal to
        defaultServicioFiltering(
            "duracionHoras.lessThanOrEqual=" + DEFAULT_DURACION_HORAS,
            "duracionHoras.lessThanOrEqual=" + SMALLER_DURACION_HORAS
        );
    }

    @Test
    @Transactional
    void getAllServiciosByDuracionHorasIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where duracionHoras is less than
        defaultServicioFiltering("duracionHoras.lessThan=" + UPDATED_DURACION_HORAS, "duracionHoras.lessThan=" + DEFAULT_DURACION_HORAS);
    }

    @Test
    @Transactional
    void getAllServiciosByDuracionHorasIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where duracionHoras is greater than
        defaultServicioFiltering(
            "duracionHoras.greaterThan=" + SMALLER_DURACION_HORAS,
            "duracionHoras.greaterThan=" + DEFAULT_DURACION_HORAS
        );
    }

    @Test
    @Transactional
    void getAllServiciosByNumTrabajadoresIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where numTrabajadores equals to
        defaultServicioFiltering(
            "numTrabajadores.equals=" + DEFAULT_NUM_TRABAJADORES,
            "numTrabajadores.equals=" + UPDATED_NUM_TRABAJADORES
        );
    }

    @Test
    @Transactional
    void getAllServiciosByNumTrabajadoresIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where numTrabajadores in
        defaultServicioFiltering(
            "numTrabajadores.in=" + DEFAULT_NUM_TRABAJADORES + "," + UPDATED_NUM_TRABAJADORES,
            "numTrabajadores.in=" + UPDATED_NUM_TRABAJADORES
        );
    }

    @Test
    @Transactional
    void getAllServiciosByNumTrabajadoresIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where numTrabajadores is not null
        defaultServicioFiltering("numTrabajadores.specified=true", "numTrabajadores.specified=false");
    }

    @Test
    @Transactional
    void getAllServiciosByNumTrabajadoresIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where numTrabajadores is greater than or equal to
        defaultServicioFiltering(
            "numTrabajadores.greaterThanOrEqual=" + DEFAULT_NUM_TRABAJADORES,
            "numTrabajadores.greaterThanOrEqual=" + UPDATED_NUM_TRABAJADORES
        );
    }

    @Test
    @Transactional
    void getAllServiciosByNumTrabajadoresIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where numTrabajadores is less than or equal to
        defaultServicioFiltering(
            "numTrabajadores.lessThanOrEqual=" + DEFAULT_NUM_TRABAJADORES,
            "numTrabajadores.lessThanOrEqual=" + SMALLER_NUM_TRABAJADORES
        );
    }

    @Test
    @Transactional
    void getAllServiciosByNumTrabajadoresIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where numTrabajadores is less than
        defaultServicioFiltering(
            "numTrabajadores.lessThan=" + UPDATED_NUM_TRABAJADORES,
            "numTrabajadores.lessThan=" + DEFAULT_NUM_TRABAJADORES
        );
    }

    @Test
    @Transactional
    void getAllServiciosByNumTrabajadoresIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where numTrabajadores is greater than
        defaultServicioFiltering(
            "numTrabajadores.greaterThan=" + SMALLER_NUM_TRABAJADORES,
            "numTrabajadores.greaterThan=" + DEFAULT_NUM_TRABAJADORES
        );
    }

    @Test
    @Transactional
    void getAllServiciosByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where estado equals to
        defaultServicioFiltering("estado.equals=" + DEFAULT_ESTADO, "estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllServiciosByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where estado in
        defaultServicioFiltering("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO, "estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllServiciosByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where estado is not null
        defaultServicioFiltering("estado.specified=true", "estado.specified=false");
    }

    @Test
    @Transactional
    void getAllServiciosByDireccionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where direccion equals to
        defaultServicioFiltering("direccion.equals=" + DEFAULT_DIRECCION, "direccion.equals=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllServiciosByDireccionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where direccion in
        defaultServicioFiltering("direccion.in=" + DEFAULT_DIRECCION + "," + UPDATED_DIRECCION, "direccion.in=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllServiciosByDireccionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where direccion is not null
        defaultServicioFiltering("direccion.specified=true", "direccion.specified=false");
    }

    @Test
    @Transactional
    void getAllServiciosByDireccionContainsSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where direccion contains
        defaultServicioFiltering("direccion.contains=" + DEFAULT_DIRECCION, "direccion.contains=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllServiciosByDireccionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where direccion does not contain
        defaultServicioFiltering("direccion.doesNotContain=" + UPDATED_DIRECCION, "direccion.doesNotContain=" + DEFAULT_DIRECCION);
    }

    @Test
    @Transactional
    void getAllServiciosByMunicipioIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where municipio equals to
        defaultServicioFiltering("municipio.equals=" + DEFAULT_MUNICIPIO, "municipio.equals=" + UPDATED_MUNICIPIO);
    }

    @Test
    @Transactional
    void getAllServiciosByMunicipioIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where municipio in
        defaultServicioFiltering("municipio.in=" + DEFAULT_MUNICIPIO + "," + UPDATED_MUNICIPIO, "municipio.in=" + UPDATED_MUNICIPIO);
    }

    @Test
    @Transactional
    void getAllServiciosByMunicipioIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where municipio is not null
        defaultServicioFiltering("municipio.specified=true", "municipio.specified=false");
    }

    @Test
    @Transactional
    void getAllServiciosByMunicipioContainsSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where municipio contains
        defaultServicioFiltering("municipio.contains=" + DEFAULT_MUNICIPIO, "municipio.contains=" + UPDATED_MUNICIPIO);
    }

    @Test
    @Transactional
    void getAllServiciosByMunicipioNotContainsSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where municipio does not contain
        defaultServicioFiltering("municipio.doesNotContain=" + UPDATED_MUNICIPIO, "municipio.doesNotContain=" + DEFAULT_MUNICIPIO);
    }

    @Test
    @Transactional
    void getAllServiciosByNotasIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where notas equals to
        defaultServicioFiltering("notas.equals=" + DEFAULT_NOTAS, "notas.equals=" + UPDATED_NOTAS);
    }

    @Test
    @Transactional
    void getAllServiciosByNotasIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where notas in
        defaultServicioFiltering("notas.in=" + DEFAULT_NOTAS + "," + UPDATED_NOTAS, "notas.in=" + UPDATED_NOTAS);
    }

    @Test
    @Transactional
    void getAllServiciosByNotasIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where notas is not null
        defaultServicioFiltering("notas.specified=true", "notas.specified=false");
    }

    @Test
    @Transactional
    void getAllServiciosByNotasContainsSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where notas contains
        defaultServicioFiltering("notas.contains=" + DEFAULT_NOTAS, "notas.contains=" + UPDATED_NOTAS);
    }

    @Test
    @Transactional
    void getAllServiciosByNotasNotContainsSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where notas does not contain
        defaultServicioFiltering("notas.doesNotContain=" + UPDATED_NOTAS, "notas.doesNotContain=" + DEFAULT_NOTAS);
    }

    @Test
    @Transactional
    void getAllServiciosByPrecioTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where precioTotal equals to
        defaultServicioFiltering("precioTotal.equals=" + DEFAULT_PRECIO_TOTAL, "precioTotal.equals=" + UPDATED_PRECIO_TOTAL);
    }

    @Test
    @Transactional
    void getAllServiciosByPrecioTotalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where precioTotal in
        defaultServicioFiltering(
            "precioTotal.in=" + DEFAULT_PRECIO_TOTAL + "," + UPDATED_PRECIO_TOTAL,
            "precioTotal.in=" + UPDATED_PRECIO_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllServiciosByPrecioTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where precioTotal is not null
        defaultServicioFiltering("precioTotal.specified=true", "precioTotal.specified=false");
    }

    @Test
    @Transactional
    void getAllServiciosByPrecioTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where precioTotal is greater than or equal to
        defaultServicioFiltering(
            "precioTotal.greaterThanOrEqual=" + DEFAULT_PRECIO_TOTAL,
            "precioTotal.greaterThanOrEqual=" + UPDATED_PRECIO_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllServiciosByPrecioTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where precioTotal is less than or equal to
        defaultServicioFiltering(
            "precioTotal.lessThanOrEqual=" + DEFAULT_PRECIO_TOTAL,
            "precioTotal.lessThanOrEqual=" + SMALLER_PRECIO_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllServiciosByPrecioTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where precioTotal is less than
        defaultServicioFiltering("precioTotal.lessThan=" + UPDATED_PRECIO_TOTAL, "precioTotal.lessThan=" + DEFAULT_PRECIO_TOTAL);
    }

    @Test
    @Transactional
    void getAllServiciosByPrecioTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where precioTotal is greater than
        defaultServicioFiltering("precioTotal.greaterThan=" + SMALLER_PRECIO_TOTAL, "precioTotal.greaterThan=" + DEFAULT_PRECIO_TOTAL);
    }

    @Test
    @Transactional
    void getAllServiciosByDescuentoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where descuento equals to
        defaultServicioFiltering("descuento.equals=" + DEFAULT_DESCUENTO, "descuento.equals=" + UPDATED_DESCUENTO);
    }

    @Test
    @Transactional
    void getAllServiciosByDescuentoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where descuento in
        defaultServicioFiltering("descuento.in=" + DEFAULT_DESCUENTO + "," + UPDATED_DESCUENTO, "descuento.in=" + UPDATED_DESCUENTO);
    }

    @Test
    @Transactional
    void getAllServiciosByDescuentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where descuento is not null
        defaultServicioFiltering("descuento.specified=true", "descuento.specified=false");
    }

    @Test
    @Transactional
    void getAllServiciosByDescuentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where descuento is greater than or equal to
        defaultServicioFiltering("descuento.greaterThanOrEqual=" + DEFAULT_DESCUENTO, "descuento.greaterThanOrEqual=" + UPDATED_DESCUENTO);
    }

    @Test
    @Transactional
    void getAllServiciosByDescuentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where descuento is less than or equal to
        defaultServicioFiltering("descuento.lessThanOrEqual=" + DEFAULT_DESCUENTO, "descuento.lessThanOrEqual=" + SMALLER_DESCUENTO);
    }

    @Test
    @Transactional
    void getAllServiciosByDescuentoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where descuento is less than
        defaultServicioFiltering("descuento.lessThan=" + UPDATED_DESCUENTO, "descuento.lessThan=" + DEFAULT_DESCUENTO);
    }

    @Test
    @Transactional
    void getAllServiciosByDescuentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where descuento is greater than
        defaultServicioFiltering("descuento.greaterThan=" + SMALLER_DESCUENTO, "descuento.greaterThan=" + DEFAULT_DESCUENTO);
    }

    @Test
    @Transactional
    void getAllServiciosByClienteIsEqualToSomething() throws Exception {
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            servicioRepository.saveAndFlush(servicio);
            cliente = ClienteResourceIT.createEntity();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        em.persist(cliente);
        em.flush();
        servicio.setCliente(cliente);
        servicioRepository.saveAndFlush(servicio);
        Long clienteId = cliente.getId();
        // Get all the servicioList where cliente equals to clienteId
        defaultServicioShouldBeFound("clienteId.equals=" + clienteId);

        // Get all the servicioList where cliente equals to (clienteId + 1)
        defaultServicioShouldNotBeFound("clienteId.equals=" + (clienteId + 1));
    }

    @Test
    @Transactional
    void getAllServiciosByTarifaIsEqualToSomething() throws Exception {
        Tarifa tarifa;
        if (TestUtil.findAll(em, Tarifa.class).isEmpty()) {
            servicioRepository.saveAndFlush(servicio);
            tarifa = TarifaResourceIT.createEntity();
        } else {
            tarifa = TestUtil.findAll(em, Tarifa.class).get(0);
        }
        em.persist(tarifa);
        em.flush();
        servicio.setTarifa(tarifa);
        servicioRepository.saveAndFlush(servicio);
        Long tarifaId = tarifa.getId();
        // Get all the servicioList where tarifa equals to tarifaId
        defaultServicioShouldBeFound("tarifaId.equals=" + tarifaId);

        // Get all the servicioList where tarifa equals to (tarifaId + 1)
        defaultServicioShouldNotBeFound("tarifaId.equals=" + (tarifaId + 1));
    }

    @Test
    @Transactional
    void getAllServiciosByTrabajadoresIsEqualToSomething() throws Exception {
        Trabajador trabajadores;
        if (TestUtil.findAll(em, Trabajador.class).isEmpty()) {
            servicioRepository.saveAndFlush(servicio);
            trabajadores = TrabajadorResourceIT.createEntity();
        } else {
            trabajadores = TestUtil.findAll(em, Trabajador.class).get(0);
        }
        em.persist(trabajadores);
        em.flush();
        servicio.addTrabajadores(trabajadores);
        servicioRepository.saveAndFlush(servicio);
        Long trabajadoresId = trabajadores.getId();
        // Get all the servicioList where trabajadores equals to trabajadoresId
        defaultServicioShouldBeFound("trabajadoresId.equals=" + trabajadoresId);

        // Get all the servicioList where trabajadores equals to (trabajadoresId + 1)
        defaultServicioShouldNotBeFound("trabajadoresId.equals=" + (trabajadoresId + 1));
    }

    private void defaultServicioFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultServicioShouldBeFound(shouldBeFound);
        defaultServicioShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultServicioShouldBeFound(String filter) throws Exception {
        restServicioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(servicio.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipoServicio").value(hasItem(DEFAULT_TIPO_SERVICIO.toString())))
            .andExpect(jsonPath("$.[*].zona").value(hasItem(DEFAULT_ZONA.toString())))
            .andExpect(jsonPath("$.[*].frecuencia").value(hasItem(DEFAULT_FRECUENCIA.toString())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].horaInicio").value(hasItem(DEFAULT_HORA_INICIO)))
            .andExpect(jsonPath("$.[*].duracionHoras").value(hasItem(sameNumber(DEFAULT_DURACION_HORAS))))
            .andExpect(jsonPath("$.[*].numTrabajadores").value(hasItem(DEFAULT_NUM_TRABAJADORES)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].municipio").value(hasItem(DEFAULT_MUNICIPIO)))
            .andExpect(jsonPath("$.[*].notas").value(hasItem(DEFAULT_NOTAS)))
            .andExpect(jsonPath("$.[*].precioTotal").value(hasItem(sameNumber(DEFAULT_PRECIO_TOTAL))))
            .andExpect(jsonPath("$.[*].descuento").value(hasItem(sameNumber(DEFAULT_DESCUENTO))));

        // Check, that the count call also returns 1
        restServicioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultServicioShouldNotBeFound(String filter) throws Exception {
        restServicioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restServicioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingServicio() throws Exception {
        // Get the servicio
        restServicioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingServicio() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the servicio
        Servicio updatedServicio = servicioRepository.findById(servicio.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedServicio are not directly saved in db
        em.detach(updatedServicio);
        updatedServicio
            .tipoServicio(UPDATED_TIPO_SERVICIO)
            .zona(UPDATED_ZONA)
            .frecuencia(UPDATED_FRECUENCIA)
            .fecha(UPDATED_FECHA)
            .horaInicio(UPDATED_HORA_INICIO)
            .duracionHoras(UPDATED_DURACION_HORAS)
            .numTrabajadores(UPDATED_NUM_TRABAJADORES)
            .estado(UPDATED_ESTADO)
            .direccion(UPDATED_DIRECCION)
            .municipio(UPDATED_MUNICIPIO)
            .notas(UPDATED_NOTAS)
            .precioTotal(UPDATED_PRECIO_TOTAL)
            .descuento(UPDATED_DESCUENTO);
        ServicioDTO servicioDTO = servicioMapper.toDto(updatedServicio);

        restServicioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, servicioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(servicioDTO))
            )
            .andExpect(status().isOk());

        // Validate the Servicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedServicioToMatchAllProperties(updatedServicio);
    }

    @Test
    @Transactional
    void putNonExistingServicio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicio.setId(longCount.incrementAndGet());

        // Create the Servicio
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServicioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, servicioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(servicioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Servicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServicio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicio.setId(longCount.incrementAndGet());

        // Create the Servicio
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(servicioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Servicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServicio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicio.setId(longCount.incrementAndGet());

        // Create the Servicio
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(servicioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Servicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServicioWithPatch() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the servicio using partial update
        Servicio partialUpdatedServicio = new Servicio();
        partialUpdatedServicio.setId(servicio.getId());

        partialUpdatedServicio
            .duracionHoras(UPDATED_DURACION_HORAS)
            .numTrabajadores(UPDATED_NUM_TRABAJADORES)
            .direccion(UPDATED_DIRECCION)
            .municipio(UPDATED_MUNICIPIO)
            .descuento(UPDATED_DESCUENTO);

        restServicioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServicio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServicio))
            )
            .andExpect(status().isOk());

        // Validate the Servicio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServicioUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedServicio, servicio), getPersistedServicio(servicio));
    }

    @Test
    @Transactional
    void fullUpdateServicioWithPatch() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the servicio using partial update
        Servicio partialUpdatedServicio = new Servicio();
        partialUpdatedServicio.setId(servicio.getId());

        partialUpdatedServicio
            .tipoServicio(UPDATED_TIPO_SERVICIO)
            .zona(UPDATED_ZONA)
            .frecuencia(UPDATED_FRECUENCIA)
            .fecha(UPDATED_FECHA)
            .horaInicio(UPDATED_HORA_INICIO)
            .duracionHoras(UPDATED_DURACION_HORAS)
            .numTrabajadores(UPDATED_NUM_TRABAJADORES)
            .estado(UPDATED_ESTADO)
            .direccion(UPDATED_DIRECCION)
            .municipio(UPDATED_MUNICIPIO)
            .notas(UPDATED_NOTAS)
            .precioTotal(UPDATED_PRECIO_TOTAL)
            .descuento(UPDATED_DESCUENTO);

        restServicioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServicio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServicio))
            )
            .andExpect(status().isOk());

        // Validate the Servicio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServicioUpdatableFieldsEquals(partialUpdatedServicio, getPersistedServicio(partialUpdatedServicio));
    }

    @Test
    @Transactional
    void patchNonExistingServicio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicio.setId(longCount.incrementAndGet());

        // Create the Servicio
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServicioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, servicioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(servicioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Servicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServicio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicio.setId(longCount.incrementAndGet());

        // Create the Servicio
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(servicioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Servicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServicio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        servicio.setId(longCount.incrementAndGet());

        // Create the Servicio
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(servicioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Servicio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServicio() throws Exception {
        // Initialize the database
        insertedServicio = servicioRepository.saveAndFlush(servicio);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the servicio
        restServicioMockMvc
            .perform(delete(ENTITY_API_URL_ID, servicio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return servicioRepository.count();
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

    protected Servicio getPersistedServicio(Servicio servicio) {
        return servicioRepository.findById(servicio.getId()).orElseThrow();
    }

    protected void assertPersistedServicioToMatchAllProperties(Servicio expectedServicio) {
        assertServicioAllPropertiesEquals(expectedServicio, getPersistedServicio(expectedServicio));
    }

    protected void assertPersistedServicioToMatchUpdatableProperties(Servicio expectedServicio) {
        assertServicioAllUpdatablePropertiesEquals(expectedServicio, getPersistedServicio(expectedServicio));
    }
}
