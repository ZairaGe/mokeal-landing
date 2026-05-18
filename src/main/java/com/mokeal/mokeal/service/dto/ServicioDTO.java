package com.mokeal.mokeal.service.dto;

import com.mokeal.mokeal.domain.enumeration.EstadoServicio;
import com.mokeal.mokeal.domain.enumeration.Frecuencia;
import com.mokeal.mokeal.domain.enumeration.TipoServicio;
import com.mokeal.mokeal.domain.enumeration.ZonaTarifa;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.mokeal.mokeal.domain.Servicio} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServicioDTO implements Serializable {

    private Long id;

    @NotNull
    private TipoServicio tipoServicio;

    @NotNull
    private ZonaTarifa zona;

    @NotNull
    private Frecuencia frecuencia;

    @NotNull
    private LocalDate fecha;

    @NotNull
    @Size(max = 5)
    private String horaInicio;

    @NotNull
    private BigDecimal duracionHoras;

    @NotNull
    private Integer numTrabajadores;

    @NotNull
    private EstadoServicio estado;

    @Size(max = 200)
    private String direccion;

    @Size(max = 100)
    private String municipio;

    @Size(max = 1000)
    private String notas;

    private BigDecimal precioTotal;

    private BigDecimal descuento;

    @NotNull
    private ClienteDTO cliente;

    private TarifaDTO tarifa;

    private Set<TrabajadorDTO> trabajadoreses = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoServicio getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(TipoServicio tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public ZonaTarifa getZona() {
        return zona;
    }

    public void setZona(ZonaTarifa zona) {
        this.zona = zona;
    }

    public Frecuencia getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(Frecuencia frecuencia) {
        this.frecuencia = frecuencia;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public BigDecimal getDuracionHoras() {
        return duracionHoras;
    }

    public void setDuracionHoras(BigDecimal duracionHoras) {
        this.duracionHoras = duracionHoras;
    }

    public Integer getNumTrabajadores() {
        return numTrabajadores;
    }

    public void setNumTrabajadores(Integer numTrabajadores) {
        this.numTrabajadores = numTrabajadores;
    }

    public EstadoServicio getEstado() {
        return estado;
    }

    public void setEstado(EstadoServicio estado) {
        this.estado = estado;
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

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    public TarifaDTO getTarifa() {
        return tarifa;
    }

    public void setTarifa(TarifaDTO tarifa) {
        this.tarifa = tarifa;
    }

    public Set<TrabajadorDTO> getTrabajadoreses() {
        return trabajadoreses;
    }

    public void setTrabajadoreses(Set<TrabajadorDTO> trabajadoreses) {
        this.trabajadoreses = trabajadoreses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServicioDTO)) {
            return false;
        }

        ServicioDTO servicioDTO = (ServicioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, servicioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServicioDTO{" +
            "id=" + getId() +
            ", tipoServicio='" + getTipoServicio() + "'" +
            ", zona='" + getZona() + "'" +
            ", frecuencia='" + getFrecuencia() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", horaInicio='" + getHoraInicio() + "'" +
            ", duracionHoras=" + getDuracionHoras() +
            ", numTrabajadores=" + getNumTrabajadores() +
            ", estado='" + getEstado() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", municipio='" + getMunicipio() + "'" +
            ", notas='" + getNotas() + "'" +
            ", precioTotal=" + getPrecioTotal() +
            ", descuento=" + getDescuento() +
            ", cliente=" + getCliente() +
            ", tarifa=" + getTarifa() +
            ", trabajadoreses=" + getTrabajadoreses() +
            "}";
    }
}
