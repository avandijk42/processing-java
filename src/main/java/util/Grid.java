package util;

import java.util.HashMap;
import java.util.function.*;

public class Grid<T> {

    private final int width;
    private final int height;

    HashMap<Coordinate,T> grid;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new HashMap<>(width * height);
    }

    public void init(Function<Coordinate,T> function) {
        for(int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Coordinate c = new Coordinate(x,y);
                grid.put(c, function.apply(c));
            }
        }
    }

    public void put(int i, int j, T item) {
        grid.put(new Coordinate(i,j), item);
    }

    public T get(int i, int j) {
        return grid.get(new Coordinate(i,j));
    }

    public void forEach(BiConsumer<Coordinate, T> consumer) {
        grid.forEach(consumer);
    }

}
