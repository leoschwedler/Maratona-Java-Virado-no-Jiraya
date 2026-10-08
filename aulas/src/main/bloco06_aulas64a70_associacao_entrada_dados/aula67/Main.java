package bloco06_aulas64a70_associacao_entrada_dados.aula67;

public class Main {
    public static void main(String[] args) {
        Jogador jogador = new Jogador("Pele");
        Jogador neymar = new Jogador("Neymar");

        Time time = new Time("Brasil", new Jogador[10]);
        time.adicionarJogador(jogador);
        time.imprime();
        jogador.imprimir();


    }
}
