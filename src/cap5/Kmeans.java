package cap5;

import cap2.parte2.Vector3;

import java.awt.image.BufferedImage;
import java.util.*;

import static cap2.parte2.Vector3.*;
import static cap2.parte2.ProcImagem.*;

public class Kmeans {
    private static class Grupo {
        int numero;
        Vector3 centroide;

        private Vector3 somaElementos = new Vector3();
        private double somaDistancias = 0;
        private int numElementos = 0;

        Vector3 maisProximo = new Vector3();
        private double menorDistancia = Double.MAX_VALUE;

        public Grupo(int numero, Vector3 centroide) {
            this.numero = numero;
            this.centroide = centroide;
        }

        public void add(Vector3 elemento) {
            numElementos += 1;
            somaElementos.add(elemento);
            somaDistancias += distancia(elemento);

            var dist = distancia(elemento);
            if (dist < menorDistancia) {
                menorDistancia = dist;
                maisProximo = elemento;
            }
        }

        public Grupo criarNoCentro() {
            return new Grupo(numero, div(somaElementos, numElementos));
        }

        public float distancia(Vector3 elem) {
            return sub(elem, centroide).size();
        }

        public double distanciaMedia() {
            return somaDistancias / numElementos;
        }
    }

    private List<Grupo> grupos;
    private double tolerancia = 0.01;

    public void setTolerancia(double tolerancia) {
        this.tolerancia = tolerancia;
    }

    public double getTolerancia() {
        return tolerancia;
    }

    private void criarGruposAleatorios(BufferedImage img, int classes) {
        grupos = new ArrayList<>();
        var rnd = new Random();
        for (var i = 0; i < classes; i++) {
            var pixel = RGBtoVec3(img.getRGB(
                    rnd.nextInt(img.getWidth()), rnd.nextInt(img.getHeight())
            ));

            grupos.add(new Grupo(i+1, pixel));
        }
    }

    private Grupo localizarGrupo(Vector3 elemento, List<Grupo> grupos) {
        Grupo maisProximo = null;
        var menorDistancia = Float.MAX_VALUE;
        for (var grupo : grupos) {
            var distancia = grupo.distancia(elemento);
            if (distancia < menorDistancia) {
                menorDistancia = distancia;
                maisProximo = grupo;
            }
        }

        return maisProximo;
    }

    private Grupo localizarGrupo(Vector3 elemento) {
        return localizarGrupo(elemento, grupos);
    }

    private void agrupar(BufferedImage img, List<Grupo> grupos) {
        for (var y = 0; y < img.getHeight(); y++) {
            for (var x = 0; x < img.getWidth(); x++) {
                var pixel = RGBtoVec3(img.getRGB(x, y));
                localizarGrupo(pixel, grupos).add(pixel);
            }
        }
    }

    private double calcularErro(List<Grupo> novos) {
        var distanciaNova = 0.0;
        var distanciaAnterior = 0.0;

        for (var i = 0; i < grupos.size(); i++) {
            distanciaAnterior += grupos.get(i).distanciaMedia();
            distanciaNova += novos.get(i).distanciaMedia();
        }
        return Math.abs(distanciaNova - distanciaAnterior) /
                (distanciaNova + distanciaAnterior);
    }


    public void treinar(BufferedImage img, int classes) {
        System.out.println("Treinando com kmeans...");
        //a. Cria os centroides aleatórios
        criarGruposAleatorios(img, classes);
        //b. Faz o agrupamento inicial
        agrupar(img, grupos);

        while (true) {
            //c. Cria os novos grupos e recalcula a posição dos centroides
            var novosGrupos = new ArrayList<Grupo>();
            for (var grupo : grupos) {
                novosGrupos.add(grupo.criarNoCentro());
            }

            //d. Agrupa os elementos
            agrupar(img, novosGrupos);

            //Se o erro for menor do que a tolerância, terminou
            var erro = calcularErro(novosGrupos);
            System.out.printf("  Erro: %.2f%n", erro);
            if (erro <= tolerancia) break;

            //Caso contrário, repete o processo
            grupos = novosGrupos;
        }
    }

    public int[][] classificar(BufferedImage img) {
        int[][] mapa = new int[img.getWidth()][img.getHeight()];
        for (var y = 0; y < img.getHeight(); y++) {
            for (var x = 0; x < img.getWidth(); x++) {
                var pixel = RGBtoVec3(img.getRGB(x, y));
                mapa[x][y] = localizarGrupo(pixel).numero;
            }
        }
        return mapa;
    }

    public BufferedImage desenhar(BufferedImage img) {
        return processar(img, p -> localizarGrupo(p).maisProximo);
    }
}
