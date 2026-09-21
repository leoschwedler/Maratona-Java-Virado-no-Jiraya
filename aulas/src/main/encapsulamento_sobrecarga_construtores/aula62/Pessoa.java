package encapsulamento_sobrecarga_construtores.aula62;

public class Pessoa {
    private String nome;

    public void imprimirNome() {
        System.out.println(nome);
    }

    public String getNome() {
        return nome;
    }

    public Pessoa(String nome) {
        this.nome = nome;
    }

    public static void imprimirNome(String nome) {
        System.out.println("Imprimir Nome statico: " + nome);
    }
}
