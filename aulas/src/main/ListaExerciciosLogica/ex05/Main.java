package ListaExerciciosLogica.ex05;

public class Main {
    public static void main(String[] args) {
        calculoSalario(800, 3000);
    }


    public static void calculoSalario(double salarioMinimo, double salarioUsuario){
        if (salarioMinimo < 1293.20){
            salarioMinimo = 1293.20;
        }
        double resultado =  salarioUsuario / salarioMinimo;
        double salario = (int) Math.floor(resultado);
        System.out.println("O usuario recebe: " + salario + " salarios minimos");
    }
}
