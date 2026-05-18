package com.mokeal.mokeal.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.mokeal.mokeal.domain.Trabajador} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrabajadorDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String nombre;

    @NotNull
    @Size(max = 20)
    private String telefono;

    @Size(max = 100)
    private String email;

    @NotNull
    private Boolean activo;

    @Size(max = 500)
    private String notas;

    private Set<ServicioDTO> servicioses = new HashSet<>();

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

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public Set<ServicioDTO> getServicioses() {
        return servicioses;
    }

    public void setServicioses(Set<ServicioDTO> servicioses) {
        this.servicioses = servicioses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrabajadorDTO)) {
            return false;
        }

        TrabajadorDTO trabajadorDTO = (TrabajadorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, trabajadorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrabajadorDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", email='" + getEmail() + "'" +
            ", activo='" + getActivo() + "'" +
            ", notas='" + getNotas() + "'" +
            ", servicioses=" + getServicioses() +
            "}";
    }
}
