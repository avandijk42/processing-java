package util;

public class Color {

  public float r;
  public float g;
  public float b;

  public Color(float r, float g, float b) {
    this.r = r;
    this.g = g;
    this.b = b;
  }

  public Color lerpTo(Color other, float step) {
    return new Color(
        this.r + step * (other.r - this.r),
        this.g + step * (other.g - this.g),
        this.b + step * (other.b - this.b)
    );
  }
}
