package cap3;

import java.awt.image.BufferedImage;

import cap2.parte2.Util;
import cap2.parte2.Vector3;
import static cap2.parte2.Vector3.*;

import static cap2.parte2.ProcImagem.*;
import static cap3.Kernels.CRUZ;

public class ProcImagem {
    //---------------------
    //Filtragem com kernels
    //---------------------
    private static int espelharIndice(int idx, int limit) {
        if (idx < 0) return -idx;
        if (idx >= limit) return limit-(idx-limit+1);
        return idx;
    }

    /**
     * Aplica o processo de convolução na imagem. Isto é, para cada pixel, calcula a média ponderada com seus vinzinhos.
     * Os pixels da borda serão espelhados
     * @param img Imagem onde a convolução será realizada
     * @param kernel Uma matriz 3x3 com os pesos
     * @return A imagem filtrada
     * @see Kernels Enumeraçõa com kernels comuns.
     */
    public static BufferedImage convolucao(BufferedImage img, float[][] kernel) {
        //Cria a imagem de saída
        var out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        //Percorre a imagem de entrada
        for (var y = 0; y < img.getHeight(); y++) {
            for (var x = 0; x < img.getWidth(); x++) {
                //Valores de r, g e b finais
                Vector3 outPixel = new Vector3();
                //Para cada pixel percorrido na imagem, precisamos percorrer os seus 9 vizinhos
                //O peso desses vizinhos estão descritos no kernel, por isso, fazemos um for para percorre-lo
                for (var ky = 0; ky < 3; ky++) {
                    for (var kx = 0; kx < 3; kx++) {
                        //Observe que os índices de kx e ky variam de 0 até 2. Já os vizinhos de x seriam
                        //x+(-1), x+0 + x+1. Por isso, subtraímos 1 de kx e ky para chegar no vizinho.
                        var px = espelharIndice(x + (kx-1), img.getWidth());
                        var py = espelharIndice(y + (ky-1), img.getHeight());

                        //Obtemos o pixel vizinho
                        var pixel = RGBtoVec3(img.getRGB(px, py));
                        //E somamos ele as cores finais multiplicadas pelo seu respectivo peso no kernel
                        outPixel.add(pixel.mul(kernel[kx][ky]));
                    }
                }

                //Calculamos a cor final
                out.setRGB(x, y, vec3ToRGB(outPixel));
            }
        }
        return out;
    }

    public static BufferedImage convolucao(BufferedImage img, float [][] kernel, int vezes) {
        for (var i = 0; i < vezes; i++) {
            img = convolucao(img, kernel);
        }
        return img;
    }

    //Atividade 2: Filtro de bordas
    public static BufferedImage filtroGradientes(BufferedImage img, float[][] gx, float[][] gy) {
        var imgGx = convolucao(img, gx);
        var imgGy = convolucao(img, gy);

        return processar(imgGx, imgGy, ((p1, p2) -> {
            p1.mul(p1); //Eleva ao quadrado
            p2.mul(p2); //Eleva ao quadrado
            return sqrt(add(p1, p2)); //Raiz(p1 + p2)
        }));
    }

    public static BufferedImage sobel(BufferedImage img) {
        return filtroGradientes(img, Kernels.BORDAS_SOBEL_GX, Kernels.BORDAS_SOBEL_GY);
    }

    //-----------------------
    //Morfologia matemática
    //-----------------------

    /**
     * Aplica o processo de erosão morfológica na imagem, considerando a luminância do pixel.
     *
     * @param img Imagem para erodir
     * @param kernel Pixels para utilizar no processo. Utilize nulo para cruz.
     * @return A imagem erodida
     * @see Kernels
     */
    public static BufferedImage erodir(BufferedImage img, boolean[][] kernel) {
        //Cria a imagem de saída
        var out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        kernel = kernel == null ? CRUZ : kernel;
        //Percorre a imagem de entrada
        for (var y = 0; y < img.getHeight(); y++) {
            for (var x = 0; x < img.getWidth(); x++) {
                //A erosão busca pelo pixel de menor valor
                var min = 1000.0f;
                //Para cada pixel percorrido na imagem, precisamos percorrer os seus 9 vizinhos
                //Os vizinhos que serão considerados estão marcados como true no kernel
                for (var ky = 0; ky < 3; ky++) {
                    for (var kx = 0; kx < 3; kx++) {
                        //Observe que os índices de kx e ky variam de 0 até 2. Já os vizinhos de x seriam
                        //x+(-1), x+0 + x+1. Por isso, subtraímos 1 de kx e ky para chegar no vizinho.
                        var px = x + (kx-1);
                        var py = y + (ky-1);

                        //Nas bordas, px ou py podem acabar caindo fora da imagem. Quando isso ocorre, pulamos para o
                        // próximo pixel.
                        if (px < 0 || px >= img.getWidth() || py < 0 || py >= img.getHeight()) {
                            continue;
                        }

                        //Obtem o tom de cinza do pixel
                        var pixel = RGBtoVec3(img.getRGB(px, py));
                        var l = pixel.dot(REC601);

                        //Se ele for mais escuro que o menor já encontrado, substitui
                        if (kernel[kx][ky] && l < min) {
                            min = l;
                        }
                    }
                }

                //Define essa cor na imagem de saída.
                out.setRGB(x, y, vec3ToRGB(new Vector3(min)));
            }
        }
        return out;
    }

