package proc.sketches;

import extensions.CustomApplet;
import javafx.util.Pair;
import processing.core.PApplet;
import util.Coordinate;
import util.FillingGrid;
import util.Point;
import util.RunningHitrate;

import java.util.*;
import java.util.stream.Collectors;

public class CirclePacking extends CustomApplet {

    private static final float WIDTH = 1600;
    private static final float HEIGHT = 1600;

    private static final int STARTING_SAMPLES = 0;
    private static final int SAMPLES_PER_FRAME = 6;
    private static final int FRAME_RATE = 20;
    private static final boolean ENABLE_VISUALIZATIONS = true;

    private static final float PACKING_RADIUS = 40;

    private static final int CONNECTIVITY = 5;

    Random random;
    List<Point> points;
    FillingGrid<Point> pointGrid;
    RunningHitrate hitrate;

    long lastFrameTime = System.currentTimeMillis();

    public void settings() {
        size((int)WIDTH, (int)HEIGHT);
    }

    public void setup() {
        background(255);
        strokeWeight(5);
        frameRate(FRAME_RATE);

        random = new Random(12345);
        points = new ArrayList<>();
        pointGrid = new FillingGrid<>(
                3 * WIDTH / 4,
                3 * HEIGHT / 4,
                PACKING_RADIUS / (float)Math.sqrt(2),
                random);
        hitrate = new RunningHitrate(314);
    }

    public void draw() {
        //redraw on every frame
        background(255);

        if (ENABLE_VISUALIZATIONS)
            visualizeOccupiedSquares(pointGrid);

        // Draw all existing points
        List<Point> allPoints = new ArrayList<>(pointGrid.getAllValues());
        for(Point a:allPoints) {
            drawPoint(a);
        }

        for (int i = 0; i < SAMPLES_PER_FRAME; i++) {
            Point point = throwDartOptimized(random, pointGrid);
            hitrate.isHit(point != null);
        }

        displayStats();
    }

    public void drawGrid() {
        float gridSize = PACKING_RADIUS / (float) Math.sqrt(2);
        for(int i = 0; i < WIDTH / gridSize; i++) {
            Point p0 = new Point(0, i * gridSize);
            Point p1 = new Point(WIDTH, i * gridSize);
            line(p0, p1);

            Point p2 = new Point(i * gridSize, 0);
            Point p3 = new Point(i * gridSize, HEIGHT);
            line(p2, p3);
        }
    }

    public void displayStats() {
        int textSize = 30;
        float startX = 3 * WIDTH / 4f + 20;
        textSize(textSize);
        fill(0);
        int samplesTaken = STARTING_SAMPLES + frameCount * SAMPLES_PER_FRAME;
        long timeDiff = System.currentTimeMillis() - lastFrameTime;
        lastFrameTime = System.currentTimeMillis();
        text(
                String.format("Samples: %d", samplesTaken),
                startX,
                textSize * 1.1f);
        text(
                String.format("Hitrate (running): %.2f%%", 100*hitrate.get()),
                startX,
                2 * (textSize * 1.1f));
        text(
                String.format("Latency: %dms", timeDiff),
                startX,
                3 * (textSize * 1.1f));
    }

    public Point uniformRandomPoint(Random random, List<Point> points) {
        return new Point(random.nextFloat() * WIDTH, random.nextFloat() * HEIGHT);
    }

    public Point throwDartOptimized(Random random, FillingGrid<Point> pointGrid) {
        Coordinate nextCoordinate = pointGrid.getRandomCoordinate();

        //Coord is only null when no unoccupied cell remains
        if (nextCoordinate != null) {
            Point currentCellValue = pointGrid.getValueAtCoordinate(nextCoordinate);

            // Cell is occupied - do not try to draw a point
            if (currentCellValue != null) {
                return null;
            }

            // Otherwise, cell is unoccupied, so try a random sample then
            // look at surrounding neighborhood to see if it should be rejected
            Point nextPoint = pointGrid.getRandomPointInCell(nextCoordinate);
            List<Coordinate> neighborhood = pointGrid.neighborhood(nextCoordinate);

            if (ENABLE_VISUALIZATIONS)
                visualizeNeighborhood(nextPoint,neighborhood);

            boolean rejectPoint = neighborhood.stream()
                    .map(pointGrid::getValueAtCoordinate)
                    .filter(Objects::nonNull)
                    .anyMatch(neighbor -> dist(nextPoint, neighbor) < PACKING_RADIUS);

            if (rejectPoint) return null;

            pointGrid.put(nextCoordinate, nextPoint);
            return nextPoint;
        }

        return null;
    }

    private void visualizeOccupiedSquares(FillingGrid<Point> pointGrid) {
        strokeWeight(2);
        fill(0f,255f,0f,100f);
        pointGrid.entries().stream()
                .map(Map.Entry::getKey)
                .forEach(this::squareFromCell);
    }

    private void visualizeNeighborhood(Point point, List<Coordinate> neighborhood) {
        Map<Coordinate,Point> neighbors = neighborhood.stream()
                .map(coordinate -> new Pair<>(coordinate, pointGrid.getValueAtCoordinate(coordinate)))
                .filter(pair -> pair.getValue() != null)
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));

        Set<Coordinate> litUpSquares = neighbors.entrySet().stream()
                .filter(entry -> dist(point, entry.getValue()) < PACKING_RADIUS)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        color(255f, 255f, 0f);
        strokeWeight(10);
        point(point);
        strokeWeight(1);

        for (Coordinate neighborCell: neighborhood) {
            if (litUpSquares.contains(neighborCell)) {
                fill(255f, 0f, 0f, 100f);
            } else {
                fill(150f, 100f);
            }
            squareFromCell(neighborCell);
        }
    }

    public void squareFromCell(Coordinate cell) {
        rect(
                cell.getX() * pointGrid.getCellSize(),
                cell.getY() * pointGrid.getCellSize(),
                pointGrid.getCellSize(),
                pointGrid.getCellSize()
        );
    }

    public void drawPoint(Point point) {
        strokeWeight(5);
        point(point);
        strokeWeight(1);
        noFill();
        ellipse(point.getX(), point.getY(), PACKING_RADIUS, PACKING_RADIUS);
        strokeWeight(3);
    }


    public static void main(String... args){
        PApplet.main("proc.sketches.CirclePacking");
    }
}
