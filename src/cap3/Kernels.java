package cap3;

public class Kernels {
    public static float[][] SUAVIZACAO_CRUZ = new float[][] {
            {   0.0f, 1f / 5f,    0.0f},
            {1f / 5f, 1f / 5f, 1f / 5f},
            {   0.0f, 1f / 5f,    0.0f}
    };

    public static float[][] SUAVIZACAO_QUADRADO = new float[][] {
            {1f / 9f, 1f / 9f, 1f / 9f},
            {1f / 9f, 1f / 9f, 1f / 9f},
            {1f / 9f, 1f / 9f, 1f / 9f}
    };

    public static float[][] SUAVIZACAO_PONDERADA = new float[][] {
            {1f / 14f, 2f / 14f, 1f / 14f},
            {2f / 14f, 2f / 14f, 2f / 14f},
            {1f / 14f, 2f / 14f, 1f / 14f}
    };

    public static float[][] SUAVIZACAO_GAUSS = new float[][] {
            {1f / 16f, 2f / 16f, 1f / 16f},
            {2f / 16f, 4f / 16f, 2f / 16f},
            {1f / 16f, 2f / 16f, 1f / 16f}
    };

    public static float[][] BORDAS_LAPLACE = new float[][] {
            { 0.0f,  1.0f, 0.0f},
            { 1.0f, -4.0f, 1.0f},
            { 0.0f,  1.0f, 0.0f},
    };

    public static float[][] BORDAS_LAPLACE_DIAGONALS = new float[][] {
            { 0.5f,  1.0f, 0.5f},
            { 1.0f, -6.0f, 1.0f},
            { 0.5f,  1.0f, 0.5f},
    };

    public static float[][] BORDAS_SOBEL_GX = new float[][] {
            { -1.0f, 0.0f,  1.0f},
            { -2.0f, 0.0f,  2.0f},
            { -1.0f, 0.0f,  1.0f},
    };

    public static float[][] BORDAS_SOBEL_GY = new float[][] {
            {  1.0f,  2.0f,  1.0f},
            {  0.0f,  0.0f,  0.0f},
            { -1.0f, -2.0f, -1.0f},
    };

    public static float[][] BORDAS_PREWITT_GX = new float[][] {
            { -1.0f, 0.0f, 1.0f},
            { -1.0f, 0.0f, 1.0f},
            { -1.0f, 0.0f, 1.0f},
    };

    public static float[][] BORDAS_PREWITT_GY = new float[][] {
            {  1.0f,  1.0f,  1.0f},
            {  0.0f,  0.0f,  0.0f},
            { -1.0f, -1.0f, -1.0f},
    };

    public static float[][] NITIDEZ = new float[][] {
            { 0.0f, -1.0f, 0.0f},
            {-1.0f,  5.0f,-1.0f},
            { 0.0f, -1.0f, 0.0f},
    };

    public static float[][] ALTO_RELEVO = new float[][] {
            { -2.0f, -1.0f, 0.0f},
            { -1.0f,  1.0f, 1.0f},
            {  0.0f,  1.0f, 2.0f},
    };

    //--------------------------------
    //Kernels da morfologia matemática
    //--------------------------------

    public static boolean[][] CRUZ = new boolean[][] {
            {false, true, false},
            {true, true, true},
            {false, true, false}
    };

    public static boolean[][] QUADRADO = new boolean[][] {
            {true, true, true},
            {true, true, true},
            {true, true, true}
    };

    public static boolean[][] LINHA_VERTICAL = new boolean[][] {
            {false, true, false},
            {false, true, false},
            {false, true, false}
    };

    public static boolean[][] LINHA_HORIZONTAL = new boolean[][] {
            {false, false, false},
            {true, true, true},
            {false, false, false}
    };
}