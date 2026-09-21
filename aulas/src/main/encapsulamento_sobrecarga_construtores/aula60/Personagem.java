package encapsulamento_sobrecarga_construtores.aula60;

public class Personagem {
    private String nome;
    private int nivel;
    private int vida;

    {
        nome = "Naruto";
        nivel = 10;
        vida = 15;
        System.out.println("Bloco de inicializacao" + this.nome + this.nivel + this.vida);
    }

    public Personagem() {
    }

    public Personagem(String nome) {
        this.nome = nome;
    }

    public Personagem(String nome, int nivel) {
        this.nome = nome;
        this.nivel = nivel;
    }

    public String getNome() {
        return nome;
    }

    public int getNivel() {
        return nivel;
    }

    public int getVida() {
        return vida;
    }
}
