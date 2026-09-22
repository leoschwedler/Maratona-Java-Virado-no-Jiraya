package associaçãoEntreObjetosEEntradaDeDados.aula67;

public class Jogador {
    private String nome;
    private Time time;

    public void adicionaTime(Time time){
        setTime(time);
    }

    public void imprimir(){
        System.out.println("-------------------");
        System.out.println("Nome: " + this.nome);
        System.out.println("-------------------");
        if(time == null) return;
        System.out.println("Time: " + this.time.getNome());
    }
    public Jogador(String nome, Time time) {
        this.nome = nome;
        this.time = time;
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
