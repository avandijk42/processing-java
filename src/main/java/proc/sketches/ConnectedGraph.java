package proc.sketches;

import extensions.CustomApplet;
import javafx.util.Pair;
import processing.core.PApplet;
import util.Point;
import util.RunningHitrate;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class ConnectedGraph extends CustomApplet {

    private static final float WIDTH = 1600;
    private static final float HEIGHT = 1600;

    private static final int STARTING_SAMPLES = 0;
    private static final int SAMPLES_PER_FRAME = 6;
    private static final int FRAME_RATE = 40;

    private static final float PACKING_RADIUS = 25;

    private static final int CONNECTIVITY = 3;

    Random random;
    List<Point> points;
    RunningHitrate hitrate;
    long lastFrameTime = System.currentTimeMillis();

    public void settings() {
        size((int)WIDTH, (int)HEIGHT);
    }

    public void setup() {
        background(255);
        strokeWeight(5);
        frameRate(FRAME_RATE);

        random = new Random(314);
        points = new ArrayList<>();
        hitrate = new RunningHitrate(314);

        for(int i=0; i<STARTING_SAMPLES; i++) {
            addNewPoint();
        }
    }

    public void draw() {
        //redraw on every frame
        background(255);

        // Draw all existing points
        for(Point a: points) {
            drawPointWithNeighborLines(CONNECTIVITY, a, points);
        }

        // Generate new points
        for(int i=0; i<SAMPLES_PER_FRAME; i++) {
            addNewPoint();
        }

        displayStats();
    }

    public void displayStats() {
        textSize(16);
        fill(0);
        int samplesTaken = STARTING_SAMPLES + frameCount * SAMPLES_PER_FRAME;
        long timeDiff = System.currentTimeMillis() - lastFrameTime;
        lastFrameTime = System.currentTimeMillis();
        text(String.format("Samples: %d", samplesTaken), 10, 20);
        text(String.format("Hitrate (running): %.2f%%", 100*hitrate.get()), 10, 40);
        text(String.format("Latency: %dms", timeDiff), 10, 60);
    }

    public void addNewPoint() {
        Point point = generatePointWithNeighborLines(
                CONNECTIVITY,
                random,
                points,
                this::throwDart
        );

        if (point != null) {
            points.add(point);
            hitrate.hit();
        } else {
            hitrate.miss();
        }
    }

    public Point uniformRandomPoint(Random random, List<Point> points) {
        return new Point(random.nextFloat() * WIDTH, random.nextFloat() * HEIGHT);
    }

    public Point circularRandomPoint(Random random, List<Point> points) {
        float theta = random.nextFloat() * PI * 2;
        float radius = WIDTH / 2 * (float)Math.sqrt(random.nextFloat());

        float x = WIDTH / 2 + radius * cos(theta);
        float y = HEIGHT / 2 + radius * sin(theta);

        return new Point(x,y);
    }

    public Point throwDart(Random random, List<Point> points) {
        BiFunction<Random,List<Point>,Point> pointGenerator = this::circularRandomPoint;
        Point a = pointGenerator.apply(random, points);

        for(Point b: points) {
            float distance = dist(a,b);
            if (distance < PACKING_RADIUS) return null;
        }

        return a;
    }

    public Point generatePointWithNeighborLines(
            int k,
            Random random,
            List<Point> points,
            BiFunction<Random,List<Point>,Point> pointGenerator) {

        Point point = pointGenerator.apply(random, points);
        if (point != null) drawPointWithNeighborLines(k, point, points);
        return point;
    }

    public void drawPointWithNeighborLines(int k, Point a, List<Point> points) {
        strokeWeight(5);
        point(a);
        strokeWeight(3);
        connectNearestNeighbors(k, a, points);
    }

    private void connectNearestNeighbors(int k, Point a, List<Point> points) {
        for(Point p: kNearestNeighbors(k, a, points)) {
            line(a, p);
        }
    }

    /**
     * O(N) search for nearest neighbors via Euclidean distance
     * @param k number of nearest neighbors
     * @param a starting point
     * @param points list of all points
     * @return list of K nearest points
     */
    private List<Point> kNearestNeighbors(int k, Point a, List<Point> points) {
        List<Point> sortedDistances = points.stream()
                .filter(point -> !point.equals(a))
                .map(point -> new Pair<>(point,dist(a,point)))
                .sorted(Comparator.comparing(Pair::getValue))
                .map(Pair::getKey)
                .collect(Collectors.toList());

        int numItemsToReturn = Math.min(sortedDistances.size(), k);

        return sortedDistances.subList(0,numItemsToReturn);
    }

    public static void main(String... args){
        PApplet.main("proc.sketches.ConnectedGraph");
    }
}
