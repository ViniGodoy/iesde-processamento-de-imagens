package cap4;

import cap2.parte2.Vector3;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.UnaryOperator;

import static cap2.parte2.ProcImagem.*;
import static cap2.parte2.Util.clamp;
import static cap2.parte2.Vector3.mul;
import static cap2.parte2.Vector3.sub;
import static java.lang.Math.round;

public class ProcImagem {
    public static Vector3 RGBtoHSBVec3(int color) {
        var c = new Color(color);
        var hsb = new float[3];
        Color.RGBtoHSB(
            c.getRed(), c.getGreen(), c.getBlue(),
            hsb
        );

        return new Vector3(hsb[0] * 360.0f, hsb[1], hsb[2]);
    }

    public static int HSBVec3ToRGB(Vector3 hsbColor) {
        return Color.HSBtoRGB(
        hsbColor.x / 360.0f,
            clamp(hsbColor.y, 0, 1),
            clamp(hsbColor.z, 0, 1)
        );
    }

    public static int[] calcularHistograma(BufferedImage img) {
        int[] histograma = new int[256];
        for (var y = 0; y < img.getHeight(); y++) {
            for (var x = 0; x < img.getWidth(); x++) {
                int tom = img.getRGB(x, y) & 0xFF;
                histograma[tom]++;
            }
        }
        return histograma;
    }

    public static int[] acumularHistograma(int[] histograma) {
        int[] acc = new int[histograma.length];
        for (int i = 1; i < histograma.length; i++) {
            acc[i] += histograma[i] + acc[i-1];
        }
        return acc;
    }

    public static int[] equalizar(int[] acc, int l) {
        //Localiza accMin
        float accMin = 0;
        for (var v : acc) {
            if (v > 0) {
                accMin = v;
                break;
            }
        }

        var pixels = acc[acc.length-1];

        //Calcula eq(t) para cada valor na imagem
        var eq = new int[l];
        for (int t = 0; t < acc.length; t++) {
            eq[t] = round(((acc[t] - accMin) / (pixels - accMin)) * (l-1));
        }
        return eq;
    }

    public static int[] equalizar(int[] acc) {
        return equalizar(acc, 256);
    }

    public static BufferedImage equalizar(BufferedImage img) {
        var h = calcularHistograma(img);
        var acc = acumularHistograma(h);
        var eq = equalizar(acc);

        var out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (var y = 0; y < img.getHeight(); y++) {
            for (var x = 0; x < img.getWidth(); x++) {
                int t = img.getRGB(x, y) & 0xFF;
                out.setRGB(x, y, new Color(eq[t], eq[t], eq[t]).getRGB());
            }
        }

        return out;
    }

    private static BufferedImage desenharHistograma(int[] histograma, int largura, int altura, Color corBarra) {
        var img = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);
        //Procura o maior valor no histograma
        float max = 0;
        for (var t : histograma) {
            if (t > max) max = t;
        }

