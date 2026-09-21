package encapsulamento_sobrecarga_construtores.aula60;

public class Main {
    public static void main(String[] args) {

        Personagem personagem = new Personagem();
        System.out.println("Construtor" + personagem.getNome() + personagem.getNivel() + personagem.getVida());
        Personagem personagem1 = new Personagem("Sasuke");
        System.out.println("Construtor" + personagem1.getNome() + personagem1.getNivel() + personagem1.getVida());
        Personagem personagem2 = new Personagem("Obito", 12);
        System.out.println("Construtor" + personagem2.getNome() + personagem2.getNivel() + personagem2.getVida());

    }
}
