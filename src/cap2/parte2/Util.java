package cap2.parte2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Util {
    private static File savePath = new File("./imagens/out");
    private static File loadPath = new File("./imagens/in");

    public static void setSavePath(String savePath) {
        Util.savePath = savePath == null ? new File(".") : new File(savePath);
    }

    public static void setLoadPath(String loadPath) {
        Util.loadPath = loadPath == null ? new File(".") : new File(loadPath);
    }

    public static final float EPSILON = 1.0f / 256.0f;

    /**
     * Compara dois floats e os considera iguais se sua diferença for menor que o intervalo de erro (epsilon)
     * @param v1 float 1
     * @param v2 float 2
     * @param epsilon Intervalo de erro
     * @return true se os dois floats forem considerados iguais
     */
    public static boolean floatEquals(float v1, float v2, float epsilon) {
        return Math.abs(v1 - v2) <= epsilon;
    }

    /**
     * Compara dois floats e os considera iguais se sua diferença for menor que o intervalo de erro dado por EPSILON.
     * @param v1 float 1
     * @param v2 float 2
     * @return true se os dois floats forem considerados iguais
     */
    public static boolean floatEquals(float v1, float v2) {
        return floatEquals(v1, v2, EPSILON);
    }

    /**
     * Garante que o valor estará dentro do intervalo definido por min e max. Caso o valor esteja fora do intervalo,
     * o extremo mais próximo será usado. Caso contrário, o próprio valor é retornado.
     *
     * @param v Valor
     * @param min Valor mínimo do intervalo
     * @param max Valor máximo do intervalo
     * @return O valor ajustado
     */
    public static float clamp(float v, float min, float max) {
        return v < min ? min : (v > max ? max : v);
    }

    /**
     * Carrega a imagem
     * @param name O nome do arquivo
     * @return A imagem corregada
     * @throws RuntimeException Se a imagem não puder ser carregada
     */
    public static BufferedImage carregar(String name) {
        try {
            if (!name.contains(".")) name += ".jpg";
            return ImageIO.read(new File(loadPath, name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Salva a imagem.
     * @param name O nome sem a extensão
     * @param formato O tipo do arquivo de imagem (jpg, png, etc)
     * @param img A imagem a ser salva.
     * @throws RuntimeException Se a imagem não puder ser salva
     */
    public static void salvar(String name, String formato, BufferedImage img) {
        try {
            if (!savePath.exists()) savePath.mkdirs();
            var file = new File(savePath,name + "." + formato);
            ImageIO.write(img, formato, file);
            System.out.println("Salvo: " + file.getPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Salva a imagem no formato png. Em caso de falha, exibe o erro e aborta o programa.
     * @param name O nome sem a extensão
     * @param img A imagem a ser salva.
     * @throws RuntimeException Se a imagem não puder ser salva
     */
    public static void salvar(String name, BufferedImage img) {
        salvar(name, "jpg", img);
    }
}
