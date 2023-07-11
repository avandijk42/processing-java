package proc.sketches;

import processing.core.PApplet;
import util.Orientation;
import util.Point;
import util.Rectangle;

import static util.Orientation.VERTICAL;

public class Flow extends PApplet {
    private static int scale = 3;
    private static int WIDTH = 500 * scale;
    private static int HEIGHT = 500 * scale;

    private static float minVectorMagnitude = 15f * scale;
    private static float maxVectorMagnitude = 30f * scale;
    private static float minWeight = 0.25f * scale;
    private static float maxWeight = 1.25f * scale;
    float noiseScale = 0.0015f;
    int spacing = 16 * scale;
    float offset = 0;

    public void settings(){
        size(WIDTH, HEIGHT);
    }

    public void setup() {
        strokeWeight(0f);
        frameRate(60);
        colorMode(HSB);
    }

    public void draw(){
        float frameOffset = offset++ / 10f;
        float backgroundHue = 100 + 100*sin(frameOffset/6f);
        background(backgroundHue, 120, 120);
        for(float w = -spacing/2f; w < WIDTH + spacing; w += spacing) {
            for (float h = -spacing/2f; h < HEIGHT + spacing; h += spacing) {
                float magnitude = noise(w * noiseScale, h * noiseScale);
                float direction = noise(w * noiseScale, h * noiseScale);

//                strokeWeight(minWeight + magnitude * (maxWeight - minWeight));

                float triangleHue = 50*cos(frameOffset/6f);
                float triangleValue = sqrt((direction*magnitude*1.62f+frameOffset/2f)) * 255 % 255;
                fill (triangleHue, 255f, triangleValue);
                triangle(w, h,
                        magnitude * 1.62f * (maxVectorMagnitude - minVectorMagnitude) + minVectorMagnitude,
                        direction * PI * 2 + frameOffset);
            }
        }
    }

    public void vector(float x, float y, float magnitude, float direction) {
        line(x,
             y,
             x + (magnitude * cos(direction)),
             y + (magnitude * sin(direction)));
    }

    public void triangle(float x, float y, float magnitude, float direction) {
        triangle(x,
                y,
                x + (magnitude * cos(direction)),
                y + (magnitude * sin(direction)),
                x + (magnitude/1.6f * cos(direction/1.1234f)),
                y + (magnitude/1.6f * sin(direction/1.1234f))
                );
    }

    public static void main(String... args){
        PApplet.main("proc.sketches.Flow");
    }
}
