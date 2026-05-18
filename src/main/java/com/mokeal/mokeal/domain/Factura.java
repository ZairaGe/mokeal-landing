package com.mokeal.mokeal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mokeal.mokeal.domain.enumeration.EstadoFactura;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Factura.
 */
@Entity
@Table(name = "factura")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Factura implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(name = "numero", length = 30, nullable = false)
    private String numero;

    @NotNull
    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @NotNull
    @Column(name = "base_imponible", precision = 21, scale = 2, nullable = false)
    private BigDecimal baseImponible;

    @NotNull
    @Column(name = "iva", precision = 21, scale = 2, nullable = false)
    private BigDecimal iva;

    @NotNull
    @Column(name = "total", precision = 21, scale = 2, nullable = false)
    private BigDecimal total;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoFactura estado;

    @Size(max = 500)
    @Column(name = "notas", length = 500)
    private String notas;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "cliente", "tarifa", "trabajadoreses" }, allowSetters = true)
    private Servicio servicio;

    @ManyToOne(optional = false)
    @NotNull
    private Cliente cliente;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Factura id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return this.numero;
    }

    public Factura numero(String numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getFechaEmision() {
        return this.fechaEmision;
    }

    public Factura fechaEmision(LocalDate fechaEmision) {
        this.setFechaEmision(fechaEmision);
        return this;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public LocalDate getFechaVencimiento() {
        return this.fechaVencimiento;
    }

    public Factura fechaVencimiento(LocalDate fechaVencimiento) {
        this.setFechaVencimiento(fechaVencimiento);
        return this;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public BigDecimal getBaseImponible() {
        return this.baseImponible;
    }

    public Factura baseImponible(BigDecimal baseImponible) {
        this.setBaseImponible(baseImponible);
        return this;
    }

    public void setBaseImponible(BigDecimal baseImponible) {
        this.baseImponible = baseImponible;
    }

    public BigDecimal getIva() {
        return this.iva;
    }

    public Factura iva(BigDecimal iva) {
        this.setIva(iva);
        return this;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getTotal() {
        return this.total;
    }

    public Factura total(BigDecimal total) {
        this.setTotal(total);
        return this;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public EstadoFactura getEstado() {
        return this.estado;
    }

    public Factura estado(EstadoFactura estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadoFactura estado) {
        this.estado = estado;
    }

    public String getNotas() {
        return this.notas;
    }

    public Factura notas(String notas) {
        this.setNotas(notas);
        return this;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public Servicio getServicio() {
        return this.servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public Factura servicio(Servicio servicio) {
        this.setServicio(servicio);
        return this;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Factura cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Factura)) {
            return false;
        }
        return getId() != null && getId().equals(((Factura) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Factura{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", fechaEmision='" + getFechaEmision() + "'" +
            ", fechaVencimiento='" + getFechaVencimiento() + "'" +
            ", baseImponible=" + getBaseImponible() +
            ", iva=" + getIva() +
            ", total=" + getTotal() +
            ", estado='" + getEstado() + "'" +
            ", notas='" + getNotas() + "'" +
            "}";
    }
}
