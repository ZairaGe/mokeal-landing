package com.mokeal.mokeal.service.criteria;

import com.mokeal.mokeal.domain.enumeration.EstadoFactura;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mokeal.mokeal.domain.Factura} entity. This class is used
 * in {@link com.mokeal.mokeal.web.rest.FacturaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /facturas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FacturaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EstadoFactura
     */
    public static class EstadoFacturaFilter extends Filter<EstadoFactura> {

        public EstadoFacturaFilter() {}

        public EstadoFacturaFilter(EstadoFacturaFilter filter) {
            super(filter);
        }

        @Override
        public EstadoFacturaFilter copy() {
            return new EstadoFacturaFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter numero;

    private LocalDateFilter fechaEmision;

    private LocalDateFilter fechaVencimiento;

    private BigDecimalFilter baseImponible;

    private BigDecimalFilter iva;

    private BigDecimalFilter total;

    private EstadoFacturaFilter estado;

    private StringFilter notas;

    private LongFilter servicioId;

    private LongFilter clienteId;

    private Boolean distinct;

    public FacturaCriteria() {}

    public FacturaCriteria(FacturaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.numero = other.optionalNumero().map(StringFilter::copy).orElse(null);
        this.fechaEmision = other.optionalFechaEmision().map(LocalDateFilter::copy).orElse(null);
        this.fechaVencimiento = other.optionalFechaVencimiento().map(LocalDateFilter::copy).orElse(null);
        this.baseImponible = other.optionalBaseImponible().map(BigDecimalFilter::copy).orElse(null);
        this.iva = other.optionalIva().map(BigDecimalFilter::copy).orElse(null);
        this.total = other.optionalTotal().map(BigDecimalFilter::copy).orElse(null);
        this.estado = other.optionalEstado().map(EstadoFacturaFilter::copy).orElse(null);
        this.notas = other.optionalNotas().map(StringFilter::copy).orElse(null);
        this.servicioId = other.optionalServicioId().map(LongFilter::copy).orElse(null);
        this.clienteId = other.optionalClienteId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FacturaCriteria copy() {
        return new FacturaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNumero() {
        return numero;
    }

    public Optional<StringFilter> optionalNumero() {
        return Optional.ofNullable(numero);
    }

    public StringFilter numero() {
        if (numero == null) {
            setNumero(new StringFilter());
        }
        return numero;
    }

    public void setNumero(StringFilter numero) {
        this.numero = numero;
    }

    public LocalDateFilter getFechaEmision() {
        return fechaEmision;
    }

    public Optional<LocalDateFilter> optionalFechaEmision() {
        return Optional.ofNullable(fechaEmision);
    }

    public LocalDateFilter fechaEmision() {
        if (fechaEmision == null) {
            setFechaEmision(new LocalDateFilter());
        }
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateFilter fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public LocalDateFilter getFechaVencimiento() {
        return fechaVencimiento;
    }

    public Optional<LocalDateFilter> optionalFechaVencimiento() {
        return Optional.ofNullable(fechaVencimiento);
    }

    public LocalDateFilter fechaVencimiento() {
        if (fechaVencimiento == null) {
            setFechaVencimiento(new LocalDateFilter());
        }
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDateFilter fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public BigDecimalFilter getBaseImponible() {
        return baseImponible;
    }

    public Optional<BigDecimalFilter> optionalBaseImponible() {
        return Optional.ofNullable(baseImponible);
    }

    public BigDecimalFilter baseImponible() {
        if (baseImponible == null) {
            setBaseImponible(new BigDecimalFilter());
        }
        return baseImponible;
    }

    public void setBaseImponible(BigDecimalFilter baseImponible) {
        this.baseImponible = baseImponible;
    }

    public BigDecimalFilter getIva() {
        return iva;
    }

    public Optional<BigDecimalFilter> optionalIva() {
        return Optional.ofNullable(iva);
    }

    public BigDecimalFilter iva() {
        if (iva == null) {
            setIva(new BigDecimalFilter());
        }
        return iva;
    }

    public void setIva(BigDecimalFilter iva) {
        this.iva = iva;
    }

    public BigDecimalFilter getTotal() {
        return total;
    }

    public Optional<BigDecimalFilter> optionalTotal() {
        return Optional.ofNullable(total);
    }

    public BigDecimalFilter total() {
        if (total == null) {
            setTotal(new BigDecimalFilter());
        }
        return total;
    }

    public void setTotal(BigDecimalFilter total) {
        this.total = total;
    }

    public EstadoFacturaFilter getEstado() {
        return estado;
    }

    public Optional<EstadoFacturaFilter> optionalEstado() {
        return Optional.ofNullable(estado);
    }

    public EstadoFacturaFilter estado() {
        if (estado == null) {
            setEstado(new EstadoFacturaFilter());
        }
        return estado;
    }

    public void setEstado(EstadoFacturaFilter estado) {
        this.estado = estado;
    }

    public StringFilter getNotas() {
        return notas;
    }

    public Optional<StringFilter> optionalNotas() {
        return Optional.ofNullable(notas);
    }

    public StringFilter notas() {
        if (notas == null) {
            setNotas(new StringFilter());
        }
        return notas;
    }

    public void setNotas(StringFilter notas) {
        this.notas = notas;
    }

    public LongFilter getServicioId() {
        return servicioId;
    }

    public Optional<LongFilter> optionalServicioId() {
        return Optional.ofNullable(servicioId);
    }

    public LongFilter servicioId() {
        if (servicioId == null) {
            setServicioId(new LongFilter());
        }
        return servicioId;
    }

    public void setServicioId(LongFilter servicioId) {
        this.servicioId = servicioId;
    }

    public LongFilter getClienteId() {
        return clienteId;
    }

    public Optional<LongFilter> optionalClienteId() {
        return Optional.ofNullable(clienteId);
    }

    public LongFilter clienteId() {
        if (clienteId == null) {
            setClienteId(new LongFilter());
        }
        return clienteId;
    }

    public void setClienteId(LongFilter clienteId) {
        this.clienteId = clienteId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FacturaCriteria that = (FacturaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(numero, that.numero) &&
            Objects.equals(fechaEmision, that.fechaEmision) &&
            Objects.equals(fechaVencimiento, that.fechaVencimiento) &&
            Objects.equals(baseImponible, that.baseImponible) &&
            Objects.equals(iva, that.iva) &&
            Objects.equals(total, that.total) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(notas, that.notas) &&
            Objects.equals(servicioId, that.servicioId) &&
            Objects.equals(clienteId, that.clienteId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            numero,
            fechaEmision,
            fechaVencimiento,
            baseImponible,
            iva,
            total,
            estado,
            notas,
            servicioId,
            clienteId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacturaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNumero().map(f -> "numero=" + f + ", ").orElse("") +
            optionalFechaEmision().map(f -> "fechaEmision=" + f + ", ").orElse("") +
            optionalFechaVencimiento().map(f -> "fechaVencimiento=" + f + ", ").orElse("") +
            optionalBaseImponible().map(f -> "baseImponible=" + f + ", ").orElse("") +
            optionalIva().map(f -> "iva=" + f + ", ").orElse("") +
            optionalTotal().map(f -> "total=" + f + ", ").orElse("") +
            optionalEstado().map(f -> "estado=" + f + ", ").orElse("") +
            optionalNotas().map(f -> "notas=" + f + ", ").orElse("") +
            optionalServicioId().map(f -> "servicioId=" + f + ", ").orElse("") +
            optionalClienteId().map(f -> "clienteId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
