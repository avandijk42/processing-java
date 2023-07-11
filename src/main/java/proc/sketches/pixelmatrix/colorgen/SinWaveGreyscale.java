package proc.sketches.pixelmatrix.colorgen;

import lombok.AllArgsConstructor;
import util.Coordinate;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class SinWaveGreyscale implements ColorGenerator{

    float amplitude;

    @Override
    public Color generate(Coordinate coordinate, int frameCount, Map<String,Float> parameters) {


        float x = parameters.get("x");
        float y = parameters.get("y");
        float a = parameters.get("a");
        float b = parameters.get("b");
        float c = parameters.get("c");

        float scaledXPos = (coordinate.getX() + x*10) / a;
        float scaledYPos = (coordinate.getY() + y*10) / b;

        float coordHash = (float)Math.sqrt(scaledXPos*scaledXPos + scaledYPos*scaledYPos);

        float grey =  amplitude / 2 + amplitude * (float) Math.sin(coordHash + frameCount/c);

        grey = grey + (float)Math.cos((coordinate.getY() + coordinate.getY())/(c*Math.PI));
        grey = Math.max(0f, Math.min(1f, grey));
        return new Color(grey, grey, grey);
    }

    @Override
    public String toString() {
        return String.format("%s(amp=%.2f)", getClass().getSimpleName(), amplitude);
    }

}
