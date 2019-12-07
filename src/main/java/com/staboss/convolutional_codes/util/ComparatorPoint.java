package com.staboss.convolutional_codes.util;

import com.staboss.convolutional_codes.model.Point;

import java.util.Comparator;

import static java.lang.Integer.parseInt;

public class ComparatorPoint implements Comparator<Point> {
    @Override
    public int compare(Point o1, Point o2) {
        return Integer.compare(parseInt(o1.getValue(), 2), parseInt(o2.getValue(), 2));
    }
}
