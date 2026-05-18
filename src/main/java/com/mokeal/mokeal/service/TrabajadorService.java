package com.mokeal.mokeal.service;

import com.mokeal.mokeal.service.dto.TrabajadorDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mokeal.mokeal.domain.Trabajador}.
 */
public interface TrabajadorService {
    /**
     * Save a trabajador.
     *
     * @param trabajadorDTO the entity to save.
     * @return the persisted entity.
     */
    TrabajadorDTO save(TrabajadorDTO trabajadorDTO);

    /**
     * Updates a trabajador.
     *
     * @param trabajadorDTO the entity to update.
     * @return the persisted entity.
     */
    TrabajadorDTO update(TrabajadorDTO trabajadorDTO);

    /**
     * Partially updates a trabajador.
     *
     * @param trabajadorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TrabajadorDTO> partialUpdate(TrabajadorDTO trabajadorDTO);

    /**
     * Get the "id" trabajador.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TrabajadorDTO> findOne(Long id);

    /**
     * Delete the "id" trabajador.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
