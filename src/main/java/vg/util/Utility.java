package vg.util;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Utility {
    public static final int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 8 / 10;
    public static final int HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 8 / 10;

    /**
     * @param points - the vertexes of a polygon
     * @return The vertexes of a polygon sorted clockwise,
     * if they aren't sorted this could lead to unexpected outputs
     */
    public static ArrayList<Point> clockwiseSort(List<Point> points) {

        float x = 0;
        float y = 0;
        for (Point p : points) {
            x += p.x;
            y += p.y;
        }
        x /= points.size();
        y /= points.size();

        Point center = new Point(Math.round(x), Math.round(y));

        points.sort((p1, p2) -> {
            if (getAngle(p1, center) > getAngle(p2, center)) return 1;
            else if (getAngle(p1, center) == getAngle(p2, center))
                return Double.compare(getAngle(p1, center), getAngle(p2, center));
            else return -1;
        });

        return new ArrayList<>(points);
    }

    public static double getAngle(Point p, Point center) {

        int x = p.x - center.x;
        int y = p.y - center.y;

        double angle = Math.atan2(y, x);
        return (angle > 0) ? angle : 2 * Math.PI + angle;
    }

    public static double getDistance(Point p, Point q) {
        return Math.sqrt(Math.pow(p.x - q.x, 2) + Math.pow(p.y - q.y, 2));
    }


    /**
     * @param p
     * @param q
     * @param r
     * @return The orientation of the points p, q, r, in this order
     */
    private static double determinant(Point p, Point q, Point r) {
        return (p.getX() * q.getY() + q.getX() * r.getY() + r.getX() * p.getY()) - (q.getY() * r.getX() + r.getY() * p.getX() + p.getY() * q.getX());
    }

    /**
     * @param s
     * @param p
     * @return True if the two segments intersect, false if they don't
     */
    public static boolean doIntersect(Segment s, Segment p) {
        Point s1 = s.p1;
        Point s2 = s.p2;
        Point p1 = p.p1;
        Point p2 = p.p2;

        return determinant(p2, p1, s1) * determinant(p2, p1, s2) < 0 && determinant(s2, s1, p1) * determinant(s2, s1, p2) < 0;
    }

    /**
     * @param s1
     * @param s2
     * @return Return an array of the point where two segments intersect. Do note that this function will return
     * a point even if they don't intersect, as it treats the segments if they are of infinite lengths
     */
    public static int[] getIntersection(Segment s1, Segment s2) {

        double a1 = s1.y1 - s1.y2;
        double b1 = s1.x2 - s1.x1;
        double c1 = s1.x1 * s1.y2 - s1.x2 * s1.y1;

        double a2 = s2.y1 - s2.y2;
        double b2 = s2.x2 - s2.x1;
        double c2 = s2.x1 * s2.y2 - s2.x2 * s2.y1;

        int x = (int) Math.round((c2 * b1 - c1 * b2) / (a1 * b2 - a2 * b1));
        int y = (int) Math.round((a1 * c2 - a2 * c1) / (a2 * b1 - a1 * b2));

        return new int[] {x, y};
    }

    /**
     * @param p1
     * @param p2
     * @return A segment perpendicular to the segment(p1, p2)
     */
    public static Segment getBisection(Point p1, Point p2) {

        double m = -1.0 / ((p1.getY() - p2.getY()) / (p1.getX() - p2.getX()));

        int x0 = (int) Math.round((p1.getX() + p2.getX()) / 2.0);
        int y0 = (int) Math.round((p1.getY() + p2.getY()) / 2.0);

        int x = -5;
        int y = (int) Math.round(m * x - m * x0 + y0);
        Point s1 = new Point(x, y);

        x = WIDTH + 5;
        y = (int) Math.round(m * x - m * x0 + y0);
        Point s2 = new Point(x, y);

        return new Segment(s1, s2);
    }


    /**
     * @param points
     * @param split
     * @return Two polygons, divided by the bisection of two points
     */
    public static List<Point>[] splitPolygon(List<Point> points, Segment split) {

        points = clockwiseSort(points);

        List<Segment> edges = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            Point p1 = points.get(i);
            Point p2 = points.get((i + 1) % points.size());
            edges.add(new Segment(p1, p2));
        }

        List<Point> intersections = new ArrayList<>();

        for (Segment e: edges) {
            if (doIntersect(e, split)) {
                int[] xy = getIntersection(e, split);
                intersections.add(new Point(xy[0], xy[1]));
                System.out.println(intersections);
            }
        }

        List<Point> allPoints = new ArrayList<>();
        allPoints.addAll(points);
        allPoints.addAll(intersections);

        allPoints = clockwiseSort(allPoints);

        int intersection1 = 0;
        int intersection2 = allPoints.size() - 1;

        for (int i = 0; i < allPoints.size(); i++) {
            if (intersections.contains(allPoints.get(i))) {
                intersection1 = i;
                break;
            }
        }

        for (int i = allPoints.size() - 1; i >= 0; i--) {
            if (intersections.contains(allPoints.get(i))) {
                intersection2 = i;
                break;
            }
        }

        List<Point> polygon1 = new ArrayList<>();
        List<Point> polygon2 = new ArrayList<>();

        for (int i = 0; i < allPoints.size(); i++) {
            if (i >= intersection1 && i <= intersection2) polygon1.add(allPoints.get(i));
            if (i <= intersection1 || i >= intersection2) polygon2.add(allPoints.get(i));
        }



        return new ArrayList[] {clockwiseSort(polygon1), clockwiseSort(polygon2)};
    }

    /**
     * @param polygon
     * @param p
     * @return True if a point is inside a polygon, false if it isn't
     * This functions draws an imaginary line from point p to the right side of the panel,
     * if the imaginary line intersects the edges of the polygon an odd number of times then
     * that point is inside the polygon
     */
    public static boolean pointInsidePolygon(List<Point> polygon, Point p) {

        polygon = clockwiseSort(polygon);
        List<Segment> edges = new ArrayList<>();

        for (int i = 0; i < polygon.size(); i++) {
            Point p1 = polygon.get(i);
            Point p2 = polygon.get((i + 1) % polygon.size());

            edges.add(new Segment(p1, p2));
        }

        Segment aux = new Segment(p, new Point(WIDTH + 5, p.y));

        int count = 0;
        for (Segment e: edges)
            if (doIntersect(aux, e))
                count++;

        return count % 2 != 0;
    }
}
