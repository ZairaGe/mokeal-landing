package com.mokeal.mokeal.service.criteria;

import com.mokeal.mokeal.domain.enumeration.EstadoServicio;
import com.mokeal.mokeal.domain.enumeration.Frecuencia;
import com.mokeal.mokeal.domain.enumeration.TipoServicio;
import com.mokeal.mokeal.domain.enumeration.ZonaTarifa;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mokeal.mokeal.domain.Servicio} entity. This class is used
 * in {@link com.mokeal.mokeal.web.rest.ServicioResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /servicios?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServicioCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TipoServicio
     */
    public static class TipoServicioFilter extends Filter<TipoServicio> {

        public TipoServicioFilter() {}

        public TipoServicioFilter(TipoServicioFilter filter) {
            super(filter);
        }

        @Override
        public TipoServicioFilter copy() {
            return new TipoServicioFilter(this);
        }
    }

    /**
     * Class for filtering ZonaTarifa
     */
    public static class ZonaTarifaFilter extends Filter<ZonaTarifa> {

        public ZonaTarifaFilter() {}

        public ZonaTarifaFilter(ZonaTarifaFilter filter) {
            super(filter);
        }

        @Override
        public ZonaTarifaFilter copy() {
            return new ZonaTarifaFilter(this);
        }
    }

    /**
     * Class for filtering Frecuencia
     */
    public static class FrecuenciaFilter extends Filter<Frecuencia> {

        public FrecuenciaFilter() {}

        public FrecuenciaFilter(FrecuenciaFilter filter) {
            super(filter);
        }

        @Override
        public FrecuenciaFilter copy() {
            return new FrecuenciaFilter(this);
        }
    }

    /**
     * Class for filtering EstadoServicio
     */
    public static class EstadoServicioFilter extends Filter<EstadoServicio> {

        public EstadoServicioFilter() {}

        public EstadoServicioFilter(EstadoServicioFilter filter) {
            super(filter);
        }

        @Override
        public EstadoServicioFilter copy() {
            return new EstadoServicioFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private TipoServicioFilter tipoServicio;

    private ZonaTarifaFilter zona;

    private FrecuenciaFilter frecuencia;

    private LocalDateFilter fecha;

    private StringFilter horaInicio;

    private BigDecimalFilter duracionHoras;

    private IntegerFilter numTrabajadores;

    private EstadoServicioFilter estado;

    private StringFilter direccion;

    private StringFilter municipio;

    private StringFilter notas;

    private BigDecimalFilter precioTotal;

    private BigDecimalFilter descuento;

    private LongFilter clienteId;

    private LongFilter tarifaId;

    private LongFilter trabajadoresId;

    private Boolean distinct;

    public ServicioCriteria() {}

    public ServicioCriteria(ServicioCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.tipoServicio = other.optionalTipoServicio().map(TipoServicioFilter::copy).orElse(null);
        this.zona = other.optionalZona().map(ZonaTarifaFilter::copy).orElse(null);
        this.frecuencia = other.optionalFrecuencia().map(FrecuenciaFilter::copy).orElse(null);
        this.fecha = other.optionalFecha().map(LocalDateFilter::copy).orElse(null);
        this.horaInicio = other.optionalHoraInicio().map(StringFilter::copy).orElse(null);
        this.duracionHoras = other.optionalDuracionHoras().map(BigDecimalFilter::copy).orElse(null);
        this.numTrabajadores = other.optionalNumTrabajadores().map(IntegerFilter::copy).orElse(null);
        this.estado = other.optionalEstado().map(EstadoServicioFilter::copy).orElse(null);
        this.direccion = other.optionalDireccion().map(StringFilter::copy).orElse(null);
        this.municipio = other.optionalMunicipio().map(StringFilter::copy).orElse(null);
        this.notas = other.optionalNotas().map(StringFilter::copy).orElse(null);
        this.precioTotal = other.optionalPrecioTotal().map(BigDecimalFilter::copy).orElse(null);
        this.descuento = other.optionalDescuento().map(BigDecimalFilter::copy).orElse(null);
        this.clienteId = other.optionalClienteId().map(LongFilter::copy).orElse(null);
        this.tarifaId = other.optionalTarifaId().map(LongFilter::copy).orElse(null);
        this.trabajadoresId = other.optionalTrabajadoresId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ServicioCriteria copy() {
        return new ServicioCriteria(this);
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

    public TipoServicioFilter getTipoServicio() {
        return tipoServicio;
    }

    public Optional<TipoServicioFilter> optionalTipoServicio() {
        return Optional.ofNullable(tipoServicio);
    }

    public TipoServicioFilter tipoServicio() {
        if (tipoServicio == null) {
            setTipoServicio(new TipoServicioFilter());
        }
        return tipoServicio;
    }

    public void setTipoServicio(TipoServicioFilter tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public ZonaTarifaFilter getZona() {
        return zona;
    }

    public Optional<ZonaTarifaFilter> optionalZona() {
        return Optional.ofNullable(zona);
    }

    public ZonaTarifaFilter zona() {
        if (zona == null) {
            setZona(new ZonaTarifaFilter());
        }
        return zona;
    }

    public void setZona(ZonaTarifaFilter zona) {
        this.zona = zona;
    }

    public FrecuenciaFilter getFrecuencia() {
        return frecuencia;
    }

    public Optional<FrecuenciaFilter> optionalFrecuencia() {
        return Optional.ofNullable(frecuencia);
    }

    public FrecuenciaFilter frecuencia() {
        if (frecuencia == null) {
            setFrecuencia(new FrecuenciaFilter());
        }
        return frecuencia;
    }

    public void setFrecuencia(FrecuenciaFilter frecuencia) {
        this.frecuencia = frecuencia;
    }

    public LocalDateFilter getFecha() {
        return fecha;
    }

    public Optional<LocalDateFilter> optionalFecha() {
        return Optional.ofNullable(fecha);
    }

    public LocalDateFilter fecha() {
        if (fecha == null) {
            setFecha(new LocalDateFilter());
        }
        return fecha;
    }

    public void setFecha(LocalDateFilter fecha) {
        this.fecha = fecha;
    }

    public StringFilter getHoraInicio() {
        return horaInicio;
    }

    public Optional<StringFilter> optionalHoraInicio() {
        return Optional.ofNullable(horaInicio);
    }

    public StringFilter horaInicio() {
        if (horaInicio == null) {
            setHoraInicio(new StringFilter());
        }
        return horaInicio;
    }

    public void setHoraInicio(StringFilter horaInicio) {
        this.horaInicio = horaInicio;
    }

    public BigDecimalFilter getDuracionHoras() {
        return duracionHoras;
    }

    public Optional<BigDecimalFilter> optionalDuracionHoras() {
        return Optional.ofNullable(duracionHoras);
    }

    public BigDecimalFilter duracionHoras() {
        if (duracionHoras == null) {
            setDuracionHoras(new BigDecimalFilter());
        }
        return duracionHoras;
    }

    public void setDuracionHoras(BigDecimalFilter duracionHoras) {
        this.duracionHoras = duracionHoras;
    }

    public IntegerFilter getNumTrabajadores() {
        return numTrabajadores;
    }

    public Optional<IntegerFilter> optionalNumTrabajadores() {
        return Optional.ofNullable(numTrabajadores);
    }

    public IntegerFilter numTrabajadores() {
        if (numTrabajadores == null) {
            setNumTrabajadores(new IntegerFilter());
        }
        return numTrabajadores;
    }

    public void setNumTrabajadores(IntegerFilter numTrabajadores) {
        this.numTrabajadores = numTrabajadores;
    }

    public EstadoServicioFilter getEstado() {
        return estado;
    }

    public Optional<EstadoServicioFilter> optionalEstado() {
        return Optional.ofNullable(estado);
    }

    public EstadoServicioFilter estado() {
        if (estado == null) {
            setEstado(new EstadoServicioFilter());
        }
        return estado;
    }

    public void setEstado(EstadoServicioFilter estado) {
        this.estado = estado;
    }

    public StringFilter getDireccion() {
        return direccion;
    }

    public Optional<StringFilter> optionalDireccion() {
        return Optional.ofNullable(direccion);
    }

    public StringFilter direccion() {
        if (direccion == null) {
            setDireccion(new StringFilter());
        }
        return direccion;
    }

    public void setDireccion(StringFilter direccion) {
        this.direccion = direccion;
    }

    public StringFilter getMunicipio() {
        return municipio;
    }

    public Optional<StringFilter> optionalMunicipio() {
        return Optional.ofNullable(municipio);
    }

    public StringFilter municipio() {
        if (municipio == null) {
            setMunicipio(new StringFilter());
        }
        return municipio;
    }

    public void setMunicipio(StringFilter municipio) {
        this.municipio = municipio;
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

    public BigDecimalFilter getPrecioTotal() {
        return precioTotal;
    }

    public Optional<BigDecimalFilter> optionalPrecioTotal() {
        return Optional.ofNullable(precioTotal);
    }

    public BigDecimalFilter precioTotal() {
        if (precioTotal == null) {
            setPrecioTotal(new BigDecimalFilter());
        }
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimalFilter precioTotal) {
        this.precioTotal = precioTotal;
    }

    public BigDecimalFilter getDescuento() {
        return descuento;
    }

    public Optional<BigDecimalFilter> optionalDescuento() {
        return Optional.ofNullable(descuento);
    }

    public BigDecimalFilter descuento() {
        if (descuento == null) {
            setDescuento(new BigDecimalFilter());
        }
        return descuento;
    }

    public void setDescuento(BigDecimalFilter descuento) {
        this.descuento = descuento;
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

    public LongFilter getTarifaId() {
        return tarifaId;
    }

    public Optional<LongFilter> optionalTarifaId() {
        return Optional.ofNullable(tarifaId);
    }

    public LongFilter tarifaId() {
        if (tarifaId == null) {
            setTarifaId(new LongFilter());
        }
        return tarifaId;
    }

    public void setTarifaId(LongFilter tarifaId) {
        this.tarifaId = tarifaId;
    }

    public LongFilter getTrabajadoresId() {
        return trabajadoresId;
    }

    public Optional<LongFilter> optionalTrabajadoresId() {
        return Optional.ofNullable(trabajadoresId);
    }

    public LongFilter trabajadoresId() {
        if (trabajadoresId == null) {
            setTrabajadoresId(new LongFilter());
        }
        return trabajadoresId;
    }

    public void setTrabajadoresId(LongFilter trabajadoresId) {
        this.trabajadoresId = trabajadoresId;
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
        final ServicioCriteria that = (ServicioCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tipoServicio, that.tipoServicio) &&
            Objects.equals(zona, that.zona) &&
            Objects.equals(frecuencia, that.frecuencia) &&
            Objects.equals(fecha, that.fecha) &&
            Objects.equals(horaInicio, that.horaInicio) &&
            Objects.equals(duracionHoras, that.duracionHoras) &&
            Objects.equals(numTrabajadores, that.numTrabajadores) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(direccion, that.direccion) &&
            Objects.equals(municipio, that.municipio) &&
            Objects.equals(notas, that.notas) &&
            Objects.equals(precioTotal, that.precioTotal) &&
            Objects.equals(descuento, that.descuento) &&
            Objects.equals(clienteId, that.clienteId) &&
            Objects.equals(tarifaId, that.tarifaId) &&
            Objects.equals(trabajadoresId, that.trabajadoresId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tipoServicio,
            zona,
            frecuencia,
            fecha,
            horaInicio,
            duracionHoras,
            numTrabajadores,
            estado,
            direccion,
            municipio,
            notas,
            precioTotal,
            descuento,
            clienteId,
            tarifaId,
            trabajadoresId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServicioCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTipoServicio().map(f -> "tipoServicio=" + f + ", ").orElse("") +
            optionalZona().map(f -> "zona=" + f + ", ").orElse("") +
            optionalFrecuencia().map(f -> "frecuencia=" + f + ", ").orElse("") +
            optionalFecha().map(f -> "fecha=" + f + ", ").orElse("") +
            optionalHoraInicio().map(f -> "horaInicio=" + f + ", ").orElse("") +
            optionalDuracionHoras().map(f -> "duracionHoras=" + f + ", ").orElse("") +
            optionalNumTrabajadores().map(f -> "numTrabajadores=" + f + ", ").orElse("") +
            optionalEstado().map(f -> "estado=" + f + ", ").orElse("") +
            optionalDireccion().map(f -> "direccion=" + f + ", ").orElse("") +
            optionalMunicipio().map(f -> "municipio=" + f + ", ").orElse("") +
            optionalNotas().map(f -> "notas=" + f + ", ").orElse("") +
            optionalPrecioTotal().map(f -> "precioTotal=" + f + ", ").orElse("") +
            optionalDescuento().map(f -> "descuento=" + f + ", ").orElse("") +
            optionalClienteId().map(f -> "clienteId=" + f + ", ").orElse("") +
            optionalTarifaId().map(f -> "tarifaId=" + f + ", ").orElse("") +
            optionalTrabajadoresId().map(f -> "trabajadoresId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
