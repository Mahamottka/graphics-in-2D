package FillerColor;

import model.Edge;
import model.Point;
import model.Polygon;
import rasterize.LineRasterizer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScanLineFiller implements Filler {

    private final LineRasterizer lineRasterizer;
    private final Polygon polygon;
    private final int color = Color.yellow.getRGB();
    private final int color2 = Color.pink.getRGB();

    public ScanLineFiller(LineRasterizer lineRasterizer, Polygon polygon) {
        this.lineRasterizer = lineRasterizer;
        this.polygon = polygon;
    }

    @Override
    public void fill() {
        scanLine();
    }

    private void scanLine() {
        //vytvořit hrany
        ArrayList<Edge> edges = new ArrayList<>();

        //Projít pointy polygonu a vytvořit z nich hrany
        for (int i = 0; i < polygon.getCount(); i++) {
            Point p1 = polygon.getPoint(i);
            int index = (i + 1) % polygon.getCount();
            Point p2 = polygon.getPoint(index);

            Edge edge = new Edge(p1.getX(), p1.getY(), p2.getX(), p2.getY());


            //pokud je hrana horizontální, nepřidáváme
            if (edge.isHorizontal()) {
                continue;
            }

            //zmenim orientaci
            edge.orientace();
            //přidám hranu do seznamu
            edges.add(edge);
        }
        //Najdu yMin a yMax
        int yMin = polygon.getPoint(0).getY();
        int yMax = yMin;

        for (Point p : polygon.getPoints()) {
            if (yMin > p.y) {
                yMin = p.y;
            }

            if (yMax < p.y) {
                yMax = p.y;
            }
        }

        int counter = 1;
        //Pro všechna y od yMin po yMax
        for (int y = yMin; y <= yMax; y++) {
            //Seznam pruseciku: List<Intiger>
            List pruseciky = new ArrayList<Integer>(); //list je interface, naprovo je implementace

            //Pro vsechny hrany
            //{
            //Existuje pruseick?
            //Ano - spocitam prusecik
            //Ulozim do seznamu pruseciku
            //}
            for (Edge edge : edges) { //tady v těch průsečících zapíšeme jenom ty Y
                if (edge.inIntersection(y)) {
                    pruseciky.add(edge.getIntersection(y));
                }
            }


            //setřidený průsečiku podle x, např bubblesort

            selectionSort(pruseciky); //automatický javácky třídění


            //spojit průsečíky lajnou lichý se sudým
            for (int i = 0; i < pruseciky.size(); i = i + 2) {
                if (counter % 4 == 0) {
                    lineRasterizer.drawLine((int) pruseciky.get(i), y, (int) pruseciky.get(i + 1), y, color);
                } else {
                    lineRasterizer.drawLine((int) pruseciky.get(i), y, (int) pruseciky.get(i + 1), y, color2);
                }
            }
            counter++;
        }

        //vykreslit polygon jako takovej - hranici
        this.polygon.drawPolygon();
    }
    private void selectionSort(List<Integer> array) {

        for(int i = 0; i < array.size() - 1; i++) {
            int min = i;
            for(int j = i + 1; j < array.size(); j++) {
                if(array.get(min) > array.get(j)) {
                    min = j;
                }
            }

            int temp = array.get(i);
            array.set(i, array.get(min));
            array.set(min, temp);
        }

    }

}
