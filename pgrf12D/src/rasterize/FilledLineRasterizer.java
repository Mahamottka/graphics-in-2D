package rasterize;

import java.awt.*;

public class FilledLineRasterizer extends LineRasterizer {
    public FilledLineRasterizer(Raster raster) {
        super(raster);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2, int color){
        drawLine(x1, y1, x2, y2,1, color);
    }


    @Override
    protected void drawLine(int x1, int y1, int x2, int y2, int dotted, int color) { //nějakej ten algoritmus, u mě trivial

        /*draw*/


        float dy = y2 - y1;
        float dx = x2 - x1;
        float k; //delta x delta y K
        int pomocna;

        if (dx == 0) {  //paralelní s osou X
            if (y2 < y1) { //obracení po ose
                pomocna = y1;
                y1 = y2;
                y2 = pomocna;
            }
            for (int i = y1; i < y2; i += dotted) { // dotted = nastavuje mezery (1 = bez mezer)
                raster.setPixel(x1, i, color);   //vykreslování jednotlivých pixelů
            }
            return; //aby se to dále neprovádělo
        }

        if (dy == 0) {  //paralelní s osou Y

            if (x2 < x1) { //obracení po ose
                pomocna = x1;
                x1 = x2;
                x2 = pomocna;
            }

            for (int i = x1; i < x2; i += dotted) { // dotted = nastavuje mezery (1 = bez mezer)
                raster.setPixel(i, y2, color);   //vykreslování jednotlivých pixelů
            }
            return;
        }

        k = dy / dx;
        float q = y1 - (k * x1);
        if (k <= 1 && k >= -1) { //tímto se osa řídí, pravej levej X trychtýř ><
            if (x2 < x1) { //obracení po ose
                pomocna = x1;
                x1 = x2;
                x2 = pomocna;

            }

            for (int i = x1; i < x2; i += dotted) {
                int yy = (int) ((k * i) + q);
                raster.setPixel(i, yy, color);
            }


        } else { //tohle se stará o Y a -Y trychtýře
            if (y2 < y1) {   //obracení po ose
                pomocna = y1;
                y1 = y2;
                y2 = pomocna;
            }


            for (int i = y1; i < y2; i += dotted) {
                int xx = (int) ((i - q) / k);
                raster.setPixel(xx, i, color);
            }
        }
    }
}
