package associaçãoEntreObjetosEEntradaDeDados.aula67;

public class Time {
    private String nome;
    private Jogador[] jogadores;

    public void adicionarJogador(Jogador jogador){
        for (int i = 0; i < this.jogadores.length; i++) {
            if(jogadores[i] == null){
                jogadores[i] = jogador;
                jogador.setTime(this);
                return;
            }
        }
    }


    public void imprime(){
        System.out.println("-------------------");
        System.out.println("Nome: " + this.nome);
        System.out.println("-------------------");
        if(jogadores == null) return;
        for (Jogador jogador : jogadores){
            if (jogador == null) return;
            System.out.println("Jogador: " + jogador.getNome());

        }
    }
    public Time(String nome) {
        this.nome = nome;
    }

    public Time(String nome, Jogador[] jogadores) {
        this.nome = nome;
        this.jogadores = jogadores;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Jogador[] getJogadores() {
        return jogadores;
    }

    public void setJogadores(Jogador[] jogadores) {
        this.jogadores = jogadores;
    }
}
