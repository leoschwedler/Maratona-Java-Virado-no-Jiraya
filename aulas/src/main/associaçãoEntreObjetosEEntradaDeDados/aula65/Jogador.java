package associaçãoEntreObjetosEEntradaDeDados.aula65;

public class Jogador {
    private String nome;
    private Time time;

    void imprime(){
        System.out.println("Nome: " + this.nome);
        if(this.time == null){
            System.out.println("Jogador sem time");
            return;
        }
        System.out.println("Time: " + this.time.getNome());
        System.out.println("=================================");
    }

    public Jogador(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
