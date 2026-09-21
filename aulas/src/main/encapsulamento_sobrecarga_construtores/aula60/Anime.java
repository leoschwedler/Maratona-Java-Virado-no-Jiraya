package encapsulamento_sobrecarga_construtores.aula60;

public class Anime {
   private String nome;

    {
        System.out.println("Bloco de inicialização\n");
    }

    public Anime() {
        System.out.println("Construtor");
    }
}
