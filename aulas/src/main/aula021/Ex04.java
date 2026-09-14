package aula021;

public class Ex04 {
    public static void main(String[] args) {
        int idade = 18;

        if (idade < 12) {
            System.out.println("Crianca");
        } else if (idade >= 12 && idade <= 17) {
            System.out.println("Adolescente");
        } else {
            System.out.println("Adulto");
        }
    }
}
