package proc.sketches;

import processing.core.PApplet;
import util.Orientation;
import util.Point;

public class Trees extends PApplet {

  private static final int MAX_ITER = 10;

  private float width = 800;
  private float height = 800;

  private float initialStep = 150f;
  private float initialStroke = 4f;
  private float stepDecayFactor = 0.6f;
  private float strokeDecayFactor = 0.9f;

  public void settings(){
    size((int)width, (int)height);
  }

  public void setup() {
    frameRate(20);
  }

  public void draw() {
    background(255f);

    performStep(new Point(width/2, height/2), initialStep, initialStroke, Orientation.VERTICAL, 0);
  }

  public void performStep(Point p, float step, float weight, Orientation o, int iteration) {

    if (iteration >=  MAX_ITER)
      return;

    float xDiff = 0;
    float yDiff = 0;

    switch(o) {
      case VERTICAL:
        yDiff = step;
        break;
      case HORIZONTAL:
        xDiff = step;
        break;
    }

    Point[] nextPoints = new Point[]{p.move(xDiff, yDiff), p.move(-1 * xDiff, -1 * yDiff)};

    strokeWeight(weight);
    line(nextPoints[0], nextPoints[1]);

    for (Point pNext: nextPoints) {
      performStep(pNext, step * stepDecayFactor, weight * strokeDecayFactor, o.opposite(), iteration + 1);
    }
  }

  private void line(Point p1, Point p2) {
    line(p1.x, p1.y, p2.x, p2.y);
  }

  public static void main(String... args){
    PApplet.main("proc.sketches.Trees");
  }

}
