package bloco09_aulas84a94_classes_abstratas_polimorfismo.aula84;

public class Main {
    public static void main(String[] args) {
        Desenvolvedor desenvolvedor = new Desenvolvedor("Leozinho", 1500);
        System.out.println(desenvolvedor.toString());
        Carro carro = new Carro("Gol", "Pequeno");
        System.out.println(carro.toString());
    }
}
