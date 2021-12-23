package cap5.parte1;

import static cap5.parte1.ProcImagem.*;
import static cap2.parte2.ProcImagem.*;
import static cap4.ProcImagem.*;
import static cap2.parte2.Util.*;

public class Main {
    public static void main(String[] args) {
        setSavePath("imagens/out/cap5");
        var lampada = escalaDeCinza(carregar("imagens/lampada"));
        lampada = limiarizacao(lampada, otsu(lampada));

        salvar("lampada", lampada);

        var resultado = conectados(lampada);
        var img = desenhar(resultado);


        salvar("fill", img);

    }
}
