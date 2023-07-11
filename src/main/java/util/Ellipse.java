package util;

import lombok.Builder;
import lombok.Getter;

import static java.lang.Math.*;

@Builder
@Getter
public class Ellipse {
    private float centerX;
    private float centerY;
    private float width;
    private float height;
    private float speed;
    private float startingAngleRadians;

    public Point evaluate(int frameCount) {
        final float frameRadians = (float) Math.PI * ((float) frameCount) / 180f;
        final float angle = startingAngleRadians + speed * frameRadians;
        final float yValue = min(centerY - height * (float) sin(angle), centerY);
        return new Point(centerX + width * (float) cos(angle), yValue);
    }
}
