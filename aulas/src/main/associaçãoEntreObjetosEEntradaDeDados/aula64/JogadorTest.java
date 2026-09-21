package associaçãoEntreObjetosEEntradaDeDados.aula64;

public class JogadorTest {
    public static void main(String[] args) {
        Jogador jogador1 = new Jogador("Leo");
        Jogador jogador2 = new Jogador("Felipe");
        Jogador jogador3 = new Jogador("Ze");

        Jogador[] jogadores = {jogador1, jogador2, jogador3};

        for (Jogador jogador : jogadores){
            jogador.imprimeNome();
        }
    }
}
