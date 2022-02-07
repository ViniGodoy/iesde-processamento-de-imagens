package cap2.parte1;

import cap1.Vector3;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.function.UnaryOperator;

public class Exemplo5 {
    public int clamp(float v) {
        int value = (int) v;
        return value < 0 ? 0 : (value > 255 ? 255 : value);
    }

    public int vec3ToRGB(Vector3 v) {
        return new Color(clamp(v.x),clamp(v.y), clamp(v.z)).getRGB();
    }

    public Vector3 RGBtoVec3(int rgb) {
        var c = new Color(rgb);
        return new Vector3(c.getRed(), c.getGreen(), c.getBlue());
    }

    BufferedImage processar(BufferedImage img, UnaryOperator<Vector3> op) {
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

    BufferedImage brilho(BufferedImage img, float valor) {
        return processar(img, (p) -> p.mul(valor));
    }

    public void run() throws Exception {
        System.out.println(new File(".").getAbsolutePath());
        var img = ImageIO.read(new File("./imagens/in/peixes.jpg"));
        ImageIO.write(brilho(img, 1.5f), "jpg", new File("f3-brilho-5.jpg"));
        ImageIO.write(brilho(img, 0.5f), "jpg", new File("f3-brilho-15.jpg"));
    }

    public static void main(String[] args) throws Exception {
        new Exemplo5().run();
    }
}
