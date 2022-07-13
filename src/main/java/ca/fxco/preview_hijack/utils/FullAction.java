package ca.fxco.preview_hijack.utils;

import ca.fxco.preview_hijack.options.TextStep;

import java.util.Arrays;
import java.util.Objects;

public class FullAction {

    private TextStep step;
    private String[] args;

    public FullAction() {}

    public FullAction(final TextStep step, final String[] args) {
        this.step = step;
        this.args = args;
    }

    public TextStep getStep() {
        return this.step;
    }

    public String[] getArgs() {
        return this.args;
    }

    public void setStep(TextStep step) {
        this.step = step;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return this.step.name() + ": " + Arrays.toString(this.args);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof final FullAction other)
            return Objects.equals(this.step, other.step) && Arrays.equals(this.args, other.args);
        return false;
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(this.step, this.args);
    }

    public static FullAction of(final TextStep step, final String[] args) {
        return new FullAction(step, args);
    }
}
