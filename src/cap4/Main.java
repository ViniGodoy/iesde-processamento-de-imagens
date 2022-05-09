package cap4;

import java.awt.*;
import java.awt.image.BufferedImage;

import static cap2.parte2.ProcImagem.limiarizacao;
import static cap2.parte2.ProcImagem.*;
import static cap2.parte2.Util.*;
import static cap4.ProcImagem.*;

public class Main {
    public static BufferedImage criarFigura3() {
        var pixels = new int[][]{
                {85, 82, 82, 82, 82, 82, 82, 85},
                {82, 85, 85, 85, 85, 85, 85, 82},
                {82, 85, 80, 85, 85, 80, 85, 82},
                {82, 85, 85, 85, 85, 85, 85, 82},
                {82, 85, 78, 85, 85, 85, 85, 82},
                {82, 85, 85, 78, 78, 85, 85, 82},
                {82, 82, 85, 85, 85, 85, 85, 85},
                {85, 85, 82, 82, 82, 82, 82, 85},
        };
        var img = new BufferedImage(8,8, BufferedImage.TYPE_INT_RGB);
        for (var y = 0; y < 8; y++) {
            for (var x = 0; x < 8; x++) {
                var c = pixels[y][x];
                img.setRGB(x, y, new Color(c, c, c).getRGB());
            }
        }
        return img;
    }

    public static BufferedImage criarFigura6() {
        var pixels = new int[][]{
                {7, 5, 5, 5, 5, 5, 5, 7},
                {4, 7, 7, 7, 7, 7, 7, 4},
                {4, 7, 0, 7, 7, 0, 7, 4},
                {6, 7, 7, 7, 7, 7, 7, 6},
                {6, 7, 1, 7, 7, 7, 7, 6},
                {4, 7, 7, 2, 2, 7, 7, 4},
                {4, 4, 7, 7, 7, 7, 6, 4},
                {7, 7, 4, 3, 3, 3, 4, 7}
        };

        var img = new BufferedImage(8,8, BufferedImage.TYPE_INT_RGB);
        for (var y = 0; y < 8; y++) {
            for (var x = 0; x < 8; x++) {
                var c = (int)(pixels[y][x] * 250.0 / 7);
                img.setRGB(x, y, new Color(c, c, c).getRGB());
            }
        }
        return img;
    }


    public static void main(String[] args) {
        setSavePath("imagens/out/cap4");

        var flores = carregar("flores");
        var floresCinza = escalaDeCinza(flores);
        var floresBaixoContraste = processar(floresCinza, p -> p.mul(0.3f).add(0.6f));
        salvar("f1-cinza", floresCinza);
        salvar("f1-baixo", floresBaixoContraste);

        var floresBaixoHistograma = calcularHistograma(floresBaixoContraste);
        salvar("f1-cinza-histograma", desenharHistograma(floresCinza));
        salvar("f1-baixo-histograma", desenharHistograma(floresBaixoHistograma));
        salvar("f2-baixo-histograma", desenharHistogramas(floresBaixoHistograma));

        var figura3 = criarFigura3();
        salvar("fig3-boneco", "png", figura3);

        var histograma = calcularHistograma(figura3);
        salvar("fig3-histograma", desenharHistogramas(histograma));
        salvar("fig4-equalizada", "png", equalizar(figura3));

        var floresEqualizada = equalizar(floresBaixoContraste);
        salvar("f5-equalizada", floresEqualizada);
        salvar("f5-equalizada-histograma", desenharHistogramas(floresEqualizada));

        var figura6 = criarFigura6();
        var histFig6 = calcularHistograma(figura6);
        salvar("f6-boneco", "png", figura6);
        salvar("f6-histograma", desenharHistogramas(figura6));

        var casteloCinza = escalaDeCinza(carregar("castelo"));
        var limiar = otsu(casteloCinza);
        salvar("f8-castelo-limiar-" + limiar, limiarizacao(casteloCinza, limiar));

        var peixesCinza = escalaDeCinza(carregar("peixes"));
        limiar = otsu(peixesCinza);
        salvar("f8-peixes-limiar-" + limiar, limiarizacao(peixesCinza, limiar));

        var carroCinza = escalaDeCinza(carregar("carro"));
        limiar = otsu(carroCinza);
        salvar("f8-carro-limiar-" + limiar, limiarizacao(carroCinza, limiar));

        var cachorrinho = carregar("cachorrinho");
        salvar("f15-12bits", quantizacao(cachorrinho, Palhetas.R4G4B4));
        salvar("f15-8bits", quantizacao(cachorrinho, Palhetas.R3G3B2));
        salvar("f15-mac", quantizacao(cachorrinho, Palhetas.MAC));

        salvar("f16-mac-dither", dither(cachorrinho, Palhetas.MAC));

        salvar("f17-castelo-dither", dither(casteloCinza, Palhetas.BINARIA));
        salvar("f17-flores-dither", dither(floresCinza, Palhetas.BINARIA));
        salvar("f17-carro-dither", dither(carroCinza, Palhetas.BINARIA));

        //Atividade 2
        //Letra a. saturar
        var peixes = carregar("peixes");
        salvar("f18-saturar-1.5", saturar(peixes, 1.5f));
        salvar("f18-saturar-0.5", saturar(peixes, 0.5f));

        //Letra b. iluminar
        salvar("f19-iluminar-1.5", iluminar(peixes, 1.5f));
        salvar("f19-iluminar-0.5", iluminar(peixes, 0.5f));

        //Letra c. deslocar
        salvar("f20-matiz-090", deslocarMatiz(peixes, 90));
        salvar("f20-matiz-180", deslocarMatiz(peixes, 180));
        salvar("f20-matiz-270", deslocarMatiz(peixes, 270));

        //Letra d.
        salvar("f21-8-quant", quantizacao(peixes, Palhetas.OITO_SOLIDAS));
        salvar("f21-8-dither", dither(peixes, Palhetas.OITO_SOLIDAS));
    }
}
