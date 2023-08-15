package vg.display;

import vg.util.Segment;
import vg.util.Utility;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static vg.util.Utility.*;

public class Panel extends JPanel {

    List<Point> sites = new ArrayList<>();
    List<List<Point>> regions = new ArrayList<>();

    public Panel() {
        this.setPreferredSize(new Dimension(Utility.WIDTH, Utility.HEIGHT));
        this.setLayout(null);

        JButton actionButton = new JButton();
        actionButton.setBounds(50, 50, 25, 25);
        actionButton.addActionListener(a -> this.triangulate());
        this.add(actionButton);

    }

    public void triangulate() {
        sites = new ArrayList<>();
        regions = new ArrayList<>();

        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            int x = random.nextInt(40, Utility.WIDTH - 40);
            int y = random.nextInt(40, Utility.HEIGHT - 40);
            sites.add(new Point(x, y));
        }

        for (int i = 0; i < sites.size(); i++) {
            Point p = sites.get(i);
            List<Point> currentPolygon = new ArrayList<>(List.of(
                    new Point(0, 0),
                    new Point(Utility.WIDTH, 0),
                    new Point(Utility.WIDTH, Utility.HEIGHT),
                    new Point(0, Utility.HEIGHT)
            ));

            for (int j = 0; j < sites.size(); j++) {
                if (i == j)
                    continue;

                Point q = sites.get(j);

                Segment bisection = getBisection(p, q);

                List<Point>[] aux = splitPolygon(currentPolygon, bisection);
                if (pointInsidePolygon(aux[0], p))
                    currentPolygon = aux[0];
                else
                    currentPolygon = aux[1];
            }

            regions.add(currentPolygon);
        }


        this.repaint();
    }


    @Override
    public void paint(Graphics grp) {
        super.paint(grp);
        Graphics2D g = (Graphics2D) grp;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        for (List<Point> region : regions) {
            for (int i = 0; i < region.size(); i++) {
                Point p1 = region.get(i);
                Point p2 = region.get((i + 1) % region.size());
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }

        g.setColor(Color.RED);
        for (Point p : sites)
            g.fillOval(p.x - 4, p.y - 4, 8, 8);
    }

}
