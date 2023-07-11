package util;


import javafx.util.Pair;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

public class FillingGrid<T> {

    private final Random random;

    private final float width;
    private final float height;
    @Getter
    private final float cellSize;
    private final int numCells;
    private Map<Coordinate,T> grid;
    private Set<Coordinate> unoccupied;

    public FillingGrid(float width, float height, float cellSize, Random random) {
        this.width = width;
        this.height = height;
        this.cellSize = cellSize;
        this.random = random;
        this.numCells = (int)Math.floor(width / cellSize);
        grid = new HashMap<>();
        unoccupied = new HashSet<>();
        for (int i = 0; i < numCells; i++) {
            for (int j = 0; j < numCells; j++) {
                Coordinate coordinate = new Coordinate(i,j);
                grid.put(coordinate, null);
                unoccupied.add(coordinate);
            }
        }
    }

    public T getValueAtCoordinate(Coordinate coordinate) {
        return grid.get(coordinate);
    }

    public Collection<T> getAllValues() {
        return grid.values().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public Collection<Map.Entry<Coordinate,T>> entries() {
        return grid.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toList());
    }

    public void put(Coordinate coordinate, T value) {
        grid.put(coordinate, value);
        unoccupied.remove(coordinate);
    }

    public Coordinate getRandomCoordinate() {
        List<Coordinate> gridKeys = new ArrayList<>(unoccupied);
        int randomKeyIndex = random.nextInt(gridKeys.size());

        if (gridKeys.isEmpty()) return null;

        return gridKeys.get(randomKeyIndex);
    }

    public Point getRandomPointInCell(Coordinate coordinate) {
        float xMin = coordinate.getX() * cellSize;
        float yMin = coordinate.getY() * cellSize;

        return new Point(
                xMin + random.nextFloat() * cellSize,
                yMin + random.nextFloat() * cellSize
        );
    }

    public List<Coordinate> neighborhood(Coordinate coordinate) {
        List<Coordinate> neighbors = new ArrayList<>();
        for(int dx = -2; dx <= 2; dx++) {
            if (dx == 0) continue;
            neighbors.add(coordinate.add(dx, 0));
        }

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy: new int[]{-1, 1}) {
                neighbors.add(coordinate.add(dx, dy));
            }
        }

        for (int dy: new int[]{-2, 2}) {
            neighbors.add(coordinate.add(0, dy));
        }

        return neighbors.stream()
                .filter(coord -> coord.getX() >= 0
                        && coord.getX() < numCells
                        && coord.getY() >= 0
                        && coord.getY() < numCells)
                .collect(Collectors.toList());
    }
}
