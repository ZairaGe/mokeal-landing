package com.mokeal.mokeal.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mokeal.mokeal.domain.Trabajador} entity. This class is used
 * in {@link com.mokeal.mokeal.web.rest.TrabajadorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /trabajadors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrabajadorCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter telefono;

    private StringFilter email;

    private BooleanFilter activo;

    private StringFilter notas;

    private LongFilter serviciosId;

    private Boolean distinct;

    public TrabajadorCriteria() {}

    public TrabajadorCriteria(TrabajadorCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nombre = other.optionalNombre().map(StringFilter::copy).orElse(null);
        this.telefono = other.optionalTelefono().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.activo = other.optionalActivo().map(BooleanFilter::copy).orElse(null);
        this.notas = other.optionalNotas().map(StringFilter::copy).orElse(null);
        this.serviciosId = other.optionalServiciosId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TrabajadorCriteria copy() {
        return new TrabajadorCriteria(this);
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

    public StringFilter getNombre() {
        return nombre;
    }

    public Optional<StringFilter> optionalNombre() {
        return Optional.ofNullable(nombre);
    }

    public StringFilter nombre() {
        if (nombre == null) {
            setNombre(new StringFilter());
        }
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public StringFilter getTelefono() {
        return telefono;
    }

    public Optional<StringFilter> optionalTelefono() {
        return Optional.ofNullable(telefono);
    }

    public StringFilter telefono() {
        if (telefono == null) {
            setTelefono(new StringFilter());
        }
        return telefono;
    }

    public void setTelefono(StringFilter telefono) {
        this.telefono = telefono;
    }

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public BooleanFilter getActivo() {
        return activo;
    }

    public Optional<BooleanFilter> optionalActivo() {
        return Optional.ofNullable(activo);
    }

    public BooleanFilter activo() {
        if (activo == null) {
            setActivo(new BooleanFilter());
        }
        return activo;
    }

    public void setActivo(BooleanFilter activo) {
        this.activo = activo;
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

    public LongFilter getServiciosId() {
        return serviciosId;
    }

    public Optional<LongFilter> optionalServiciosId() {
        return Optional.ofNullable(serviciosId);
    }

    public LongFilter serviciosId() {
        if (serviciosId == null) {
            setServiciosId(new LongFilter());
        }
        return serviciosId;
    }

    public void setServiciosId(LongFilter serviciosId) {
        this.serviciosId = serviciosId;
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
        final TrabajadorCriteria that = (TrabajadorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(telefono, that.telefono) &&
            Objects.equals(email, that.email) &&
            Objects.equals(activo, that.activo) &&
            Objects.equals(notas, that.notas) &&
            Objects.equals(serviciosId, that.serviciosId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, telefono, email, activo, notas, serviciosId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrabajadorCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNombre().map(f -> "nombre=" + f + ", ").orElse("") +
            optionalTelefono().map(f -> "telefono=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalActivo().map(f -> "activo=" + f + ", ").orElse("") +
            optionalNotas().map(f -> "notas=" + f + ", ").orElse("") +
            optionalServiciosId().map(f -> "serviciosId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
