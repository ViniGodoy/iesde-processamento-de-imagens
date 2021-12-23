package cap5.parte1;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class ProcImagem {

    private static class Coord {
        public int x;
        public int y;

        public Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static boolean frente(BufferedImage img, int x, int y) {
        if (x < 0 || y < 0 || x >= img.getWidth() || y >= img.getHeight()) return false;
        return new Color(img.getRGB(x, y)).getRed() != 0;
    }

    public static void floodFill(BufferedImage img, int x, int y, int valor, int[][] mapa) {
        var pixels = new LinkedList<Coord>();
        pixels.add(new Coord(x, y));
        while (!pixels.isEmpty()) {
            Coord c = pixels.removeLast();
            if (frente(img, c.x, c.y) && mapa[c.x][c.y] == 0) {
                mapa[c.x][c.y] = valor;
                pixels.add(new Coord(c.x+1, c.y));
                pixels.add(new Coord(c.x, c.y+1));
                pixels.add(new Coord(c.x-1, c.y));
                pixels.add(new Coord(c.x, c.y-1));
            }
        }
    }

    public static int[][] conectados(BufferedImage img) {
        var resultado = new int[img.getWidth()][img.getHeight()];

        int label = 1;
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                if (frente(img, x, y) && resultado[x][y] == 0) {
                    floodFill(img, x, y, label++, resultado);
                }
            }
        }
        return resultado;
    }

    public static BufferedImage desenhar(int[][] cc) {
        var cores = new int[] {
            Color.GREEN.getRGB(), Color.RED.getRGB(), Color.BLUE.getRGB(),
            Color.YELLOW.getRGB(), Color.MAGENTA.getRGB(), Color.CYAN.getRGB(),
            Color.ORANGE.getRGB(), Color.PINK.getRGB()
        };

        BufferedImage img = new BufferedImage(cc.length, cc[0].length, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                if (cc[x][y] == 0) continue;
                img.setRGB(x, y, cores[cc[x][y]-1 % cores.length]);
            }
        }
        return img;
    }
}
