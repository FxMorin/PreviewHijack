package ca.fxco.preview_hijack.utils;

import ca.fxco.preview_hijack.options.TextCheck;

import java.util.Arrays;
import java.util.Objects;

public class FullCondition {

    private TextCheck check;
    private String[] args;
    private boolean inverse;

    public FullCondition() {}

    public FullCondition(final TextCheck check, final String[] args) {
        this.check = check;
        this.args = args;
        this.inverse = false;
    }

    public FullCondition(final TextCheck check, final String[] args, final boolean inverse) {
        this.check = check;
        this.args = args;
        this.inverse = inverse;
    }

    public TextCheck getCheck() {
        return this.check;
    }

    public String[] getArgs() {
        return this.args;
    }

    public boolean getInverse() {
        return this.inverse;
    }

    public void setCheck(TextCheck check) {
        this.check = check;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public void setInverse(boolean inverse) {
        this.inverse = inverse;
    }

    @Override
    public String toString() {
        return this.check.name() + ": " + Arrays.toString(this.args);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof final FullCondition other)
            return Objects.equals(this.check, other.check) && Arrays.equals(this.args, other.args);
        return false;
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(this.check, this.args);
    }

    public static FullCondition of(final TextCheck check, final String[] args) {
        return new FullCondition(check, args);
    }

    public static FullCondition of(final TextCheck check, final String[] args, final boolean inverse) {
        return new FullCondition(check, args, inverse);
    }
}
