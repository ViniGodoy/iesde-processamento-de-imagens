package cap2.parte2;

import static cap2.parte2.ProcImagem.*;
import static cap2.parte2.Util.*;

public class Main {
    public static void main(String[] args) {
        setSavePath("imagens/out/cap2");
        //Figura 4: Conversão para escala de cinza
        var retrato = carregar("imagens/retrato");
        salvar("f4-retrato-cinza-media", escalaDeCinzaMedia(retrato));
        salvar("f4-retrato-cinza-rec601", escalaDeCinza(retrato));

        //Figura 5: Separação de canais
        salvar("f5-retrato-cinza-r-cor", canalVermelho(retrato, true));
        salvar("f5-retrato-cinza-g-cor", canalVerde(retrato, true));
        salvar("f5-retrato-cinza-b-cor", canalAzul(retrato, true));

        salvar("f5-retrato-cinza-r", canalVermelho(retrato, false));
        salvar("f5-retrato-cinza-g", canalVerde(retrato, false));
        salvar("f5-retrato-cinza-b", canalAzul(retrato, false));

        //Figura 6: Limiarização / binarização / thresholding
        var renata = carregar("imagens/renata");
        var cinza = escalaDeCinza(renata);
        salvar("f6-renata-limiar-70", limiarizacao(cinza, 70));
        salvar("f6-renata-limiar-100", limiarizacao(cinza, 100));
        salvar("f6-renata-limiar-130", limiarizacao(cinza, 130));

        //Figura 7: Correção gamma
        salvar("f7-renata-gamma-0.5", gamma(renata, 0.5f));
        salvar("f7-renata-gamma-1.5", gamma(renata, 1.5f));

        //Figura 9: Tinting
        Vector3 amarelo = new Vector3(209, 182, 123).div(255);
        Vector3 roxo = new Vector3(163, 109, 176).div(255);
        salvar("f9-renata-tint-amarelo", tingir(renata, amarelo));
        salvar("f9-renata-tint-roxo", tingir(renata, roxo));

        //Figura 10: Negativo
        salvar("f10-renata-negativo", negativo(renata));

        //Figura 11: Detecção de movimento (subtração)
        var pets1 = escalaDeCinza(carregar("imagens/pets1"));
        var pets2 = escalaDeCinza(carregar("imagens/pets2"));
        var subtracao = subtrair(pets2, pets1);
        salvar("f11-pets-subtracao", negativo(subtracao));
        salvar("f11-pets-limiar", limiarizacao(subtracao, 18));


        //Figura 12: Mistura (alpha blending)
        var img2 = carregar("imagens/ursinha");
        salvar("f12-renata-mistura-0", misturar(img2, renata, 0f));
        salvar("f12-renata-mistura-25", misturar(img2, renata, 0.25f));
        salvar("f12-renata-mistura-50", misturar(img2, renata, 0.50f));
        salvar("f12-renata-mistura-75", misturar(img2, renata, 0.75f));
        salvar("f12-renata-mistura-100", misturar(img2, renata, 1f));

        //Figura 13: Soma de imagens
        var lampada1 = carregar("imagens/lampada");
        var lampada2 = carregar("imagens/lampada_desfoque");
        salvar("f13-lampada-soma", somar(lampada1, lampada2));

        //Figura 14: Cinza com marcador
        var marcador = carregar("imagens/marcador");
        salvar("f14-cinza-com-marcador", escalaDeCinzaComMarcador(img2, marcador));

        //Atividade 1: Espelhar
        salvar("f15-espelhar-horizontal", espelhar(renata, true, false));
        salvar("f15-espelhar-vertical", espelhar(renata, false, true));
        salvar("f15-espelhar-ambos", espelhar(renata, true, true));

        //Atividade 2: Separar - gera o marcador para as áreas escuras da imagem
        salvar("f16-separar", marcarSe(img2, p -> p.dot(REC601) <= 0.15));

    }
}
