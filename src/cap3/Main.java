package cap3;

import static cap2.parte2.ProcImagem.*;
import static cap2.parte2.Util.*;
import static cap3.Kernels.*;
import static cap3.ProcImagem.*;

public class Main {
    public static void main(String[] args) {
        setSavePath("imagens/out/cap3");

        var retrato = carregar("imagens/retrato");
        var cinza = escalaDeCinza(retrato);
        salvar("retrato-cinza", cinza);

        //Figura 9: Suavização
        var ruido = carregar("imagens/ruido");
        salvar("f9-suavizacao-gauss", convolucao(ruido, SUAVIZACAO_GAUSS, 5));
        //Figura 11: Bordas

        var laplace = convolucao(cinza, BORDAS_LAPLACE);
        salvar( "f12-retrato-laplace", negativo(laplace));
        salvar("f12-retrato-laplace-limiar", negativo(limiarizacao(laplace, 15)));

        //Figura 12: Sobel
        salvar("f14-retrato-sobel-gx", negativo(convolucao(cinza, BORDAS_SOBEL_GX)));
        salvar("f14-retrato-sobel-gy", negativo(convolucao(cinza, BORDAS_SOBEL_GY)));
        salvar("f14-retrato-sobel", negativo(sobel(cinza)));

        var ursinha = carregar("imagens/ursinha");
        salvar("f17-ursinha-nitidez", convolucao(ursinha, NITIDEZ));


        //Figura 20: Erosão e dilatação
        var renata = escalaDeCinza(carregar("imagens/renata"));
        var linhas = limiarizacao(sobel(renata), 60);
        salvar("f20-renata-original", linhas);
        salvar("f20-renata-erosao", erodir(linhas, CRUZ));
        salvar("f20-renata-dilatar", dilatar(linhas, CRUZ));

        //Figura 21: Bordas com erosão, dilatação e subtração
        var erodida = erodir(renata, 4, CRUZ);
        salvar("f21-renata-original", renata);
        salvar("f21-renata-erodida", erodida);
        salvar("f21-renata-erodida-bordas", subtrair(renata, erodida));

        //Figura 23
        var dilatada = dilatar(renata, 4, CRUZ);
        salvar("f21-renata-dilatada", dilatada);
        salvar("f21-renata-dilatada-bordas", subtrair(dilatada, renata));

        //Figura 24: Limiarização do carro
        var carro = escalaDeCinza(carregar("imagens/carro"));
        var limiarCarro = limiarizacao(carro, 200);
        salvar("f24-carro-cinza", carro);
        salvar("f24-carro-limiar", limiarCarro);

        //Figura 25: Abertura
        salvar("f25-carro-abertura", abertura(limiarCarro, 1, CRUZ));

        //Figura 26: Fechamento
        salvar("f26-carro-fechamento", fechamento(limiarCarro, 3, CRUZ));

        //Figura 27: Reconstrução
        var carroAberto = erodir(limiarCarro, 8, QUADRADO);
        salvar("f27-carro-abertura-forte", carroAberto);
        salvar("f27-carro-reconstruido", reconstruir(carroAberto, limiarCarro));

        //Atividade 1:
        renata = carregar("imagens/renata");
        salvar("f28-alto_relevo", convolucao(renata, ALTO_RELEVO));
    }


}
