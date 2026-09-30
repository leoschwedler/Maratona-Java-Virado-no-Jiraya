package ListaExerciciosLogica.ex06;

public class Main {
    public static void main(String[] args) {
        reajuste(1000);
    }

    public static void reajuste(double valor){
        double taxa = 0.05;
        double resultado = valor * taxa;
        System.out.println("O usuario teve um reajuste de salario de: " + resultado +" e agora o seu salario fica: " + (valor - resultado));
    }
}
