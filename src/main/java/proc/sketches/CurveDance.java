package proc.sketches;

import processing.core.PApplet;
import util.Color;
import util.HSBColor;

public class CurveDance extends PApplet {

  private float width = 800;
  private float height = 800;

  private float[] startBezier = getNewBezier();
  private float[] endBezier = getNewBezier();
  private float[] controlPoints = getNewBezier();

  private HSBColor startColor = new HSBColor(199f, 100f, 36f);
  private HSBColor endColor = new HSBColor(39f, 100f, 100f);

  private int steps = 60;

  public void settings(){
    size((int)width, (int)height);
  }

  public void setup() {
    colorMode(HSB, 360, 100, 100);
    frameRate(30);
  }

  public void draw() {
    background(255f);
    noFill();

    final int step = frameCount % steps;
    if (step == 0) {
      System.arraycopy(endBezier, 0, startBezier, 0, startBezier.length);
      endBezier = getNewBezier();
    }

    float[] lerpedBezier = lerpedBezierData(startBezier, endBezier, step);

    strokeWeight(8f);
    fillBezierWithLines(lerpedBezier);

    strokeWeight(2f);
    stroke(0);
    bezier(lerpedBezier);

    float[] scrambledBezier = new float[]{
        lerpedBezier[0], lerpedBezier[1],
        lerpedBezier[4], lerpedBezier[3],
        lerpedBezier[5], lerpedBezier[2],
        lerpedBezier[6], lerpedBezier[7],
    };
    strokeWeight(8f);
    fillBezierWithLines(scrambledBezier);

    strokeWeight(2f);
    stroke(0);
    bezier(scrambledBezier);
  }

  private float[] getNewBezier() {
    float[] bezier = new float[8];
    for (int i=0; i<8; i++) {
      bezier[i] = random(width);
    }
    return bezier;
  }

  private float lerp(float p1, float p2, int step) {
    float progress = step / (float)steps;
    return p1 + progress * (p2 - p1);
  }

  private void bezier(float[] bezierData) {
    bezier(
        bezierData[0], bezierData[1], //anchor 1
        bezierData[2], bezierData[3], //control 1
        bezierData[4], bezierData[5], //control 2
        bezierData[6], bezierData[7]  //anchor 2
    );
  }

  private float[] lerpedBezierData(float[] start, float[] end, int step) {
    return new float[] {
        lerp(start[0], end[0], step),
        lerp(start[1], end[1], step),
        lerp(start[2], end[2], step),
        lerp(start[3], end[3], step),
        lerp(start[4], end[4], step),
        lerp(start[5], end[5], step),
        lerp(start[6], end[6], step),
        lerp(start[7], end[7], step)
    };
  }

  private void fillBezierWithLines(float[] bezierData) {
    final int resolution = 60;

    for (int step=0; step<resolution; step++) {
      float percent = step / (float)resolution;
      float t = 0.5f * sq(percent);
      float x1 = bezierPoint(bezierData[0], bezierData[2], bezierData[4], bezierData[6], t);
      float y1 = bezierPoint(bezierData[1], bezierData[3], bezierData[5], bezierData[7], t);


      float x2 = bezierPoint(bezierData[0], bezierData[2], bezierData[4], bezierData[6], 1-t);
      float y2 = bezierPoint(bezierData[1], bezierData[3], bezierData[5], bezierData[7], 1-t);

      stroke(startColor.lerpTo(endColor, percent));
      line(x1, y1, x2, y2);
    }
  }

  private void stroke(Color color) {
    stroke(color.r, color.g, color.b);
  }

  private void stroke(HSBColor color) {
    stroke(color.h, color.s, color.b);
  }

  public static void main(String... args){
    PApplet.main("proc.sketches.CurveDance");
  }

}
