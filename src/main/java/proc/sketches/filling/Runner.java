package proc.sketches.filling;

import processing.core.PApplet;
import util.Orientation;
import util.Point;
import util.Rectangle;

import static util.Orientation.VERTICAL;

public class Runner extends PApplet {

    private static int WIDTH = 720;
    private static int HEIGHT = 360;

    public void settings(){
        size(WIDTH, HEIGHT);
    }

    public void draw(){
        Rectangle seed = new Rectangle(
                new Point(0f, 0f), new Point(WIDTH, 0f),
                new Point(WIDTH, HEIGHT), new Point(0f, HEIGHT));
        drawLine(seed, VERTICAL);
    }

    private void drawLine(Rectangle rectangle, Orientation orientation) {
        Point center = rectangle.center();
        switch (orientation) {
            case VERTICAL:
                float distance = rectangle.height() / 4;
                Point top = new Point(center.getX(), center.getY() + distance);
                Point bottom = new Point(center.getX(), center.getY() - distance);
                line(top, bottom);
            case HORIZONTAL:
                distance = rectangle.width() / 4;
                Point left = new Point(center.getX() - distance, center.getY());
                Point right = new Point(center.getX() + distance, center.getY());
                line(left, right);
        }
    }

    private void line(Point p1, Point p2) {
        line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    public static void main(String... args){
        PApplet.main("proc.sketches.filling.Runner");
    }
}
