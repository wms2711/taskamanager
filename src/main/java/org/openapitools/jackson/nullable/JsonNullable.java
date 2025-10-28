package org.openapitools.jackson.nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;
import java.util.Optional;

/**
 * A container for nullable values that can be serialized/deserialized with Jackson.
 */
public final class JsonNullable<T> {
    private final T value;
    private final boolean isPresent;

    private JsonNullable(T value, boolean isPresent) {
        this.value = value;
        this.isPresent = isPresent;
    }

    public static <T> JsonNullable<T> of(T value) {
        return new JsonNullable<>(Objects.requireNonNull(value), true);
    }

    public static <T> JsonNullable<T> empty() {
        return new JsonNullable<>(null, false);
    }

    public static <T> JsonNullable<T> undefined() {
        return new JsonNullable<>(null, false);
    }

    @JsonValue
    public T get() {
        if (!isPresent) {
            throw new IllegalStateException("Value is not present");
        }
        return value;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public boolean isEmpty() {
        return !isPresent;
    }

    public Optional<T> toOptional() {
        return isPresent ? Optional.of(value) : Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonNullable<?> that = (JsonNullable<?>) o;
        return isPresent == that.isPresent && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, isPresent);
    }

    @Override
    public String toString() {
        return isPresent ? String.valueOf(value) : "null";
    }

    @JsonCreator
    public static <T> JsonNullable<T> fromOptional(Optional<T> optional) {
        return optional.map(JsonNullable::of).orElseGet(JsonNullable::empty);
    }
}
