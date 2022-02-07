package cap6;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;

import static org.opencv.core.Core.*;
import static org.opencv.highgui.HighGui.*;
import static org.opencv.imgproc.Imgproc.*;
import static org.opencv.imgcodecs.Imgcodecs.*;

public class Cartoon {
    static {
        nu.pattern.OpenCV.loadLocally();
    }


    public static void main(String[] args) {
        var video = new VideoCapture();

        //Troque o nome do arquivo por 0 para usar a camera
        video.open("./imagens/in/video.mp4");

        if (!video.isOpened())
            throw new IllegalStateException("Não foi possível iniciar o vídeo!");

        var quadro = new Mat();
        var q = 0;
        while (video.read(quadro)) {
            //Suavização do quadro, gerando uma imagem mais simples
            var simples = new Mat();
            blur(quadro, simples, new Size(6,6));

            //Deteção das bordas
            var bordas = new Mat();
            var canny = new Mat();
            Canny(simples, canny, 50, 80);
            bitwise_not(canny, bordas);

            //Combinando as duas imagens
            var cartoon = new Mat();
            cvtColor(bordas, bordas, COLOR_GRAY2BGR);
            bitwise_and(bordas, simples, cartoon);

            //Exibe o resultado.
            imshow("Resultado", cartoon);
            if (waitKey(33) == 'X') break;

            //Salva o quadro em 3s para gerar as imagens presentes no capítulo
            if (q == 3*30) {
                imwrite("./imagens/out/cap6/f7_quadro.jpg", quadro);
                imwrite("./imagens/out/cap6/f7_simples.jpg", simples);
                imwrite("./imagens/out/cap6/f8_bordas.jpg", bordas);
                imwrite("./imagens/out/cap6/f8_canny.jpg", canny);
                imwrite("./imagens/out/cap6/f8_cartoon.jpg", cartoon);
            }
            q++;
        }
        video.release();
        System.exit(0);
    }
}
