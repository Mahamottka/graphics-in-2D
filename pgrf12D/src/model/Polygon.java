package model;

import rasterize.FilledLineRasterizer;
import rasterize.LineRasterizer;
import rasterize.RasterBufferImage;

import java.awt.*;
import java.util.ArrayList;

public class Polygon {

    private Point point;
    private final LineRasterizer lineRasterizer;
    ArrayList<Point> points = new ArrayList<>();
    private int color = Color.red.getRGB();

    public void setColor(int color) {
        this.color = color;
    }

    public Polygon(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }


    public void addPoint(Point point) {
        points.add(point);
    }

    public void clearPoints(Boolean clearPoins){ //clearuje pointy z pole aby se mohlo kreslit znovu
        if (clearPoins){
            points.clear();
        }
    }

    public void insertArrayList(ArrayList<Point> inserted){
        points.addAll(inserted);
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public Point getPoint(int index) {
        return points.get(index);
    }

    public int getCount() {
        return points.size();
    }

    public void drawPolygon() {
        if (points.size() > 1) {
            for (int i = 0; i < points.size() - 1; i++) {
                Point A = points.get(i);
                Point B = points.get(i + 1);
                lineRasterizer.drawLine(A.getX(), A.getY(), B.getX(), B.getY(), color);
            }

        }
        if (points.size() > 2) { //první a poslední bod spojení
            Point A = points.get(0);
            Point B = points.get(points.size() - 1);
            lineRasterizer.drawLine(A.getX(), A.getY(), B.getX(), B.getY(), color);
        }
    }


}
