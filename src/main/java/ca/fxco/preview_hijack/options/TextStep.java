package ca.fxco.preview_hijack.options;

import ca.fxco.preview_hijack.PreviewHijack;
import ca.fxco.preview_hijack.utils.ByteMinMax;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public enum TextStep {

    REGEX((s,a) -> s.replaceAll(a[0],a[1]), 2, 2),
    SET((s,a) -> a[0], 1, 1),
    PREPEND((s,a) -> a[0] + s,1, 1),
    APPEND((s,a) -> s + a[0],1, 1),
    REPEAT((s,a) -> s.repeat(Integer.parseInt(a[0])),1, 1),
    SPLIT((s,a) -> s.split(a[0])[Integer.parseInt(a[1])],2,2),
    SUBSTRING((s,a) -> s.substring(
            Math.min(s.length(),Integer.parseInt(a[0])),
            a.length == 2 ? Math.min(s.length(), Integer.parseInt(a[1])) : s.length()
    ), 1, 2),
    REVERSE((s,a) -> new StringBuilder().append(s).reverse().toString()),
    TO_LOWER_CASE((s,a) -> s.toLowerCase()),
    TO_UPPER_CASE((s,a) -> s.toUpperCase()),
    STRIP((s,a) -> s.strip()),
    TRIM((s,a) -> s.trim()),
    CLEAR((s,a) -> "");

    @Nullable
    private final BiFunction<String, String[], String> stringFormatter;
    private final ByteMinMax MinMax;

    TextStep(@Nullable BiFunction<String, String[], String> stringFormatter) {
        this.stringFormatter = stringFormatter;
        this.MinMax = new ByteMinMax();
    }

    TextStep(@Nullable BiFunction<String, String[], String> stringFormatter, int min) {
        this.stringFormatter = stringFormatter;
        this.MinMax = new ByteMinMax((byte)min);
    }

    TextStep(@Nullable BiFunction<String, String[], String> stringFormatter, int min, int max) {
        this.stringFormatter = stringFormatter;
        this.MinMax = new ByteMinMax((byte)min,(byte)max);
    }

    public String formatString(String text, String[] args) {
        if (this.stringFormatter == null || args == null) return text;
        try {
            return this.stringFormatter.apply(text, args);
        } catch (Exception err) {
            PreviewHijack.LOGGER.error("Failed to execute stringFormatter: "+this.name()+"\n"+err);
            return text;
        }
    }

    public void validateOption(String text, String[] args) {
        if (this.stringFormatter == null) return;
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
        this.stringFormatter.apply(text, args); // Attempt apply
    }
}