        var pesoH = max / img.getHeight();
        for (var y = 0; y < img.getHeight(); y++) {
            for (var x = 0; x < img.getWidth(); x++) {

                var c = Math.round(x * 255.0f / img.getWidth());
                var r = corBarra.getRGB();
                var w = Color.WHITE.getRGB();
                var h = y * pesoH;
                img.setRGB(x, img.getHeight() - y - 1, h <= histograma[c] ? r : w);
            }
        }
        return img;
    }

    public static BufferedImage desenharHistograma(int[] histograma, int largura, int altura, boolean acc) {
        var hist = desenharHistograma(histograma, largura, altura, Color.BLACK);
        if (acc) {
            var accHist = desenharHistograma(acumularHistograma(histograma),
                    largura, altura, Color.ORANGE);

            hist = misturar(hist, accHist, 0.3f);
        }
        return hist;
    }

    public static BufferedImage desenharHistograma(int[] histograma) {
        return desenharHistograma(histograma, 512, 384, false);
    }

    public static BufferedImage desenharHistograma(BufferedImage imagem) {
        return desenharHistograma(calcularHistograma(imagem));
    }

    public static BufferedImage desenharHistogramas(int[] histograma) {
        return desenharHistograma(histograma, 512, 384, true);
    }

    public static BufferedImage desenharHistogramas(BufferedImage imagem) {
        return desenharHistogramas(calcularHistograma(imagem));
    }

    public static int otsu(BufferedImage img) {
        var histograma = calcularHistograma(img);
        var total = img.getWidth() * img.getHeight();

        //Soma o valor de todos os tons de cinza da imagem
        var soma = 0;
        for (var t=0; t<256; t++) {
            soma += t * histograma[t];
        }

        var somaB = 0;
        float pB = 0, pF;

        float maiorVariancia = 0;
        int melhorLimiar = 0;

        for (int l=0 ; l<256 ; l++) {
            // Não separou nenhum pixel para o fundo?
            pB += histograma[l];
            if (pB == 0) continue;

            //Todos os pixels acabaram na frente?
            pF = total - pB;
            if (pF == 0) break;

            somaB +=  l * histograma[l];

            //Medias do fundo e da frente
            float mB = somaB / pB;
            float mF = (soma - somaB) / pF;

            // Variância entre classes
            float varEntre = pB * pF * (mB - mF) * (mB - mF);

            // Procura pelo limiar onde a Vec é máxima
            if (varEntre > maiorVariancia) {
                maiorVariancia = varEntre;
                melhorLimiar = l;
            }
        }

        return melhorLimiar;
    }

    public static BufferedImage limiarizacao(BufferedImage img) {
        final float limiar = otsu(img) / 255f;
        return processar(img, p ->
            p.x < limiar ? new Vector3() : new Vector3(1)
        );
    }

    public static int corMaisProxima(int p, int[] palheta) {
        var minDist = Float.MAX_VALUE;
        var minCor = 0;
        var vp = RGBtoVec3(p);

        for (int cor : palheta) {
            var vc = RGBtoVec3(cor);
            float dist = sub(vp, vc).sizeSqr();
            if (dist < minDist) {
                minDist = dist;
                minCor = cor;
            }
        }
        return minCor;
    }

    public static BufferedImage quantizacao(BufferedImage img, int[] palheta) {
        var out = new BufferedImage(img.getWidth(), img.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        for (var y = 0; y < img.getHeight(); y++) {
            for (var x = 0; x < img.getWidth(); x++) {
                out.setRGB(x, y, corMaisProxima(img.getRGB(x, y), palheta));
            }
        }

        return out;
    }

    public static void addErro(BufferedImage img, int x, int y, Vector3 erro, float peso) {
        //Ignora se estiver fora da imagem
        if (x < 0 || y < 0 ||
            x >= img.getWidth() ||
            y >= img.getHeight()
        ) return;

        //Soma o erro e o define na imagem
        //Equivalente a p(x, y) += erro * peso;
        var p = RGBtoVec3(img.getRGB(x, y));
        p.add(mul(erro, peso));
        img.setRGB(x, y, vec3ToRGB(p));
    }

    public static BufferedImage dither(BufferedImage img, int[] palheta) {
        var out = new BufferedImage(img.getWidth(), img.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        //Copia a imagem original para não danifica-la
        for (var y = 0; y < img.getHeight(); y++) {
            for (var x = 0; x < img.getWidth(); x++) {
                out.setRGB(x, y, img.getRGB(x, y));
            }
        }

        for (var y = 0; y < out.getHeight(); y++) {
            for (var x = 0; x < out.getWidth(); x++) {
                var antiga = out.getRGB(x, y);
                var nova = corMaisProxima(antiga, palheta);

                var erro = sub(RGBtoVec3(antiga), RGBtoVec3(nova));
                addErro(out, x+1, y+0, erro, 7f / 16);
                addErro(out, x-1, y+1, erro, 3f / 16);
                addErro(out, x+0, y+1, erro, 5f / 16);
                addErro(out, x+1, y+1, erro, 1f / 16);
                out.setRGB(x, y, nova);
            }
        }

        return out;
    }

    //-----------
    //Atividade 2
    //-----------

    public static BufferedImage processarComHSB(BufferedImage img, UnaryOperator<Vector3> op) {
        var out = new BufferedImage(img.getWidth(), img.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                var pixelIn = RGBtoHSBVec3(img.getRGB(x, y));
                var pixelOut = op.apply(pixelIn);
                out.setRGB(x, y, HSBVec3ToRGB(pixelOut));
            }
        }

        return out;
    }

    public static BufferedImage deslocarMatiz(BufferedImage img, float angulo) {
        return processarComHSB(img, p-> {
            p.x += angulo;
            return p;
        });
    }

    public static BufferedImage saturar(BufferedImage img, float fator) {
        return processarComHSB(img, p-> {
            p.y *= fator;
            return p;
        });
    }

    public static BufferedImage iluminar(BufferedImage img, float fator) {
        return processarComHSB(img, p-> {
            p.z *= fator;
            return p;
        });
    }
}
