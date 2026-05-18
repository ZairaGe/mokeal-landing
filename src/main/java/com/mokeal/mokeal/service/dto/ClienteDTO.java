package com.mokeal.mokeal.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mokeal.mokeal.domain.Cliente} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClienteDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String nombre;

    @NotNull
    @Size(max = 20)
    private String telefono;

    @Size(max = 100)
    private String email;

    @Size(max = 20)
    private String nifCif;

    @Size(max = 200)
    private String direccion;

    @Size(max = 100)
    private String municipio;

    @Size(max = 10)
    private String codigoPostal;

    @Size(max = 500)
    private String notas;

    @NotNull
    private Boolean activo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNifCif() {
        return nifCif;
    }

    public void setNifCif(String nifCif) {
        this.nifCif = nifCif;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClienteDTO)) {
            return false;
        }

        ClienteDTO clienteDTO = (ClienteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, clienteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClienteDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", email='" + getEmail() + "'" +
            ", nifCif='" + getNifCif() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", municipio='" + getMunicipio() + "'" +
            ", codigoPostal='" + getCodigoPostal() + "'" +
            ", notas='" + getNotas() + "'" +
            ", activo='" + getActivo() + "'" +
            "}";
    }
}
