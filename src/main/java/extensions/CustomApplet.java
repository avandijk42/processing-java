package extensions;

import processing.core.PApplet;
import util.Point;

public class CustomApplet extends PApplet {

    public void point(Point p) {
        point(p.getX(), p.getY());
    }
    public float dist(Point a, Point b) {
        return dist(a.getX(), a.getY(), b.getX(), b.getY());
    }
    public void line(Point a, Point b) {
        line(a.getX(), a.getY(), b.getX(), b.getY());
    }
}
