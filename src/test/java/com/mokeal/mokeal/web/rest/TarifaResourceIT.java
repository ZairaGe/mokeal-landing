package com.mokeal.mokeal.web.rest;

import static com.mokeal.mokeal.domain.TarifaAsserts.*;
import static com.mokeal.mokeal.web.rest.TestUtil.createUpdateProxyForBean;
import static com.mokeal.mokeal.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mokeal.mokeal.IntegrationTest;
import com.mokeal.mokeal.domain.Tarifa;
import com.mokeal.mokeal.domain.enumeration.TipoServicio;
import com.mokeal.mokeal.domain.enumeration.ZonaTarifa;
import com.mokeal.mokeal.repository.TarifaRepository;
import com.mokeal.mokeal.service.dto.TarifaDTO;
import com.mokeal.mokeal.service.mapper.TarifaMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link TarifaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TarifaResourceIT {

    private static final ZonaTarifa DEFAULT_ZONA = ZonaTarifa.MADRID_CAPITAL;
    private static final ZonaTarifa UPDATED_ZONA = ZonaTarifa.COMUNIDAD_MADRID;

    private static final TipoServicio DEFAULT_TIPO_SERVICIO = TipoServicio.HOGAR;
    private static final TipoServicio UPDATED_TIPO_SERVICIO = TipoServicio.OFICINA;

    private static final BigDecimal DEFAULT_PRECIO_FIRST_HORA = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRECIO_FIRST_HORA = new BigDecimal(2);

    private static final BigDecimal DEFAULT_PRECIO_HORA_ADICIONAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRECIO_HORA_ADICIONAL = new BigDecimal(2);

    private static final Integer DEFAULT_MINIMO_HORAS = 1;
    private static final Integer UPDATED_MINIMO_HORAS = 2;

    private static final BigDecimal DEFAULT_PRECIO_POR_KM = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRECIO_POR_KM = new BigDecimal(2);

    private static final Boolean DEFAULT_ACTIVA = false;
    private static final Boolean UPDATED_ACTIVA = true;

    private static final String ENTITY_API_URL = "/api/tarifas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TarifaRepository tarifaRepository;

    @Autowired
    private TarifaMapper tarifaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTarifaMockMvc;

    private Tarifa tarifa;

    private Tarifa insertedTarifa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tarifa createEntity() {
        return new Tarifa()
            .zona(DEFAULT_ZONA)
            .tipoServicio(DEFAULT_TIPO_SERVICIO)
            .precioFirstHora(DEFAULT_PRECIO_FIRST_HORA)
            .precioHoraAdicional(DEFAULT_PRECIO_HORA_ADICIONAL)
            .minimoHoras(DEFAULT_MINIMO_HORAS)
            .precioPorKm(DEFAULT_PRECIO_POR_KM)
            .activa(DEFAULT_ACTIVA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tarifa createUpdatedEntity() {
        return new Tarifa()
            .zona(UPDATED_ZONA)
            .tipoServicio(UPDATED_TIPO_SERVICIO)
            .precioFirstHora(UPDATED_PRECIO_FIRST_HORA)
            .precioHoraAdicional(UPDATED_PRECIO_HORA_ADICIONAL)
            .minimoHoras(UPDATED_MINIMO_HORAS)
            .precioPorKm(UPDATED_PRECIO_POR_KM)
            .activa(UPDATED_ACTIVA);
    }

    @BeforeEach
    void initTest() {
        tarifa = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTarifa != null) {
            tarifaRepository.delete(insertedTarifa);
            insertedTarifa = null;
        }
    }

    @Test
    @Transactional
    void createTarifa() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Tarifa
        TarifaDTO tarifaDTO = tarifaMapper.toDto(tarifa);
        var returnedTarifaDTO = om.readValue(
            restTarifaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarifaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TarifaDTO.class
        );

        // Validate the Tarifa in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTarifa = tarifaMapper.toEntity(returnedTarifaDTO);
        assertTarifaUpdatableFieldsEquals(returnedTarifa, getPersistedTarifa(returnedTarifa));

        insertedTarifa = returnedTarifa;
    }

    @Test
    @Transactional
    void createTarifaWithExistingId() throws Exception {
        // Create the Tarifa with an existing ID
        tarifa.setId(1L);
        TarifaDTO tarifaDTO = tarifaMapper.toDto(tarifa);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTarifaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarifaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tarifa in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkZonaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tarifa.setZona(null);

        // Create the Tarifa, which fails.
        TarifaDTO tarifaDTO = tarifaMapper.toDto(tarifa);

        restTarifaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarifaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoServicioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tarifa.setTipoServicio(null);

        // Create the Tarifa, which fails.
        TarifaDTO tarifaDTO = tarifaMapper.toDto(tarifa);

        restTarifaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarifaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecioFirstHoraIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tarifa.setPrecioFirstHora(null);

        // Create the Tarifa, which fails.
        TarifaDTO tarifaDTO = tarifaMapper.toDto(tarifa);

        restTarifaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarifaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecioHoraAdicionalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tarifa.setPrecioHoraAdicional(null);

        // Create the Tarifa, which fails.
        TarifaDTO tarifaDTO = tarifaMapper.toDto(tarifa);

        restTarifaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarifaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMinimoHorasIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tarifa.setMinimoHoras(null);

        // Create the Tarifa, which fails.
        TarifaDTO tarifaDTO = tarifaMapper.toDto(tarifa);

        restTarifaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarifaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tarifa.setActiva(null);

        // Create the Tarifa, which fails.
        TarifaDTO tarifaDTO = tarifaMapper.toDto(tarifa);

        restTarifaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarifaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTarifas() throws Exception {
        // Initialize the database
        insertedTarifa = tarifaRepository.saveAndFlush(tarifa);

        // Get all the tarifaList
        restTarifaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tarifa.getId().intValue())))
            .andExpect(jsonPath("$.[*].zona").value(hasItem(DEFAULT_ZONA.toString())))
            .andExpect(jsonPath("$.[*].tipoServicio").value(hasItem(DEFAULT_TIPO_SERVICIO.toString())))
            .andExpect(jsonPath("$.[*].precioFirstHora").value(hasItem(sameNumber(DEFAULT_PRECIO_FIRST_HORA))))
            .andExpect(jsonPath("$.[*].precioHoraAdicional").value(hasItem(sameNumber(DEFAULT_PRECIO_HORA_ADICIONAL))))
            .andExpect(jsonPath("$.[*].minimoHoras").value(hasItem(DEFAULT_MINIMO_HORAS)))
            .andExpect(jsonPath("$.[*].precioPorKm").value(hasItem(sameNumber(DEFAULT_PRECIO_POR_KM))))
            .andExpect(jsonPath("$.[*].activa").value(hasItem(DEFAULT_ACTIVA)));
    }

    @Test
    @Transactional
    void getTarifa() throws Exception {
        // Initialize the database
        insertedTarifa = tarifaRepository.saveAndFlush(tarifa);

        // Get the tarifa
        restTarifaMockMvc
            .perform(get(ENTITY_API_URL_ID, tarifa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tarifa.getId().intValue()))
            .andExpect(jsonPath("$.zona").value(DEFAULT_ZONA.toString()))
            .andExpect(jsonPath("$.tipoServicio").value(DEFAULT_TIPO_SERVICIO.toString()))
            .andExpect(jsonPath("$.precioFirstHora").value(sameNumber(DEFAULT_PRECIO_FIRST_HORA)))
            .andExpect(jsonPath("$.precioHoraAdicional").value(sameNumber(DEFAULT_PRECIO_HORA_ADICIONAL)))
            .andExpect(jsonPath("$.minimoHoras").value(DEFAULT_MINIMO_HORAS))
            .andExpect(jsonPath("$.precioPorKm").value(sameNumber(DEFAULT_PRECIO_POR_KM)))
            .andExpect(jsonPath("$.activa").value(DEFAULT_ACTIVA));
    }

    @Test
    @Transactional
    void getNonExistingTarifa() throws Exception {
        // Get the tarifa
        restTarifaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTarifa() throws Exception {
        // Initialize the database
        insertedTarifa = tarifaRepository.saveAndFlush(tarifa);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tarifa
        Tarifa updatedTarifa = tarifaRepository.findById(tarifa.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTarifa are not directly saved in db
        em.detach(updatedTarifa);
        updatedTarifa
            .zona(UPDATED_ZONA)
            .tipoServicio(UPDATED_TIPO_SERVICIO)
            .precioFirstHora(UPDATED_PRECIO_FIRST_HORA)
            .precioHoraAdicional(UPDATED_PRECIO_HORA_ADICIONAL)
            .minimoHoras(UPDATED_MINIMO_HORAS)
            .precioPorKm(UPDATED_PRECIO_POR_KM)
            .activa(UPDATED_ACTIVA);
        TarifaDTO tarifaDTO = tarifaMapper.toDto(updatedTarifa);

        restTarifaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tarifaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarifaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Tarifa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTarifaToMatchAllProperties(updatedTarifa);
    }

    @Test
    @Transactional
    void putNonExistingTarifa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarifa.setId(longCount.incrementAndGet());

        // Create the Tarifa
        TarifaDTO tarifaDTO = tarifaMapper.toDto(tarifa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTarifaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tarifaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarifaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarifa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTarifa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarifa.setId(longCount.incrementAndGet());

        // Create the Tarifa
        TarifaDTO tarifaDTO = tarifaMapper.toDto(tarifa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarifaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tarifaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarifa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTarifa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarifa.setId(longCount.incrementAndGet());

        // Create the Tarifa
        TarifaDTO tarifaDTO = tarifaMapper.toDto(tarifa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarifaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tarifaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tarifa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTarifaWithPatch() throws Exception {
        // Initialize the database
        insertedTarifa = tarifaRepository.saveAndFlush(tarifa);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tarifa using partial update
        Tarifa partialUpdatedTarifa = new Tarifa();
        partialUpdatedTarifa.setId(tarifa.getId());

        partialUpdatedTarifa.minimoHoras(UPDATED_MINIMO_HORAS).precioPorKm(UPDATED_PRECIO_POR_KM);

        restTarifaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTarifa.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTarifa))
            )
            .andExpect(status().isOk());

        // Validate the Tarifa in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTarifaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTarifa, tarifa), getPersistedTarifa(tarifa));
    }

    @Test
    @Transactional
    void fullUpdateTarifaWithPatch() throws Exception {
        // Initialize the database
        insertedTarifa = tarifaRepository.saveAndFlush(tarifa);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tarifa using partial update
        Tarifa partialUpdatedTarifa = new Tarifa();
        partialUpdatedTarifa.setId(tarifa.getId());

        partialUpdatedTarifa
            .zona(UPDATED_ZONA)
            .tipoServicio(UPDATED_TIPO_SERVICIO)
            .precioFirstHora(UPDATED_PRECIO_FIRST_HORA)
            .precioHoraAdicional(UPDATED_PRECIO_HORA_ADICIONAL)
            .minimoHoras(UPDATED_MINIMO_HORAS)
            .precioPorKm(UPDATED_PRECIO_POR_KM)
            .activa(UPDATED_ACTIVA);

        restTarifaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTarifa.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTarifa))
            )
            .andExpect(status().isOk());

        // Validate the Tarifa in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTarifaUpdatableFieldsEquals(partialUpdatedTarifa, getPersistedTarifa(partialUpdatedTarifa));
    }

    @Test
    @Transactional
    void patchNonExistingTarifa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarifa.setId(longCount.incrementAndGet());

        // Create the Tarifa
        TarifaDTO tarifaDTO = tarifaMapper.toDto(tarifa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTarifaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tarifaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tarifaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarifa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTarifa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarifa.setId(longCount.incrementAndGet());

        // Create the Tarifa
        TarifaDTO tarifaDTO = tarifaMapper.toDto(tarifa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarifaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tarifaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarifa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTarifa() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tarifa.setId(longCount.incrementAndGet());

        // Create the Tarifa
        TarifaDTO tarifaDTO = tarifaMapper.toDto(tarifa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarifaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tarifaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tarifa in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTarifa() throws Exception {
        // Initialize the database
        insertedTarifa = tarifaRepository.saveAndFlush(tarifa);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tarifa
        restTarifaMockMvc
            .perform(delete(ENTITY_API_URL_ID, tarifa.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tarifaRepository.count();
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

    protected Tarifa getPersistedTarifa(Tarifa tarifa) {
        return tarifaRepository.findById(tarifa.getId()).orElseThrow();
    }

    protected void assertPersistedTarifaToMatchAllProperties(Tarifa expectedTarifa) {
        assertTarifaAllPropertiesEquals(expectedTarifa, getPersistedTarifa(expectedTarifa));
    }

    protected void assertPersistedTarifaToMatchUpdatableProperties(Tarifa expectedTarifa) {
        assertTarifaAllUpdatablePropertiesEquals(expectedTarifa, getPersistedTarifa(expectedTarifa));
    }
}
