package com.mokeal.mokeal.domain;

import static com.mokeal.mokeal.domain.TarifaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mokeal.mokeal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TarifaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tarifa.class);
        Tarifa tarifa1 = getTarifaSample1();
        Tarifa tarifa2 = new Tarifa();
        assertThat(tarifa1).isNotEqualTo(tarifa2);

        tarifa2.setId(tarifa1.getId());
        assertThat(tarifa1).isEqualTo(tarifa2);

        tarifa2 = getTarifaSample2();
        assertThat(tarifa1).isNotEqualTo(tarifa2);
    }
}
