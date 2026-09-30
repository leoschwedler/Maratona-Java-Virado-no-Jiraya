package ListaExerciciosBatismoJava.ex03;

public abstract class Ninja {
    protected String nome;
    protected int idade;
    protected String missao;
    protected char nivelDificuldade;
    protected Status status;


    public Ninja() {
    }

    public Ninja(String nome, int idade, String missao, char nivelDificuldade, Status status) {
        this.nome = nome;
        this.idade = idade;
        this.missao = missao;
        this.nivelDificuldade = nivelDificuldade;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Classe mae{" +
                "nome='" + nome + '\'' +
                ", idade=" + idade +
                ", missa='" + missao + '\'' +
                ", nivelDificuldade=" + nivelDificuldade +
                ", status=" + status +
                '}';
    }


}
