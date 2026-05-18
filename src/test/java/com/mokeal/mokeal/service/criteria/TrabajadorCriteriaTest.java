package com.mokeal.mokeal.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TrabajadorCriteriaTest {

    @Test
    void newTrabajadorCriteriaHasAllFiltersNullTest() {
        var trabajadorCriteria = new TrabajadorCriteria();
        assertThat(trabajadorCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void trabajadorCriteriaFluentMethodsCreatesFiltersTest() {
        var trabajadorCriteria = new TrabajadorCriteria();

        setAllFilters(trabajadorCriteria);

        assertThat(trabajadorCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void trabajadorCriteriaCopyCreatesNullFilterTest() {
        var trabajadorCriteria = new TrabajadorCriteria();
        var copy = trabajadorCriteria.copy();

        assertThat(trabajadorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(trabajadorCriteria)
        );
    }

    @Test
    void trabajadorCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var trabajadorCriteria = new TrabajadorCriteria();
        setAllFilters(trabajadorCriteria);

        var copy = trabajadorCriteria.copy();

        assertThat(trabajadorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(trabajadorCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var trabajadorCriteria = new TrabajadorCriteria();

        assertThat(trabajadorCriteria).hasToString("TrabajadorCriteria{}");
    }

    private static void setAllFilters(TrabajadorCriteria trabajadorCriteria) {
        trabajadorCriteria.id();
        trabajadorCriteria.nombre();
        trabajadorCriteria.telefono();
        trabajadorCriteria.email();
        trabajadorCriteria.activo();
        trabajadorCriteria.notas();
        trabajadorCriteria.serviciosId();
        trabajadorCriteria.distinct();
    }

    private static Condition<TrabajadorCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNombre()) &&
                condition.apply(criteria.getTelefono()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getActivo()) &&
                condition.apply(criteria.getNotas()) &&
                condition.apply(criteria.getServiciosId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TrabajadorCriteria> copyFiltersAre(TrabajadorCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNombre(), copy.getNombre()) &&
                condition.apply(criteria.getTelefono(), copy.getTelefono()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getActivo(), copy.getActivo()) &&
                condition.apply(criteria.getNotas(), copy.getNotas()) &&
                condition.apply(criteria.getServiciosId(), copy.getServiciosId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
