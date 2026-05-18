package com.mokeal.mokeal.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mokeal.mokeal.domain.Cliente} entity. This class is used
 * in {@link com.mokeal.mokeal.web.rest.ClienteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /clientes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClienteCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter telefono;

    private StringFilter email;

    private StringFilter nifCif;

    private StringFilter direccion;

    private StringFilter municipio;

    private StringFilter codigoPostal;

    private StringFilter notas;

    private BooleanFilter activo;

    private Boolean distinct;

    public ClienteCriteria() {}

    public ClienteCriteria(ClienteCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nombre = other.optionalNombre().map(StringFilter::copy).orElse(null);
        this.telefono = other.optionalTelefono().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.nifCif = other.optionalNifCif().map(StringFilter::copy).orElse(null);
        this.direccion = other.optionalDireccion().map(StringFilter::copy).orElse(null);
        this.municipio = other.optionalMunicipio().map(StringFilter::copy).orElse(null);
        this.codigoPostal = other.optionalCodigoPostal().map(StringFilter::copy).orElse(null);
        this.notas = other.optionalNotas().map(StringFilter::copy).orElse(null);
        this.activo = other.optionalActivo().map(BooleanFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ClienteCriteria copy() {
        return new ClienteCriteria(this);
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

    public StringFilter getNifCif() {
        return nifCif;
    }

    public Optional<StringFilter> optionalNifCif() {
        return Optional.ofNullable(nifCif);
    }

    public StringFilter nifCif() {
        if (nifCif == null) {
            setNifCif(new StringFilter());
        }
        return nifCif;
    }

    public void setNifCif(StringFilter nifCif) {
        this.nifCif = nifCif;
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

    public StringFilter getCodigoPostal() {
        return codigoPostal;
    }

    public Optional<StringFilter> optionalCodigoPostal() {
        return Optional.ofNullable(codigoPostal);
    }

    public StringFilter codigoPostal() {
        if (codigoPostal == null) {
            setCodigoPostal(new StringFilter());
        }
        return codigoPostal;
    }

    public void setCodigoPostal(StringFilter codigoPostal) {
        this.codigoPostal = codigoPostal;
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
        final ClienteCriteria that = (ClienteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(telefono, that.telefono) &&
            Objects.equals(email, that.email) &&
            Objects.equals(nifCif, that.nifCif) &&
            Objects.equals(direccion, that.direccion) &&
            Objects.equals(municipio, that.municipio) &&
            Objects.equals(codigoPostal, that.codigoPostal) &&
            Objects.equals(notas, that.notas) &&
            Objects.equals(activo, that.activo) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, telefono, email, nifCif, direccion, municipio, codigoPostal, notas, activo, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClienteCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNombre().map(f -> "nombre=" + f + ", ").orElse("") +
            optionalTelefono().map(f -> "telefono=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalNifCif().map(f -> "nifCif=" + f + ", ").orElse("") +
            optionalDireccion().map(f -> "direccion=" + f + ", ").orElse("") +
            optionalMunicipio().map(f -> "municipio=" + f + ", ").orElse("") +
            optionalCodigoPostal().map(f -> "codigoPostal=" + f + ", ").orElse("") +
            optionalNotas().map(f -> "notas=" + f + ", ").orElse("") +
            optionalActivo().map(f -> "activo=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
