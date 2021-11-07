package util;

import static processing.core.PApplet.lerp;

public class Bezier {

  public Point point1;
  public Point anchor1;
  public Point point2;
  public Point anchor2;

  public Bezier(Point p1, Point a1, Point a2, Point p2) {
    point1 = p1;
    anchor1 = a1;
    anchor2 = a2;
    point2 = p2;
  }

  public Point[] getPoints() {
    return new Point[]{point1, anchor1, anchor2, point2};
  }

  public void updatePoint(int index, Point p) {
    switch (index) {
      case 0:
        point1 = p;
        break;
      case 1:
        anchor1 = p;
        break;
      case 2:
        anchor2 = p;
        break;
      case 3:
        point2 = p;
        break;
    }
  }

  public Bezier lerpBezier(Bezier other, float amount) {
    return new Bezier (
        new Point(lerp(this.point1.x, other.point1.x, amount), lerp(this.point1.y, other.point1.y, amount)),
        new Point(lerp(this.anchor1.x, other.anchor1.x, amount), lerp(this.anchor1.y, other.anchor1.y, amount)),
        new Point(lerp(this.anchor2.x, other.anchor2.x, amount), lerp(this.anchor2.y, other.anchor2.y, amount)),
        new Point(lerp(this.point2.x, other.point2.x, amount), lerp(this.point2.y, other.point2.y, amount))
    );
  }
}
