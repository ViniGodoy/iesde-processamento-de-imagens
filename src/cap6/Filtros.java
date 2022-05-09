package cap6;

import org.opencv.core.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.opencv.imgcodecs.Imgcodecs.*;
import static org.opencv.imgproc.Imgproc.*;
import static org.opencv.core.Core.*;

public class Filtros {
    static {
        nu.pattern.OpenCV.loadLocally();
    }

    public static void main(String[] args) {
        //Limiarização
        var renata = imread("./imagens/in/castelo.jpg");
        var cinza = new Mat();
        cvtColor(renata, cinza, COLOR_BGR2GRAY);
        imwrite("./imagens/out/cap6/f1_cinza.jpg", cinza);

        var limiar = new Mat();
        threshold(cinza, limiar, 130, 255, THRESH_TOZERO);
        imwrite("./imagens/out/cap6/f1_limiar_130.jpg", limiar);

        var t = threshold(cinza, limiar, 0, 255, THRESH_BINARY + THRESH_OTSU);
        imwrite("./imagens/out/cap6/f1_limiar_otsu.jpg", limiar);
        System.out.println("Treshold utilizado: " + t);

        adaptiveThreshold(cinza, limiar, 255, ADAPTIVE_THRESH_GAUSSIAN_C, THRESH_BINARY, 11, 4);
        imwrite("./imagens/out/cap6/f2_limiar_adaptativo.jpg", limiar);

        //Morfologia matemática
        var gemeas = imread("./imagens/in/lagarto.jpg");

        var dilatada = new Mat();
        var erodida = new Mat();

        var cruz = getStructuringElement(MORPH_CROSS, new Size(5,5));
        var centro = new Point(-1, -1);
        morphologyEx(gemeas, dilatada, MORPH_DILATE, cruz, centro, 3);
        morphologyEx(gemeas, erodida, MORPH_ERODE, cruz, centro, 3);

        imwrite("./imagens/out/cap6/f3_dilatada.jpg", dilatada);
        imwrite("./imagens/out/cap6/f3_erodida.jpg", erodida);

        //Histograma
        var frutas = imread("./imagens/in/frutas2.jpg");
        //1. Converte para HSV
        var hsv = new Mat();
        cvtColor(frutas,hsv,COLOR_BGR2HSV);
        //2. Separa os canais
        var canais = new ArrayList<Mat>();
        split(hsv, canais);
        //3. Equaliza o canal V. Note que H=0, S=1, V=2
        equalizeHist(canais.get(2), canais.get(2));
        //4. Une novamente os canais
        merge(canais, hsv);
        //5. Gera a imagem equalizada em BGR
        var equalizada = new Mat();
        cvtColor(hsv,equalizada,COLOR_HSV2BGR);
        imwrite("./imagens/out/cap6/f4_equalizada.jpg", equalizada);

        //Calculo do histograma
        var histograma = new Mat();
        calcHist(List.of(frutas),
            new MatOfInt(0),
            new Mat(),
            histograma,
            new MatOfInt(256), //Subpartes
            new MatOfFloat(0f, 255f) //Intervalo
        );
        System.out.println(histograma.dump());

        //Segmentos conectados
        var imagem = imread("./imagens/in/moedas.jpg");

        //Simplifica a imagem com o filtro de Canny
        var canny = new Mat();
        var fechado = new Mat();
        Canny(imagem, canny, 100, 150);
        morphologyEx(canny, fechado, MORPH_CLOSE, cruz, centro, 12);

        //Localiza os contornos
        var contornos = new ArrayList<MatOfPoint>();
        var hierarquia = new Mat();
        findContours(fechado, contornos, hierarquia, RETR_TREE, CHAIN_APPROX_SIMPLE);

        //Busca os retângulos
        var poligonos  = new MatOfPoint2f[contornos.size()];
        var retangulos = new RotatedRect[contornos.size()];
        for (var i = 0; i < contornos.size(); i++) {
            var contorno = new MatOfPoint2f(contornos.get(i).toArray());
            poligonos[i] = new MatOfPoint2f();
            approxPolyDP(contorno, poligonos[i], 3, true);
            retangulos[i] = minAreaRect(contorno);
        }

        //Desenha o resultado
        var desenho = Mat.zeros(fechado.size(), CvType.CV_8UC3);
        var rnd = new Random();
        for (var i = 0; i < contornos.size(); i++) {
            //Sorteia uma cor
            var cor = new Scalar(
                rnd.nextInt(256),
                rnd.nextInt(256),
                rnd.nextInt(256)
            );

            //Desenha o contorno
            drawContours(desenho, contornos, i, cor);

            //Desenha as 4 linhas do retângulo
            //e um circulo no seu centro
            var rectPoints = new Point[4];
            retangulos[i].points(rectPoints);
            circle(desenho, retangulos[i].center, 2, cor);
            for (var j = 0; j < 4; j++) {
                line(desenho, rectPoints[j], rectPoints[(j+1) % 4], cor);
            }
        }

        imwrite("./imagens/out/cap6/f5_moedas_canny.jpg", canny);
        imwrite("./imagens/out/cap6/f5_moedas_canny_fechado.jpg", fechado);
        imwrite("./imagens/out/cap6/f6_moedas_desenho.jpg", desenho);

        //Atividade 2
        //1. Converte para YCrCb
        var yCrCb = new Mat();
        cvtColor(frutas,yCrCb,COLOR_BGR2YCrCb);
        //2. Separa os canais
        canais = new ArrayList<>();
        split(yCrCb, canais);
        //3. Equaliza o canal Y. Note que Y=0, Cr=1, Cb=2
        equalizeHist(canais.get(0), canais.get(0));
        //4. Une novamente os canais
        merge(canais, yCrCb);
        //5. Gera a imagem equalizada em BGR
        equalizada = new Mat();
        cvtColor(yCrCb,equalizada,COLOR_YCrCb2BGR);
        imwrite("./imagens/out/cap6/at2_equalizada_yCrBb.jpg", equalizada);
    }
}
