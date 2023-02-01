package model;

import java.util.ArrayList;

public class Edge {
    private int x1, y1, x2, y2;

    public Edge(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public boolean isHorizontal(){
        return y1 == y2;
    }

    public void orientace(){ //ty usecky aby smerovala dolu
        if (y2 < y1) {
            int pomocna = y2;
            y2 = y1;
            y1 = pomocna;

            pomocna = x2;
            x2 = x1;
            x1 = pomocna;
        }
    }

   public boolean inIntersection(int y){
       return (y >= y1 && y < y2);
    }

    public boolean inIntersection(int y, int x){
        return (((y2-y1)*x) -((x2-x1)*y) + (x2*y1) - (y2*x1)) <= 0;
    }

    public int getIntersection(int y){
        float dx = x2- x1;
        float dy = y2 - y1;

        float k = (dy / dx);
        float q = (y1 - (k * x1));

        float x = ((y-q)/k);
        return (int)Math.ceil(x); //.ceil zaokrouhluje nahodu
    }

    public Point getIntersection(Point a, Point b){
        int k = a.getX();
        int j = a.getY();
        int p = b.getX();
        int l = b.getY();
        System.out.println("Body A: " + k + " " + j);
        System.out.println("Body B: " + p + " " + l);
        double x = (double) (((x1*y2) - (x2*y1))*(a.getX() - b.getX()) - ((a.getX() * b.getY()) - (b.getX()*a.getY())) * (x1 - x2)) / ((x1-x2)*(a.getY() - b.getY()) - (y1 - y2) * (a.getX() - b.getX()));
        double y = (double) (((x1*y2) - (x2*y1))*(a.getY() - b.getY()) - ((a.getX() * b.getY()) - (b.getX()*a.getY())) * (y1 - y2)) / ((x1-x2)*(a.getY() - b.getY()) - (y1 - y2) * (a.getX() - b.getX()));
        System.out.println("Finalni X,Y: " + x + " "+ y);
        return new Point((int) x,(int )y);
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }
}
