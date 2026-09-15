package estrutura_condicionais.aula021;

public class Ex05 {
    public static void main(String[] args) {
        double nota = 8.5;

        if(nota < 5){
            System.out.println("Reprovado");
        } else if (nota >= 5 && nota < 7) {
            System.out.println("Regular");
        } else if (nota >= 7 && nota < 9) {
            System.out.println("Bom");
        }else {
            System.out.println("Excelente");
        }
    }
}
