package cap5;

import cap2.parte2.Vector3;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static cap2.parte2.ProcImagem.RGBtoVec3;
import static cap2.parte2.Vector3.sub;

public class Knn {
    private static class Amostra {
        public int classe;
        public Vector3 elemento;
        public Amostra(int classe, Vector3 elemento) {
            this.classe = classe;
            this.elemento = elemento;
        }
    }

    private static class CorProximaDe implements Comparator<Amostra> {
        private Vector3 pixel;

        public CorProximaDe(Vector3 pixel) {
            this.pixel = pixel;
        }
        @Override
        public int compare(Amostra o1, Amostra o2) {
            var d1 = sub(pixel, o1.elemento).sizeSqr();
            var d2 = sub(pixel, o2.elemento).sizeSqr();

            if (d1 < d2) return -1;
            if (d1 > d2) return 1;
            return 0;
        }
    }

    private List<Amostra> amostras = new ArrayList<>();

    public void adicionarAmostra(int classe, Vector3 elemento) {
        amostras.add(new Amostra(classe ,elemento));
    }

    private int localizarClasse(Vector3 pixel, int k) {
        //Ordena os elementos pela distância
        amostras.sort(new CorProximaDe(pixel));

        //Conta por classe até os k vizinhos
        var contagem = new HashMap<Integer, Integer>();
        for (var i = 0; i < k; i++) {
            var c = amostras.get(i).classe;
            contagem.put(c, contagem.containsKey(c) ? contagem.get(c)+1 : 1);
        }

        //Localiza a classe com maior contagem
        var classe = -1;
        var maxCont = 0;
        for (var entry : contagem.entrySet()) {
            if (entry.getValue() > maxCont) {
                classe = entry.getKey();
                maxCont = entry.getValue();
            }
        }

        return classe;
    }

    public int[][] classificar(BufferedImage img, int k) {
        var mapa = new int[img.getWidth()][img.getHeight()];
        for (var y = 0; y < img.getHeight(); y++) {
            for (var x = 0; x < img.getWidth(); x++) {
                var pixel = RGBtoVec3(img.getRGB(x, y));
                mapa[x][y] = localizarClasse(pixel, k);
            }
        }
        return mapa;
    }
}
