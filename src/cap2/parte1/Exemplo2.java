package cap2.parte1;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Exemplo2 {
public static void main(String[] args) throws IOException {
    var img = new BufferedImage(256, 50, BufferedImage.TYPE_INT_RGB);

    for (int y = 0; y < img.getHeight(); y++) {
        for (int x = 0; x < img.getWidth(); x++) {
            var coluna = new Color(0, 0, x);
            img.setRGB(x, y, coluna.getRGB());
        }
    }

    ImageIO.write(img, "png", new File("azul.png"));
}
}
