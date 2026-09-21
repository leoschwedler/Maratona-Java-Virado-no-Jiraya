package encapsulamento_sobrecarga_construtores.aula61;

public class Jogo {
    private String jogador;
    private int pontos;
    private static  int jogadoresCriados;
    public static int pontuacaoMaxima;

    public Jogo(String jogador, int pontos) {
        this.jogador = jogador;
        this.pontos = pontos;
        jogadoresCriados++;
    }
}
