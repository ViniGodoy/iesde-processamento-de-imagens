package cap2.parte1;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.function.UnaryOperator;

public class Exemplo4 {
    public int clamp(int value) {
        return value < 0 ? 0 : (value > 255 ? 255 : value);
    }

    BufferedImage processar(BufferedImage img, UnaryOperator<Color> op) {
        var out = new BufferedImage(img.getWidth(), img.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        for (var y = 0; y < img.getHeight(); y++) {
            for (var x = 0; x < img.getWidth(); x++) {
                var pixelIn = new Color(img.getRGB(x, y));
                var pixelOut = op.apply(pixelIn);
                out.setRGB(x, y, pixelOut.getRGB());
            }
        }

        return out;
    }

    BufferedImage brilho(BufferedImage img, float valor) {
        return processar(img, (p) -> {
            var r = (int)(p.getRed() * valor);
            var g = (int)(p.getGreen() * valor);
            var b = (int)(p.getBlue() * valor);

            return new Color(clamp(r),clamp(g),clamp(b));
        });
    }

    public void run() throws Exception {
        var img = ImageIO.read(new File("imagens/renata.jpg"));
        ImageIO.write(brilho(img, 1.5f), "jpg", new File("bright.jpg"));
        ImageIO.write(brilho(img, 0.5f), "jpg", new File("dark.jpg"));
    }

    public static void main(String[] args) throws Exception {
        new Exemplo3().run();
    }
}
