package com.mokeal.mokeal.domain;

import static com.mokeal.mokeal.domain.ClienteTestSamples.*;
import static com.mokeal.mokeal.domain.FacturaTestSamples.*;
import static com.mokeal.mokeal.domain.ServicioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mokeal.mokeal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacturaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Factura.class);
        Factura factura1 = getFacturaSample1();
        Factura factura2 = new Factura();
        assertThat(factura1).isNotEqualTo(factura2);

        factura2.setId(factura1.getId());
        assertThat(factura1).isEqualTo(factura2);

        factura2 = getFacturaSample2();
        assertThat(factura1).isNotEqualTo(factura2);
    }

    @Test
    void servicioTest() {
        Factura factura = getFacturaRandomSampleGenerator();
        Servicio servicioBack = getServicioRandomSampleGenerator();

        factura.setServicio(servicioBack);
        assertThat(factura.getServicio()).isEqualTo(servicioBack);

        factura.servicio(null);
        assertThat(factura.getServicio()).isNull();
    }

    @Test
    void clienteTest() {
        Factura factura = getFacturaRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        factura.setCliente(clienteBack);
        assertThat(factura.getCliente()).isEqualTo(clienteBack);

        factura.cliente(null);
        assertThat(factura.getCliente()).isNull();
    }
}
