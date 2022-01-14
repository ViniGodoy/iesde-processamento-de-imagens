package cap6;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import static org.opencv.highgui.HighGui.*;

public class Video {
    static {
        nu.pattern.OpenCV.loadLocally();
    }

    public static void main(String[] args) {
        var video = new VideoCapture();
        //Troque o nome do arquivo por 0 para usar a camera
        video.open("./imagens/video.mp4");

        if (!video.isOpened())
            throw new IllegalStateException("Não foi possível iniciar o vídeo!");

        var quadro = new Mat();
        while (video.read(quadro)) {
            imshow("Video", quadro);
            if (waitKey(33) == 'X') break;
        }
        video.release();
        System.exit(0);
    }
}