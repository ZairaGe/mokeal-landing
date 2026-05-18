package com.mokeal.mokeal.web.rest;

import com.mokeal.mokeal.repository.TarifaRepository;
import com.mokeal.mokeal.service.TarifaService;
import com.mokeal.mokeal.service.dto.TarifaDTO;
import com.mokeal.mokeal.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mokeal.mokeal.domain.Tarifa}.
 */
@RestController
@RequestMapping("/api/tarifas")
public class TarifaResource {

    private static final Logger LOG = LoggerFactory.getLogger(TarifaResource.class);

    private static final String ENTITY_NAME = "tarifa";

    @Value("${jhipster.clientApp.name:mokeal}")
    private String applicationName;

    private final TarifaService tarifaService;

    private final TarifaRepository tarifaRepository;

    public TarifaResource(TarifaService tarifaService, TarifaRepository tarifaRepository) {
        this.tarifaService = tarifaService;
        this.tarifaRepository = tarifaRepository;
    }

    /**
     * {@code POST  /tarifas} : Create a new tarifa.
     *
     * @param tarifaDTO the tarifaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tarifaDTO, or with status {@code 400 (Bad Request)} if the tarifa has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TarifaDTO> createTarifa(@Valid @RequestBody TarifaDTO tarifaDTO) throws URISyntaxException {
        LOG.debug("REST request to save Tarifa : {}", tarifaDTO);
        if (tarifaDTO.getId() != null) {
            throw new BadRequestAlertException("A new tarifa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tarifaDTO = tarifaService.save(tarifaDTO);
        return ResponseEntity.created(new URI("/api/tarifas/" + tarifaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, tarifaDTO.getId().toString()))
            .body(tarifaDTO);
    }

    /**
     * {@code PUT  /tarifas/:id} : Updates an existing tarifa.
     *
     * @param id the id of the tarifaDTO to save.
     * @param tarifaDTO the tarifaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tarifaDTO,
     * or with status {@code 400 (Bad Request)} if the tarifaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tarifaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TarifaDTO> updateTarifa(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TarifaDTO tarifaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Tarifa : {}, {}", id, tarifaDTO);
        if (tarifaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tarifaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tarifaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tarifaDTO = tarifaService.update(tarifaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tarifaDTO.getId().toString()))
            .body(tarifaDTO);
    }

    /**
     * {@code PATCH  /tarifas/:id} : Partial updates given fields of an existing tarifa, field will ignore if it is null
     *
     * @param id the id of the tarifaDTO to save.
     * @param tarifaDTO the tarifaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tarifaDTO,
     * or with status {@code 400 (Bad Request)} if the tarifaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tarifaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tarifaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TarifaDTO> partialUpdateTarifa(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TarifaDTO tarifaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Tarifa partially : {}, {}", id, tarifaDTO);
        if (tarifaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tarifaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tarifaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TarifaDTO> result = tarifaService.partialUpdate(tarifaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tarifaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tarifas} : get all the Tarifas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Tarifas in body.
     */
    @GetMapping("")
    public List<TarifaDTO> getAllTarifas() {
        LOG.debug("REST request to get all Tarifas");
        return tarifaService.findAll();
    }

    /**
     * {@code GET  /tarifas/:id} : get the "id" tarifa.
     *
     * @param id the id of the tarifaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tarifaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TarifaDTO> getTarifa(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Tarifa : {}", id);
        Optional<TarifaDTO> tarifaDTO = tarifaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tarifaDTO);
    }

    /**
     * {@code DELETE  /tarifas/:id} : delete the "id" tarifa.
     *
     * @param id the id of the tarifaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTarifa(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Tarifa : {}", id);
        tarifaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
