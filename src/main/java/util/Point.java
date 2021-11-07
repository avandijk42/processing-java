package util;

import lombok.Getter;

@Getter
public class Point {

  public float x;
  public float y;

  public Point(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public Point move(float xDiff, float yDiff) {
    return new Point(x + xDiff, y + yDiff);
  }
}
