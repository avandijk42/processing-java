package proc.sketches;

import java.util.ArrayList;
import processing.core.PApplet;
import util.Bezier;
import util.HSBColor;
import util.Point;

public class CurveTest extends PApplet {

  private float width = 1000;
  private float height = 600;

  private float controlPointRadius = 20f;
  private float reflectPointRadius = 15f;

  private final boolean SHOW_CONTROLS = false;
  private final int TRACE_DEPTH = 30;
  private final int TARGET_DECAY_GREY = 230;

  private ArrayList<Bezier> traceQueue = new ArrayList<>();

  boolean inPointEditMode = false;
  int selectedPoint = -1;

  Bezier bez1 = newBezier();
  Bezier bez2 = newBezier();

  private int steps = 30;


  public void settings(){
    size((int)width, (int)height);
  }

  public void setup() {
    colorMode(HSB, 360, 100, 100);
    frameRate(45);
  }

  public void draw() {
    background(255f);
    noFill();

    final int step = frameCount % steps;
    if (step == 0) {
      bez1 = bez2;
      bez2 = newBezier();
    }

    updateTraceQueue(step);

    float weight = 0.5f;
    int traceIndex = 0;
    for(Bezier bez: traceQueue) {
      strokeWeight(weight);
      stroke(TARGET_DECAY_GREY - traceIndex * TARGET_DECAY_GREY / (float)TRACE_DEPTH);
      drawBezShape(bez);

      weight /= 0.95f;
      traceIndex++;
    }
  }

  private void drawBezShape(Bezier bez) {
    bezier(bez);

    if(SHOW_CONTROLS) {
      drawControlPoints(bez);
      connectPairs(bez);
    }
    reflectionBezier(bez, SHOW_CONTROLS);
  }

  private void updateTraceQueue(int step) {
    if(traceQueue.size() >= TRACE_DEPTH) {
      traceQueue.remove(0);
    }

    Bezier nextBez = bez1.lerpBezier(bez2, step / (float)steps);
    traceQueue.add(nextBez);
  }

  private float[] getNewBezier() {
    float[] bezier = new float[8];
    for (int i=0; i<8; i+=2) {
      bezier[i] = random(width);
      bezier[i+1] = random(height);
    }
    return bezier;
  }

  private Bezier newBezier() {
    float[] bezierData = getNewBezier();
    return new Bezier(
        new Point(bezierData[0], bezierData[1]),
        new Point(bezierData[2], bezierData[3]),
        new Point(bezierData[4], bezierData[5]),
        new Point(bezierData[6], bezierData[7])
    );
  }

  private float lerp(float p1, float p2, int step) {
    float progress = step / (float)steps;
    return p1 + progress * (p2 - p1);
  }

  private void bezier(Bezier bez) {
    bezier(
        bez.point1.x, bez.point1.y,
        bez.anchor1.x, bez.anchor1.y,
        bez.anchor2.x, bez.anchor2.y,
        bez.point2.x, bez.point2.y
    );
  }

  private void drawControlPoints(Bezier bez) {
    ellipse(bez.point1.x, bez.point1.y, controlPointRadius, controlPointRadius);
    ellipse(bez.anchor1.x, bez.anchor1.y, controlPointRadius, controlPointRadius);
    ellipse(bez.anchor2.x, bez.anchor2.y, controlPointRadius, controlPointRadius);
    ellipse(bez.point2.x, bez.point2.y, controlPointRadius, controlPointRadius);
  }

  private void connectPairs(Bezier bez) {
    line(bez.point1.x, bez.point1.y, bez.anchor1.x, bez.anchor1.y);
    line(bez.point2.x, bez.point2.y, bez.anchor2.x, bez.anchor2.y);
  }

  private void reflectionBezier(Bezier bez, boolean showControls) {
    float reflectionDistance = 2f;
    float x1 = lerp(bez.anchor1.x, bez.point1.x, reflectionDistance);
    float y1 = lerp(bez.anchor1.y, bez.point1.y, reflectionDistance);
    float x2 = lerp(bez.anchor2.x, bez.point2.x, reflectionDistance);
    float y2 = lerp(bez.anchor2.y, bez.point2.y, reflectionDistance);


    if (showControls) {
      ellipse(x1, y1, reflectPointRadius, reflectPointRadius);
      ellipse(x2, y2, reflectPointRadius, reflectPointRadius);
    }

    Bezier reflection = new Bezier(
        bez.point1,
        new Point(x1, y1),
        new Point(x2, y2),
        bez.point2
    );

    bezier(reflection);
  }

  @Override
  public void mousePressed() {
    super.mousePressed();

    inPointEditMode = true;
    for (int i=0; i< bez1.getPoints().length; i++) {
      Point p = bez1.getPoints()[i];
      if (distance(p, mousePoint()) < 20) {
        selectedPoint = i;
      }
    }
  }

  @Override
  public void mouseReleased() {
    super.mouseReleased();

    inPointEditMode = false;
    selectedPoint = -1;
  }

  @Override
  public void mouseDragged() {
    super.mouseDragged();

    if (selectedPoint >= 0) {
      bez1.updatePoint(selectedPoint, mousePoint());
    }
  }

  private Point mousePoint() {
    return new Point(mouseX, mouseY);
  }

  private float distance(Point p1, Point p2) {
    return sqrt(sq(p2.x - p1.x) + sq(p2.y - p1.y));
  }

  private void stroke(HSBColor color) {
    stroke(color.h, color.s, color.b);
  }

  public static void main(String... args){
    PApplet.main("proc.sketches.CurveTest");
  }

}
