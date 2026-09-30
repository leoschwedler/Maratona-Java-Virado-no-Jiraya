package ListaExerciciosLogica.ex09;

public class Main {
    public static void main(String[] args) {
        imc(80,1.71);
    }

    public static void imc(double peso, double altura){
        double resultado = peso / (altura * altura);
        if(resultado <= 18.5){
            System.out.println("Abaixo do peso");
        }else if(resultado >= 18.6 && resultado < 25.0){
            System.out.println("Peso ideal (parabéns)  ");
        }else if(resultado >= 25.0 && resultado < 30.0){
            System.out.println("Levemente acima do peso");
        }else if(resultado >= 30.0 && resultado < 35.0){
            System.out.println("Obesidade grau I ");
        }else if(resultado >= 35.0 && resultado < 40){
            System.out.println("Obesidade grau II (severa)");
        }else {
            System.out.println("Obesidade grau III (mórbida)");
        }
    }
}
