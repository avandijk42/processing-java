package util;

public enum Orientation {
    HORIZONTAL,
    VERTICAL;

    public Orientation opposite() {
        return this == HORIZONTAL ? VERTICAL : HORIZONTAL;
    }
}
