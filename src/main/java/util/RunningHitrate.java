package util;

import java.util.ArrayList;
import java.util.List;

public class RunningHitrate {

    List<Integer> events;
    int hitsSum;
    int total;

    final int size;

    public RunningHitrate(int size) {
        this.events = new ArrayList<>();
        this.size = size;
    }

    public void hit() {
        update(1);
    }

    public void miss() {
        update(0);
    }

    public void isHit(boolean hit) {
        update(hit ? 1 : 0);
    }

    public void update(int value) {
        if (total >= size) {
            hitsSum -= events.remove(0);
        } else {
            total++;
        }
        hitsSum += value;
        events.add(value);
    }

    public float get() {
        return hitsSum / (float) total;
    }
}
