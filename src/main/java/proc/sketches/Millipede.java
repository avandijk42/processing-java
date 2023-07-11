package proc.sketches;

import processing.core.PApplet;
import util.Ellipse;
import util.Point;

public class Millipede extends PApplet {

    private static final float WIDTH = 1000;
    private static final float HEIGHT = 1000;

    Ellipse ellipse;

    public void settings() {
        size((int)WIDTH, (int)HEIGHT);
    }

    public void setup() {
        background(255);
        frameRate(20);

        ellipse = Ellipse.builder()
                .centerX(WIDTH/2)
                .centerY(HEIGHT/2)
                .width(135)
                .height(75)
                .startingAngleRadians(0)
                .speed(PI * 2)
                .build();

    }

    public void draw() {
        background(255);

        strokeWeight(1f);
        line(0, HEIGHT/2, WIDTH, HEIGHT/2);

        int frameOffsetDegrees = 0;
        float xStep = 90f;
        for (float xOffset = -5 * WIDTH/4; xOffset < 5 * WIDTH/4 ; xOffset += xStep) {
            Point pos = ellipse.evaluate(frameCount + frameOffsetDegrees);

            strokeWeight(3f);
            noFill();
            bezier(WIDTH/2 + xOffset, 100f,
                    WIDTH/2 + xOffset, 200f,
                    pos.getX() + xOffset, pos.getY()-100f,
                    pos.getX() + xOffset, pos.getY());

            strokeWeight(1f);
            ellipse(pos.getX() + xOffset, pos.getY(), 5, 5);

            frameOffsetDegrees += 3;
        }

    }

    public static void main(String... args){
        PApplet.main("proc.sketches.Millipede");
    }
}
