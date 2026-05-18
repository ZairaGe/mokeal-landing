package com.mokeal.mokeal.domain;

import static com.mokeal.mokeal.domain.ServicioTestSamples.*;
import static com.mokeal.mokeal.domain.TrabajadorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mokeal.mokeal.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TrabajadorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Trabajador.class);
        Trabajador trabajador1 = getTrabajadorSample1();
        Trabajador trabajador2 = new Trabajador();
        assertThat(trabajador1).isNotEqualTo(trabajador2);

        trabajador2.setId(trabajador1.getId());
        assertThat(trabajador1).isEqualTo(trabajador2);

        trabajador2 = getTrabajadorSample2();
        assertThat(trabajador1).isNotEqualTo(trabajador2);
    }

    @Test
    void serviciosTest() {
        Trabajador trabajador = getTrabajadorRandomSampleGenerator();
        Servicio servicioBack = getServicioRandomSampleGenerator();

        trabajador.addServicios(servicioBack);
        assertThat(trabajador.getServicioses()).containsOnly(servicioBack);
        assertThat(servicioBack.getTrabajadoreses()).containsOnly(trabajador);

        trabajador.removeServicios(servicioBack);
        assertThat(trabajador.getServicioses()).doesNotContain(servicioBack);
        assertThat(servicioBack.getTrabajadoreses()).doesNotContain(trabajador);

        trabajador.servicioses(new HashSet<>(Set.of(servicioBack)));
        assertThat(trabajador.getServicioses()).containsOnly(servicioBack);
        assertThat(servicioBack.getTrabajadoreses()).containsOnly(trabajador);

        trabajador.setServicioses(new HashSet<>());
        assertThat(trabajador.getServicioses()).doesNotContain(servicioBack);
        assertThat(servicioBack.getTrabajadoreses()).doesNotContain(trabajador);
    }
}
