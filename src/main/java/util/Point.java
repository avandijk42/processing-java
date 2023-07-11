package util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Point {

    private float x;
    private float y;

    public Point plus(float x0, float y0) {
        return new Point(x + x0, y + y0);
    }

}
