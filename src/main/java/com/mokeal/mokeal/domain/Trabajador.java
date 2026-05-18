package com.mokeal.mokeal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Trabajador.
 */
@Entity
@Table(name = "trabajador")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Trabajador implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @NotNull
    @Size(max = 20)
    @Column(name = "telefono", length = 20, nullable = false)
    private String telefono;

    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Size(max = 500)
    @Column(name = "notas", length = 500)
    private String notas;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "trabajadoreses")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cliente", "tarifa", "trabajadoreses" }, allowSetters = true)
    private Set<Servicio> servicioses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Trabajador id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Trabajador nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Trabajador telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return this.email;
    }

    public Trabajador email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Trabajador activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getNotas() {
        return this.notas;
    }

    public Trabajador notas(String notas) {
        this.setNotas(notas);
        return this;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public Set<Servicio> getServicioses() {
        return this.servicioses;
    }

    public void setServicioses(Set<Servicio> servicios) {
        if (this.servicioses != null) {
            this.servicioses.forEach(i -> i.removeTrabajadores(this));
        }
        if (servicios != null) {
            servicios.forEach(i -> i.addTrabajadores(this));
        }
        this.servicioses = servicios;
    }

    public Trabajador servicioses(Set<Servicio> servicios) {
        this.setServicioses(servicios);
        return this;
    }

    public Trabajador addServicios(Servicio servicio) {
        this.servicioses.add(servicio);
        servicio.getTrabajadoreses().add(this);
        return this;
    }

    public Trabajador removeServicios(Servicio servicio) {
        this.servicioses.remove(servicio);
        servicio.getTrabajadoreses().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Trabajador)) {
            return false;
        }
        return getId() != null && getId().equals(((Trabajador) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Trabajador{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", email='" + getEmail() + "'" +
            ", activo='" + getActivo() + "'" +
            ", notas='" + getNotas() + "'" +
            "}";
    }
}
