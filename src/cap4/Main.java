package cap4;

import java.awt.*;
import java.awt.image.BufferedImage;

import static cap2.parte2.ProcImagem.limiarizacao;
import static cap2.parte2.ProcImagem.*;
import static cap2.parte2.Util.*;
import static cap4.ProcImagem.*;

public class Main {
    public static BufferedImage criarFigura3() {
        int [][] pixels = {
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
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                var c = pixels[y][x];
                img.setRGB(x, y, new Color(c, c, c).getRGB());
            }
        }
        return img;
    }

    public static BufferedImage criarFigura6() {
        int [][] pixels = {
                {7,	5, 5, 5, 5, 5, 5, 7},
                {4,	7, 7, 7, 7, 7, 7, 4},
                {4,	7, 0, 7, 7, 0, 7, 4},
                {6,	7, 7, 7, 7, 7, 7, 6},
                {6,	7, 1, 7, 7, 7, 7, 6},
                {4,	7, 7, 2, 2, 7, 7, 4},
                {4,	4, 7, 7, 7, 7, 6, 4},
                {7,	7, 4, 3, 3, 3, 4, 7}
        };

        var img = new BufferedImage(8,8, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                var c = (int)(pixels[y][x] * 250.0 / 7);
                img.setRGB(x, y, new Color(c, c, c).getRGB());
            }
        }
        return img;
    }


    public static void main(String[] args) {
        setSavePath("imagens/out/cap4");
        var gemeas = carregar("imagens/gemeas");
        var gemeasCinza = escalaDeCinza(gemeas);
        var gemeasBaixoContraste = processar(gemeasCinza, p -> p.mul(0.3f).add(0.6f));
        salvar("fig1-gemeas-cinza", gemeasCinza);
        salvar("fig1-gemeas-baixo", gemeasBaixoContraste);
        var gemeasBaixoHistograma = calcularHistograma(gemeasBaixoContraste);
        salvar("fig1-gemeas-cinza-histograma", desenharHistograma(gemeasCinza));
        salvar("fig1-gemeas-baixo-histograma", desenharHistograma(gemeasBaixoHistograma));
        salvar("fig2-gemeas-baixo-histograma", desenharHistogramas(gemeasBaixoHistograma));

        var figura3 = criarFigura3();
        salvar("fig3-boneco", "png", figura3);

        var histograma = calcularHistograma(figura3);
        salvar("fig3-histograma", desenharHistogramas(histograma));
        salvar("fig4-equalizada", "png", equalizar(figura3));
        var gemeasEqualizada = equalizar(gemeasBaixoContraste);
        salvar("fig5-equalizada", gemeasEqualizada);
        salvar("fig5-equalizada-histograma", desenharHistogramas(gemeasEqualizada));

        var figura6 = criarFigura6();
        var histFig6 = calcularHistograma(figura6);
        salvar("fig6-boneco", "png", figura6);
        salvar("fig6-histograma", desenharHistogramas(figura6));

        var renataCinza = escalaDeCinza(carregar("imagens/renata"));
        int limiar = otsu(renataCinza);
        salvar("fig8-renata-limiar-" + limiar, limiarizacao(renataCinza, limiar));

        limiar = otsu(gemeasCinza);
        salvar("fig8-gemeas-limiar-" + limiar, limiarizacao(gemeasCinza, limiar));

        var carroCinza = escalaDeCinza(carregar("imagens/carro"));
        limiar = otsu(carroCinza);
        salvar("fig8-carro-limiar-" + limiar, limiarizacao(carroCinza, limiar));

        var alice = carregar("imagens/alice");
        salvar("fig15-alice-12bits", quantizacao(alice, Palhetas.R4G4B4));
        salvar("fig15-alice-8bits", quantizacao(alice, Palhetas.R3G3B2));
        salvar("fig15-alice-mac", quantizacao(alice, Palhetas.MAC));

        salvar("fig16-alice-mac", dither(alice, Palhetas.MAC));

        salvar("fig17-renata-dither", dither(renataCinza, Palhetas.BINARIA));
        salvar("fig17-gemeas-dither", dither(gemeasCinza, Palhetas.BINARIA));
        salvar("fig17-carro-dither", dither(carroCinza, Palhetas.BINARIA));

        //Atividade 2
        //Letra a. saturar
        salvar("fig18-alice-saturar-1.5", saturar(alice, 1.5f));
        salvar("fig18-alice-saturar-0.5", saturar(alice, 0.5f));

        //Letra b. iluminar
        salvar("fig19-alice-iluminar-1.5", iluminar(alice, 1.5f));
        salvar("fig19-alice-iluminar-0.5", iluminar(alice, 0.5f));

        //Letra c. deslocar
        salvar("fig20-alice-matiz-090", deslocarMatiz(alice, 90));
        salvar("fig20-alice-matiz-180", deslocarMatiz(alice, 180));
        salvar("fig20-alice-matiz-270", deslocarMatiz(alice, 270));

        //Letra d.
        salvar("fig21-alice-8-quant", quantizacao(alice, Palhetas.OITO_SOLIDAS));
        salvar("fig21-alice-8-dither", dither(alice, Palhetas.OITO_SOLIDAS));
    }
}
