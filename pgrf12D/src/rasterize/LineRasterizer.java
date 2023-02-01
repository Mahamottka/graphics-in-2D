package rasterize;

import model.Line;


public abstract class LineRasterizer {

    protected Raster raster;

    public LineRasterizer(Raster raster) {
        this.raster = raster;
    }

    public void rasterizeDotted(Line line, int d, int color){
        drawLine(line.getX1(), line.getY1(), line.getX2(), line.getY2(), d, color);
    }

    public void rasterize(Line line, int color) {
        drawLine(line.getX1(), line.getY1(), line.getX2(), line.getY2(), color);
    }


    protected void drawLine(int x1, int y1, int x2, int y2, int dotted, int color) {
    }
    public void drawLine(int x1, int y1, int x2, int y2, int color) {
    }
}
