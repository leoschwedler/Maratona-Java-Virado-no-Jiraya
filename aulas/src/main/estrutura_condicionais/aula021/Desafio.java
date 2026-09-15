package estrutura_condicionais.aula021;

public class Desafio {
    public static void main(String[] args) {
        double nota = 8.7;

        if (nota <= 4.9){
            System.out.println("Reprovado");
        } else if (nota >= 5.0 && nota < 6.9) {
            System.out.println("Regular");
        } else if (nota >= 7.0 && nota < 8.9) {
            System.out.println("Bom");
        }else {
            System.out.println("Excelente");
        }
    }
}
