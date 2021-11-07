package cap2.parte2;

import static cap2.parte2.Util.*;
import static cap2.parte2.ProcImagem.*;
public class Main {
    public static void main(String[] args) {
        var img = carregar("imagens/renata.jpg");
        //Figura 4: Conversão para escala de cinza
        var cinza = escalaDeCinza(img);
        setSavePath("imagens/out/cap2");
        /*salvar("f4-renata-cinza-media", escalaDeCinzaMedia(img));
        salvar("f4-renata-cinza-rec601", escalaDeCinza(img));

        //Figura 5: Separação de canais
        salvar("f5-renata-cinza-r-cor", canalVermelho(img, true));
        salvar("f5-renata-cinza-g-cor", canalVerde(img, true));
        salvar("f5-renata-cinza-b-cor", canalAzul(img, true));

        salvar("f5-renata-cinza-r", canalVermelho(img, false));
        salvar("f5-renata-cinza-g", canalVerde(img, false));
        salvar("f5-renata-cinza-b", canalAzul(img, false));

        //Figura 6: Limiarização / binarização / thresholding
        salvar("f6-renata-limiar-70", limiarizacao(cinza, 70));
        salvar("f6-renata-limiar-100", limiarizacao(cinza, 100));
        salvar("f6-renata-limiar-130", limiarizacao(cinza, 130));

        //Figura 7: Correção gamma
        salvar("f7-renata-gamma-0.5", gamma(img, 0.5f));
        salvar("f7-renata-gamma-1.5", gamma(img, 1.5f));

        //Figura 9: Tinting
        Vector3 amarelo = new Vector3(209, 182, 123).div(255);
        Vector3 roxo = new Vector3(163, 109, 176).div(255);
        salvar("f9-renata-tint-amarelo", tingir(img, amarelo));
        salvar("f9-renata-tint-roxo", tingir(img, roxo));

        //Figura 10: Negativo
        salvar("f10-renata-negativo", negativo(img));

        //Figura 11: Detecção de movimento (subtração)
        var pets1 = escalaDeCinza(carregar("imagens/pets1.jpg"));
        var pets2 = escalaDeCinza(carregar("imagens/pets2.jpg"));
        var subtracao = subtrair(pets2, pets1);
        salvar("f11-pets-subtracao", negativo(subtracao));
        salvar("f11-pets-limiar", limiarizacao(subtracao, 18));
        */

        //Figura 12: Mistura (alpha blending)
        var img2 = carregar("imagens/ursinha.jpg");

        salvar("f12-renata-mistura-0", misturar(img2, img, 0f));
        salvar("f12-renata-mistura-25", misturar(img2, img, 0.25f));
        salvar("f12-renata-mistura-50", misturar(img2, img, 0.50f));
        salvar("f12-renata-mistura-75", misturar(img2, img, 0.75f));
        salvar("f12-renata-mistura-100", misturar(img2, img, 1f));

        //Figura 13: Soma de imagens
        var lampada1 = carregar("imagens/lampada.jpg");
        var lampada2 = carregar("imagens/lampada_desfoque.jpg");
        salvar("f13-lampada-soma", somar(lampada1, lampada2));

        //Figura 14: Cinza com marcador
        var marcador = carregar("imagens/marcador.jpg");
        salvar("f14-cinza-com-marcador", escalaDeCinzaComMarcador(img2, marcador));

        //Atividade 1: Espelhar
        salvar("f15-espelhar-horizontal", espelhar(img, true, false));
        salvar("f15-espelhar-vertical", espelhar(img, false, true));
        salvar("f15-espelhar-ambos", espelhar(img, true, true));

        //Atividade 2: Separar - gera o marcador para as áreas escuras da imagem
        salvar("f16-separar", marcarSe(img2, p -> p.dot(REC601) <= 0.15));
    }
}
