package proc.sketches.pixelmatrix;

import extensions.CustomApplet;
import proc.sketches.pixelmatrix.colorgen.ColorGenerator;
import proc.sketches.pixelmatrix.colorgen.SinWaveGreyscale;
import processing.core.PApplet;
import processing.event.KeyEvent;
import util.Coordinate;
import util.Grid;
import util.Point;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PixelMatrix extends CustomApplet {

    private static final float HEIGHT = 800;
    private static final float WIDTH = 1200;
    private static final int FRAME_RATE = 20;

    private static final int MATRIX_WIDTH = 96;
    private static final int MATRIX_HEIGHT = 64;
    private static final float PIXEL_WIDTH = 10;

    Grid<Pixel> matrix;
    ColorGenerator generator = new SinWaveGreyscale(1f);
    HashMap<String,ColorGenerator> generators = new HashMap<>();
    HashMap<String,Float> parameters;

    String selectedGenerator;
    int selectedParameterIndex = 0;
    String selectedParameter;

    @Override
    public void settings() {
        size((int)WIDTH, (int)HEIGHT);
    }

    public void setup() {
        background(255);
        strokeWeight(1);
        fill(50f);
        frameRate(FRAME_RATE);

        matrix = getPixelMatrix();
        parameters = new HashMap<>();

        parameters.put("x", MATRIX_WIDTH/3f);
        parameters.put("y", 3*MATRIX_HEIGHT/8f);
        parameters.put("a", 16f);
        parameters.put("b", 4f);
        parameters.put("c", 10f);

        selectedParameter = "a";

        generators = new HashMap<>();
        generators.put("1", new SinWaveGreyscale(1f));
        generators.put("2", new SinWaveGreyscale(0.5f));
        generators.put("3", new SinWaveGreyscale(0.25f));

        selectedGenerator = "1";
    }

    public void draw() {
        //redraw on every frame
        background(200);

        generator = generators.get(selectedGenerator);
        matrix.forEach((coordinate, pixel) -> pixel.color = generator.generate(coordinate, frameCount, parameters));

        drawPixelMatrix(matrix);

        textSize(16f);
        fill(0);

        StringBuilder generatorsLog = new StringBuilder();
        generators.forEach((key, generator) ->
                generatorsLog.append(
                        String.format("%s: %s, ", displayKey(key, selectedGenerator), generator)));

        text(generatorsLog.toString(),10f,20f);

        StringBuilder paramsLog = new StringBuilder();
        parameters.forEach((key, param) ->
                paramsLog.append(
                        String.format("%s: %f, ", displayKey(key, selectedParameter), param)));

        text(paramsLog.toString(),10f,40f);
    }

    public void drawPixelMatrix(Grid<Pixel> matrix) {
        matrix.forEach((Coordinate matrixPos, Pixel pixel) -> {
            fill(pixel.color.getRGB());
            rect(pixel.screenPos.getX(), pixel.screenPos.getY(), PIXEL_WIDTH, PIXEL_WIDTH);
        });
    }

    public Grid<Pixel> getPixelMatrix(){
        Point center = new Point(WIDTH/2, HEIGHT/2);
        float matrixScreenWidth = MATRIX_WIDTH * PIXEL_WIDTH;
        float matrixScreenHeight = MATRIX_HEIGHT * PIXEL_WIDTH;

        Point start = center.plus(-matrixScreenWidth/2, -matrixScreenHeight/2);

        Grid<Pixel> matrix = new Grid<>(MATRIX_WIDTH, MATRIX_HEIGHT);
        matrix.init((Coordinate matrixPos) ->
                new Pixel(
                        matrixPos,
                        new Point(
                                start.getX() + PIXEL_WIDTH * matrixPos.getX(),
                                start.getY() + PIXEL_WIDTH * matrixPos.getY()),
                        Color.DARK_GRAY)
        );

        return matrix;
    }

    private String displayKey(String key, String selected) {
        String wrappingCharater = key.equals(selected) ? "|" : " ";
        return String.format("%s%s%s", wrappingCharater, key, wrappingCharater);
    }

    @Override
    public void keyPressed() {
        List<String> parameterKeyList = new ArrayList<>(parameters.keySet());
        if (keyCode == LEFT) {
            selectedParameterIndex = max(0, selectedParameterIndex - 1);
        } else if (keyCode == RIGHT) {
            selectedParameterIndex = min(parameters.size()-1, selectedParameterIndex + 1);
        }
        selectedParameter = parameterKeyList.get(selectedParameterIndex);

        String keyString = String.valueOf(key);
        if(generators.containsKey(keyString)) {
            selectedGenerator = keyString;
        }

        float valueChange = keyCode == UP ? 0.3f : keyCode == DOWN ? -0.3f : 0;
        parameters.compute(selectedParameter, (k,v) -> v + valueChange);
    }

    public static void main(String... args){
        PApplet.main("proc.sketches.pixelmatrix.PixelMatrix");
    }

}
