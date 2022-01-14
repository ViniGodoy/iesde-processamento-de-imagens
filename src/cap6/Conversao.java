package cap6;

import org.opencv.core.Mat;
import static org.opencv.highgui.HighGui.*;
import static org.opencv.imgcodecs.Imgcodecs.*;
import static org.opencv.imgproc.Imgproc.*;


public class Conversao {
    static {
        nu.pattern.OpenCV.loadLocally();
    }

    public static void main(String[] args) {
        var img = imread("./imagens/gemeas.jpg");
        var cinza = new Mat();

        cvtColor(img, cinza, COLOR_BGR2GRAY);

        imshow("Escala de cinza", cinza);
        waitKey(0);
        destroyAllWindows();

        imwrite("./imagens/out/cap6/gemeasGray.jpg", cinza);
    }
}
