package cap2.parte1;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Exemplo3 {
    public int clamp(int value) {
        return value < 0 ? 0 : (value > 255 ? 255 : value);
    }

    BufferedImage brilho(BufferedImage img, float valor) {
        var out = new BufferedImage(img.getWidth(), img.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        for (var y = 0; y < img.getHeight(); y++) {
            for (var x = 0; x < img.getWidth(); x++) {
                var pixelIn = new Color(img.getRGB(x, y));

                var r = (int)(pixelIn.getRed() * valor);
                var g = (int)(pixelIn.getGreen() * valor);
                var b = (int)(pixelIn.getBlue() * valor);

                var pixelOut = new Color(clamp(r),clamp(g),clamp(b));
                out.setRGB(x, y, pixelOut.getRGB());
            }
        }
        return out;
    }

    public void run() throws Exception {
        var img = ImageIO.read(new File("./imagens/in/peixes.jpg"));
        ImageIO.write(brilho(img, 1.5f), "jpg", new File("bright.jpg"));
        ImageIO.write(brilho(img, 0.5f), "jpg", new File("dark.jpg"));
    }

    public static void main(String[] args) throws Exception {
        new Exemplo3().run();
    }
}
