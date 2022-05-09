package cap5;

import cap3.Kernels;

import static cap5.ProcImagem.*;
import static cap2.parte2.ProcImagem.*;
import static cap4.ProcImagem.*;
import static cap2.parte2.Util.*;
import static cap3.ProcImagem.*;

public class Main {
    public static void main(String[] args) {
        setSavePath("imagens/out/cap5");

        var moedas = escalaDeCinza(carregar("moedas"));
        moedas = negativo(limiarizacao(moedas, otsu(moedas)));
        salvar("f1_moedas_limiar", moedas);

        moedas = fechamento(moedas,3,  Kernels.CRUZ);
        salvar("f2_moedas_fechamento", moedas);

        var resultado = segmentosConectados(moedas);
        var img = desenharSegmentos(resultado);

        salvar("f3_moedas_conectados", img);

        //Atividade 1
        var resultadoEx = segmentosConectadosEx(moedas);
        salvar("at1_moedas_conectados", desenharSegmentosEx(resultadoEx));
        System.out.println(resultadoEx.getSegmentos().size() + " segmentos detectados:");
        for (var s : resultadoEx.getSegmentos()) {
            System.out.println("  " + s);
        }

        var campo = carregar("campo");
        var knn = new Knn();
        //Gramado
        knn.adicionarAmostra(1, RGBtoVec3(90, 130, 63));
        knn.adicionarAmostra(1, RGBtoVec3(150, 193, 113 ));
        knn.adicionarAmostra(1, RGBtoVec3(41, 47, 39));
        knn.adicionarAmostra(1, RGBtoVec3(168, 166, 107));

        //Chao
        knn.adicionarAmostra(2, RGBtoVec3(244, 174, 129));
        knn.adicionarAmostra(2, RGBtoVec3(224, 151, 97 ));
        knn.adicionarAmostra(2, RGBtoVec3(230, 212, 191));

        //Ceu (incluindo núvens)
        knn.adicionarAmostra(3, RGBtoVec3(255, 254, 255));
        knn.adicionarAmostra(3, RGBtoVec3(127, 169, 210 ));
        knn.adicionarAmostra(3, RGBtoVec3(59, 130, 195));
        knn.adicionarAmostra(3, RGBtoVec3(196, 210, 231));

        salvar("fig6_knn_campo", desenharSegmentos(knn.classificar(campo, 1)));

        //Classificacao do campo com kmeans de 4 classes
        var kmeans = new Kmeans();
        System.out.println("Treinando...");
        kmeans.treinar(campo, 10);
        System.out.println("Classificando");

        salvar("fig9_kmeans_campo_centroide", kmeans.desenhar(campo));
        salvar("fig9_kmeans_campo_segmentos", desenharSegmentos(kmeans.classificar(campo)));
    }
}
