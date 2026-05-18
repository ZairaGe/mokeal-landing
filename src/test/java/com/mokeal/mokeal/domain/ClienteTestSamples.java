package com.mokeal.mokeal.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ClienteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Cliente getClienteSample1() {
        return new Cliente()
            .id(1L)
            .nombre("nombre1")
            .telefono("telefono1")
            .email("email1")
            .nifCif("nifCif1")
            .direccion("direccion1")
            .municipio("municipio1")
            .codigoPostal("codigoPostal1")
            .notas("notas1");
    }

    public static Cliente getClienteSample2() {
        return new Cliente()
            .id(2L)
            .nombre("nombre2")
            .telefono("telefono2")
            .email("email2")
            .nifCif("nifCif2")
            .direccion("direccion2")
            .municipio("municipio2")
            .codigoPostal("codigoPostal2")
            .notas("notas2");
    }

    public static Cliente getClienteRandomSampleGenerator() {
        return new Cliente()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .telefono(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .nifCif(UUID.randomUUID().toString())
            .direccion(UUID.randomUUID().toString())
            .municipio(UUID.randomUUID().toString())
            .codigoPostal(UUID.randomUUID().toString())
            .notas(UUID.randomUUID().toString());
    }
}
