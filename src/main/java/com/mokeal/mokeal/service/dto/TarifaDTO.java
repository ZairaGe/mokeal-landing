package com.mokeal.mokeal.service.dto;

import com.mokeal.mokeal.domain.enumeration.TipoServicio;
import com.mokeal.mokeal.domain.enumeration.ZonaTarifa;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.mokeal.mokeal.domain.Tarifa} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TarifaDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonaTarifa zona;

    @NotNull
    private TipoServicio tipoServicio;

    @NotNull
    private BigDecimal precioFirstHora;

    @NotNull
    private BigDecimal precioHoraAdicional;

    @NotNull
    private Integer minimoHoras;

    private BigDecimal precioPorKm;

    @NotNull
    private Boolean activa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonaTarifa getZona() {
        return zona;
    }

    public void setZona(ZonaTarifa zona) {
        this.zona = zona;
    }

    public TipoServicio getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(TipoServicio tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public BigDecimal getPrecioFirstHora() {
        return precioFirstHora;
    }

    public void setPrecioFirstHora(BigDecimal precioFirstHora) {
        this.precioFirstHora = precioFirstHora;
    }

    public BigDecimal getPrecioHoraAdicional() {
        return precioHoraAdicional;
    }

    public void setPrecioHoraAdicional(BigDecimal precioHoraAdicional) {
        this.precioHoraAdicional = precioHoraAdicional;
    }

    public Integer getMinimoHoras() {
        return minimoHoras;
    }

    public void setMinimoHoras(Integer minimoHoras) {
        this.minimoHoras = minimoHoras;
    }

    public BigDecimal getPrecioPorKm() {
        return precioPorKm;
    }

    public void setPrecioPorKm(BigDecimal precioPorKm) {
        this.precioPorKm = precioPorKm;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TarifaDTO)) {
            return false;
        }

        TarifaDTO tarifaDTO = (TarifaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tarifaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TarifaDTO{" +
            "id=" + getId() +
            ", zona='" + getZona() + "'" +
            ", tipoServicio='" + getTipoServicio() + "'" +
            ", precioFirstHora=" + getPrecioFirstHora() +
            ", precioHoraAdicional=" + getPrecioHoraAdicional() +
            ", minimoHoras=" + getMinimoHoras() +
            ", precioPorKm=" + getPrecioPorKm() +
            ", activa='" + getActiva() + "'" +
            "}";
    }
}
