package proc.sketches.pixelmatrix;

import lombok.Builder;
import util.Coordinate;
import util.Point;

import java.awt.*;

@Builder
public class Pixel {

    Coordinate matrixPos;
    Point screenPos;
    Color color;


}
