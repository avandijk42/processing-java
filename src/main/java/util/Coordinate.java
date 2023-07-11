package util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Coordinate {
    private int x;
    private int y;


    public Coordinate add(int dx, int dy) {
        return new Coordinate(this.x + dx, this.y + dy);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Coordinate)) return false;
        Coordinate other = (Coordinate) o;
        return this.x == other.x && this.y == other.y;
    }

    @Override
    public int hashCode() {
        return String.format("%d,%d", x, y).hashCode();
    }
}
