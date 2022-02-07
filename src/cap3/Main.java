package cap3;

import static cap2.parte2.ProcImagem.*;
import static cap2.parte2.Util.*;
import static cap3.Kernels.*;
import static cap3.ProcImagem.*;

public class Main {
    public static void main(String[] args) {
        setSavePath("imagens/out/cap3");

        //Figura 9: Suavização
        var ruido = carregar("ruido");
        salvar("f9-suavizacao-gauss", convolucao(ruido, SUAVIZACAO_GAUSS, 5));
        //Figura 11: Bordas

        var lagarto = carregar("lagarto");
        var cinza = escalaDeCinza(lagarto);
        salvar("lagarto-cinza", cinza);

        var laplace = convolucao(cinza, BORDAS_LAPLACE);
        salvar( "f12-laplace", negativo(laplace));
        salvar("f12-laplace-limiar", negativo(limiarizacao(laplace, 15)));

        //Figura 12: Sobel
        var linhas = sobel(cinza);
        salvar("f14-sobel-gx", negativo(convolucao(cinza, BORDAS_SOBEL_GX)));
        salvar("f14-sobel-gy", negativo(convolucao(cinza, BORDAS_SOBEL_GY)));
        salvar("f14-sobel", negativo(linhas));

        var gatinho = carregar("gatinho2");
        salvar("f17-nitidez", convolucao(gatinho, NITIDEZ));


        //Figura 20: Erosão e dilatação
        salvar("f20-original", linhas);
        salvar("f20-erosao", erodir(linhas, CRUZ));
        salvar("f20-dilatar", dilatar(linhas, CRUZ));

        //Figura 21: Bordas com erosão, dilatação e subtração
        var erodida = erodir(cinza, 4, CRUZ);
        salvar("f21-original", cinza);
        salvar("f21-erodida", erodida);
        salvar("f21-erodida-bordas", subtrair(cinza, erodida));

        //Figura 23
        var dilatada = dilatar(cinza, 4, CRUZ);
        salvar("f21-dilatada", dilatada);
        salvar("f21-dilatada-bordas", subtrair(dilatada, cinza));

        //Figura 24: Limiarização do carro
        var carro = escalaDeCinza(carregar("carro"));
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
        var flores = carregar("flores");
        salvar("f28-alto_relevo", convolucao(flores, ALTO_RELEVO));
    }


}
