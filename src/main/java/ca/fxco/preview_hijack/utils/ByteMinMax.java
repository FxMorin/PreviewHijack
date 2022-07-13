package ca.fxco.preview_hijack.utils;

public class ByteMinMax extends MinMax<Byte> {

    public ByteMinMax() {
        super((byte)0, Byte.MAX_VALUE);
    }

    public ByteMinMax(Byte min) {
        super(min, Byte.MAX_VALUE);
    }

    public ByteMinMax(Byte min, Byte max) {
        super(min, max);
    }

    public ByteMinMax swap() {
        return of((byte)(this.max * -1), (byte)(this.min * -1));
    }

    public short distance() {
        return (short)(this.max - this.min);
    }

    public boolean isWithin(byte val) {
        return val <= this.max && val >= this.min;
    }

    public static ByteMinMax of(final Byte min, final Byte max) {
        return new ByteMinMax(min, max);
    }

    public static boolean isValid(final int val) {
        return val >= Byte.MIN_VALUE && val <= Byte.MAX_VALUE;
    }

    public static boolean isValid(final short val) {
        return val >= Byte.MIN_VALUE && val <= Byte.MAX_VALUE;
    }

    public static boolean isValid(final float val) {
        return val >= Byte.MIN_VALUE && val <= Byte.MAX_VALUE;
    }
}
