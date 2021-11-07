package util;

public class HSBColor {

  public float h;
  public float s;
  public float b;

  public HSBColor(float h, float s, float b) {
    this.h = h;
    this.s = s;
    this.b = b;
  }

  public HSBColor lerpTo(HSBColor other, float step) {
    return new HSBColor(
        this.h + step * (other.h - this.h),
        this.s + step * (other.s - this.s),
        this.b + step * (other.b - this.b)
    );
  }
}
