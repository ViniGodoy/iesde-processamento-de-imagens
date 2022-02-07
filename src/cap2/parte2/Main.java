package cap2.parte2;

import static cap2.parte2.ProcImagem.*;
import static cap2.parte2.Util.*;

public class Main {
    public static void main(String[] args) {
        setSavePath("imagens/out/cap2");
        //Figura 4: Conversão para escala de cinza
        var peixes = carregar("peixes");
        var cinza = escalaDeCinza(peixes);

        salvar("f4-cinza-media", escalaDeCinzaMedia(peixes));
        salvar("f4-cinza-rec601", cinza);

        //Figura 5: Separação de canais
        salvar("f5-cinza-r-cor", canalVermelho(peixes, true));
        salvar("f5-cinza-g-cor", canalVerde(peixes, true));
        salvar("f5-cinza-b-cor", canalAzul(peixes, true));

        salvar("f5-cinza-r", canalVermelho(peixes, false));
        salvar("f5-cinza-g", canalVerde(peixes, false));
        salvar("f5-cinza-b", canalAzul(peixes, false));

        //Figura 6: Limiarização / binarização / thresholding
        salvar("f6-limiar-70", limiarizacao(cinza, 70));
        salvar("f6-limiar-100", limiarizacao(cinza, 100));
        salvar("f6-limiar-130", limiarizacao(cinza, 130));

        //Figura 7: Correção gamma
        salvar("f7-gamma-0.5", gamma(peixes, 0.5f));
        salvar("f7-gamma-1.5", gamma(peixes, 1.5f));

        //Figura 9: Tinting
        var img1 = carregar("gatinho");
        Vector3 amarelo = new Vector3(209, 182, 123).div(255);
        Vector3 roxo = new Vector3(163, 109, 176).div(255);
        salvar("f9-tint-amarelo", tingir(img1, amarelo));
        salvar("f9-tint-roxo", tingir(img1, roxo));

        //Figura 10: Negativo
        salvar("f10-negativo", negativo(img1));

        //Figura 11: Detecção de movimento (subtração)
        var pets1 = escalaDeCinza(carregar("pets1"));
        var pets2 = escalaDeCinza(carregar("pets2"));
        var subtracao = subtrair(pets2, pets1);
        salvar("f11-subtracao", negativo(subtracao));
        salvar("f11-limiar", limiarizacao(subtracao, 18));


        //Figura 12: Mistura (alpha blending)
        var img2 = carregar("cachorrinho");
        salvar("f12-mistura-0", misturar(img1, img2, 0f));
        salvar("f12-mistura-25", misturar(img1, img2,  0.25f));
        salvar("f12-mistura-50", misturar(img1, img2,  0.50f));
        salvar("f12-mistura-75", misturar(img1, img2,  0.75f));
        salvar("f12-mistura-100", misturar(img1, img2,  1f));

        //Figura 13: Soma de imagens
        var lampada1 = carregar("lampada");
        var lampada2 = carregar("lampada_desfoque");
        salvar("f13-soma", somar(lampada1, lampada2));

        //Figura 14: Cinza com marcador
        var porquinho = carregar("porquinho");
        var mascara = carregar("mascara");
        salvar("f14-cinza-com-mascara", escalaDeCinzaComMascara(porquinho, mascara));

        //Atividade 1: Espelhar
        salvar("f15-espelhar-horizontal", espelhar(img2, true, false));
        salvar("f15-espelhar-vertical", espelhar(img2, false, true));
        salvar("f15-espelhar-ambos", espelhar(img2, true, true));

        //Atividade 2: Separar - gera a mascara para as áreas escuras da imagem
        salvar("f16-separar", marcarSe(peixes, p -> p.dot(REC601) <= 0.15));

    }
}
