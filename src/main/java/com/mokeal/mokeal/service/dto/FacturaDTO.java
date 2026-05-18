package com.mokeal.mokeal.service.dto;

import com.mokeal.mokeal.domain.enumeration.EstadoFactura;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mokeal.mokeal.domain.Factura} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FacturaDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 30)
    private String numero;

    @NotNull
    private LocalDate fechaEmision;

    private LocalDate fechaVencimiento;

    @NotNull
    private BigDecimal baseImponible;

    @NotNull
    private BigDecimal iva;

    @NotNull
    private BigDecimal total;

    @NotNull
    private EstadoFactura estado;

    @Size(max = 500)
    private String notas;

    @NotNull
    private ServicioDTO servicio;

    @NotNull
    private ClienteDTO cliente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public BigDecimal getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(BigDecimal baseImponible) {
        this.baseImponible = baseImponible;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public EstadoFactura getEstado() {
        return estado;
    }

    public void setEstado(EstadoFactura estado) {
        this.estado = estado;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public ServicioDTO getServicio() {
        return servicio;
    }

    public void setServicio(ServicioDTO servicio) {
        this.servicio = servicio;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FacturaDTO)) {
            return false;
        }

        FacturaDTO facturaDTO = (FacturaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, facturaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacturaDTO{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", fechaEmision='" + getFechaEmision() + "'" +
            ", fechaVencimiento='" + getFechaVencimiento() + "'" +
            ", baseImponible=" + getBaseImponible() +
            ", iva=" + getIva() +
            ", total=" + getTotal() +
            ", estado='" + getEstado() + "'" +
            ", notas='" + getNotas() + "'" +
            ", servicio=" + getServicio() +
            ", cliente=" + getCliente() +
            "}";
    }
}
