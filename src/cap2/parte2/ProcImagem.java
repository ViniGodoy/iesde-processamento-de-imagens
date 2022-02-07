package cap2.parte2;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static cap2.parte2.Util.clamp;
import static cap2.parte2.Vector3.*;

public class ProcImagem {
    public static final Vector3 REC601 = new Vector3(0.299f, 0.587f, 0.114f);

    public static int red(Vector3 v) {
        return (int) clamp(v.x * 255, 0, 255);
    }

    public static int green(Vector3 v) {
        return (int) clamp(v.y * 255, 0, 255);
    }
    public static int blue(Vector3 v) {
        return (int) clamp(v.z * 255, 0, 255);
    }

    public static int vec3ToRGB(Vector3 v) {
        return new Color(red(v), green(v), blue(v)).getRGB();
    }

    public static Vector3 RGBtoVec3(int rgb) {
        var c = new Color(rgb);
        return new Vector3(c.getRed(), c.getGreen(), c.getBlue()).div(255);
    }

    public static Vector3 RGBtoVec3(int r, int g, int b) {
        return new Vector3(r, g, b).div(255);
    }

    public static BufferedImage processar(BufferedImage img, UnaryOperator<Vector3> op) {
        var out = new BufferedImage(img.getWidth(), img.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                var pixelIn = RGBtoVec3(img.getRGB(x, y));
                var pixelOut = op.apply(pixelIn);
                out.setRGB(x, y, vec3ToRGB(pixelOut));
            }
        }

        return out;
    }

    public static BufferedImage processarComMascara(
            BufferedImage img, BufferedImage mascara, UnaryOperator<Vector3> op)
    {
        var width = Math.min(img.getWidth(), mascara.getWidth());
        var height = Math.min(img.getHeight(), mascara.getHeight());

        var out = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (var y = 0; y < img.getHeight(); y++) {
            for (var x = 0; x < img.getWidth(); x++) {
                var a = RGBtoVec3(mascara.getRGB(x,y)).x;

                if (a == 0) {
                    out.setRGB(x, y, img.getRGB(x, y));
                } else {
                    var pixelIn = RGBtoVec3(img.getRGB(x, y));
                    var pixelOut = op.apply(pixelIn);
                    var mistura = add(pixelIn.mul(1 - a), pixelOut.mul(a));
                    out.setRGB(x, y, vec3ToRGB(mistura));
                }
            }
        }

        return out;
    }

    public static BufferedImage processar(BufferedImage img1,
        BufferedImage img2, BinaryOperator<Vector3> op)
    {
        int width = Math.min(img1.getWidth(), img2.getWidth());
        int height = Math.min(img1.getHeight(), img2.getHeight());

        var out = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (var y = 0; y < height; y++) {
            for (var x = 0; x < width; x++) {
                var p1 = RGBtoVec3(img1.getRGB(x, y));
                var p2 = RGBtoVec3(img2.getRGB(x, y));
                out.setRGB(x, y, vec3ToRGB(op.apply(p1, p2)));
            }
        }

        return out;
    }

    public static BufferedImage espelhar(BufferedImage img, boolean horizontal, boolean vertical) {
        var out = new BufferedImage(img.getWidth(), img.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                var sx = horizontal ? (img.getWidth() - x - 1) : x;
                var sy = vertical ? (img.getHeight() - y - 1) : y;
                out.setRGB(x, y, img.getRGB(sx, sy));
            }
        }

        return out;
    }

    public static BufferedImage copiarSe(
        BufferedImage img, boolean binario, Predicate<Vector3> criterio)
    {
        return processar(img, p -> criterio.test(p) ?
                (binario ? new Vector3(1) : p) : new Vector3());
    }

    public static BufferedImage copiarSe(BufferedImage img, Predicate<Vector3> criterio) {
        return copiarSe(img, false, criterio);
    }

    public static BufferedImage marcarSe(BufferedImage img, Predicate<Vector3> criterio) {
        return copiarSe(img, true, criterio);
    }

    public static BufferedImage brilho(BufferedImage img, float valor) {
        return processar(img, p -> p.mul(valor));
    }

    public static BufferedImage escalaDeCinzaMedia(BufferedImage img) {
        return processar(img, p -> new Vector3((p.x + p.y + p.z) / 3));
    }

    public static BufferedImage escalaDeCinza(BufferedImage img) {
        return processar(img, p -> new Vector3(p.dot(REC601)));
    }

    public static BufferedImage escalaDeCinzaComMascara(BufferedImage img, BufferedImage mascara) {
        return processarComMascara(img, mascara, p -> new Vector3(p.dot(REC601)));
    }


    public static BufferedImage canalVermelho(BufferedImage img, boolean cor) {
        return processar(img, p -> cor ? p.set(p.x, 0, 0) : p.set(p.x));
    }

    public static BufferedImage canalVerde(BufferedImage img, boolean cor) {
        return processar(img, p -> cor ? p.set(0, p.y, 0) : p.set(p.y));
    }

    public static BufferedImage canalAzul(BufferedImage img, boolean cor) {
        return processar(img, p -> cor ? p.set(0, 0, p.z) : p.set(p.z));
    }

    public static BufferedImage limiarizacao(BufferedImage img, int limiar) {
        return processar(img, p ->
                p.x < limiar / 255f ? new Vector3() : new Vector3(1)
        );
    }

    public static BufferedImage tingir(BufferedImage img, Vector3 cor) {
        return processar(img, p -> p.mul(cor));
    }

    public static BufferedImage gamma(BufferedImage img, float gamma) {
        return processar(img, p -> pow(p, gamma));
    }

    public static BufferedImage negativo(BufferedImage img) {
        return processar(img, p -> sub(1, p));
    }

    public static BufferedImage subtrair(BufferedImage img1, BufferedImage img2) {
        return processar(img1, img2, (p1, p2) -> p1.sub(p2));
    }

    public static BufferedImage somar(BufferedImage img1, BufferedImage img2) {
        return processar(img1, img2, (p1, p2) -> p1.add(p2));
    }

    public static BufferedImage misturar(BufferedImage img1, BufferedImage img2, float alfa) {
        final var a = clamp(alfa, 0, 1);
        return processar(img1, img2, (p1, p2) -> add(p1.mul(1f - a), p2.mul(a)));
    }
}
