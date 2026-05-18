package com.mokeal.mokeal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mokeal.mokeal.domain.enumeration.EstadoServicio;
import com.mokeal.mokeal.domain.enumeration.Frecuencia;
import com.mokeal.mokeal.domain.enumeration.TipoServicio;
import com.mokeal.mokeal.domain.enumeration.ZonaTarifa;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Servicio.
 */
@Entity
@Table(name = "servicio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Servicio implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_servicio", nullable = false)
    private TipoServicio tipoServicio;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "zona", nullable = false)
    private ZonaTarifa zona;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "frecuencia", nullable = false)
    private Frecuencia frecuencia;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull
    @Size(max = 5)
    @Column(name = "hora_inicio", length = 5, nullable = false)
    private String horaInicio;

    @NotNull
    @Column(name = "duracion_horas", precision = 21, scale = 2, nullable = false)
    private BigDecimal duracionHoras;

    @NotNull
    @Column(name = "num_trabajadores", nullable = false)
    private Integer numTrabajadores;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoServicio estado;

    @Size(max = 200)
    @Column(name = "direccion", length = 200)
    private String direccion;

    @Size(max = 100)
    @Column(name = "municipio", length = 100)
    private String municipio;

    @Size(max = 1000)
    @Column(name = "notas", length = 1000)
    private String notas;

    @Column(name = "precio_total", precision = 21, scale = 2)
    private BigDecimal precioTotal;

    @Column(name = "descuento", precision = 21, scale = 2)
    private BigDecimal descuento;

    @ManyToOne(optional = false)
    @NotNull
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tarifa tarifa;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_servicio__trabajadores",
        joinColumns = @JoinColumn(name = "servicio_id"),
        inverseJoinColumns = @JoinColumn(name = "trabajadores_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "servicioses" }, allowSetters = true)
    private Set<Trabajador> trabajadoreses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Servicio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoServicio getTipoServicio() {
        return this.tipoServicio;
    }

    public Servicio tipoServicio(TipoServicio tipoServicio) {
        this.setTipoServicio(tipoServicio);
        return this;
    }

    public void setTipoServicio(TipoServicio tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public ZonaTarifa getZona() {
        return this.zona;
    }

    public Servicio zona(ZonaTarifa zona) {
        this.setZona(zona);
        return this;
    }

    public void setZona(ZonaTarifa zona) {
        this.zona = zona;
    }

    public Frecuencia getFrecuencia() {
        return this.frecuencia;
    }

    public Servicio frecuencia(Frecuencia frecuencia) {
        this.setFrecuencia(frecuencia);
        return this;
    }

    public void setFrecuencia(Frecuencia frecuencia) {
        this.frecuencia = frecuencia;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public Servicio fecha(LocalDate fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getHoraInicio() {
        return this.horaInicio;
    }

    public Servicio horaInicio(String horaInicio) {
        this.setHoraInicio(horaInicio);
        return this;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public BigDecimal getDuracionHoras() {
        return this.duracionHoras;
    }

    public Servicio duracionHoras(BigDecimal duracionHoras) {
        this.setDuracionHoras(duracionHoras);
        return this;
    }

    public void setDuracionHoras(BigDecimal duracionHoras) {
        this.duracionHoras = duracionHoras;
    }

    public Integer getNumTrabajadores() {
        return this.numTrabajadores;
    }

    public Servicio numTrabajadores(Integer numTrabajadores) {
        this.setNumTrabajadores(numTrabajadores);
        return this;
    }

    public void setNumTrabajadores(Integer numTrabajadores) {
        this.numTrabajadores = numTrabajadores;
    }

    public EstadoServicio getEstado() {
        return this.estado;
    }

    public Servicio estado(EstadoServicio estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadoServicio estado) {
        this.estado = estado;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Servicio direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getMunicipio() {
        return this.municipio;
    }

    public Servicio municipio(String municipio) {
        this.setMunicipio(municipio);
        return this;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getNotas() {
        return this.notas;
    }

    public Servicio notas(String notas) {
        this.setNotas(notas);
        return this;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public BigDecimal getPrecioTotal() {
        return this.precioTotal;
    }

    public Servicio precioTotal(BigDecimal precioTotal) {
        this.setPrecioTotal(precioTotal);
        return this;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public BigDecimal getDescuento() {
        return this.descuento;
    }

    public Servicio descuento(BigDecimal descuento) {
        this.setDescuento(descuento);
        return this;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Servicio cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    public Tarifa getTarifa() {
        return this.tarifa;
    }

    public void setTarifa(Tarifa tarifa) {
        this.tarifa = tarifa;
    }

    public Servicio tarifa(Tarifa tarifa) {
        this.setTarifa(tarifa);
        return this;
    }

    public Set<Trabajador> getTrabajadoreses() {
        return this.trabajadoreses;
    }

    public void setTrabajadoreses(Set<Trabajador> trabajadors) {
        this.trabajadoreses = trabajadors;
    }

    public Servicio trabajadoreses(Set<Trabajador> trabajadors) {
        this.setTrabajadoreses(trabajadors);
        return this;
    }

    public Servicio addTrabajadores(Trabajador trabajador) {
        this.trabajadoreses.add(trabajador);
        return this;
    }

    public Servicio removeTrabajadores(Trabajador trabajador) {
        this.trabajadoreses.remove(trabajador);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Servicio)) {
            return false;
        }
        return getId() != null && getId().equals(((Servicio) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Servicio{" +
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
            "}";
    }
}
