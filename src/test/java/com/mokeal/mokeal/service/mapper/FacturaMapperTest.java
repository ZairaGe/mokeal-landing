package com.mokeal.mokeal.service.mapper;

import static com.mokeal.mokeal.domain.FacturaAsserts.*;
import static com.mokeal.mokeal.domain.FacturaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FacturaMapperTest {

    private FacturaMapper facturaMapper;

    @BeforeEach
    void setUp() {
        facturaMapper = new FacturaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFacturaSample1();
        var actual = facturaMapper.toEntity(facturaMapper.toDto(expected));
        assertFacturaAllPropertiesEquals(expected, actual);
    }
}
