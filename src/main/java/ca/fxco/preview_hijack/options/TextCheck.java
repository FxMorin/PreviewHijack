package ca.fxco.preview_hijack.options;

import ca.fxco.preview_hijack.PreviewHijack;
import ca.fxco.preview_hijack.utils.ByteMinMax;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public enum TextCheck {

    WORDLIST((s,a) -> List.of(a).contains(s.toLowerCase()),1),
    REGEX((s,a) -> s.matches(a[0]), 1, 1),
    STARTS_WITH((s,a) -> s.startsWith(a[0]),1, 1),
    ENDS_WITH((s,a) -> s.endsWith(a[0]),1, 1),
    EQUALS((s,a) -> s.equals(a[0]),1, 1),
    EQUALS_IGNORE_CASE((s,a) -> s.equalsIgnoreCase(a[0]), 1, 1),
    CONTAINS((s,a) -> s.contains(a[0]),1,1),
    IS_EMPTY((s,a) -> s.isEmpty()),
    LENGTH_GREATER_THEN((s,a) -> s.length() > Integer.parseInt(a[0]), 1, 1),
    LENGTH_SMALLER_THEN((s,a) -> s.length() < Integer.parseInt(a[0]), 1, 1),
    LENGTH_EQUAL((s,a) -> s.length() == Integer.parseInt(a[0]), 1, 1),
    CHAR_AT((s,a) -> s.charAt(Integer.parseInt(a[0])) == a[1].charAt(0), 2, 2);

    @Nullable
    private final BiPredicate<String, String[]> stringChecker;
    private final ByteMinMax MinMax;

    TextCheck(@Nullable BiPredicate<String, String[]> stringChecker) {
        this.stringChecker = stringChecker;
        this.MinMax = new ByteMinMax();
    }

    TextCheck(@Nullable BiPredicate<String, String[]> stringChecker, int min) {
        this.stringChecker = stringChecker;
        this.MinMax = new ByteMinMax((byte)min);
    }

    TextCheck(@Nullable BiPredicate<String, String[]> stringChecker, int min, int max) {
        this.stringChecker = stringChecker;
        this.MinMax = new ByteMinMax((byte)min,(byte)max);
    }

    public boolean checkString(String text, String[] args) {
        if (this.stringChecker == null || args == null) return true;
        try {
            return this.stringChecker.test(text, args);
        } catch (Exception err) {
            PreviewHijack.LOGGER.error("Failed to execute stringChecker: "+this.name()+"\n"+err);
            return false;
        }
    }

    public void validateOption(String text, String[] args) {
        if (this.stringChecker == null) return;
        if (args == null && this.MinMax.getMin() != 0) {
            throw new IndexOutOfBoundsException("No inputs for textAction `" + this.name() + "` where given - [" +
                    this.MinMax.getMin() + "] are required!");
        }
        if (args != null && !this.MinMax.isWithin((byte)args.length)) {
            throw new IndexOutOfBoundsException(
                    "Bad inputs for textAction `" + this.name() +
                            "` - Should have between " + this.MinMax + " got ["+args.length+"]"
            );
        }
        this.stringChecker.test(text, args); // Attempt test
    }
}
