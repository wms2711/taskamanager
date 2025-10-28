// src/test/java/org/openapitools/jackson/nullable/JsonNullableTest.java
package org.openapitools.jackson.nullable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;

class JsonNullableTest {

    @Test
    void of_shouldCreatePresentValue() {
        JsonNullable<String> nullable = JsonNullable.of("hello");

        assertThat(nullable.isPresent()).isTrue();
        assertThat(nullable.isEmpty()).isFalse();
        assertThat(nullable.get()).isEqualTo("hello");
        assertThat(nullable.toOptional()).contains("hello");
    }

    @Test
    void empty_shouldCreateAbsentValue() {
        JsonNullable<String> nullable = JsonNullable.empty();

        assertThat(nullable.isPresent()).isFalse();
        assertThat(nullable.isEmpty()).isTrue();
        assertThat(nullable.toOptional()).isEmpty();
    }

    @Test
    void undefined_shouldBehaveLikeEmpty() {
        JsonNullable<String> nullable = JsonNullable.undefined();

        assertThat(nullable.isPresent()).isFalse();
        assertThat(nullable.isEmpty()).isTrue();
        assertThat(nullable.toOptional()).isEmpty();
    }

    @Test
    void get_shouldThrowWhenNotPresent() {
        JsonNullable<String> nullable = JsonNullable.empty();

        assertThatThrownBy(nullable::get)
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Value is not present");
    }

    @Test
    void equals_shouldCompareValueAndPresence() {
        JsonNullable<String> a1 = JsonNullable.of("test");
        JsonNullable<String> a2 = JsonNullable.of("test");
        JsonNullable<String> b = JsonNullable.of("other");
        JsonNullable<String> empty1 = JsonNullable.empty();
        JsonNullable<String> empty2 = JsonNullable.empty();

        assertThat(a1).isEqualTo(a2).isNotEqualTo(b).isNotEqualTo(empty1);
        assertThat(empty1).isEqualTo(empty2).isNotEqualTo(a1);
    }

    @Test
    void hashCode_shouldBeConsistentWithEquals() {
        JsonNullable<String> a1 = JsonNullable.of("test");
        JsonNullable<String> a2 = JsonNullable.of("test");
        JsonNullable<String> empty1 = JsonNullable.empty();
        JsonNullable<String> empty2 = JsonNullable.empty();

        assertThat(a1).hasSameHashCodeAs(a2);
        assertThat(empty1).hasSameHashCodeAs(empty2);
    }

    @Test
    void toString_shouldShowValueOrNull() {
        JsonNullable<String> present = JsonNullable.of("data");
        JsonNullable<String> absent = JsonNullable.empty();

        assertThat(present.toString()).isEqualTo("data");
        assertThat(absent.toString()).isEqualTo("null");
    }

    @Test
    void fromOptional_shouldHandlePresentAndEmpty() {
        Optional<String> present = Optional.of("value");
        Optional<String> empty = Optional.empty();

        JsonNullable<String> fromPresent = JsonNullable.fromOptional(present);
        JsonNullable<String> fromEmpty = JsonNullable.fromOptional(empty);

        assertThat(fromPresent.isPresent()).isTrue();
        assertThat(fromPresent.get()).isEqualTo("value");
        assertThat(fromEmpty.isPresent()).isFalse();
    }

    @Test
    void of_shouldRejectNull() {
        assertThatThrownBy(() -> JsonNullable.of(null))
            .isInstanceOf(NullPointerException.class);
    }
}