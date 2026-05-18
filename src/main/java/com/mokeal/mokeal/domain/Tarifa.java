package com.mokeal.mokeal.domain;

import com.mokeal.mokeal.domain.enumeration.TipoServicio;
import com.mokeal.mokeal.domain.enumeration.ZonaTarifa;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Tarifa.
 */
@Entity
@Table(name = "tarifa")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Tarifa implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "zona", nullable = false)
    private ZonaTarifa zona;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_servicio", nullable = false)
    private TipoServicio tipoServicio;

    @NotNull
    @Column(name = "precio_first_hora", precision = 21, scale = 2, nullable = false)
    private BigDecimal precioFirstHora;

    @NotNull
    @Column(name = "precio_hora_adicional", precision = 21, scale = 2, nullable = false)
    private BigDecimal precioHoraAdicional;

    @NotNull
    @Column(name = "minimo_horas", nullable = false)
    private Integer minimoHoras;

    @Column(name = "precio_por_km", precision = 21, scale = 2)
    private BigDecimal precioPorKm;

    @NotNull
    @Column(name = "activa", nullable = false)
    private Boolean activa;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tarifa id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonaTarifa getZona() {
        return this.zona;
    }

    public Tarifa zona(ZonaTarifa zona) {
        this.setZona(zona);
        return this;
    }

    public void setZona(ZonaTarifa zona) {
        this.zona = zona;
    }

    public TipoServicio getTipoServicio() {
        return this.tipoServicio;
    }

    public Tarifa tipoServicio(TipoServicio tipoServicio) {
        this.setTipoServicio(tipoServicio);
        return this;
    }

    public void setTipoServicio(TipoServicio tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public BigDecimal getPrecioFirstHora() {
        return this.precioFirstHora;
    }

    public Tarifa precioFirstHora(BigDecimal precioFirstHora) {
        this.setPrecioFirstHora(precioFirstHora);
        return this;
    }

    public void setPrecioFirstHora(BigDecimal precioFirstHora) {
        this.precioFirstHora = precioFirstHora;
    }

    public BigDecimal getPrecioHoraAdicional() {
        return this.precioHoraAdicional;
    }

    public Tarifa precioHoraAdicional(BigDecimal precioHoraAdicional) {
        this.setPrecioHoraAdicional(precioHoraAdicional);
        return this;
    }

    public void setPrecioHoraAdicional(BigDecimal precioHoraAdicional) {
        this.precioHoraAdicional = precioHoraAdicional;
    }

    public Integer getMinimoHoras() {
        return this.minimoHoras;
    }

    public Tarifa minimoHoras(Integer minimoHoras) {
        this.setMinimoHoras(minimoHoras);
        return this;
    }

    public void setMinimoHoras(Integer minimoHoras) {
        this.minimoHoras = minimoHoras;
    }

    public BigDecimal getPrecioPorKm() {
        return this.precioPorKm;
    }

    public Tarifa precioPorKm(BigDecimal precioPorKm) {
        this.setPrecioPorKm(precioPorKm);
        return this;
    }

    public void setPrecioPorKm(BigDecimal precioPorKm) {
        this.precioPorKm = precioPorKm;
    }

    public Boolean getActiva() {
        return this.activa;
    }

    public Tarifa activa(Boolean activa) {
        this.setActiva(activa);
        return this;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tarifa)) {
            return false;
        }
        return getId() != null && getId().equals(((Tarifa) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tarifa{" +
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
