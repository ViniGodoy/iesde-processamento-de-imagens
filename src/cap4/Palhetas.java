package cap4;

import java.awt.*;

public class Palhetas {
    public static int[] BINARIA = {0xFF000000, 0xFFFFFF};

    public static int[] MAC  = {
            new Color(255, 255, 255).getRGB(),
            new Color(252, 244, 4).getRGB(),
            new Color(255, 100, 4).getRGB(),
            new Color(220, 8, 8).getRGB(),
            new Color(240, 8, 132).getRGB(),
            new Color(72, 0, 164).getRGB(),
            new Color(0, 0, 212).getRGB(),
            new Color(0, 172, 232).getRGB(),
            new Color(32, 184, 20).getRGB(),
            new Color(0, 100, 16).getRGB(),
            new Color(88, 44, 4).getRGB(),
            new Color(144, 112, 56).getRGB(),
            new Color(192, 192, 192).getRGB(),
            new Color(128, 128, 128).getRGB(),
            new Color(64, 64, 64).getRGB(),
            new Color(0, 0, 0).getRGB(),
    };

    public static int[] OITO_SOLIDAS = {
            0x000000, 0xFFFFFF, 0xFF0000, 0x00FF00,
            0x0000FF, 0xFFFF00, 0x00FFFF, 0xFF00FF
    };

    public static int[] R4G4B4 = criarR4G4B4();
    public static int[] R3G3B2 = criarR3G3B2();


    //12 bits
    private static int[] criarR4G4B4() {
        var rgb = new int[4096];
        var c = 0;
        for (var r = 0; r < 16; r++) {
            for (var g = 0; g < 16; g++) {
                for (var b = 0; b < 16; b++) {
                    var w4 = 255.0f / 15;
                    rgb[c++] = new Color(
                            (int)(r * w4),
                            (int)(g * w4),
                            (int)(b * w4)
                    ).getRGB();
                }
            }
        }
        return rgb;
    }

    private static int[] criarR3G3B2() {
        var rgb = new int[256];
        var c = 0;
        for (var r = 0; r < 8; r++) {
            for (var g = 0; g < 8; g++) {
                for (var b = 0; b < 4; b++) {
                    var w3 = 255.0f / 7;
                    var w2 = 255.0f / 3;
                    rgb[c++] = new Color(
                            (int)(r * w3),
                            (int)(g * w3),
                            (int)(b * w2)
                    ).getRGB();
                }
            }
        }
        return rgb;
    }
}
