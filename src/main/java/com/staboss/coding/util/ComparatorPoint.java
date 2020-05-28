package com.staboss.coding.util;

import com.staboss.coding.model.GridPoint;

import java.util.Comparator;

import static java.lang.Integer.parseInt;

public class ComparatorPoint implements Comparator<GridPoint> {
    @Override
    public int compare(GridPoint o1, GridPoint o2) {
        return Integer.compare(parseInt(o1.getValue(), 2), parseInt(o2.getValue(), 2));
    }
}