    /**
     * Aplica o processo de erosão morfológica na imagem, considerando a luminância do pixel.
     *
     * @param img Imagem para erodir
     * @param vezes Numero de vezes para erodir
     * @param kernel Pixels para utilizar no processo. Utilize nulo para cruz.
     * @return A imagem erodida
     * @see Kernels
     */
    public static BufferedImage erodir(BufferedImage img, int vezes, boolean[][] kernel) {
        var out = img;
        for (var i = 0; i < vezes; i++) {
            out = erodir(out, kernel);
        }
        return out;
    }

    /**
     * Applies morphological dilation in the image, based in pixel luminance.
     *
     * @param img The image to dilate.
     * @param kernel Pixels to use in the process (structuring element)
     * @return The dilated image.
     * @see Kernels
     */
    public static BufferedImage dilatar(BufferedImage img, boolean[][] kernel) {
        //Cria a imagem de saída
        var out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        kernel = kernel == null ? CRUZ : kernel;

        //Percorre a imagem de entrada
        for (var y = 0; y < img.getHeight(); y++) {
            for (var x = 0; x < img.getWidth(); x++) {
                //A dilatação busca pelo pixel de maior valor
                var max = -1.0f;

                //Para cada pixel percorrido na imagem, precisamos percorrer os seus 9 vizinhos
                //Os vizinhos que serão considerados estão marcados como true no kernel
                for (var ky = 0; ky < 3; ky++) {
                    for (var kx = 0; kx < 3; kx++) {
                        //Observe que os índices de kx e ky variam de 0 até 2. Já os vizinhos de x seriam
                        //x+(-1), x+0 + x+1. Por isso, subtraímos 1 de kx e ky para chegar no vizinho.
                        var px = x + (kx-1);
                        var py = y + (ky-1);

                        //Nas bordas, px ou py podem acabar caindo fora da imagem. Quando isso ocorre, pulamos para o
                        // próximo pixel.
                        if (px < 0 || px >= img.getWidth() || py < 0 || py >= img.getHeight()) {
                            continue;
                        }

                        //Obtem o tom de cinza do pixel
                        var pixel = RGBtoVec3(img.getRGB(px, py));
                        float l = pixel.dot(REC601);

                        //Se ele for mais claro que o maior já encontrado, substitui
                        if (kernel[kx][ky] && l > max) {
                            max = l;
                        }
                    }
                }

                //Define essa cor na imagem de saída.
                out.setRGB(x, y, vec3ToRGB(new Vector3(max)));
            }
        }
        return out;
    }

    /**
     * Applies morphological dilation in the image, based in pixel luminance.
     *
     * @param img The image to dilate.
     * @param times Number of time to dilate.
     * @param kernel Pixels to use in the process (structuring element)
     * @return The dilated image.
     * @see Kernels
     */
    public static BufferedImage dilatar(BufferedImage img, int times, boolean[][] kernel) {
        BufferedImage out = img;
        for (int i = 0; i < times; i++) {
            out = dilatar(out, kernel);
        }
        return out;
    }

    /**
     * Applies morphological opening in the image, based in pixel luminance.
     *
     * The opening is a sequence of erosions followed by a same size sequence of dilations.
     *
     * @param img The image to open.
     * @param times Number of times to apply
     * @param kernel Pixels to use in the process (structuring element)
     * @return The opened image
     * @see Kernels
     */
    public static BufferedImage abertura(BufferedImage img, int times, boolean[][] kernel) {
        return dilatar(erodir(img, times, kernel), times, kernel);
    }

    /**
     * Applies morphological closing in the image, based in pixel luminance.
     *
     * The closing is a sequence of dilations followed by a same size sequence of erosions.
     *
     * @param img The image to open.
     * @param times Number of times to apply
     * @param kernel Pixels to use in the process (structuring element)
     * @return The opened image
     * @see Kernels
     */
    public static BufferedImage fechamento(BufferedImage img, int times, boolean[][] kernel) {
        return erodir(dilatar(img, times, kernel), times, kernel);
    }

    /**
     * Testa se as duas imagens são idênticas
     * @param img1 Imagem 1
     * @param img2 Imagem 2
     * @return Verdadeiro se forem idênticas
     */
    public static boolean equals(BufferedImage img1, BufferedImage img2) {
        if (img1.getWidth() != img2.getWidth()) return false;
        if (img1.getHeight() != img2.getHeight()) return false;

        for (var y = 0; y < img1.getHeight(); y++) {
            for (var x = 0; x < img1.getWidth(); x++) {
                if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Aplica o processo de reconstrução morfológica
     * @param img Imagem a reconstruir
     * @param mascara Mascara (geralmente a imagem original)
     * @return A imagem reconstruída
     */
    public static BufferedImage reconstruir(BufferedImage img, BufferedImage mascara) {
        var r = img;
        while (true) {
            var d = dilatar(r, 1, CRUZ);
            var m = processar(d, mascara, (pd, pm) -> pm.x < pd.x ? pm : pd);
            if (equals(r, m)) break;
            r = m;
        }
        return r;
    }
}
