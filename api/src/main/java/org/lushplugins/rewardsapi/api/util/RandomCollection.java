package org.lushplugins.rewardsapi.api.util;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

// TODO: Remove class when updating to LushLib 0.10.9 or higher
public class RandomCollection<E> {
    private static final Random random = new Random();
    private final NavigableMap<Double, E> map = new TreeMap<>();
    private double total = 0;

    public RandomCollection<E> add(E result, double weight) {
        if (weight <= 0) {
            return this;
        }

        total += weight;
        map.put(total, result);
        return this;
    }

    public E next() {
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }

    public void clear() {
        map.clear();
        total = 0;
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public NavigableMap<Double, E> getMap() {
        return map;
    }

    public double getTotalWeight() {
        return total;
    }
}
