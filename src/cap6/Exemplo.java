package cap6;

import static org.opencv.core.CvType.*;
import org.opencv.core.Mat;

public class Exemplo {
    static {
        nu.pattern.OpenCV.loadLocally();
    }

    public static void main(String[] args) {
        var mat = Mat.eye(3, 3, CV_8UC1);
        System.out.println(mat.dump());
    }
}
