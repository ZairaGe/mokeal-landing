package com.mokeal.mokeal.service.mapper;

import static com.mokeal.mokeal.domain.TarifaAsserts.*;
import static com.mokeal.mokeal.domain.TarifaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TarifaMapperTest {

    private TarifaMapper tarifaMapper;

    @BeforeEach
    void setUp() {
        tarifaMapper = new TarifaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTarifaSample1();
        var actual = tarifaMapper.toEntity(tarifaMapper.toDto(expected));
        assertTarifaAllPropertiesEquals(expected, actual);
    }
}
