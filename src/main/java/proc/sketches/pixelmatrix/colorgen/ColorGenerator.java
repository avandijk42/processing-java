package proc.sketches.pixelmatrix.colorgen;

import proc.sketches.pixelmatrix.TunableParameter;
import util.Coordinate;

import java.awt.*;
import java.util.Map;

public interface ColorGenerator {

    public Color generate(Coordinate coordinate, int frameCount, Map<String, Float> params);
}
