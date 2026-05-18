package com.mokeal.mokeal.domain;

import static com.mokeal.mokeal.domain.ClienteTestSamples.*;
import static com.mokeal.mokeal.domain.ServicioTestSamples.*;
import static com.mokeal.mokeal.domain.TarifaTestSamples.*;
import static com.mokeal.mokeal.domain.TrabajadorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mokeal.mokeal.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ServicioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Servicio.class);
        Servicio servicio1 = getServicioSample1();
        Servicio servicio2 = new Servicio();
        assertThat(servicio1).isNotEqualTo(servicio2);

        servicio2.setId(servicio1.getId());
        assertThat(servicio1).isEqualTo(servicio2);

        servicio2 = getServicioSample2();
        assertThat(servicio1).isNotEqualTo(servicio2);
    }

    @Test
    void clienteTest() {
        Servicio servicio = getServicioRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        servicio.setCliente(clienteBack);
        assertThat(servicio.getCliente()).isEqualTo(clienteBack);

        servicio.cliente(null);
        assertThat(servicio.getCliente()).isNull();
    }

    @Test
    void tarifaTest() {
        Servicio servicio = getServicioRandomSampleGenerator();
        Tarifa tarifaBack = getTarifaRandomSampleGenerator();

        servicio.setTarifa(tarifaBack);
        assertThat(servicio.getTarifa()).isEqualTo(tarifaBack);

        servicio.tarifa(null);
        assertThat(servicio.getTarifa()).isNull();
    }

    @Test
    void trabajadoresTest() {
        Servicio servicio = getServicioRandomSampleGenerator();
        Trabajador trabajadorBack = getTrabajadorRandomSampleGenerator();

        servicio.addTrabajadores(trabajadorBack);
        assertThat(servicio.getTrabajadoreses()).containsOnly(trabajadorBack);

        servicio.removeTrabajadores(trabajadorBack);
        assertThat(servicio.getTrabajadoreses()).doesNotContain(trabajadorBack);

        servicio.trabajadoreses(new HashSet<>(Set.of(trabajadorBack)));
        assertThat(servicio.getTrabajadoreses()).containsOnly(trabajadorBack);

        servicio.setTrabajadoreses(new HashSet<>());
        assertThat(servicio.getTrabajadoreses()).doesNotContain(trabajadorBack);
    }
}
