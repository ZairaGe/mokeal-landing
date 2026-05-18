package com.mokeal.mokeal.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mokeal.mokeal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TarifaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TarifaDTO.class);
        TarifaDTO tarifaDTO1 = new TarifaDTO();
        tarifaDTO1.setId(1L);
        TarifaDTO tarifaDTO2 = new TarifaDTO();
        assertThat(tarifaDTO1).isNotEqualTo(tarifaDTO2);
        tarifaDTO2.setId(tarifaDTO1.getId());
        assertThat(tarifaDTO1).isEqualTo(tarifaDTO2);
        tarifaDTO2.setId(2L);
        assertThat(tarifaDTO1).isNotEqualTo(tarifaDTO2);
        tarifaDTO1.setId(null);
        assertThat(tarifaDTO1).isNotEqualTo(tarifaDTO2);
    }
}
