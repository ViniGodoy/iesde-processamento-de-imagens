package cap2.parte1;

public class Exemplo1 {
    public static void main(String[] args) {
        var pixel = 0xFF3B9E03;

        //Imprime -12870141
        System.out.println(pixel);

        //Separa o pixel em suas componentes
        var a = (pixel >> 24) & 0xFF;
        var r = (pixel >> 16) & 0xFF;
        var g = (pixel >> 8) & 0xFF;
        var b = (pixel) & 0xFF;
        //Imprime A=255 R=59 G=158 B=03
        System.out.printf("A=%2d R=%02d G=%02d B=%02d%n", a, r, g, b);

        //Une as componentes novamente em um pixel:
        var cor = (a << 24) | (r << 16) | (g << 8) | b;

        //Imprime novamente -12870141
        System.out.println(cor);
    }
}
