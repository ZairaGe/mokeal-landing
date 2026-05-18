package com.mokeal.mokeal.web.rest;

import static com.mokeal.mokeal.domain.TrabajadorAsserts.*;
import static com.mokeal.mokeal.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mokeal.mokeal.IntegrationTest;
import com.mokeal.mokeal.domain.Servicio;
import com.mokeal.mokeal.domain.Trabajador;
import com.mokeal.mokeal.repository.TrabajadorRepository;
import com.mokeal.mokeal.service.dto.TrabajadorDTO;
import com.mokeal.mokeal.service.mapper.TrabajadorMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link TrabajadorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrabajadorResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final String DEFAULT_NOTAS = "AAAAAAAAAA";
    private static final String UPDATED_NOTAS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/trabajadors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TrabajadorRepository trabajadorRepository;

    @Autowired
    private TrabajadorMapper trabajadorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrabajadorMockMvc;

    private Trabajador trabajador;

    private Trabajador insertedTrabajador;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trabajador createEntity() {
        return new Trabajador()
            .nombre(DEFAULT_NOMBRE)
            .telefono(DEFAULT_TELEFONO)
            .email(DEFAULT_EMAIL)
            .activo(DEFAULT_ACTIVO)
            .notas(DEFAULT_NOTAS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trabajador createUpdatedEntity() {
        return new Trabajador()
            .nombre(UPDATED_NOMBRE)
            .telefono(UPDATED_TELEFONO)
            .email(UPDATED_EMAIL)
            .activo(UPDATED_ACTIVO)
            .notas(UPDATED_NOTAS);
    }

    @BeforeEach
    void initTest() {
        trabajador = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTrabajador != null) {
            trabajadorRepository.delete(insertedTrabajador);
            insertedTrabajador = null;
        }
    }

    @Test
    @Transactional
    void createTrabajador() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Trabajador
        TrabajadorDTO trabajadorDTO = trabajadorMapper.toDto(trabajador);
        var returnedTrabajadorDTO = om.readValue(
            restTrabajadorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trabajadorDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TrabajadorDTO.class
        );

        // Validate the Trabajador in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTrabajador = trabajadorMapper.toEntity(returnedTrabajadorDTO);
        assertTrabajadorUpdatableFieldsEquals(returnedTrabajador, getPersistedTrabajador(returnedTrabajador));

        insertedTrabajador = returnedTrabajador;
    }

    @Test
    @Transactional
    void createTrabajadorWithExistingId() throws Exception {
        // Create the Trabajador with an existing ID
        trabajador.setId(1L);
        TrabajadorDTO trabajadorDTO = trabajadorMapper.toDto(trabajador);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrabajadorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trabajadorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Trabajador in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        trabajador.setNombre(null);

        // Create the Trabajador, which fails.
        TrabajadorDTO trabajadorDTO = trabajadorMapper.toDto(trabajador);

        restTrabajadorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trabajadorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelefonoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        trabajador.setTelefono(null);

        // Create the Trabajador, which fails.
        TrabajadorDTO trabajadorDTO = trabajadorMapper.toDto(trabajador);

        restTrabajadorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trabajadorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        trabajador.setActivo(null);

        // Create the Trabajador, which fails.
        TrabajadorDTO trabajadorDTO = trabajadorMapper.toDto(trabajador);

        restTrabajadorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trabajadorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTrabajadors() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList
        restTrabajadorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trabajador.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].notas").value(hasItem(DEFAULT_NOTAS)));
    }

    @Test
    @Transactional
    void getTrabajador() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get the trabajador
        restTrabajadorMockMvc
            .perform(get(ENTITY_API_URL_ID, trabajador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trabajador.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.notas").value(DEFAULT_NOTAS));
    }

    @Test
    @Transactional
    void getTrabajadorsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        Long id = trabajador.getId();

        defaultTrabajadorFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTrabajadorFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTrabajadorFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where nombre equals to
        defaultTrabajadorFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where nombre in
        defaultTrabajadorFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where nombre is not null
        defaultTrabajadorFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllTrabajadorsByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where nombre contains
        defaultTrabajadorFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where nombre does not contain
        defaultTrabajadorFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByTelefonoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where telefono equals to
        defaultTrabajadorFiltering("telefono.equals=" + DEFAULT_TELEFONO, "telefono.equals=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByTelefonoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where telefono in
        defaultTrabajadorFiltering("telefono.in=" + DEFAULT_TELEFONO + "," + UPDATED_TELEFONO, "telefono.in=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByTelefonoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where telefono is not null
        defaultTrabajadorFiltering("telefono.specified=true", "telefono.specified=false");
    }

    @Test
    @Transactional
    void getAllTrabajadorsByTelefonoContainsSomething() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where telefono contains
        defaultTrabajadorFiltering("telefono.contains=" + DEFAULT_TELEFONO, "telefono.contains=" + UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByTelefonoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where telefono does not contain
        defaultTrabajadorFiltering("telefono.doesNotContain=" + UPDATED_TELEFONO, "telefono.doesNotContain=" + DEFAULT_TELEFONO);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where email equals to
        defaultTrabajadorFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where email in
        defaultTrabajadorFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where email is not null
        defaultTrabajadorFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllTrabajadorsByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where email contains
        defaultTrabajadorFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where email does not contain
        defaultTrabajadorFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where activo equals to
        defaultTrabajadorFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where activo in
        defaultTrabajadorFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where activo is not null
        defaultTrabajadorFiltering("activo.specified=true", "activo.specified=false");
    }

    @Test
    @Transactional
    void getAllTrabajadorsByNotasIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where notas equals to
        defaultTrabajadorFiltering("notas.equals=" + DEFAULT_NOTAS, "notas.equals=" + UPDATED_NOTAS);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByNotasIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where notas in
        defaultTrabajadorFiltering("notas.in=" + DEFAULT_NOTAS + "," + UPDATED_NOTAS, "notas.in=" + UPDATED_NOTAS);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByNotasIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where notas is not null
        defaultTrabajadorFiltering("notas.specified=true", "notas.specified=false");
    }

    @Test
    @Transactional
    void getAllTrabajadorsByNotasContainsSomething() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where notas contains
        defaultTrabajadorFiltering("notas.contains=" + DEFAULT_NOTAS, "notas.contains=" + UPDATED_NOTAS);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByNotasNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        // Get all the trabajadorList where notas does not contain
        defaultTrabajadorFiltering("notas.doesNotContain=" + UPDATED_NOTAS, "notas.doesNotContain=" + DEFAULT_NOTAS);
    }

    @Test
    @Transactional
    void getAllTrabajadorsByServiciosIsEqualToSomething() throws Exception {
        Servicio servicios;
        if (TestUtil.findAll(em, Servicio.class).isEmpty()) {
            trabajadorRepository.saveAndFlush(trabajador);
            servicios = ServicioResourceIT.createEntity(em);
        } else {
            servicios = TestUtil.findAll(em, Servicio.class).get(0);
        }
        em.persist(servicios);
        em.flush();
        trabajador.addServicios(servicios);
        trabajadorRepository.saveAndFlush(trabajador);
        Long serviciosId = servicios.getId();
        // Get all the trabajadorList where servicios equals to serviciosId
        defaultTrabajadorShouldBeFound("serviciosId.equals=" + serviciosId);

        // Get all the trabajadorList where servicios equals to (serviciosId + 1)
        defaultTrabajadorShouldNotBeFound("serviciosId.equals=" + (serviciosId + 1));
    }

    private void defaultTrabajadorFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTrabajadorShouldBeFound(shouldBeFound);
        defaultTrabajadorShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTrabajadorShouldBeFound(String filter) throws Exception {
        restTrabajadorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trabajador.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].notas").value(hasItem(DEFAULT_NOTAS)));

        // Check, that the count call also returns 1
        restTrabajadorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTrabajadorShouldNotBeFound(String filter) throws Exception {
        restTrabajadorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTrabajadorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTrabajador() throws Exception {
        // Get the trabajador
        restTrabajadorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTrabajador() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trabajador
        Trabajador updatedTrabajador = trabajadorRepository.findById(trabajador.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTrabajador are not directly saved in db
        em.detach(updatedTrabajador);
        updatedTrabajador
            .nombre(UPDATED_NOMBRE)
            .telefono(UPDATED_TELEFONO)
            .email(UPDATED_EMAIL)
            .activo(UPDATED_ACTIVO)
            .notas(UPDATED_NOTAS);
        TrabajadorDTO trabajadorDTO = trabajadorMapper.toDto(updatedTrabajador);

        restTrabajadorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trabajadorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trabajadorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Trabajador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTrabajadorToMatchAllProperties(updatedTrabajador);
    }

    @Test
    @Transactional
    void putNonExistingTrabajador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trabajador.setId(longCount.incrementAndGet());

        // Create the Trabajador
        TrabajadorDTO trabajadorDTO = trabajadorMapper.toDto(trabajador);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrabajadorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trabajadorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trabajadorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trabajador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrabajador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trabajador.setId(longCount.incrementAndGet());

        // Create the Trabajador
        TrabajadorDTO trabajadorDTO = trabajadorMapper.toDto(trabajador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrabajadorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trabajadorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trabajador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrabajador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trabajador.setId(longCount.incrementAndGet());

        // Create the Trabajador
        TrabajadorDTO trabajadorDTO = trabajadorMapper.toDto(trabajador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrabajadorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trabajadorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trabajador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrabajadorWithPatch() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trabajador using partial update
        Trabajador partialUpdatedTrabajador = new Trabajador();
        partialUpdatedTrabajador.setId(trabajador.getId());

        partialUpdatedTrabajador.email(UPDATED_EMAIL).notas(UPDATED_NOTAS);

        restTrabajadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrabajador.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrabajador))
            )
            .andExpect(status().isOk());

        // Validate the Trabajador in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrabajadorUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTrabajador, trabajador),
            getPersistedTrabajador(trabajador)
        );
    }

    @Test
    @Transactional
    void fullUpdateTrabajadorWithPatch() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trabajador using partial update
        Trabajador partialUpdatedTrabajador = new Trabajador();
        partialUpdatedTrabajador.setId(trabajador.getId());

        partialUpdatedTrabajador
            .nombre(UPDATED_NOMBRE)
            .telefono(UPDATED_TELEFONO)
            .email(UPDATED_EMAIL)
            .activo(UPDATED_ACTIVO)
            .notas(UPDATED_NOTAS);

        restTrabajadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrabajador.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrabajador))
            )
            .andExpect(status().isOk());

        // Validate the Trabajador in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrabajadorUpdatableFieldsEquals(partialUpdatedTrabajador, getPersistedTrabajador(partialUpdatedTrabajador));
    }

    @Test
    @Transactional
    void patchNonExistingTrabajador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trabajador.setId(longCount.incrementAndGet());

        // Create the Trabajador
        TrabajadorDTO trabajadorDTO = trabajadorMapper.toDto(trabajador);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrabajadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trabajadorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trabajadorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trabajador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrabajador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trabajador.setId(longCount.incrementAndGet());

        // Create the Trabajador
        TrabajadorDTO trabajadorDTO = trabajadorMapper.toDto(trabajador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrabajadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trabajadorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trabajador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrabajador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trabajador.setId(longCount.incrementAndGet());

        // Create the Trabajador
        TrabajadorDTO trabajadorDTO = trabajadorMapper.toDto(trabajador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrabajadorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(trabajadorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trabajador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrabajador() throws Exception {
        // Initialize the database
        insertedTrabajador = trabajadorRepository.saveAndFlush(trabajador);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the trabajador
        restTrabajadorMockMvc
            .perform(delete(ENTITY_API_URL_ID, trabajador.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return trabajadorRepository.count();
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

    protected Trabajador getPersistedTrabajador(Trabajador trabajador) {
        return trabajadorRepository.findById(trabajador.getId()).orElseThrow();
    }

    protected void assertPersistedTrabajadorToMatchAllProperties(Trabajador expectedTrabajador) {
        assertTrabajadorAllPropertiesEquals(expectedTrabajador, getPersistedTrabajador(expectedTrabajador));
    }

    protected void assertPersistedTrabajadorToMatchUpdatableProperties(Trabajador expectedTrabajador) {
        assertTrabajadorAllUpdatablePropertiesEquals(expectedTrabajador, getPersistedTrabajador(expectedTrabajador));
    }
}
