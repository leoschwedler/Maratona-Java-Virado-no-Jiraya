package ListaExerciciosLogica.ex02;

public class Main {
    public static void main(String[] args) {
        parOuInpar(-1);
    }

    public static void parOuInpar(int numero){
        boolean negativo = false;
        String resultado;
        if(numero % 2 == 0){
            resultado = "Par";
            if (numero < 0){
                negativo = true;
            }
        }else {
            resultado = "Impar";
            if (numero < 0){
                negativo = true;
            }
        }
        String result2 = negativo ? "Numero é negativo" : "Numero é postivo";
        System.out.println("O numero é: " + resultado + " e o " + result2 );
    }
}
