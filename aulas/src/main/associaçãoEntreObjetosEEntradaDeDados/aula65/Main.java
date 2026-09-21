package associaçãoEntreObjetosEEntradaDeDados.aula65;

public class Main {
    public static void main(String[] args) {
        Time time = new Time("Vasco");
        Time time1 = new Time("Gremeio");
        Time time2 = new Time("Tricolor");

        Jogador jogador = new Jogador("neymar");
        jogador.setTime(time);
        jogador.imprime();

        Jogador jogador1 = new Jogador("Robinho");
        jogador1.setTime(time);
        jogador1.imprime();

        Jogador jogador2 = new Jogador("Kaka");
        jogador2.setTime(time);
        jogador2.imprime();

        Jogador jogador3 = new Jogador("Cafu");
        jogador3.imprime();
    }
}
