package ca.fxco.preview_hijack.utils;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MinMax<T> {

    protected final T min;
    protected final T max;

    public MinMax(final T min, final T max) {
        this.min = min;
        this.max = max;
    }

    public T getMin() {
        return this.min;
    }

    public T getMax() {
        return this.max;
    }

    /**
     * Should be flipped. So make sure to multiply both sides by -1
     * Since min should always be smaller then max
     */
    public MinMax<T> swap() {
        return of(this.max, this.min);
    }

    @Override
    public String toString() {
        return "[" + this.min + ", " + this.max + "]";
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof final MinMax<?> other)) return false;
        return Objects.equals(this.min, other.min) && Objects.equals(this.max, other.max);
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(this.min, this.max);
    }

    public static <T> MinMax<T> of(final T min, final T max) {
        return new MinMax<>(min, max);
    }

    public static <T> Collector<MinMax<T>, ?, Map<T, T>> toMap() {
        return Collectors.toMap(MinMax::getMin, MinMax::getMax);
    }
}
