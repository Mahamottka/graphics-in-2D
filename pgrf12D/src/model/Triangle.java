package model;

import rasterize.LineRasterizer;

import java.awt.*;
import java.util.ArrayList;


public class Triangle {
    private LineRasterizer lineRasterizer;
    private Point pointTri;
    ArrayList<Point> podstava = new ArrayList<>();
    private int color = Color.blue.getRGB();
    int x3,y3;

    public Triangle(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    public void addPodstava(Point pointTri) {
        if (podstava.size() < 2) {
            podstava.add(pointTri);
        }
    }

    public void clearPoints(Boolean clearPoints) { //clearuje pointy z podstavy aby se mohlo kreslit znovu
        if (clearPoints) {
            podstava.clear();
        }
    }

    public void mousePoints(int x, int y){
        x3 = x;
        y3 = y;
    }

    Point A;    //bod usecky
    Point B;    //bod usecky


    public void drawTriangle() {
        if (podstava.size() <= 2) {
            A = podstava.get(0);

            B = podstava.get(podstava.size() - 1);
            lineRasterizer.drawLine(A.getX(), A.getY(), B.getX(), B.getY(), color); //tady mám vykreslení podstavy
        }
        if (podstava.size() == 2) {
            int x1 = ((B.x + A.x) / 2);
            int y1 = ((B.y + A.y) / 2);
            float dx = B.x - A.x;
            float dy = B.y - A.y;
            float k1 = (dy / dx);
            float k2 = (-1 / k1);
            float q2 = (y1 - k2 * x1);
            float k3 = k1;
            float q3 = (y3 - (k3*x3));
            float x = (q3 - q2) / (k2 - k3);
            float y = (k2 * x) + q2;   //dosazení do rovnice

            lineRasterizer.drawLine(A.getX(), A.getY(), (int)x, (int)y, color);
            lineRasterizer.drawLine(B.getX(), B.getY(), (int)x, (int)y, color);
        }


    }

}
