package proc.sketches;

import processing.core.PApplet;

public class Mandlebrot extends PApplet {
    /**
     * The Mandelbrot Set
     * by Daniel Shiffman.
     * (slight modification by l8l)
     *
     * Simple rendering of the Mandelbrot set.
     */

    float startWidth;
    float startHeight;
    float startXMin;
    float startYMin;
    int iterations;

    private static final int UP = 38;
    private static final int DOWN = 40;
    private static final int LEFT = 37;
    private static final int RIGHT = 39;

    public void settings() {
        size(1920, 1080);
        noLoop();
    }

    public void setup() {
        background(255);

        // Establish a range of values on the complex plane
        // A different range will allow us to "zoom" in or out on the fractal

        // It all starts with the width, try higher or lower values
        startWidth = 4f;
        startHeight = (startWidth * height) / width;

        // Start at negative half the width and height
        startXMin = -startWidth/2;
        startYMin = -startHeight/2;

        iterations = 10;
    }

    public void draw() {
        drawMandlebrot(startWidth, startHeight, startXMin, startYMin, iterations);

        text(String.format("its: %d", iterations), 20, 20);
    }

    public void drawMandlebrot(float w, float h, float xmin, float ymin, int maxIterations) {

        // Make sure we can write to the pixels[] array.
        // Only need to do this once since we don't do any other drawing.
        loadPixels();

        // x goes from xmin to xmax
        float xmax = xmin + w;
        // y goes from ymin to ymax
        float ymax = ymin + h;

        // Calculate amount we increment x,y for each pixel
        float dx = (xmax - xmin) / (width);
        float dy = (ymax - ymin) / (height);

        // Start y
        float y = ymin;
        for (int j = 0; j < height; j++) {
            // Start x
            float x = xmin;
            for (int i = 0; i < width; i++) {

                // Now we test, as we iterate z = z^2 + c does z tend towards infinity?
                float a = x;
                float b = y;
                int n = 0;
                float max = 4.0f;  // Infinity in our finite world is simple, let's just consider it 4
                float absOld = 0.0f;
                float convergeNumber = maxIterations; // this will change if the while loop breaks due to non-convergence
                while (n < maxIterations) {
                    // We suppose z = a+ib
                    float aa = a * a;
                    float bb = b * b;
                    float abs = sqrt(aa + bb);
                    if (abs > max) { // |z| = sqrt(a^2+b^2)
                        // Now measure how much we exceeded the maximum:
                        float diffToLast = (float) (abs - absOld);
                        float diffToMax  = (float) (max - absOld);
                        convergeNumber = n + diffToMax/diffToLast;
                        break;  // Bail
                    }
                    float twoab = 2.0f * a * b;
                    a = aa - bb + x; // this operation corresponds to z -> z^2+c where z=a+ib c=(x,y)
                    b = twoab + y;
                    n++;
                    absOld = abs;
                }

                // We color each pixel based on how long it takes to get to infinity
                // If we never got there, let's pick the color black
                if (n == maxIterations) {
                    pixels[i+j*width] = color(0);
                } else {
                    // Gosh, we could make fancy colors here if we wanted
                    float norm = map(convergeNumber, 0, maxIterations, 0, 1);
                    pixels[i+j*width] = color(map(sqrt(norm), 0, 1, 0, 255));
                }
                x += dx;
            }
            y += dy;
        }
        updatePixels();
        noLoop();
    }

    public void keyPressed() {
        boolean updated = true;
        final float positionChange = startWidth / 40;
        final int itersChange = 1;
        System.out.println(keyCode);
        switch (keyCode) {
            case UP:
                startYMin -= positionChange;
                break;
            case DOWN:
                startYMin += positionChange;
                break;
            case LEFT:
                startXMin -= positionChange;
                break;
            case RIGHT:
                startXMin += positionChange;
                break;
            default:
                updated = false;
        }

        float oldWidth = startWidth;
        float oldHeight = startHeight;
        switch(key) {
            case 'a':
                iterations -= itersChange;
                updated = true;
                break;
            case 'd':
                iterations += itersChange;
                updated = true;
                break;
            case 'w':
                startWidth -= startWidth * 0.1f;
                startHeight -= startHeight * 0.1f;
                startXMin -= (startWidth-oldWidth)/2;
                startYMin -= (startHeight-oldHeight)/2;
                updated = true;
                break;
            case 's':
                startWidth += startWidth * 0.1f;
                startHeight += startHeight * 0.1f;
                startXMin -= (startWidth-oldWidth)/2;
                startYMin -= (startHeight-oldHeight)/2;
                updated = true;
                break;
        }

        if(updated) {
            loop();
        }
    }

    public static void main(String... args){
        PApplet.main("proc.sketches.Mandlebrot");
    }
}
