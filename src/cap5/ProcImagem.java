package cap5;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ProcImagem {
    private static class Coord {
        public int x, y;
        public Coord(int x, int y) {
            this.x = x; this.y = y;
        }
    }

    private static boolean deveProcessar(BufferedImage img, int[][] mapa, int x, int y)
    {
        return x >= 0 && x < img.getWidth() && y >= 0 && y < img.getHeight() //1. Está dentro da imagem
            && new Color(img.getRGB(x, y)).getRed() != 0                     //2. Não é preto
            && mapa[x][y] == 0;                                              //3. Não foi rotulado
    }

    public static void floodFill(BufferedImage img, int x, int y, int rotulo, int[][] mapa) {
        var pilha = new LinkedList<Coord>();
        pilha.add(new Coord(x, y));
        while (!pilha.isEmpty()) {
            //1. Retira o pixel a ser processado
            var c = pilha.removeLast();
            //2. Testa se deve processar
            if (deveProcessar(img, mapa, c.x, c.y)) {
                //3. Rotula o pixel
                mapa[c.x][c.y] = rotulo;

                //4. Adiciona seus 4 vizinhos
                pilha.add(new Coord(c.x+1, c.y));
                pilha.add(new Coord(c.x, c.y+1));
                pilha.add(new Coord(c.x-1, c.y));
                pilha.add(new Coord(c.x, c.y-1));
            }
        }
    }

    public static int[][] segmentosConectados(BufferedImage img) {
        var mapa = new int[img.getWidth()][img.getHeight()];

        var rotulo = 1;
        for (var x = 0; x < img.getWidth(); x++) {
            for (var y = 0; y < img.getHeight(); y++) {
                if (deveProcessar(img, mapa, x, y)) {
                    floodFill(img, x, y, rotulo++, mapa);
                }
            }
        }
        return mapa;
    }

    public static BufferedImage desenharSegmentos(int[][] mapa) {
        var cores = new int[] {
            Color.GREEN.getRGB(), Color.RED.getRGB(), Color.BLUE.getRGB(),
            Color.YELLOW.getRGB(), Color.MAGENTA.getRGB(), Color.CYAN.getRGB(),
            Color.ORANGE.getRGB(), Color.PINK.getRGB(), Color.GRAY.getRGB(),
            Color.WHITE.getRGB()
        };

        var img = new BufferedImage(mapa.length, mapa[0].length, BufferedImage.TYPE_INT_RGB);
        for (var x = 0; x < img.getWidth(); x++) {
            for (var y = 0; y < img.getHeight(); y++) {
                if (mapa[x][y] == 0) continue;
                img.setRGB(x, y, cores[(mapa[x][y]-1) % cores.length]);
            }
        }
        return img;
    }

    //**************************
    //* Resolução da atividade 1
    //**************************
    public static class Segmento {
        private int rotulo;
        private int quantidade;
        private Rectangle retangulo;

        public Segmento(int rotulo, int quantidade,
                        int minX, int maxX, int minY, int maxY)
        {
            this.rotulo = rotulo;
            this.quantidade = quantidade;
            this.retangulo = new Rectangle(minX, minY, maxX-minX+1, maxY-minY+1);
        }

        public int getQuantidade() {
            return quantidade;
        }

        public int getRotulo() {
            return rotulo;
        }

        public Rectangle getRetangulo() {
            return retangulo;
        }

        @Override
        public String toString() {
            return ("Segmento " + rotulo +
                    ": quantidade=" + quantidade +
                    ", retangulo=" + retangulo)
                    .replace("java.awt.Rectangle", "");
        }
    }

    public static class SegmentosConectados {
        private int[][] mapa;
        private java.util.List<Segmento> segmentos;

        public SegmentosConectados(int[][] mapa) {
            this.mapa = mapa;
            this.segmentos = new ArrayList<>();
        }

        void add(Segmento segmento) {
            segmentos.add(segmento);
        }

        public int[][] getMapa() {
            return mapa;
        }

        public List<Segmento> getSegmentos() {
            return Collections.unmodifiableList(segmentos);
        }
    }

    public static Segmento floodFillEx(BufferedImage img, int x, int y, int rotulo, int[][] mapa) {
        var pilha = new LinkedList<Coord>();
        pilha.add(new Coord(x, y));
        var qtde = 0;
        int minX = x, maxX = x;
        int minY = y, maxY = y;

        while (!pilha.isEmpty()) {
            //1. Retira o pixel a ser processado
            var c = pilha.removeLast();
            //2. Testa se deve processar
            if (deveProcessar(img, mapa, c.x, c.y)) {
                //3. Rotula o pixel
                mapa[c.x][c.y] = rotulo;

                //Recolhe as informações extras (exercício 1)
                qtde++;
                if (c.x < minX) minX = c.x;
                if (c.x > maxX) maxX = c.x;
                if (c.y < minY) minY = c.y;
                if (c.y > maxY) maxY = c.y;

                //4. Adiciona seus 8 vizinhos
                pilha.add(new Coord(c.x+1, c.y));
                pilha.add(new Coord(c.x, c.y+1));
                pilha.add(new Coord(c.x-1, c.y));
                pilha.add(new Coord(c.x, c.y-1));

                pilha.add(new Coord(c.x+1, c.y+1));
                pilha.add(new Coord(c.x+1, c.y-1));
                pilha.add(new Coord(c.x-1, c.y+1));
                pilha.add(new Coord(c.x-1, c.y-1));
            }
        }

        return new Segmento(rotulo, qtde, minX, maxX, minY, maxY);
    }

    public static SegmentosConectados segmentosConectadosEx(BufferedImage img) {
        var mapa = new int[img.getWidth()][img.getHeight()];
        var resultado = new SegmentosConectados(mapa);

        var rotulo = 1;
        for (var x = 0; x < img.getWidth(); x++) {
            for (var y = 0; y < img.getHeight(); y++) {
                if (deveProcessar(img, mapa, x, y)) {
                    resultado.add(floodFillEx(img, x, y, rotulo++, mapa));
                }
            }
        }
        return resultado;
    }

    public static BufferedImage desenharSegmentosEx(SegmentosConectados sc) {
        var cores = new int[] {
                Color.GREEN.getRGB(), Color.RED.getRGB(), Color.BLUE.getRGB(),
                Color.YELLOW.getRGB(), Color.MAGENTA.getRGB(), Color.CYAN.getRGB(),
                Color.ORANGE.getRGB(), Color.PINK.getRGB()
        };

        var mapa = sc.getMapa();
        var img = new BufferedImage(mapa.length, mapa[0].length, BufferedImage.TYPE_INT_RGB);
        for (var x = 0; x < img.getWidth(); x++) {
            for (var y = 0; y < img.getHeight(); y++) {
                if (mapa[x][y] == 0) continue;
                img.setRGB(x, y, cores[(mapa[x][y]-1) % cores.length]);
            }
        }

        //Desenha o retângulo dos segmentos
        var g2d = img.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(
                1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
            0, new float[]{9}, 0)
        );
        sc.getSegmentos().forEach(s -> g2d.draw(s.getRetangulo()));
        g2d.dispose();

        return img;
    }
}
