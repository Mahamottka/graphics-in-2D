
import FillerColor.Filler;
import FillerColor.ScanLineFiller;
import FillerColor.SeedFillerBackGround;
import FillerColor.SeedFillerBorder;
import model.*;
import model.Point;
import model.Polygon;
import rasterize.FilledLineRasterizer;
import rasterize.LineRasterizer;
import rasterize.RasterBufferImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


public class Canvas {
    private final JFrame frame;
    private final JPanel panel;
    private final RasterBufferImage raster;
    private final LineRasterizer lineRasterizer;
    private int defaultX, defaultY; //pokud nebudu měnit hodnotu tak napsat sem, pokud ne do konstrutoru
    private final ArrayList<Line> lines;  //tohle bude skladovat čáry
    private Line line;
    private Point point, point2;
    private Point pointTri;
    private Polygon polygon, polygonOrez, polygonOut;

    private Triangle triangle;

    private JLabel label;


    ModKresleni modKresleni = ModKresleni.POLYGON; //defaultní hodnota

    enum ModKresleni {
        LINE, POLYGON, TRIANGLE, DRAWING
    }

    int dotted = 3;
    int color = Color.red.getRGB();
    int colorTazeni = Color.yellow.getRGB();

    public Canvas(int width, int height) {
        defaultX = 0;
        defaultY = 0;
        frame = new JFrame();
        lines = new ArrayList<>();
        label = new JLabel();

        label.setForeground(Color.white);


        frame.setLayout(new BorderLayout());
        frame.setTitle("PGRF1");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        raster = new RasterBufferImage(800, 600); //musí to opovídat šířce plátna v tom mainu
        lineRasterizer = new FilledLineRasterizer(raster);

        polygon = new Polygon(lineRasterizer); //musí být furt stejná protože zůstává pořád stejný akorát přidám body
        polygonOrez = new Polygon(lineRasterizer);
        polygonOut = new Polygon(lineRasterizer);
        triangle = new Triangle(lineRasterizer); //pro trojuhelník

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                raster.present(g);
            }
        };

        label.setText("Režim kreslení polygonu");
        panel.add(label);


        panel.setPreferredSize(new Dimension(width, height));

        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        panel.requestFocus();       //velice důležité, při smazání nefunguje
        panel.requestFocusInWindow();

        panel.addMouseListener(new MouseAdapter() { //tohle je přímo class, zjistujeme pocatecni body
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (modKresleni == ModKresleni.LINE) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        defaultX = e.getX();
                        defaultY = e.getY();
                    }
                }

                if (modKresleni == ModKresleni.POLYGON) {
                    raster.clear();
                    if (e.getButton() == MouseEvent.BUTTON1) { //tohle je klíčový pro polygon
                        point = new Point(e.getX(), e.getY());
                        polygon.addPoint(point);

                    }
                    polygon.drawPolygon();

                    if (e.getButton() == MouseEvent.BUTTON3) {
                        point2 = new Point(e.getX(), e.getY());
                        polygonOrez.setColor(Color.yellow.getRGB());
                        polygonOrez.addPoint(point2);

                    }
                    polygonOrez.drawPolygon();
                    panel.repaint();
                }

                if (modKresleni == ModKresleni.DRAWING) {
                    try{
                        if (e.getButton() == MouseEvent.BUTTON2){ //podle borderu
                            Filler seedFillerBorder = new SeedFillerBorder(e.getX(), e.getY(), raster, colorTazeni, color, height, width);
                            seedFillerBorder.fill();
                            panel.repaint();

                        }
                        if (e.getButton() == MouseEvent.BUTTON1){ //podle pozadí
                            Filler seedFiller = new SeedFillerBackGround(e.getX(), e.getY(), raster, colorTazeni, Color.black.getRGB(), width, height);
                            seedFiller.fill();
                            panel.repaint();
                        }
                        if (e.getButton() == MouseEvent.BUTTON3){ //scanLine
                            Filler scanLine = new ScanLineFiller(lineRasterizer, polygon);
                            scanLine.fill();
                            panel.repaint();
                        }
                    } catch (Exception exception){
                        JOptionPane.showMessageDialog(null, "Před kreslením je potřeba zadat mód!");
                        //jebe s tím dost možná repaint
                    }
                }

                if (modKresleni == ModKresleni.TRIANGLE) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        raster.clear();
                        pointTri = new Point(e.getX(), e.getY());
                        triangle.addPodstava(pointTri);
                        triangle.drawTriangle();
                        panel.repaint();
                    }
                }

            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (modKresleni == ModKresleni.LINE) {
                    raster.clear();

                    line = new Line(defaultX, defaultY, e.getX(), e.getY()); //tady nastavuju první bod

                    if ((e.getX() >= 0 && e.getX() < width) && (e.getY() >= 0 && e.getY() < height)) //tohle mě limituje na obrazovku
                        lineRasterizer.rasterizeDotted(line, dotted, colorTazeni); //pouze cara, nezustava

                    for (Line l : lines) { //stará se o uložení
                        lineRasterizer.rasterize(l, color); //vykreslení toho listu, cara zustava
                    }

                    panel.repaint();
                }
            }

        });
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                if (modKresleni == ModKresleni.LINE) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        lines.add(line);
                    }
                }


            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                if (modKresleni == ModKresleni.TRIANGLE) {
                    triangle.mousePoints(e.getX(), e.getY());
                }
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_L: //přepne do režimu kresli line
                        modKresleni = ModKresleni.LINE;
                        label.setText("Režim kreslení čáry");
                        break;
                    case KeyEvent.VK_P: //přepne do režimu kresli polygonu
                        modKresleni = ModKresleni.POLYGON;
                        label.setText("Režim kreslení polygonu");
                        break;
                    case KeyEvent.VK_T: //přepne do režimu kresli trojuhelník
                        modKresleni = ModKresleni.TRIANGLE;
                        label.setText("Režim kreslení trojuhelníku");
                        break;
                    case KeyEvent.VK_D:
                        modKresleni = ModKresleni.DRAWING;
                        label.setText("Rezim vybarvovani");
                        break;
                    case KeyEvent.VK_C: //vyclearuje vsechno nakreslene
                        lines.clear();
                        polygon.clearPoints(true);
                        polygonOrez.clearPoints(true);
                        polygonOut.clearPoints(true);
                        triangle.clearPoints(true);
                        break;
                    case KeyEvent.VK_NUMPAD1:
                        colorTazeni = Color.yellow.getRGB();
                        break;
                    case KeyEvent.VK_NUMPAD2:
                        colorTazeni = Color.red.getRGB();
                        break;
                    case KeyEvent.VK_NUMPAD3:
                        colorTazeni = Color.blue.getRGB();
                        break;
                    case KeyEvent.VK_NUMPAD4:
                        colorTazeni = Color.green.getRGB();
                        break;
                    case KeyEvent.VK_NUMPAD5:
                        colorTazeni = Color.orange.getRGB();
                        break;
                    case KeyEvent.VK_NUMPAD6:
                        colorTazeni = Color.white.getRGB();
                        break;
                    case KeyEvent.VK_NUMPAD7:
                        colorTazeni = Color.magenta.getRGB();
                        break;
                    case KeyEvent.VK_NUMPAD8:
                        colorTazeni = Color.gray.getRGB();
                        break;
                    case KeyEvent.VK_NUMPAD9:
                        colorTazeni = Color.cyan.getRGB();
                        break;
                }
            }


            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_PAGE_UP:
                        dotted++;
                        if (dotted >= 10) dotted = 10; //nastavení teckovaný cary
                        break;
                    case KeyEvent.VK_PAGE_DOWN:
                        dotted--;
                        if (dotted <= 1) dotted = 1;
                        break;
                }
            }
        });

    }

    public void start() {
        raster.clear();
        panel.repaint();
    }


}
