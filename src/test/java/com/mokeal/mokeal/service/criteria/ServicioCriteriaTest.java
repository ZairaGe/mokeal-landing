package com.mokeal.mokeal.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ServicioCriteriaTest {

    @Test
    void newServicioCriteriaHasAllFiltersNullTest() {
        var servicioCriteria = new ServicioCriteria();
        assertThat(servicioCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void servicioCriteriaFluentMethodsCreatesFiltersTest() {
        var servicioCriteria = new ServicioCriteria();

        setAllFilters(servicioCriteria);

        assertThat(servicioCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void servicioCriteriaCopyCreatesNullFilterTest() {
        var servicioCriteria = new ServicioCriteria();
        var copy = servicioCriteria.copy();

        assertThat(servicioCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(servicioCriteria)
        );
    }

    @Test
    void servicioCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var servicioCriteria = new ServicioCriteria();
        setAllFilters(servicioCriteria);

        var copy = servicioCriteria.copy();

        assertThat(servicioCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(servicioCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var servicioCriteria = new ServicioCriteria();

        assertThat(servicioCriteria).hasToString("ServicioCriteria{}");
    }

    private static void setAllFilters(ServicioCriteria servicioCriteria) {
        servicioCriteria.id();
        servicioCriteria.tipoServicio();
        servicioCriteria.zona();
        servicioCriteria.frecuencia();
        servicioCriteria.fecha();
        servicioCriteria.horaInicio();
        servicioCriteria.duracionHoras();
        servicioCriteria.numTrabajadores();
        servicioCriteria.estado();
        servicioCriteria.direccion();
        servicioCriteria.municipio();
        servicioCriteria.notas();
        servicioCriteria.precioTotal();
        servicioCriteria.descuento();
        servicioCriteria.clienteId();
        servicioCriteria.tarifaId();
        servicioCriteria.trabajadoresId();
        servicioCriteria.distinct();
    }

    private static Condition<ServicioCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTipoServicio()) &&
                condition.apply(criteria.getZona()) &&
                condition.apply(criteria.getFrecuencia()) &&
                condition.apply(criteria.getFecha()) &&
                condition.apply(criteria.getHoraInicio()) &&
                condition.apply(criteria.getDuracionHoras()) &&
                condition.apply(criteria.getNumTrabajadores()) &&
                condition.apply(criteria.getEstado()) &&
                condition.apply(criteria.getDireccion()) &&
                condition.apply(criteria.getMunicipio()) &&
                condition.apply(criteria.getNotas()) &&
                condition.apply(criteria.getPrecioTotal()) &&
                condition.apply(criteria.getDescuento()) &&
                condition.apply(criteria.getClienteId()) &&
                condition.apply(criteria.getTarifaId()) &&
                condition.apply(criteria.getTrabajadoresId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ServicioCriteria> copyFiltersAre(ServicioCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTipoServicio(), copy.getTipoServicio()) &&
                condition.apply(criteria.getZona(), copy.getZona()) &&
                condition.apply(criteria.getFrecuencia(), copy.getFrecuencia()) &&
                condition.apply(criteria.getFecha(), copy.getFecha()) &&
                condition.apply(criteria.getHoraInicio(), copy.getHoraInicio()) &&
                condition.apply(criteria.getDuracionHoras(), copy.getDuracionHoras()) &&
                condition.apply(criteria.getNumTrabajadores(), copy.getNumTrabajadores()) &&
                condition.apply(criteria.getEstado(), copy.getEstado()) &&
                condition.apply(criteria.getDireccion(), copy.getDireccion()) &&
                condition.apply(criteria.getMunicipio(), copy.getMunicipio()) &&
                condition.apply(criteria.getNotas(), copy.getNotas()) &&
                condition.apply(criteria.getPrecioTotal(), copy.getPrecioTotal()) &&
                condition.apply(criteria.getDescuento(), copy.getDescuento()) &&
                condition.apply(criteria.getClienteId(), copy.getClienteId()) &&
                condition.apply(criteria.getTarifaId(), copy.getTarifaId()) &&
                condition.apply(criteria.getTrabajadoresId(), copy.getTrabajadoresId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
