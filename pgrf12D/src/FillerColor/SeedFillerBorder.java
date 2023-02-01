package FillerColor;

import rasterize.Raster;
import utilities.IntPair;

import java.util.ArrayList;

public class SeedFillerBorder implements Filler {

    private final int x, y;
    private Raster raster;
    private int fillColor, borderColor;
    int height;
    int width;

    public SeedFillerBorder(int x, int y, Raster raster, int fillColor, int borderColor, int height, int width) {
        this.x = x;
        this.y = y;
        this.raster = raster;
        this.fillColor = fillColor;
        this.borderColor = borderColor;
        this.height = height;
        this.width = width;
    }

    @Override
    public void fill() {
        seedFill(x, y);
    }


    //pseudo rekurze z důvodu omezení stacku a při velkých obrazcích jde hodnota stacku do astronomických čísel
    //tedka používám pamět a ne stack, která se může hodit do swapu (paměti je víc než stacku)


    private void seedFill(int x, int y) {   //stará se aby se zavolalo na všech pixelech

            ArrayList<IntPair> toFill = seedFillExecute(x, y); //fronta
            while (!toFill.isEmpty()) {  //iteruje pres vsechna pole v tofill a vola seedfillexe
                IntPair current = toFill.remove(0); //odstraneni prvniho
                toFill.addAll(seedFillExecute(current.getX(), current.getY())); //do pole přidá všechna co se mají udělat
            }
    }

    private ArrayList<IntPair> seedFillExecute(int x, int y) {
            ArrayList<IntPair> ret = new ArrayList<>();
            //načte se barva do pixelu
            int pixelColor = raster.getPixel(x, y);

            //podmínka pokud barva pixelu se nerovná barvě borderu -> skončím
            if (pixelColor == borderColor || pixelColor == fillColor) {
                return ret;
            }


            //obarvím pixel
            raster.setPixel(x, y, fillColor);

            //kontroluje políčka vedle pixelu a kokntroluje jestli se má udělat či ne
            if (raster.getPixel(x - 1, y) != fillColor || raster.getPixel(x - 1, y) != borderColor) {
                ret.add(new IntPair(x - 1, y));
            }
            if (raster.getPixel(x, y - 1) != fillColor || raster.getPixel(x, y - 1) != borderColor) {
                ret.add(new IntPair(x, y - 1));
            }
            if (raster.getPixel(x + 1, y) != fillColor || raster.getPixel(x + 1, y) != borderColor) {
                ret.add(new IntPair(x + 1, y));
            }
            if (raster.getPixel(x, y + 1) != fillColor || raster.getPixel(x, y + 1) != borderColor) {
                ret.add(new IntPair(x, y + 1));
            }
            return ret;

    }
}
