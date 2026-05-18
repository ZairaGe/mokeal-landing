package com.mokeal.mokeal.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ServicioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Servicio getServicioSample1() {
        return new Servicio()
            .id(1L)
            .horaInicio("horaInicio1")
            .numTrabajadores(1)
            .direccion("direccion1")
            .municipio("municipio1")
            .notas("notas1");
    }

    public static Servicio getServicioSample2() {
        return new Servicio()
            .id(2L)
            .horaInicio("horaInicio2")
            .numTrabajadores(2)
            .direccion("direccion2")
            .municipio("municipio2")
            .notas("notas2");
    }

    public static Servicio getServicioRandomSampleGenerator() {
        return new Servicio()
            .id(longCount.incrementAndGet())
            .horaInicio(UUID.randomUUID().toString())
            .numTrabajadores(intCount.incrementAndGet())
            .direccion(UUID.randomUUID().toString())
            .municipio(UUID.randomUUID().toString())
            .notas(UUID.randomUUID().toString());
    }
}
