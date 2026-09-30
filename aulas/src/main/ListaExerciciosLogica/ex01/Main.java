package ListaExerciciosLogica.ex01;

public class Main {
    public static void main(String[] args) {
        somaABC(5,9,15);
    }



    public static void somaABC(double a, double b, double c){
        double valorAB = a + b;
        String resultado = valorAB > c ? "A+B é maior que C" : "C é maior que A+B";
        System.out.println(resultado);
    }
}
