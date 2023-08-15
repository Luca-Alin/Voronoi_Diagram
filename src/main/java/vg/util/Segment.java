package vg.util;

import java.awt.*;
import java.util.Objects;

public class Segment {
    public Point p1;
    public Point p2;
    public int x1;
    public int y1;
    public int x2;
    public int y2;

    public Segment(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
        x1 = p1.x;
        y1 = p1.y;
        x2 = p2.x;
        y2 = p2.y;
    }

    public Segment(int x1, int y1, int x2, int y2) {
        this(new Point(x1, y1), new Point(x2, y2));
    }

    @Override
    public String toString() {
        return "Segment{" + "p1=(" + p1.x + ", " + p1.y + "), " + ", p2=" + p2.x + ", " + p2.y + ")}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Segment segment = (Segment) o;
        return Objects.equals(p1, segment.p1) && Objects.equals(p2, segment.p2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(p1, p2);
    }
}
