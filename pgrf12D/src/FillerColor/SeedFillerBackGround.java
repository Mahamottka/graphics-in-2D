package FillerColor;

import rasterize.Raster;

public class SeedFillerBackGround implements Filler {

    private final int x, y;
    private Raster raster;
    private int fillColor, backgroundColor;
    int width;
    int height;

    public SeedFillerBackGround(int x, int y, Raster raster, int fillColor, int backgroundColor, int width, int height) {
        this.x = x;
        this.y = y;
        this.raster = raster;
        this.fillColor = fillColor;
        this.backgroundColor = backgroundColor;
        this.width = width;
        this.height = height;
    }

    @Override
    public void fill() {
        seedFill(x, y);
    }


    //musel se zvýšit stack
    private void seedFill(int x, int y) {
        if ((x >= 0) && (y >= 0) && (x < width) && (y < height)) {
            //načtu barvu z pixelu
            int pixelColor = raster.getPixel(x, y);

            //podmínka pokud barva pixelu se nerovná barvě pozadí -> skončím
            if (pixelColor != backgroundColor) {
                return;
            }

            //obarvím pixel
            raster.setPixel(x, y, fillColor);

            //rekurzivně zavolám seedFill pro 4 sousedy
            seedFill(x - 1, y);
            seedFill(x, y - 1);
            seedFill(x + 1, y);
            seedFill(x, y + 1);
        }
    }

}
