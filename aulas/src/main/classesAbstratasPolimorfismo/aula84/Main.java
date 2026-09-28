package classesAbstratasPolimorfismo.aula84;

public class Main {
    public static void main(String[] args) {
        Desenvolvedor desenvolvedor = new Desenvolvedor("Leozinho", 1500);
        System.out.println(desenvolvedor.toString());
        Carro carro = new Carro("Gol", "Pequeno");
        System.out.println(carro.toString());
    }
}
