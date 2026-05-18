package com.mokeal.mokeal.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TarifaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Tarifa getTarifaSample1() {
        return new Tarifa().id(1L).minimoHoras(1);
    }

    public static Tarifa getTarifaSample2() {
        return new Tarifa().id(2L).minimoHoras(2);
    }

    public static Tarifa getTarifaRandomSampleGenerator() {
        return new Tarifa().id(longCount.incrementAndGet()).minimoHoras(intCount.incrementAndGet());
    }
}
