package associaçãoEntreObjetosEEntradaDeDados.aula64;

public class Main {
    public static void main(String[] args) {

        Jogador jogador1 = new Jogador("Leo");
        Jogador jogador2 = new Jogador("jose");
        Jogador jogador3 = new Jogador("Maiano");
        Jogador jogador4 = new Jogador("Gabriel");

        Jogador[] jogadores = {jogador1,jogador2, jogador3, jogador4};
        Main main = new Main();
        main.encontrarJogador(jogadores, "Gabriel");



    }


    public void encontrarJogador(Jogador[] array, String nome){
        int index = 0;
        for (Jogador jogador: array){
            if(jogador.getNome().equals(nome)){
                System.out.println("Parabens achou o jogador: " + jogador.getNome());
                break;
            }
            System.out.println("Ainda nao achou o jogador index: " + index);
            index++;
        }
    }
}
