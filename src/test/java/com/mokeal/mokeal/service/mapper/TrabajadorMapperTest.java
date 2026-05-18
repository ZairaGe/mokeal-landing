package com.mokeal.mokeal.service.mapper;

import static com.mokeal.mokeal.domain.TrabajadorAsserts.*;
import static com.mokeal.mokeal.domain.TrabajadorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrabajadorMapperTest {

    private TrabajadorMapper trabajadorMapper;

    @BeforeEach
    void setUp() {
        trabajadorMapper = new TrabajadorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTrabajadorSample1();
        var actual = trabajadorMapper.toEntity(trabajadorMapper.toDto(expected));
        assertTrabajadorAllPropertiesEquals(expected, actual);
    }
}
